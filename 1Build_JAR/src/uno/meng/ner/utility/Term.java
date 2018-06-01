
package uno.meng.ner.utility;

import uno.meng.ner.corpus.tag.Nature;
import uno.meng.ner.utility.LexiconUtility;

/**
 * 一个单词，用户可以直接访问此单词的全部属性
 */
public class Term {
  /**
   * 词语
   */
  public String word;

  /**
   * 词性
   */
  public Nature nature;

  /**
   * 在文本中的起始位置（需开启分词器的offset选项）
   */
  public int offset;

  /**
   * 构造一个单词
   * 
   * @param word 词语
   * @param nature 词性
   */
  public Term(String word, Nature nature) {
    this.word = word;
    this.nature = nature;
  }

  @Override
  public String toString() {
    if (false)
      return word + "/" + nature;
    return word;
  }

  /**
   * 长度
   * 
   * @return
   */
  public int length() {
    return word.length();
  }

  /**
   * 
   * @return 频次，0代表这是个OOV
   */
  public int getFrequency() {
    return LexiconUtility.getFrequency(word);
  }


  /**
   * 判断Term是否相等
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Term) {
      Term term = (Term) obj;
      if (this.nature == term.nature && this.word.equals(term.word)) {
        return true;
      }
    }
    return super.equals(obj);
  }
}
