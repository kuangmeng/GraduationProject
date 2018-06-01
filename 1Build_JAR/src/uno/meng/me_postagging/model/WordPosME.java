package uno.meng.me_postagging.model;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import opennlp.tools.ml.BeamSearch;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.TrainerFactory.TrainerType;
import opennlp.tools.ml.maxent.io.PlainTextGISModelReader;
import opennlp.tools.ml.maxent.io.PlainTextGISModelWriter;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.Event;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.SequenceClassificationModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.SequenceValidator;
import opennlp.tools.util.TrainingParameters;
import uno.meng.me_postagging.event.WordPosSampleEventStream;
import uno.meng.me_postagging.feature.WordPosContextGenerator;
import uno.meng.me_postagging.sequencevalidator.DefaultWordPosSequenceValidator;
import uno.meng.me_postagging.stream.FileInputStreamFactory;
import uno.meng.me_postagging.stream.WordPosSample;
import uno.meng.me_postagging.stream.WordPosSampleStream;

/**
 * 训练模型，标记序列
 *
 */
public class WordPosME implements WordPos {

  public static final int DEFAULT_BEAM_SIZE = 20;
  private WordPosContextGenerator contextGenerator;
  private int size;
  private Sequence bestSequence;
  private SequenceClassificationModel<String> model;
  @SuppressWarnings("unused")
  private WordPosModel modelPackage;
  private List<String> characters = new ArrayList<>();
  private List<String> segwords = new ArrayList<>();

  private SequenceValidator<String> sequenceValidator;

  /**
   * 构造函数，初始化工作
   * 
   * @param model 模型
   * @param contextGen 特征
   */
  public WordPosME(WordPosModel model, WordPosContextGenerator contextGen) {
    init(model, contextGen);
  }

  /**
   * 初始化工作
   * 
   * @param model 模型
   * @param contextGen 特征
   */
  private void init(WordPosModel model, WordPosContextGenerator contextGen) {
    int beamSize = WordPosME.DEFAULT_BEAM_SIZE;

    String beamSizeString = model.getManifestProperty(BeamSearch.BEAM_SIZE_PARAMETER);

    if (beamSizeString != null) {
      beamSize = Integer.parseInt(beamSizeString);
    }

    modelPackage = model;

    contextGenerator = contextGen;
    size = beamSize;
    sequenceValidator = new DefaultWordPosSequenceValidator();
    if (model.getWordSegPosSequenceModel() != null) {
      this.model = model.getWordSegPosSequenceModel();
    } else {
      this.model = new BeamSearch<String>(beamSize, model.getWordSegPosModel(), 0);
    }

  }

