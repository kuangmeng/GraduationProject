package uno.meng.ner.perceptron;

import uno.meng.crf_seg.util.CharTable;
import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.instance.Instance;
import uno.meng.ner.perceptron.instance.InstanceHandler;
import uno.meng.ner.perceptron.model.LinearModel;
import uno.meng.ner.perceptron.model.StructuredPerceptron;
import uno.meng.ner.perceptron.utility.IOUtility;
import uno.meng.ner.perceptron.utility.Utility;
import java.io.IOException;

/**
 * 需要处理实例的消费者
 *
 */
public abstract class InstanceConsumer {

  protected abstract Instance createInstance(Sentence sentence, final FeatureMap featureMap);

  protected double[] evaluate(String developFile, String modelFile) throws IOException {
    return evaluate(developFile, new LinearModel(modelFile));
  }

  protected double[] evaluate(String developFile, final LinearModel model) throws IOException {
    final int[] stat = new int[2];
    IOUtility.loadInstance(developFile, new InstanceHandler() {
      @Override
      public boolean process(Sentence sentence) {
        Utility.normalize(sentence);
        Instance instance = createInstance(sentence, model.featureMap);
        IOUtility.evaluate(instance, model, stat);
        return false;
      }
    });

    return new double[] {stat[1] / (double) stat[0] * 100};
  }

  protected String normalize(String text) {
    return CharTable.convert(text);
  }
}
