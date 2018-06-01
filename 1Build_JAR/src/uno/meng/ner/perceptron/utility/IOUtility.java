package uno.meng.ner.perceptron.utility;

import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.corpus.io.IOUtil;
import uno.meng.ner.perceptron.instance.Instance;
import uno.meng.ner.perceptron.instance.InstanceHandler;
import uno.meng.ner.perceptron.model.LinearModel;
import java.io.*;
import java.util.regex.Pattern;

public class IOUtility extends IOUtil {
  private static Pattern PATTERN_SPACE = Pattern.compile("\\s+");

  public static String[] readLineToArray(String line) {
    line = line.trim();
    if (line.length() == 0)
      return new String[0];
    return PATTERN_SPACE.split(line);
  }

  public static int loadInstance(final String path, InstanceHandler handler) throws IOException {
    int size = 0;
    File root = new File(path);
    File allFiles[];
    if (root.isDirectory()) {
      allFiles = root.listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          return pathname.isFile() && pathname.getName().endsWith(".txt");
        }
      });
    } else {
      allFiles = new File[] {root};
    }

    for (File file : allFiles) {
      BufferedReader br =
          new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
      String line;
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.length() == 0) {
          continue;
        }
        Sentence sentence = Sentence.create(line);
        if (sentence.wordList.size() == 0)
          continue;
        ++size;
        if (size % 1000 == 0) {
          System.out.println("13语料: " + size / 1000 + "k...");
        }
        // debug
        // if (size == 100) break;
        if (handler.process(sentence))
          break;
      }
    }

    return size;
  }

  public static double[] evaluate(Instance[] instances, LinearModel model) {
    int[] stat = new int[2];
    for (int i = 0; i < instances.length; i++) {
      evaluate(instances[i], model, stat);
      if (i % 100 == 0 || i == instances.length - 1) {
        System.err.printf("%c进度: %.2f%%", 13, (i + 1) / (float) instances.length * 100);
        System.err.flush();
      }
    }
    return new double[] {stat[1] / (double) stat[0] * 100};
  }

  public static void evaluate(Instance instance, LinearModel model, int[] stat) {
    int[] predLabel = new int[instance.length()];
    model.viterbiDecode(instance, predLabel);
    stat[0] += instance.tagArray.length;
    for (int i = 0; i < predLabel.length; i++) {
      if (predLabel[i] == instance.tagArray[i]) {
        ++stat[1];
      }
    }
  }
}
