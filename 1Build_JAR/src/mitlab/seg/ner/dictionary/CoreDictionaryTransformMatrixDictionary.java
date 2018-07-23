package mitlab.seg.ner.dictionary;

import mitlab.seg.ner.corpus.tag.Nature;

/**
 * 核心词典词性转移矩阵
 */
public class CoreDictionaryTransformMatrixDictionary {
  public static TransformMatrixDictionary<Nature> transformMatrixDictionary;
  static {
    transformMatrixDictionary = new TransformMatrixDictionary<Nature>(Nature.class);
    long start = System.currentTimeMillis();
    if (!transformMatrixDictionary
        .load(mitlab.seg.Constants.CoreDictionaryTransformMatrixDictionaryPath)) {
      throw new IllegalArgumentException(
          "加载核心词典词性转移矩阵" + mitlab.seg.Constants.CoreDictionaryTransformMatrixDictionaryPath + "失败");
    } else {
      System.out
          .println("加载核心词典词性转移矩阵" + mitlab.seg.Constants.CoreDictionaryTransformMatrixDictionaryPath
              + "成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
    }
  }
}
