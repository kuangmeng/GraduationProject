package uno.meng.me_postagging.feature;

import opennlp.tools.util.BeamSearchContextGenerator;

/**
 * 上下文特征生成器接口
 *
 */
public interface WordPosContextGenerator extends BeamSearchContextGenerator<String> {

  /**
   * 根据当前下标生成上下文
   * 
   * @param i 当前下标
   * @param characters 当前的字符
   * @param tagAndposes 当前字符的标记及其词性标记
   * @param ac 额外的信息
   * @return
   */
  String[] getContext(int i, String[] characters, String[] tagAndposes, Object[] ac);
}
