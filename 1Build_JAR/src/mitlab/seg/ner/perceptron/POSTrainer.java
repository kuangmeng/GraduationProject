package mitlab.seg.ner.perceptron;

import java.io.IOException;
import mitlab.seg.ner.corpus.document.sentence.Sentence;
import mitlab.seg.ner.perceptron.feature.FeatureMap;
import mitlab.seg.ner.perceptron.instance.Instance;
import mitlab.seg.ner.perceptron.instance.POSInstance;
import mitlab.seg.ner.perceptron.tagset.POSTagSet;
import mitlab.seg.ner.perceptron.tagset.TagSet;

public class POSTrainer extends PerceptronTrainer {
  @Override
  protected TagSet createTagSet() {
    return new POSTagSet();
  }

  @Override
  protected Instance createInstance(Sentence sentence, FeatureMap featureMap) {
    return POSInstance.create(sentence, featureMap);
  }

  @Override
  public Result train(String trainingFile, String developFile, String modelFile)
      throws IOException {
    // 词性标注模型压缩会显著降低效果
    return train(trainingFile, developFile, modelFile, 0, 10,
        Runtime.getRuntime().availableProcessors());
  }
}
