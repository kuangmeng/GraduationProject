package mitlab.seg.ner.tokenizer.lexical;

import java.util.List;

/**
 * 分词器接口
 */
public interface Segmenter {
  /**
   * 中文分词
   *
   * @param text 文本
   * @return 词语
   */
  List<String> segment(String text);

  void segment(String text, String normalized, List<String> output);
}
