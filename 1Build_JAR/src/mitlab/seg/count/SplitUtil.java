package mitlab.seg.count;

public class SplitUtil {
  public String name;
  public int[] offset = new int[mitlab.seg.Constants.MAX];
  public int num = 1;

  public SplitUtil(String name, int offset) {
    this.name = name;
    this.offset[0] = offset;
    this.num = 1;
  }

  private void addNum() {
    this.num++;
  }

  // 先addOffset，再addNum
  public void addOffset(int off) {
    this.offset[this.num] = off;
    addNum();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int[] getOffset() {
    return offset;
  }

  public void setOffset(int[] offset) {
    this.offset = offset;
  }

  public int getNum() {
    return num;
  }

  public void setNum(int num) {
    this.num = num;
  }

  public String toString() {
    String ret = new String();
    ret = "N:" + name + "\tO:";
    for (int i = 0; i < this.num; i++) {
      ret += offset[i] + "\t";
    }
    ret += "N:" + num;
    return ret;
  }
}
