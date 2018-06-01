package uno.meng.me_postagging.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 解析后文本样式类
 *
 */
public class WordPosSample {

  public List<String> characters;
  public List<String> words;
  public List<String> tagsAndposes;
  private String[][] addtionalContext;

  /**
   * 构造
   * 
   * @param characters 字符
   * @param tags 字符标记序列
   * @param words 词语
   * @param poses 词性
   */
  public WordPosSample(String[] characters, String[] words, String[] tagsAndposes) {
    this(characters, words, tagsAndposes, null);
  }

  /**
   * 构造
   * 
   * @param characters 字符
   * @param tags 字符标记序列
   * @param words 词语
   * @param poses 词性
   */
  public WordPosSample(List<String> characters, List<String> words, List<String> tagsAndposes) {
    this(characters, words, tagsAndposes, null);
  }

  /**
   * 构造
   * 
   * @param characters 字符
   * @param tags 字符标记序列
   * @param words 词语
   * @param poses 词性
   * @param additionalContext
   */
  public WordPosSample(String[] characters, String[] words, String[] tagsAndposes,
      String[][] additionalContext) {
    this(Arrays.asList(characters), Arrays.asList(words), Arrays.asList(tagsAndposes),
        additionalContext);
  }

  /**
   * 构造
   * 
   * @param characters 字符
   * @param tags 字符标记序列
   * @param words 词语
   * @param poses 词性
   * @param additionalContext
   */
  public WordPosSample(List<String> characters, List<String> words, List<String> tagsAndposes,
      String[][] additionalContext) {
    this.characters = Collections.unmodifiableList(characters);
    this.words = Collections.unmodifiableList(words);
    this.tagsAndposes = Collections.unmodifiableList(tagsAndposes);

    String[][] ac;
    if (additionalContext != null) {
      ac = new String[additionalContext.length][];

      for (int i = 0; i < additionalContext.length; i++) {
        ac[i] = new String[additionalContext[i].length];
        System.arraycopy(additionalContext[i], 0, ac[i], 0, additionalContext[i].length);
      }
    } else {
      ac = null;
    }
    this.addtionalContext = ac;
  }

  /**
   * 得到词语
   * 
   * @return
   */
  public String[] getWords() {
    return this.words.toArray(new String[words.size()]);
  }

  /**
   * 返回分词加上词性的标记
   * 
   * @return
   */
  public String[] getTagsAndPoses() {
    return this.tagsAndposes.toArray(new String[tagsAndposes.size()]);
  }

  /**
   * 获得字符
   * 
   * @return
   */
  public String[] getCharacters() {
    return this.characters.toArray(new String[characters.size()]);
  }

  /**
   * 额外的信息
   * 
   * @return
   */
  public String[][] getAditionalContext() {
    return this.addtionalContext;
  }

  /**
   * 得到对应的词性序列
   * 
   * @param tagsandposesPre 字的边界_词性 这种格式组成的序列
   * @return
   */
  public static String[] toPos(String[] tagsandposesPre) {
    List<String> poses = new ArrayList<String>();
    for (int i = 0; i < tagsandposesPre.length; i++) {
      String tag = tagsandposesPre[i].split("_")[0];
      String pos = tagsandposesPre[i].split("_")[1];
      if (tag.equals("B")) {
        poses.add(pos);
      } else if (tag.equals("M")) {

      } else if (tag.equals("E")) {

      } else if (tag.equals("S")) {
        poses.add(pos);
      }
    }
    return poses.toArray(new String[poses.size()]);
  }

  /**
   * 得到和输入样本一致的样式
   * 
   * @return
   */
  public String toSample() {
    String sample = "";
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    for (int i = 0; i < tagsAndposes.size(); i++) {
      String[] str = tagsAndposes.get(i).split("_");
      if (str[0].equals("B")) {
        list1.add(words.get(i));
        list2.add(str[1]);
      } else if (str[0].equals("M")) {

      } else if (str[0].equals("E")) {

      } else if (str[0].equals("S")) {
        list1.add(words.get(i));
        list2.add(str[1]);
      }
    }
    for (int i = 0; i < list1.size() && i < list2.size(); i++) {
      sample += list1.get(i) + "/" + list1.get(i) + " ";
    }
    return sample;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof WordPosSample) {
      WordPosSample a = (WordPosSample) obj;

      return Arrays.equals(getCharacters(), a.getCharacters())
          && Arrays.equals(getTagsAndPoses(), a.getTagsAndPoses())
          && Arrays.equals(getTagsAndPoses(), a.getTagsAndPoses());
    } else {
      return false;
    }
  }
}
