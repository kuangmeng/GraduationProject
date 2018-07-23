package mitlab.seg.ner.perceptron.feature;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import mitlab.seg.ner.corpus.io.ByteArray;
import mitlab.seg.ner.corpus.io.ICacheAble;
import mitlab.seg.ner.perceptron.common.IStringIdMap;
import mitlab.seg.ner.perceptron.common.TaskType;
import mitlab.seg.ner.perceptron.tagset.CWSTagSet;
import mitlab.seg.ner.perceptron.tagset.NERTagSet;
import mitlab.seg.ner.perceptron.tagset.POSTagSet;
import mitlab.seg.ner.perceptron.tagset.TagSet;

public abstract class FeatureMap implements IStringIdMap, ICacheAble {
  public abstract int size();

  public int[] allLabels() {
    return tagSet.allTags();
  }

  public int bosTag() {
    return tagSet.size();
  }

  public TagSet tagSet;

  public FeatureMap(TagSet tagSet) {
    this.tagSet = tagSet;
  }

  public abstract Set<Map.Entry<String, Integer>> entrySet();

  public FeatureMap() {}

  @Override
  public void save(DataOutputStream out) throws IOException {
    tagSet.save(out);
    out.writeInt(size());
    for (Map.Entry<String, Integer> entry : entrySet()) {
      out.writeUTF(entry.getKey());
    }
  }

  @Override
  public boolean load(ByteArray byteArray) {
    loadTagSet(byteArray);
    int size = byteArray.nextInt();
    for (int i = 0; i < size; i++) {
      idOf(byteArray.nextUTF());
    }
    return true;
  }

  protected final void loadTagSet(ByteArray byteArray) {
    TaskType type = TaskType.values()[byteArray.nextInt()];
    switch (type) {
      case CWS:
        tagSet = new CWSTagSet();
        break;
      case POS:
        tagSet = new POSTagSet();
        break;
      case NER:
        tagSet = new NERTagSet();
        break;
    }
    tagSet.load(byteArray);
  }
}
