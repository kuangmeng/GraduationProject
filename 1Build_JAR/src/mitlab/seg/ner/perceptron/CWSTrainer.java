package mitlab.seg.ner.perceptron;

import java.io.IOException;
import java.util.List;
import mitlab.seg.ner.corpus.document.sentence.Sentence;
import mitlab.seg.ner.corpus.document.sentence.word.Word;
import mitlab.seg.ner.perceptron.feature.FeatureMap;
import mitlab.seg.ner.perceptron.instance.CWSInstance;
import mitlab.seg.ner.perceptron.instance.Instance;
import mitlab.seg.ner.perceptron.model.LinearModel;
import mitlab.seg.ner.perceptron.tagset.CWSTagSet;
import mitlab.seg.ner.perceptron.tagset.TagSet;
import mitlab.seg.ner.perceptron.utility.Utility;

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
