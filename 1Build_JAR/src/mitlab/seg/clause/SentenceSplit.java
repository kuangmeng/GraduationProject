package mitlab.seg.clause;

/**
 * 句子切分
 * 
 * @author 匡盟盟
 *
 */

public class SentenceSplit {
  public static TernarySearchTrie eosDic = getTst(); // 可能是句子结束符号的标点

  private static TernarySearchTrie getTst() {
    TernarySearchTrie tst = new TernarySearchTrie();
    tst.add(". ");
    tst.add(".\"");
    tst.add("\".");
    tst.add(".\n");
    tst.add(".\r\n");
    tst.add("!");
    tst.add("!\"");
    tst.add("\"!");
    tst.add("?");
    tst.add("? ");
    tst.add("?\"");
    tst.add("\"?");
    tst.add(";");
    tst.add("..");
    tst.add("...");
    tst.add("....");
    tst.add(".....");
    tst.add("......");
    tst.add("..\"");
    tst.add("...\"");
    tst.add("....\"");
    tst.add(".....\"");
    tst.add("......\"");
    tst.add("。");
    tst.add("！");
    tst.add("？");
    tst.add("！");
    tst.add("…");
    return tst;
  }

  // 找下一个切分点
  public static int nextPoint(String text, int lastEOS) {
    int i = lastEOS;
    while (i < text.length()) {
      // 然后再找标点符号
      String toFind = eosDic.matchLong(text, i);
      if (toFind != null) {
        return i + toFind.length();
      } else {
        i++;
      }
    }
    return text.length();
  }

}
