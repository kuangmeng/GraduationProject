package mitlab.seg.crf_seg.dic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 加载词典用的类
 */
public class DicReader {


  public static BufferedReader getReader(String name) {
    File f = new File(name);
    InputStream in = null;
    try {
      in = new FileInputStream(f);
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try {
      return new BufferedReader(new InputStreamReader(in, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      System.out.println("不支持的编码");
    }
    return null;
  }

  public static InputStream getInputStream(String name) {
    File f = new File(name);
    InputStream in = null;
    try {
      in = new FileInputStream(f);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return in;
  }
}
