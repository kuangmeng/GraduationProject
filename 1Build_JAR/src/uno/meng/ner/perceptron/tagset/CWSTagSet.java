package uno.meng.ner.perceptron.tagset;

import uno.meng.ner.perceptron.common.TaskType;

public class CWSTagSet extends TagSet {
  public final int B;
  public final int M;
  public final int E;
  public final int S;
  /**
   * 超出句首
   */
  public final int BOS;

  public CWSTagSet() {
    super(TaskType.CWS);
    B = add("B");
    M = add("M");
    E = add("E");
    S = add("S");
    BOS = size();
    lock();
  }
}
