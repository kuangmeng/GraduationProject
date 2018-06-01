package uno.meng.crf_seg.util;

import org.nlpcn.commons.lang.util.FileFinder;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.ObjConver;
import org.nlpcn.commons.lang.util.StringUtil;
import uno.meng.crf_seg.dic.DicReader;
import uno.meng.ner.domain.SegItem;
import uno.meng.ner.library.DATDictionary;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 这个类储存一些公用变量.
 * 
 */
public class MengSegConfig {

  public static final Map<String, String> ENV = new HashMap<>();

  static {
    /**
     * 配置文件变量
     */
    ResourceBundle rb = null;
    try {
      rb = ResourceBundle.getBundle("MengSeg");
    } catch (Exception e) {
      try {
        File find = FileFinder.find("MengSeg.properties", 1);
        if (find != null && find.isFile()) {
          rb = new PropertyResourceBundle(
              IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
        }
      } catch (Exception e1) {
      }
    }
    if (rb == null) {
    } else {

      for (String key : rb.keySet()) {
        ENV.put(key, rb.getString(key));
        try {
          String value = rb.getString(key);

          Field field = MengSegConfig.class.getField(key);
          field.set(null, ObjConver.conversion(rb.getString(key), field.getType()));
        } catch (Exception e) {
        }
      }

    }
  }


  /**
   * 人名词典
   *
   * @return
   */
  public static BufferedReader getPersonReader() {
    return DicReader.getReader("data/ner/person/person.dic");
  }

  /**
   * 机构名词典
   *
   * @return
   */
  public static BufferedReader getCompanReader() {
    return DicReader.getReader("data/ner/company/company.data");
  }

  /**
   * 机构名词典
   *
   * @return
   */
  public static BufferedReader getNewWordReader() {
    return DicReader.getReader("data/ner/newWord/new_word_freq.dic");
  }

  /**
   * 核心词典
   *
   * @return
   */
  public static BufferedReader getArraysReader() {
    return DicReader.getReader("data/dictionary/arrays.dic");
  }

  /**
   * 数字词典
   *
   * @return
   */
  public static BufferedReader getNumberReader() {
    return DicReader.getReader("data/ner/number/numberLibrary.dic");
  }


  /**
   * 词性表
   *
   * @return
   */
  public static BufferedReader getNatureMapReader() {
    return DicReader.getReader("data/ner/nature/nature.map");
  }

  /**
   * 词性关联表
   *
   * @return
   */
  public static BufferedReader getNatureTableReader() {
    return DicReader.getReader("data/ner/nature/nature.table");
  }

  /**
   * 人名识别
   *
   * @return
   */
  public static BufferedReader getPersonDicReader()
      throws FileNotFoundException, UnsupportedEncodingException {
    return IOUtil
        .getReader(new SequenceInputStream(DicReader.getInputStream("data/ner/person/person.txt"),
            DicReader.getInputStream("data/ner/person/person_split.txt")), "UTF-8");
  }

  /**
   * 人名识别
   *
   * @return
   */
  public static BufferedReader getForeignDicReader()
      throws FileNotFoundException, UnsupportedEncodingException {
    return IOUtil
        .getReader(new SequenceInputStream(DicReader.getInputStream("data/ner/person/foreign.txt"),
            DicReader.getInputStream("data/ner/person/person_split.txt")), "UTF-8");
  }



  /**
   * 词与词之间的关联表数据
   *
   * @return
   */
  public static void initBigramTables() {
    try (BufferedReader reader =
        IOUtil.getReader(DicReader.getInputStream("data/ner/relation/bigramdict.dic"), "UTF-8")) {
      String temp = null;
      String[] strs = null;
      int freq = 0;
      while ((temp = reader.readLine()) != null) {
        if (StringUtil.isBlank(temp)) {
          continue;
        }
        strs = temp.split("\t");
        freq = Integer.parseInt(strs[1]);
        strs = strs[0].split("@");
        SegItem fromItem = DATDictionary.getItem(strs[0]);

        SegItem toItem = DATDictionary.getItem(strs[1]);

        if (fromItem == SegItem.NULL && strs[0].contains("#")) {
          fromItem = SegItem.BEGIN;
        }

        if (toItem == SegItem.NULL && strs[1].contains("#")) {
          toItem = SegItem.END;
        }

        if (fromItem == SegItem.NULL || toItem == SegItem.NULL) {
          continue;
        }

        if (fromItem.bigramEntryMap == null) {
          fromItem.bigramEntryMap = new HashMap<Integer, Integer>();
        }

        fromItem.bigramEntryMap.put(toItem.getIndex(), freq);

      }
    } catch (NumberFormatException e) {
      System.out.println("数字格式异常");
    } catch (UnsupportedEncodingException e) {
      System.out.println("不支持的编码");
    } catch (IOException e) {
      System.out.println("IO异常");
    }
  }


}
