package mitlab.seg.ner.utility;

import java.util.Map;
import java.util.TreeMap;
import mitlab.seg.ner.corpus.io.IOUtil;

/**
 * 词性标注集中英映射表
 *
 * @author hankcs
 */
public class PartOfSpeechTagDictionary {
  /**
   * 词性映射表
   */
  public static Map<String, String> translator = new TreeMap<String, String>();

  static {
    load(mitlab.seg.Constants.PartOfSpeechTagDictionary);
  }

  public static void load(String path) {
    IOUtil.LineIterator iterator = new IOUtil.LineIterator(path);
    iterator.next(); // header
    while (iterator.hasNext()) {
      String[] args = iterator.next().split(",");
      if (args.length < 3)
        continue;
      translator.put(args[1], args[2]);
    }
  }

  /**
   * 翻译词性
   *
   * @param tag
   * @return
   */
  public static String translate(String tag) {
    String cn = translator.get(tag);
    if (cn == null)
      return tag;
    return cn;
  }
}
