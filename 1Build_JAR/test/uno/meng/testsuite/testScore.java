package uno.meng.testsuite;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.junit.Test;
import org.nlpcn.commons.lang.util.StringUtil;
import uno.meng.MengSeg;
import uno.meng.crf_seg.library.CrfLibrary;

public class testScore {
  
  private static String testPath = "test.txt";
  private String interval = "\t";
  @Test
  public void test() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(testPath)));
    MengSeg ms = CrfLibrary.get();
    String temp_str = null;
    int line_number = 0;// 记录行数
    int ansj_term_number = 0;// 记录分出的term数量
    int result_num = 0;
    
    double P = 0.0;
    double R = 0.0;
    double F = 0.0;

    int allSuccess = 0;

    String[] had_words_array = null;// 按split分开后的数组
    String body = null;
    while ((temp_str = br.readLine()) != null) {

      if (StringUtil.isBlank(temp_str)) {
        continue;
      }

      int error = 0;
      int success = 0;
      temp_str = temp_str.trim();
      body = temp_str.replaceAll(interval, "");
      had_words_array = new String[body.length()];
      int offe = 0;

      List<String> paser = ms.Seg(body);
      String[] result = temp_str.split(interval);
      for (int i = 0; i < result.length; i++) {
        had_words_array[offe] = result[i];
        offe += result[i].length();
      }

      offe = 0;
      for (String word : paser) {
        if (had_words_array[offe] == null) {
          error++;
        } else if (had_words_array[offe].equalsIgnoreCase(word)) {
          success++;
        } else {
          success++;
        }
        offe += word.length();
      }

      // 分出的个数
      ansj_term_number += paser.size();
      // 词语的个数
      result_num += result.length;
      // 累计正确数
      allSuccess += success;

      if (error > 0) {
        System.out.println("标准答案:" + temp_str);
        System.out.println(
            "我的结果:" + paser.toString().replace("[", "").replace("]", "").replace(", ", interval));
        System.out.println("[" + line_number + "]P:" + ((double) success / paser.size()));
      }
      line_number++;
    }
    
    // 正确数/总词数
    P = allSuccess / (double) ansj_term_number;
    // 正确数/标注文本中的词数
    R = allSuccess / (double) result_num;
    F = (2 * P * R) / (P + R);
    
    System.out.println("\nP:" + P);
    System.out.println("R:" + R);
    System.out.println("F:" + F);
    br.close();
  }

}
