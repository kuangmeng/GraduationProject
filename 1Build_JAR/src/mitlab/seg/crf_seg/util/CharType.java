package mitlab.seg.crf_seg.util;

import mitlab.seg.crf_seg.util.ByteArray;

/**
 * 字符类型
 *
 */
public class CharType {
  /**
   * 单字节
   */
  public static final byte CT_SINGLE = 5;

  /**
   * 分隔符"!,.?()[]{}+=
   */
  public static final byte CT_DELIMITER = CT_SINGLE + 1;

  /**
   * 中文字符
   */
  public static final byte CT_CHINESE = CT_SINGLE + 2;

  /**
   * 字母
   */
  public static final byte CT_LETTER = CT_SINGLE + 3;

  /**
   * 数字
   */
  public static final byte CT_NUM = CT_SINGLE + 4;

  /**
   * 序号
   */
  public static final byte CT_INDEX = CT_SINGLE + 5;

  /**
   * 其他
   */
  public static final byte CT_OTHER = CT_SINGLE + 12;

  public static byte[] type;

  public static String fname = MengSegConfig.ENV.get("chartype");

  static {
    type = new byte[65536];
    ByteArray byteArray = ByteArray.createByteArray(fname);
    if (byteArray == null) {
      System.err.println("字符类型对应表加载失败：" + fname);
      System.exit(-1);
    } else {
      while (byteArray.hasMore()) {
        int b = byteArray.nextChar();
        int e = byteArray.nextChar();
        byte t = byteArray.nextByte();
        for (int i = b; i <= e; ++i) {
          type[i] = t;
        }
      }
    }
  }

  /**
   * 获取字符的类型
   *
   * @param c
   * @return
   */
  public static byte get(char c) {
    return type[(int) c];
  }
}
