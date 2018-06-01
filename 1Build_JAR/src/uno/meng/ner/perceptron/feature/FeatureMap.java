package uno.meng.ner.perceptron.feature;

import uno.meng.ner.corpus.io.ByteArray;
import uno.meng.ner.corpus.io.ICacheAble;
import uno.meng.ner.perceptron.common.IStringIdMap;
import uno.meng.ner.perceptron.common.TaskType;
import uno.meng.ner.perceptron.tagset.CWSTagSet;
import uno.meng.ner.perceptron.tagset.NERTagSet;
import uno.meng.ner.perceptron.tagset.POSTagSet;
import uno.meng.ner.perceptron.tagset.TagSet;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
