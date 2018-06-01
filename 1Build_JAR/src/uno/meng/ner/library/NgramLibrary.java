package uno.meng.ner.library;

import uno.meng.crf_seg.util.MengSegConfig;
import uno.meng.ner.domain.Term;

/**
 * 两个词之间的关联
 * 
 * 
 */
public class NgramLibrary {
  static {
    long start = System.currentTimeMillis();
    MengSegConfig.initBigramTables();
    // System.out.println("init ngram ok use time :" + (System.currentTimeMillis() - start));
  }

  /**
   * 查找两个词与词之间的频率
   * 
   * @param from
   * @param to
   * @return
   */
  public static int getTwoWordFreq(Term from, Term to) {
    if (from.item().bigramEntryMap == null) {
      return 0;
    }
    Integer freq = from.item().bigramEntryMap.get(to.item().getIndex());
    if (freq == null) {
      return 0;
    } else {
      return freq;
    }
  }

}
