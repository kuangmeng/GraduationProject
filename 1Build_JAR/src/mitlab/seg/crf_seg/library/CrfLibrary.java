package mitlab.seg.crf_seg.library;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import mitlab.seg.MengSeg;
import mitlab.seg.crf_seg.Model;
import mitlab.seg.crf_seg.dic.PathToStream;
import mitlab.seg.crf_seg.model.CRFModel;
import mitlab.seg.crf_seg.util.MengSegConfig;
import mitlab.seg.crf_seg.util.MengSegMap;
import java.util.Set;

public class CrfLibrary {
  // CRF模型
  private static final Map<String, MengSegMap<String, MengSeg>> CRF = new HashMap<>();

  public static final String DEFAULT = "crf";

  static {
    for (Entry<String, String> entry : MengSegConfig.ENV.entrySet()) {
      if (entry.getKey().startsWith(DEFAULT)) {
        put(entry.getKey(), entry.getValue());
      }
    }
    putIfAbsent(DEFAULT, "data/seg/model/crf.model");
  }

  public static MengSeg get() {
    return get(DEFAULT);
  }

  /**
   * 根据key获取crf分词器
   * 
   * @param key
   * @return crf分词器
   */
  public static MengSeg get(String key) {

    MengSegMap<String, MengSeg> kv = CRF.get(key);

    if (kv == null) {
      if (MengSegConfig.ENV.containsKey(key)) {
        putIfAbsent(key, MengSegConfig.ENV.get(key));
        return get(key);
      }
      return null;
    }

    MengSeg sw = kv.getV();
    if (sw == null) {
      sw = initCRFModel(kv);
    }
    return sw;
  }

  /**
   * 加载CRF模型
   * 
   * @param modelPath
   * @return
   */
  private static synchronized MengSeg initCRFModel(MengSegMap<String, MengSeg> kv) {
    try {
      if (kv.getV() != null) {
        return kv.getV();
      }

      try (InputStream is = PathToStream.stream(kv.getK())) {
        MengSeg crfSplitWord = new MengSeg(Model.load(CRFModel.class, is));
        kv.setV(crfSplitWord);
        return crfSplitWord;
      }
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 动态添加
   * 
   * @param dicDefault
   * @param dicDefault2
   * @param dic2
   */
  public static void put(String key, String path) {

    put(key, path, null);
  }

  public static void put(String key, String path, MengSeg sw) {
    CRF.put(key, MengSegMap.with(path, sw));
    MengSegConfig.ENV.put(key, path);
  }

  /**
   * 删除一个key
   * 
   * @param key
   * @return
   */
  public static MengSegMap<String, MengSeg> remove(String key) {
    MengSegConfig.ENV.remove(key);
    return CRF.remove(key);
  }

  /**
   * 刷新一个,将值设置为null
   * 
   * @param key
   * @return
   */
  public static void reload(String key) {
    MengSegMap<String, MengSeg> kv = CRF.get(key);
    if (kv != null) {
      CRF.get(key).setV(null);
    }
  }

  public static Set<String> keys() {
    return CRF.keySet();
  }

  public static void putIfAbsent(String key, String path) {
    if (!CRF.containsKey(key)) {
      CRF.put(key, MengSegMap.with(path, (MengSeg) null));
    }
  }
}
