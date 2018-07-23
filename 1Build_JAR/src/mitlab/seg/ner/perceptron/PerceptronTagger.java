package mitlab.seg.ner.perceptron;

import java.io.IOException;
import mitlab.seg.ner.corpus.document.sentence.Sentence;
import mitlab.seg.ner.perceptron.instance.Instance;
import mitlab.seg.ner.perceptron.model.LinearModel;
import mitlab.seg.ner.perceptron.model.StructuredPerceptron;

/**
 * 抽象的感知机标注器
 *
 */
public abstract class PerceptronTagger extends InstanceConsumer {
  /**
   * 用StructurePerceptron实现在线学习
   */
  protected final StructuredPerceptron model;

  public PerceptronTagger(LinearModel model) {
    assert model != null;
    this.model = model instanceof StructuredPerceptron ? (StructuredPerceptron) model
        : new StructuredPerceptron(model.featureMap, model.parameter);
  }

  public PerceptronTagger(StructuredPerceptron model) {
    assert model != null;
    this.model = model;
  }

  public LinearModel getModel() {
    return model;
  }

  /**
   * 在线学习
   *
   * @param instance
   * @return
   */
  public boolean learn(Instance instance) {
    if (instance == null)
      return false;
    model.update(instance);
    return true;
  }

  /**
   * 在线学习
   *
   * @param sentence
   * @return
   */
  public boolean learn(Sentence sentence) {
    return learn(createInstance(sentence, model.featureMap));
  }

  /**
   * 性能测试
   *
   * @param corpora 数据集
   * @return 默认返回accuracy，有些子类可能返回P,R,F1
   * @throws IOException
   */
  public double[] evaluate(String corpora) throws IOException {
    return evaluate(corpora, this.getModel());
  }
}
