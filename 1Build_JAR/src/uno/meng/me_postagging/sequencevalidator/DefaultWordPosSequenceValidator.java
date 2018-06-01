package uno.meng.me_postagging.sequencevalidator;

import opennlp.tools.util.SequenceValidator;

/**
 * 序列验证类
 *
 */
public class DefaultWordPosSequenceValidator implements SequenceValidator<String> {

  /**
   * 验证序列是否正确
   * 
   * @param i 当前字符下标
   * @param characters 字符序列
   * @param tagsAndPoses 字符标记和字符的词性标记
   * @param out 得到的下一个字符的输出结果
   */
  @Override
  public boolean validSequence(int i, String[] characters, String[] tagsAndPoses, String out) {
    String temptag = out.split("_")[0];
    String temppos = out.split("_")[1];
    // 保证分词的结果是一样的
    if (!temptag.equals(characters[i].split("_")[1])) {
      return false;
    } else {
      // 如果是开始或者是单个的，词性可以是任意的
      if (temptag.equals("S")) {
        return true;
      } else if (temptag.equals("B")) {
        return true;
      } else if (temptag.equals("M") && temppos.equals(tagsAndPoses[i - 1].split("_")[1])) {
        return true;
      } else if (temptag.equals("E") && temppos.equals(tagsAndPoses[i - 1].split("_")[1])) {
        return true;
      }
    }
    return false;
  }
}
