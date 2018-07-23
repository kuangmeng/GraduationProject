package mitlab.seg.ner.perceptron.model;

import mitlab.seg.ner.perceptron.feature.FeatureMap;

/**
 * 平均感知机算法学习的线性模型
 *
 */
public class AveragedPerceptron extends LinearModel {
  public AveragedPerceptron(FeatureMap featureMap, float[] parameter) {
    super(featureMap, parameter);
  }

  public AveragedPerceptron(FeatureMap featureMap) {
    super(featureMap);
  }

  /**
   * 根据答案和预测更新参数
   *
   * @param goldIndex 预测正确的特征函数（非压缩形式）
   * @param predictIndex 命中的特征函数
   */
  public void update(int[] goldIndex, int[] predictIndex, double[] total, int[] timestamp,
      int iter) {
    for (int i = 0; i < goldIndex.length; ++i) {
      if (goldIndex[i] == predictIndex[i])
        continue;
      else {
        update(goldIndex[i], 1, total, timestamp, iter);
        if (predictIndex[i] >= 0 && predictIndex[i] < parameter.length)
          update(predictIndex[i], -1, total, timestamp, iter);
        else {
          throw new IllegalArgumentException("更新参数时传入了非法的下标");
        }
      }
    }
  }

  private void update(int index, float value, double[] total, int[] timestamp, int current) {
    int passed = current - timestamp[index];
    total[index] += passed * parameter[index];
    parameter[index] += value;
    timestamp[index] = current;
  }

  public void average(double[] total, int[] timestamp, int current) {
    for (int i = 0; i < parameter.length; i++) {
      parameter[i] = (float) ((total[i] + (current - timestamp[i]) * parameter[i]) / current);
    }
  }
}
