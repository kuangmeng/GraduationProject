package mitlab.seg.ner.perceptron.common;

import java.util.TreeMap;


public class FrequencyMap extends TreeMap<String, Integer> {
  public int totalFrequency;

  public int add(String word) {
    ++totalFrequency;
    Integer frequency = get(word);
    if (frequency == null) {
      put(word, 1);
      return 1;
    } else {
      put(word, ++frequency);
      return frequency;
    }
  }
}
