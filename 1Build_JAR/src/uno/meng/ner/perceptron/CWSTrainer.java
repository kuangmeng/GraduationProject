package uno.meng.ner.perceptron;

import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.corpus.document.sentence.word.Word;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.instance.CWSInstance;
import uno.meng.ner.perceptron.instance.Instance;
import uno.meng.ner.perceptron.model.LinearModel;
import uno.meng.ner.perceptron.tagset.CWSTagSet;
import uno.meng.ner.perceptron.tagset.TagSet;
import uno.meng.ner.perceptron.utility.Utility;
import java.io.IOException;
import java.util.List;

/**
 * 感知机分词器训练工具
 *
 */
public class CWSTrainer extends PerceptronTrainer {
  @Override
  protected TagSet createTagSet() {
    return new CWSTagSet();
  }

  @Override
  protected Instance createInstance(Sentence sentence, FeatureMap mutableFeatureMap) {
    List<Word> wordList = sentence.toSimpleWordList();
    String[] termArray = Utility.toWordArray(wordList);
    Instance instance = new CWSInstance(termArray, mutableFeatureMap);
    return instance;
  }

  @Override
  public double[] evaluate(String developFile, LinearModel model) throws IOException {
    PerceptronSegmenter segmenter = new PerceptronSegmenter(model);
    double[] prf = Utility.prf(Utility.evaluateCWS(developFile, segmenter));
    return prf;
  }

}
