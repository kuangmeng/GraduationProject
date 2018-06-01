package uno.meng.TRAIN;

import uno.meng.crf_pp.learn.CrfLearn;

public class TrainCRF4Seg {
  public static void main(String[] args) {
    String[] options = {"-f", "3", "-c", "4.0", "-t"};
    testLearnModel(options);
  }

  public static void testLearnModel(String[] option) {
    String template = "data/seg/train/template";
    String train = "data/seg/train/train.txt";
    String result = "data/seg/model/crf.model";
    String[] args = new String[] {template, train, result};
    if (option != null) {
      String[] newargs = new String[option.length + args.length];
      for (int i = 0; i < option.length; i++) {
        newargs[i] = option[i];
      }
      for (int i = 0; i < args.length; i++) {
        newargs[option.length + i] = args[i];
      }
      args = newargs;
    }
    CrfLearn.run(args);
  }
}
