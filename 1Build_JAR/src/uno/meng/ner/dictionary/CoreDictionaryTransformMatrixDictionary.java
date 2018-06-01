package uno.meng.ner.dictionary;

import uno.meng.ner.corpus.tag.Nature;

/**
 * 核心词典词性转移矩阵
 */
public class CoreDictionaryTransformMatrixDictionary {
  public static TransformMatrixDictionary<Nature> transformMatrixDictionary;
  static {
    transformMatrixDictionary = new TransformMatrixDictionary<Nature>(Nature.class);
    long start = System.currentTimeMillis();
    if (!transformMatrixDictionary
        .load(uno.meng.Constants.CoreDictionaryTransformMatrixDictionaryPath)) {
      throw new IllegalArgumentException(
          "加载核心词典词性转移矩阵" + uno.meng.Constants.CoreDictionaryTransformMatrixDictionaryPath + "失败");
    } else {
      System.out
          .println("加载核心词典词性转移矩阵" + uno.meng.Constants.CoreDictionaryTransformMatrixDictionaryPath
              + "成功，耗时：" + (System.currentTimeMillis() - start) + " ms");
    }
  }
}
