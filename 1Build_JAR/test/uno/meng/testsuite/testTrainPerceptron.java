package uno.meng.testsuite;

import java.io.IOException;
import org.junit.Test;
import uno.meng.ner.perceptron.CWSTrainer;
import uno.meng.ner.perceptron.NERTrainer;
import uno.meng.ner.perceptron.POSTrainer;
import uno.meng.ner.perceptron.PerceptronSegmenter;
import uno.meng.ner.perceptron.PerceptronTrainer;
import uno.meng.ner.perceptron.tagset.NERTagSet;
import uno.meng.ner.perceptron.tagset.TagSet;

public class testTrainPerceptron {

  @Test
  public void testTrainCWS() throws IOException {
    PerceptronTrainer trainer = new CWSTrainer();
    PerceptronTrainer.Result result =
        trainer.train("data/seg/train/test.txt", uno.meng.Constants.CWSMODEL);
    System.out.printf("准确率F1:%.2f\n", result.getAccuracy());
    PerceptronSegmenter segment = new PerceptronSegmenter(result.getModel());
    // 也可以用
    // Segment segment = new AveragedPerceptronSegment(POS_MODEL_FILE);
    System.out.println(segment.segment("商品与服务"));
    segment.learn("下雨天 地面 积水");
    System.out.println(segment.segment("下雨天地面积水分外严重"));
  }

  @Test
  public void testTrainPOSTagging() throws IOException {
    PerceptronTrainer trainer = new POSTrainer();
    trainer.train("data/seg/train/1998/199801.txt", uno.meng.Constants.POSMODEL);
  }
  
  @Test
  public void testTrainNER() throws IOException {
    PerceptronTrainer trainer = new NERTrainer();
    trainer.train("data/seg/train/1998/199801.txt", uno.meng.Constants.NERMODEL);
  }
  
  @Test
  public void testTrainUserDefineNER() throws IOException {
    PerceptronTrainer trainer = new NERTrainer() {
      @Override
      protected TagSet createTagSet() {
        NERTagSet tagSet = new NERTagSet();
        tagSet.nerLabels.add("ns");
        tagSet.nerLabels.add("nt");
        tagSet.nerLabels.add("nz");
        return tagSet;
      }
    };
    trainer.train("data/seg/train/1998/199801.txt", uno.meng.Constants.NERMODEL);
  }
  
}
