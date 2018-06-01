package uno.meng.ner.perceptron.instance;

import java.util.List;
import uno.meng.ner.perceptron.feature.FeatureMap;
import uno.meng.ner.perceptron.tagset.TagSet;

public class Instance {
  public int[][] featureMatrix;
  public int[] tagArray;

  protected Instance() {}

  protected static int[] toFeatureArray(List<Integer> featureVector) {
    int[] featureArray = new int[featureVector.size() + 1]; // 最后一列留给转移特征
    int index = -1;
    for (Integer feature : featureVector) {
      featureArray[++index] = feature;
    }

    return featureArray;
  }

  public int[] getFeatureAt(int position) {
    return featureMatrix[position];
  }

  public int length() {
    return tagArray.length;
  }

  protected static void addFeature(CharSequence rawFeature, List<Integer> featureVector,
      FeatureMap featureMap, boolean create) {
    int id = featureMap.idOf(rawFeature.toString());
    if (create || id != -1) {
      featureVector.add(id);
    }
  }

  /**
   * 添加特征，同时清空缓存
   *
   * @param rawFeature
   * @param featureVector
   * @param featureMap
   * @param create
   */
  protected static void addFeatureThenClear(StringBuilder rawFeature, List<Integer> featureVector,
      FeatureMap featureMap, boolean create) {
    int id = featureMap.idOf(rawFeature.toString());
    if (create || id != -1) {
      featureVector.add(id);
    }
    rawFeature.setLength(0);
  }

  /**
   * 根据标注集还原字符形式的标签
   *
   * @param tagSet
   * @return
   */
  public String[] tags(TagSet tagSet) {
    assert tagArray != null;

    String[] tags = new String[tagArray.length];
    for (int i = 0; i < tags.length; i++) {
      tags[i] = tagSet.stringOf(tagArray[i]);
    }

    return tags;
  }

  /**
   * 实例大小（有多少个要预测的元素）
   *
   * @return
   */
  public int size() {
    return featureMatrix.length;
  }
}
