package uno.meng.testsuite;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.List;
import org.junit.Test;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;

public class testFinalResult {

  public static MengSeg crfSplitWord = CrfLibrary.get();

  @Test
  public void test() {
    try {
      File f = new File("test_2.txt");
      File f2 = new File("ret_2.txt");

      BufferedReader bre = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      Writer w = new FileWriter(f2);
      String str = null;
      while ((str = bre.readLine()) != null) {
        str = str.trim();
        List<String> tmp = seg(str);
        System.out.println(tmp);
        if (tmp == null) {
          w.write("\n");
        } else {
          for (int i = 0; i < tmp.size() - 1; i++) {
            w.write(tmp.get(i) + "\t");;// 写到文件
          }
          w.write(tmp.get(tmp.size() - 1) + "\n");
        }
      }

      w.close();
      bre.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  

  public static List<String> seg(String sen) {
    if (sen == "" || sen.equals("") || sen == null) {
      return null;
    } else {
      List<String> termList = crfSplitWord.Seg(sen);
      return termList;
    }
  }

}
