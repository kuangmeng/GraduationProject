/*
 * <summary></summary> <author>He Han</author> <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/5/14 21:36</create-date>
 *
 * <copyright file="Predefine.java" company="上海林原信息科技有限公司"> Copyright (c) 2003-2014, 上海林原信息科技有限公司.
 * All Right Reserved, http://www.linrunsoft.com/ This source is subject to the LinrunSpace License.
 * Please contact 上海林原信息科技有限公司 to get more information. </copyright>
 */
package uno.meng.ner.utility;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * 一些预定义的静态全局变量
 */
public class Predefine {

  /**
   * 地址 ns
   */
  public final static String TAG_PLACE = "未##地";
  /**
   * 句子的开始 begin
   */
  public final static String TAG_BIGIN = "始##始";
  /**
   * 其它
   */
  public final static String TAG_OTHER = "未##它";
  /**
   * 团体名词 nt
   */
  public final static String TAG_GROUP = "未##团";
  /**
   * 数词 m
   */
  public final static String TAG_NUMBER = "未##数";
  /**
   * 数量词 mq （现在觉得应该和数词同等处理，比如一个人和一人都是合理的）
   */
  public final static String TAG_QUANTIFIER = "未##量";
  /**
   * 专有名词 nx
   */
  public final static String TAG_PROPER = "未##专";
  /**
   * 时间 t
   */
  public final static String TAG_TIME = "未##时";
  /**
   * 字符串 x
   */
  public final static String TAG_CLUSTER = "未##串";
  /**
   * 结束 end
   */
  public final static String TAG_END = "末##末";
  /**
   * 人名 nr
   */
  public final static String TAG_PEOPLE = "未##人";

}
