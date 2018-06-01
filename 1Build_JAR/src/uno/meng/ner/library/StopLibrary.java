package uno.meng.ner.library;

import uno.meng.crf_seg.dic.PathToStream;
import uno.meng.crf_seg.util.MengSegConfig;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;
import uno.meng.crf_seg.util.MengSegMap;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StopLibrary {


  public static final String DEFAULT = "stop";

  // 用户自定义词典
  private static final Map<String, MengSegMap<String, StopWord>> STOP = new HashMap<>();

  static {
    for (Entry<String, String> entry : MengSegConfig.ENV.entrySet()) {
      if (entry.getKey().startsWith(DEFAULT)) {
        put(entry.getKey(), entry.getValue());
      }
    }
    putIfAbsent(DEFAULT, "data/dictionary/seg/stop.dic");
  }

  /**
   * 词性过滤
   * 
   * @param key
   * @param filterNatures
   */
  public static void insertStopNatures(String key, String... filterNatures) {
    StopWord fr = get(key);
    fr.insertStopNatures(filterNatures);
  }

  /**
   * 正则过滤
   * 
   * @param key
   * @param regexes
   */
  public static void insertStopRegexes(String key, String... regexes) {
    StopWord fr = get(key);
    fr.insertStopRegexes(regexes);
  }

  /**
   * 增加停用词
   * 
   * @param key
   * @param regexes
   */
  public static void insertStopWords(String key, String... stopWords) {
    StopWord fr = get(key);
    fr.insertStopWords(stopWords);
  }

  /**
   * 增加停用词
   * 
   * @param key
   * @param regexes
   */
  public static void insertStopWords(String key, List<String> stopWords) {
    StopWord fr = get(key);
    fr.insertStopWords(stopWords);
  }

  public static StopWord get() {
    return get(DEFAULT);
  }

  /**
   * 根据模型名称获取crf模型
   * 
   * @param modelName
   * @return
   */
  public static StopWord get(String key) {
    MengSegMap<String, StopWord> kv = STOP.get(key);

    if (kv == null) {
      if (MengSegConfig.ENV.containsKey(key)) {
        putIfAbsent(key, MengSegConfig.ENV.get(key));
        return get(key);
      }
      System.out.println("STOP " + key + " not found in config ");
      return null;
    }
    StopWord stopWord = kv.getV();
    if (stopWord == null) {
      stopWord = init(key, kv, false);
    }
    return stopWord;

  }

  /**
   * 用户自定义词典加载
   * 
   * @param key
   * @param path
   * @return
   */
  private synchronized static StopWord init(String key, MengSegMap<String, StopWord> kv,
      boolean reload) {
    StopWord stopWord = kv.getV();

    if (stopWord != null) {
      if (reload) {
        stopWord.clear();
      } else {
        return stopWord;
      }
    } else {
      stopWord = new StopWord();
    }

    try {
      System.out.println("begin init FILTER !");
      long start = System.currentTimeMillis();
      String temp = null;
      String[] strs = null;
      try (BufferedReader br = IOUtil.getReader(PathToStream.stream(kv.getK()), "UTF-8")) {
        while ((temp = br.readLine()) != null) {
          if (StringUtil.isNotBlank(temp)) {
            strs = temp.split("\t");

            if (strs.length == 1) {
              stopWord.insertStopWords(strs[0]);
            } else {
              switch (strs[1]) {
                case "nature":
                  stopWord.insertStopNatures(strs[0]);
                  break;
                case "regex":
                  stopWord.insertStopRegexes(strs[0]);
                  break;
                default:
                  stopWord.insertStopWords(strs[0]);
                  break;
              }
            }

          }
        }
      }
      System.out.println(
          "load stop use time:" + (System.currentTimeMillis() - start) + " path is : " + kv.getK());
      kv.setV(stopWord);
      return stopWord;
    } catch (Exception e) {
      System.out.println("Init Stop library error :" + e.getMessage() + ", path: " + kv.getK());
      STOP.remove(key);
      return null;
    }
  }

  /**
   * 动态添加词典
   * 
   * @param FILTERDefault
   * @param FILTERDefault2
   * @param FILTER2
   */
  public static void put(String key, String path, StopWord stopWord) {
    STOP.put(key, MengSegMap.with(path, stopWord));
    MengSegConfig.ENV.put(key, path);
  }

  /**
   * 动态添加词典
   * 
   * @param FILTERDefault
   * @param FILTERDefault2
   * @param FILTER2
   */
  public static void putIfAbsent(String key, String path) {
    if (!STOP.containsKey(key)) {
      STOP.put(key, MengSegMap.with(path, (StopWord) null));
    }
  }

  /**
   * 动态添加词典
   * 
   * @param FILTERDefault
   * @param FILTERDefault2
   * @param FILTER2
   */
  public static void put(String key, String path) {
    put(key, path, null);
  }

  /**
   * 动态添加词典
   * 
   * @param <T>
   * @param <T>
   * 
   * @param FILTERDefault
   * @param FILTERDefault2
   * @param FILTER2
   */
  public static synchronized StopWord putIfAbsent(String key, String path, StopWord stopWord) {
    MengSegMap<String, StopWord> kv = STOP.get(key);
    if (kv != null && kv.getV() != null) {
      return kv.getV();
    }
    put(key, path, stopWord);
    return stopWord;
  }

  public static MengSegMap<String, StopWord> remove(String key) {
    MengSegMap<String, StopWord> kv = STOP.get(key);
    if (kv != null && kv.getV() != null) {
      kv.getV().clear();
    }
    MengSegConfig.ENV.remove(key);
    return STOP.remove(key);
  }

  public static Set<String> keys() {
    return STOP.keySet();
  }

  public static void reload(String key) {

    if (!MengSegConfig.ENV.containsKey(key)) { // 如果变量中不存在直接删掉这个key不解释了
      remove(key);
    }

    putIfAbsent(key, MengSegConfig.ENV.get(key));

    MengSegMap<String, StopWord> kv = STOP.get(key);

    init(key, kv, true);
  }

}
