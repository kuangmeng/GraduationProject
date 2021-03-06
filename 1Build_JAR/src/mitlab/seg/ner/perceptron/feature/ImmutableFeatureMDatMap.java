package mitlab.seg.ner.perceptron.feature;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import mitlab.seg.ner.corpus.io.ByteArray;
import mitlab.seg.ner.perceptron.tagset.TagSet;
import mitlab.seg.ner.trie.datrie.MutableDoubleArrayTrieInteger;

/**
 * 用MutableDoubleArrayTrie实现的ImmutableFeatureMap
 */
public class ImmutableFeatureMDatMap extends ImmutableFeatureMap {
  MutableDoubleArrayTrieInteger dat;

  public ImmutableFeatureMDatMap() {
    super();
    dat = new MutableDoubleArrayTrieInteger();
  }

  public ImmutableFeatureMDatMap(MutableDoubleArrayTrieInteger dat, TagSet tagSet) {
    super(tagSet);
    this.dat = dat;
  }

  public ImmutableFeatureMDatMap(Map<String, Integer> featureIdMap, TagSet tagSet) {
    super(tagSet);
    dat = new MutableDoubleArrayTrieInteger(featureIdMap);
  }

  public ImmutableFeatureMDatMap(Set<Map.Entry<String, Integer>> featureIdSet, TagSet tagSet) {
    super(tagSet);
    dat = new MutableDoubleArrayTrieInteger();
    for (Map.Entry<String, Integer> entry : featureIdSet) {
      dat.put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public int idOf(String string) {
    return dat.get(string);
  }

  @Override
  public int size() {
    return dat.size();
  }

  @Override
  public Set<Map.Entry<String, Integer>> entrySet() {
    return dat.entrySet();
  }

  @Override
  public void save(DataOutputStream out) throws IOException {
    tagSet.save(out);
    dat.save(out);
  }

  @Override
  public boolean load(ByteArray byteArray) {
    loadTagSet(byteArray);
    return dat.load(byteArray);
  }
}
