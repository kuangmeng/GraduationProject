package mitlab.seg.ner.perceptron.utility;

import java.util.*;
import mitlab.seg.ner.corpus.tag.Nature;

public class PosTagUtility {
  public static Set<String> tagSet;

  static {
    tagSet = new LinkedHashSet<String>();
    String allTags = "a " + "b " + "c " + "d " + "e " + "f " + "h " + "i " + "j " + "k " + "l "
        + "m " + "n " + "nr " + "ns " + "nt " + "nu " + "ne " + "nz " + "o " + "p " + "q " + "r "
        + "s " + "t " + "u " + "v " + "w " + "x " + "y ";

    String[] tagArray = allTags.split("\\s+");
    Arrays.sort(tagArray, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return new Integer(o2.length()).compareTo(o1.length());
      }
    });
    for (String tag : tagArray) {
      tagSet.add(tag);
    }
  }

  public static String convert(String tag) {
    for (String t : tagSet) {
      if (tag.startsWith(t)) {
        return t;
      }
    }

    return "n";
  }

  public static String convert(Nature tag) {
    return convert(tag.toString());
  }
}
