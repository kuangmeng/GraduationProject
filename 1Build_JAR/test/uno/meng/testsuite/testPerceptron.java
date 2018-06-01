package uno.meng.testsuite;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;
import uno.meng.ner.perceptron.PerceptionNERecognizer;
import uno.meng.ner.perceptron.PerceptronPOSTagger;
import uno.meng.ner.perceptron.PerceptronSegmenter;

public class testPerceptron {

  @Test
  public void testCWSLearner() throws IOException {
    PerceptronSegmenter segmenter = new PerceptronSegmenter(uno.meng.Constants.CWSMODEL);
    segmenter.learn("下雨天 地面 积水");
    System.out.println(segmenter.segment("下雨天地面积水分外严重"));
  }
  
  @Test
  public void testPOSTagging() throws IOException {
    PerceptronPOSTagger tagger = new PerceptronPOSTagger(uno.meng.Constants.POSMODEL);
    System.out.println(Arrays.toString(tagger.tag("中国 交响乐团 谭利华 在 布达拉宫 广场 演出".split(" "))));
  }

  @Test
  public void testNER() throws IOException {
    PerceptionNERecognizer recognizer = new PerceptionNERecognizer(uno.meng.Constants.NERMODEL);
    System.out.println(Arrays.toString(
        recognizer.recognize("吴忠市 公司 谭利华 来到 布达拉宫 广场".split(" "), "ns n nr p ns n".split(" "))));
  }
}
