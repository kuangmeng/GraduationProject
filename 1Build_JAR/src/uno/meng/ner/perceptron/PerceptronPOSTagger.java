package uno.meng.ner.perceptron;

import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.perceptron.common.TaskType;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.instance.Instance;
import uno.meng.ner.perceptron.instance.POSInstance;
import uno.meng.ner.perceptron.model.LinearModel;
import uno.meng.ner.tokenizer.lexical.POSTagger;
import java.io.IOException;
import java.util.List;

/**
 * 词性标注器
 *
 */
public class PerceptronPOSTagger extends PerceptronTagger implements POSTagger {
  public PerceptronPOSTagger(LinearModel model) {
    super(model);
    if (model.featureMap.tagSet.type != TaskType.POS) {
      throw new IllegalArgumentException(
          String.format("错误的模型类型: 传入的不是词性标注模型，而是 %s 模型", model.featureMap.tagSet.type));
    }
  }

  public PerceptronPOSTagger(String modelPath) throws IOException {
    this(new LinearModel(modelPath));
  }

  /**
   * 加载配置文件指定的模型
   * 
   * @throws IOException
   */
  public PerceptronPOSTagger() throws IOException {
    this(uno.meng.Constants.POSMODEL);
  }

  /**
   * 标注
   *
   * @param words
   * @return
   */
  @Override
  public String[] tag(String... words) {
    POSInstance instance = new POSInstance(words, model.featureMap);
    instance.tagArray = new int[words.length];

    model.viterbiDecode(instance, instance.tagArray);
    return instance.tags(model.tagSet());
  }

  /**
   * 标注
   *
   * @param wordList
   * @return
   */
  @Override
  public String[] tag(List<String> wordList) {
    String[] termArray = new String[wordList.size()];
    wordList.toArray(termArray);
    return tag(termArray);
  }

  /**
   * 在线学习
   *
   * @param segmentedTaggedSentence 人民日报2014格式的句子
   * @return 是否学习成功（失败的原因是参数错误）
   */
  public boolean learn(String segmentedTaggedSentence) {
    return learn(POSInstance.create(segmentedTaggedSentence, model.featureMap));
  }

  /**
   * 在线学习
   *
   * @param wordTags [单词]/[词性]数组
   * @return 是否学习成功（失败的原因是参数错误）
   */
  public boolean learn(String... wordTags) {
    String[] words = new String[wordTags.length];
    String[] tags = new String[wordTags.length];
    for (int i = 0; i < wordTags.length; i++) {
      String[] wordTag = wordTags[i].split("//");
      words[i] = wordTag[0];
      tags[i] = wordTag[1];
    }
    return learn(new POSInstance(words, tags, model.featureMap));
  }

  @Override
  protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
    return POSInstance.create(sentence, featureMap);
  }
}
