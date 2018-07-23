package mitlab.seg.ner.perceptron.feature;

import java.util.Map;

public class FeatureSortItem {
  public String key;
  public Integer id;
  public float total;

  public FeatureSortItem(Map.Entry<String, Integer> entry, float[] parameter, int tagSetSize) {
    key = entry.getKey();
    id = entry.getValue();
    for (int i = 0; i < tagSetSize; ++i) {
      total += Math.abs(parameter[id * tagSetSize + i]);
    }
  }
}
