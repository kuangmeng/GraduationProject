package uno.meng.me_postagging.model;

/**
 * 词性标记的接口
 *
 */
public interface WordPos {

  /**
   * 对分词之后的结果进行词性标记
   * 
   * @param words 分词之后的一句话
   * @return
   */
  public String[] wordpos(String words);

  /**
   * 对分词之后的结果进行词性标记
   * 
   * @param words 分词之后的词语数组
   * @return
   */
  public String[] wordpos(String[] words);
}
