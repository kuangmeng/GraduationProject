package uno.meng.TRAIN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import opennlp.tools.util.TrainingParameters;
import uno.meng.me_postagging.feature.WordPosContextGenerator;
import uno.meng.me_postagging.feature.WordPosContextGeneratorConf;
import uno.meng.me_postagging.model.WordPosME;

/**
 * 运行类
 */
public class TrainME4POSTagging {

  private static String flag = "model";
  
  // 静态内部类
  public static class Corpus {
    // 文件名和编码
    public String name;
    public String encoding;
    public String trainFile;
    public String testFile;
    public String modelbinaryFile;
    public String modeltxtFile;
    public String errorFile;
  }

  private static String[] corpusName = {"pos"};

  public static void main(String[] args) throws IOException {
    runFeature();
  }

  /**
   * 根据配置文件配置的信息获取特征
   * 
   * @throws IOException IO异常
   */
  private static void runFeature() throws IOException {
    // 配置参数
    TrainingParameters params = TrainingParameters.defaultParams();
    params.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(3));

    // 加载语料文件
    Properties config = new Properties();
    InputStream configStream =
        TrainME4POSTagging.class.getClassLoader().getResourceAsStream("MengPOSTagging.properties");
    config.load(configStream);
    Corpus[] corpora = getCorporaFromConf(config);// 获取语料
    WordPosContextGenerator contextGen = getContextGenerator(config);
    runFeatureOnCorporaByFlag(contextGen, corpora, params);
  }

  /**
   * 根据命令行参数执行相应的操作
   * 
   * @param contextGen 上下文特征生成器
   * @param corpora 语料信息内部类对象数组
   * @param params 训练模型的参数
   * @throws IOException
   * @throws FileNotFoundException
   */
  private static void runFeatureOnCorporaByFlag(WordPosContextGenerator contextGen,
      Corpus[] corpora, TrainingParameters params) throws IOException {
    if (flag == "model" || flag.equals("model")) {
      for (int i = 0; i < corpora.length; i++) {
        modelOutOnCorpus(contextGen, corpora[i], params);
      }
    }
  }

  /**
   * 训练模型，输出模型文件
   * 
   * @param contextGen 上下文特征生成器
   * @param corpus 语料对象
   * @param params 训练模型的参数
   * @throws UnsupportedOperationException
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static void modelOutOnCorpus(WordPosContextGenerator contextGen, Corpus corpus,
      TrainingParameters params) {
    System.out.println("ContextGenerator: " + contextGen);
    System.out.println("Training on " + corpus.name + "...");
    // 训练模型
    WordPosME.train(new File(corpus.trainFile), new File(corpus.modelbinaryFile),
        new File(corpus.modeltxtFile), params, contextGen, corpus.encoding);
  }

  /**
   * 得到生成特征的实例对象
   * 
   * @param config 配置文件
   * @return
   */
  private static WordPosContextGenerator getContextGenerator(Properties config) {
    String featureClass = config.getProperty("feature.class");
    if (featureClass.equals("uno.meng.me_postagging.feature.WordSegPosContextGeneratorConf")) {
      // 初始化需要哪些特征
      return new WordPosContextGeneratorConf(config);
    } else {
      return null;
    }
  }

  private static Corpus[] getCorporaFromConf(Properties config) {
    Corpus[] corpuses = new Corpus[corpusName.length];
    for (int i = 0; i < corpuses.length; i++) {
      String name = corpusName[i];
      String encoding = config.getProperty(name + "." + "corpus.encoding");
      String trainFile = config.getProperty(name + "." + "corpus.train.file");
      String modelbinaryFile = config.getProperty(name + "." + "corpus.modelbinary.file");
      String modeltxtFile = config.getProperty(name + "." + "corpus.modeltxt.file");
      Corpus corpus = new Corpus();
      corpus.name = name;
      corpus.encoding = encoding;
      corpus.trainFile = trainFile;
      corpus.modeltxtFile = modeltxtFile;
      corpus.modelbinaryFile = modelbinaryFile;
      corpuses[i] = corpus;
    }
    return corpuses;
  }

}
