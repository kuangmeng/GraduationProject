package mitlab.seg.me_postagging.parse;

import mitlab.seg.me_postagging.stream.WordPosSample;

/**
 * 解析语料接口
 *
 */
public interface WordPosParseStrage {

  /**
   * 解析语料读取的一条语句
   * 
   * @param sentence 要解析的句子
   * @return WordSegPosSample格式的语料信息
   */
  public WordPosSample parse(String sentence);
}