  /**
   * 训练模型
   * 
   * @param file 训练文件
   * @param params 训练
   * @param contextGen 特征
   * @param encoding 编码
   * @return 模型和模型信息的包裹结果
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static WordPosModel train(File file, TrainingParameters params,
      WordPosContextGenerator contextGen, String encoding) {
    WordPosModel model = null;
    try {
      ObjectStream<String> lineStream =
          new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
      ObjectStream<WordPosSample> sampleStream = new WordPosSampleStream(lineStream);
      model = WordPosME.train("zh", sampleStream, params, contextGen);
      return model;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 训练模型
   * 
   * @param languageCode 编码
   * @param sampleStream 文件流
   * @param contextGen 特征
   * @param encoding 编码
   * @return 模型和模型信息的包裹结果
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static WordPosModel train(String languageCode, ObjectStream<WordPosSample> sampleStream,
      TrainingParameters params, WordPosContextGenerator contextGen) throws IOException {
    String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);
    int beamSize = WordPosME.DEFAULT_BEAM_SIZE;
    if (beamSizeString != null) {
      beamSize = Integer.parseInt(beamSizeString);
    }
    MaxentModel posModel = null;
    Map<String, String> manifestInfoEntries = new HashMap<String, String>();
    // event_model_trainer
    TrainerType trainerType = TrainerFactory.getTrainerType(params.getSettings());
    SequenceClassificationModel<String> seqPosModel = null;
    if (TrainerType.EVENT_MODEL_TRAINER.equals(trainerType)) {
      // sampleStream为PhraseAnalysisSampleStream对象
      ObjectStream<Event> es = new WordPosSampleEventStream(sampleStream, contextGen);
      EventTrainer trainer =
          TrainerFactory.getEventTrainer(params.getSettings(), manifestInfoEntries);
      posModel = trainer.train(es);
    }

    if (posModel != null) {
      return new WordPosModel(languageCode, posModel, beamSize, manifestInfoEntries);
    } else {
      return new WordPosModel(languageCode, seqPosModel, manifestInfoEntries);
    }
  }

  /**
   * 训练模型，并将模型写出
   * 
   * @param file 训练的文本
   * @param modelbinaryFile 二进制的模型文件
   * @param modeltxtFile 文本类型的模型文件
   * @param params 训练的参数配置
   * @param contextGen 上下文 产生器
   * @param encoding 编码方式
   * @return
   */
  public static WordPosModel train(File file, File modelbinaryFile, File modeltxtFile,
      TrainingParameters params, WordPosContextGenerator contextGen, String encoding) {
    OutputStream modelOut = null;
    PlainTextGISModelWriter modelWriter = null;
    WordPosModel model = null;
    try {
      ObjectStream<String> lineStream =
          new PlainTextByLineStream(new FileInputStreamFactory(file), encoding);
      ObjectStream<WordPosSample> sampleStream = new WordPosSampleStream(lineStream);
      model = WordPosME.train("zh", sampleStream, params, contextGen);
      // 模型的持久化，写出的为二进制文件
      modelOut = new BufferedOutputStream(new FileOutputStream(modelbinaryFile));
      model.serialize(modelOut);
      // 模型的写出，文本文件
      modelWriter =
          new PlainTextGISModelWriter((AbstractModel) model.getWordSegPosModel(), modeltxtFile);
      modelWriter.persist();
      return model;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (modelOut != null) {
        try {
          modelOut.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public String[] tag(String[] characters, String[] words) {
    bestSequence =
        model.bestSequence(characters, (String[]) words, contextGenerator, sequenceValidator);
    // System.out.println(bestSequence);
    List<String> t = bestSequence.getOutcomes();

    return t.toArray(new String[t.size()]);
  }

  /**
   * 根据训练得到的模型文件得到
   * 
   * @param modelFile 模型文件
   * @param params 参数
   * @param contextGen 上下文生成器
   * @param encoding 编码方式
   * @return
   */
  public static WordPosModel readModel(File modelFile, TrainingParameters params,
      WordPosContextGenerator contextGen, String encoding) {
    PlainTextGISModelReader modelReader = null;
    AbstractModel abModel = null;
    WordPosModel model = null;
    String beamSizeString = params.getSettings().get(BeamSearch.BEAM_SIZE_PARAMETER);

    int beamSize = WordPosME.DEFAULT_BEAM_SIZE;
    if (beamSizeString != null) {
      beamSize = Integer.parseInt(beamSizeString);
    }

    try {
      Map<String, String> manifestInfoEntries = new HashMap<String, String>();
      modelReader = new PlainTextGISModelReader(modelFile);
      abModel = modelReader.getModel();
      model = new WordPosModel(encoding, abModel, beamSize, manifestInfoEntries);

      System.out.println("读取模型成功");
      return model;
    } catch (UnsupportedOperationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 对分词之后的结果进行词性标记
   * 
   * @param words 分词之后的一句话
   * @return
   */
  @Override
  public String[] wordpos(String sentence) {
    String[] words = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
    return wordpos(words);
  }

  /**
   * 对分词之后的结果进行词性标记
   * 
   * @param words 分词之后的词语数组
   * @return 得到词语对应的词性标记
   */
  @Override
  public String[] wordpos(String[] words) {
    String[] pos = tag(words);
    return pos;
  }

  /**
   * 根据分词的数组生成字符序列，字符的标记序列
   * 
   * @param words 分词之后的词语数组
   */
  public void taglabel(String[] words) {
    characters.clear();
    segwords.clear();
    for (int i = 0; i < words.length; i++) {
      String temp = words[i];
      segwords.add(temp);
      if (temp.length() == 1) {
        characters.add(temp + "_S");
        continue;
      }
      for (int j = 0; j < temp.length(); j++) {
        char c = temp.charAt(j);
        if (j == 0) {
          characters.add(c + "_B");
        } else if (j == temp.length() - 1) {
          characters.add(c + "_E");
        } else {
          characters.add(c + "_M");
        }
      }
    }
  }

  /**
   * 得到的最好的标记序列
   * 
   * @param words 分词的数组
   * @return
   */
  public String[] tag(String[] words) {
    taglabel(words);
    String[] characterandpos = this.tag(characters.toArray(new String[characters.size()]),
        segwords.toArray(new String[segwords.size()]));
    return WordPosSample.toPos(characterandpos);
  }

  /**
   * 得到最好的numTaggings个标记序列
   * 
   * @param numTaggings 个数
   * @param words 分词的数组
   * @return 词性标注的序列
   */
  public String[][] tag(int numTaggings, String[] words) {
    taglabel(words);
    Sequence[] bestSequences =
        model.bestSequences(numTaggings, characters.toArray(new String[characters.size()]),
            segwords.toArray(new String[segwords.size()]), contextGenerator, sequenceValidator);
    String[][] tags = new String[bestSequences.length][];
    String[][] poses = new String[bestSequences.length][];
    for (int si = 0; si < tags.length; si++) {
      List<String> t = bestSequences[si].getOutcomes();
      tags[si] = t.toArray(new String[t.size()]);
      poses[si] = WordPosSample.toPos(tags[si]);
    }
    return poses;
  }

  /**
   * 最好的K个序列
   * 
   * @param sentence 分词之后的词语数组
   * @return
   */
  public Sequence[] topKSequences(String[] sentence) {
    return this.topKSequences(sentence, null);
  }

  /**
   * 最好的K个序列
   * 
   * @param words 分词之后的词语数组
   * @param additionaContext
   * @return
   */
  public Sequence[] topKSequences(String[] words, Object[] additionaContext) {
    taglabel(words);
    return model.bestSequences(size, characters.toArray(new String[characters.size()]),
        segwords.toArray(new String[segwords.size()]), contextGenerator, sequenceValidator);
  }

  /**
   * 生成词典
   * 
   * @param sample 样本流
   * @return
   * @throws IOException
   */
  public static HashSet<String> buildDictionary(ObjectStream<WordPosSample> samples)
      throws IOException {
    HashSet<String> dict = new HashSet<>();
    WordPosSample sample;
    while ((sample = samples.read()) != null) {
      String[] words = sample.getWords();
      for (int i = 0; i < words.length; i++) {
        dict.add(words[i]);
      }
    }
    return dict;
  }

  /**
   * 
   * @param file 训练文件
   * @param encoding 编码方式
   * @return
   * @throws IOException
   */
  public static HashSet<String> buildDictionary(File file, String encoding) throws IOException {
    BufferedReader data =
        new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
    String sentences;
    HashSet<String> dict = new HashSet<String>();
    while ((sentences = data.readLine()) != null) {
      if (sentences.compareTo("") != 0) {
        String wordsandposes[] = WhitespaceTokenizer.INSTANCE.tokenize(sentences);
        for (int i = 1; i < wordsandposes.length; i++) {
          String[] wordanspos = wordsandposes[i].split("/");
          String word = wordanspos[0];
          dict.add(word);
        }
      }
    }

    data.close();

    return dict;
  }
}
