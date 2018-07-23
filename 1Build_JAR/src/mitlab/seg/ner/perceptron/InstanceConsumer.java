package mitlab.seg.ner.perceptron;

import java.io.IOException;
import mitlab.seg.crf_seg.util.CharTable;
import mitlab.seg.ner.corpus.document.sentence.Sentence;
import mitlab.seg.ner.perceptron.feature.FeatureMap;
import mitlab.seg.ner.perceptron.instance.Instance;
import mitlab.seg.ner.perceptron.instance.InstanceHandler;
import mitlab.seg.ner.perceptron.model.LinearModel;
import mitlab.seg.ner.perceptron.model.StructuredPerceptron;
import mitlab.seg.ner.perceptron.utility.IOUtility;
import mitlab.seg.ner.perceptron.utility.Utility;

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
