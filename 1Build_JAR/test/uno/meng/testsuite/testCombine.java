package uno.meng.testsuite;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;
import uno.meng.ner.domain.Term;

public class testCombine {
  
  public static MengSeg crfSplitWord = CrfLibrary.get();

  @Test
  public void test() {
    List<String> tmp = crfSplitWord.Seg("国办发布网络提速降费十四条指导意见鼓励流量不清零。");
    Term a3 = new Term("办发布", 1);
    Term a1 = new Term("十四条", 10);
    Term a2 = new Term("不清零", 21);
    List<Term> tmp2 = new ArrayList<Term>();
    tmp2.add(a3);
    tmp2.add(a1);
    tmp2.add(a2);
    List<String> tmp3 = crfSplitWord.segTemp(tmp, tmp2, 25);
    System.out.println(tmp3);
  }

}
