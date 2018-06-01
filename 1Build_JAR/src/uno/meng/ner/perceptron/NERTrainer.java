package uno.meng.ner.perceptron;

import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.instance.Instance;
import uno.meng.ner.perceptron.instance.NERInstance;
import uno.meng.ner.perceptron.tagset.NERTagSet;
import uno.meng.ner.perceptron.tagset.TagSet;

public class NERTrainer extends PerceptronTrainer {
  /**
   * 重载此方法以支持任意自定义NER类型，例如：<br>
   * NERTagSet tagSet = new NERTagSet();<br>
   * tagSet.nerLabels.add("nr");<br>
   * tagSet.nerLabels.add("ns");<br>
   * tagSet.nerLabels.add("nt");<br>
   * return tagSet;<br>
   * 
   * @return
   */
  @Override
  protected TagSet createTagSet() {
    NERTagSet tagSet = new NERTagSet();
    tagSet.nerLabels.add("nr");
    tagSet.nerLabels.add("ns");
    tagSet.nerLabels.add("nt");
    return tagSet;
  }

  @Override
  protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
    return NERInstance.create(sentence, featureMap);
  }
}
