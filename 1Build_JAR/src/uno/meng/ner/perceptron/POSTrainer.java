package uno.meng.ner.perceptron;

import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.instance.Instance;
import uno.meng.ner.perceptron.instance.POSInstance;
import uno.meng.ner.perceptron.tagset.POSTagSet;
import uno.meng.ner.perceptron.tagset.TagSet;
import java.io.IOException;

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
