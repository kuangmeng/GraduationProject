package mitlab.seg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import mitlab.seg.ner.whole_NER;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.perceptron.PerceptionNERecognizer;
import mitlab.seg.ner.perceptron.PerceptronPOSTagger;

public class MengNER {
  public static ArrayList<String> words;
  public static ArrayList<String> tags;
  public static ArrayList<String> poses;

  public static List<Term> NER(String str, int choose) throws IOException {
    return whole_NER.NER(str, choose);
  }

  public static List<Term> NER(String str) throws IOException {
    return whole_NER.NER(str, 0);
  }


}
