package mitlab.seg.ner.corpus.document.sentence.word;

import java.io.Serializable;

/**
 * 词语接口
 */
public interface IWord extends Serializable {
  /**
   * 获取单词
   * 
   * @return
   */
  String getValue();

  /**
   * 获取标签
   * 
   * @return
   */
  String getLabel();

  /**
   * 设置标签
   * 
   * @param label
   */
  void setLabel(String label);

  /**
   * 设置单词
   * 
   * @param value
   */
  void setValue(String value);

  /**
   * 单词长度
   * 
   * @return
   */
  int length();
}
