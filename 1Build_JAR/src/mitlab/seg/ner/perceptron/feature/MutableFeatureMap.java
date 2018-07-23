package mitlab.seg.ner.perceptron.feature;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import mitlab.seg.ner.perceptron.tagset.TagSet;

public class MutableFeatureMap extends FeatureMap {
  public Map<String, Integer> featureIdMap;
  // TreeMap 5136
  // Bin 2712
  // DAT minutes
  // trie4j 3411

  public MutableFeatureMap(TagSet tagSet) {
    super(tagSet);
    featureIdMap = new TreeMap<String, Integer>();
    for (int i = 0; i < tagSet.size(); i++) {
      idOf("BL=" + tagSet.stringOf(i));
    }
    idOf("BL=_BL_");
  }

  @Override
  public Set<Map.Entry<String, Integer>> entrySet() {
    return featureIdMap.entrySet();
  }

  @Override
  public int idOf(String string) {
    Integer id = featureIdMap.get(string);
    if (id == null) {
      id = featureIdMap.size();
      featureIdMap.put(string, id);
    }

    return id;
  }

  public int size() {
    return featureIdMap.size();
  }

  public Set<String> featureSet() {
    return featureIdMap.keySet();
  }

  @Override
  public int[] allLabels() {
    return tagSet.allTags();
  }

  @Override
  public int bosTag() {
    return tagSet.size();
  }
}
