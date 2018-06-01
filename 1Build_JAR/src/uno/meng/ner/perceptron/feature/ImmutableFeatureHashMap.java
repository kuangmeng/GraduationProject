package uno.meng.ner.perceptron.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import uno.meng.ner.perceptron.tagset.TagSet;

public class ImmutableFeatureHashMap extends ImmutableFeatureMap {
  public Map<String, Integer> featureIdMap;

  public ImmutableFeatureHashMap(Map<String, Integer> featureIdMap, TagSet tagSet) {
    super(tagSet);
    this.featureIdMap = featureIdMap;
  }

  public ImmutableFeatureHashMap(Set<Map.Entry<String, Integer>> entrySet, TagSet tagSet) {
    super(tagSet);
    this.featureIdMap = new HashMap<String, Integer>();
    for (Map.Entry<String, Integer> entry : entrySet) {
      featureIdMap.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public int idOf(String string) {
    Integer id = featureIdMap.get(string);
    if (id == null)
      return -1;
    return id;
  }

  @Override
  public int size() {
    return featureIdMap.size();
  }

  @Override
  public Set<Map.Entry<String, Integer>> entrySet() {
    return featureIdMap.entrySet();
  }
}
