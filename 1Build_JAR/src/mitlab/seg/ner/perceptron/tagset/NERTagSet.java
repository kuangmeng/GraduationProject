package mitlab.seg.ner.perceptron.tagset;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mitlab.seg.ner.corpus.io.ByteArray;
import mitlab.seg.ner.perceptron.common.TaskType;

public class NERTagSet extends TagSet {
  public final String O_TAG = "O";
  public final char O_TAG_CHAR = 'O';
  public final String B_TAG_PREFIX = "B-";
  public final char B_TAG_CHAR = 'B';
  public final String M_TAG_PREFIX = "M-";
  public final String E_TAG_PREFIX = "E-";
  public final String S_TAG = "S";
  public final char S_TAG_CHAR = 'S';
  public final Set<String> nerLabels = new HashSet<String>();

  /**
   * 非NER
   */
  public final int O;

  public NERTagSet() {
    super(TaskType.NER);
    O = add(O_TAG);
  }

  public static String posOf(String tag) {
    int index = tag.indexOf('-');
    if (index == -1) {
      return tag;
    }

    return tag.substring(index + 1);
  }

  @Override
  public boolean load(ByteArray byteArray) {
    super.load(byteArray);
    nerLabels.clear();
    for (Map.Entry<String, Integer> entry : this) {
      String tag = entry.getKey();
      int index = tag.indexOf('-');
      if (index != -1) {
        nerLabels.add(tag.substring(index + 1));
      }
    }

    return true;
  }
}
