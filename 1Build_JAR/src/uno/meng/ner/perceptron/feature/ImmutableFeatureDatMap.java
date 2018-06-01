package uno.meng.ner.perceptron.feature;

import uno.meng.ner.perceptron.tagset.TagSet;
import uno.meng.ner.trie.DoubleArrayTrie;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ImmutableFeatureDatMap extends ImmutableFeatureMap {
  DoubleArrayTrie<Integer> dat;

  public ImmutableFeatureDatMap(TreeMap<String, Integer> featureIdMap, TagSet tagSet) {
    super(tagSet);
    dat = new DoubleArrayTrie<Integer>();
    dat.build(featureIdMap);
  }

  @Override
  public int idOf(String string) {
    return dat.exactMatchSearch(string);
  }

  @Override
  public int size() {
    return dat.size();
  }

  @Override
  public Set<Map.Entry<String, Integer>> entrySet() {
    throw new UnsupportedOperationException("这份DAT实现不支持遍历");
  }
}
