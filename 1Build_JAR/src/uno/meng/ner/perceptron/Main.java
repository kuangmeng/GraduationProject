package uno.meng.ner.perceptron;

import uno.meng.Constants;
import uno.meng.ner.corpus.document.sentence.Sentence;
import uno.meng.ner.perceptron.cli.Args;
import uno.meng.ner.perceptron.cli.Argument;
import uno.meng.ner.perceptron.common.TaskType;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import static java.lang.System.out;


public class Main {
  private static class Option {
    @Argument(description = "任务类型:CWS|POS|NER")
    TaskType task = TaskType.CWS;

    @Argument(description = "执行训练任务")
    boolean train;

    @Argument(description = "执行预测任务")
    boolean test;

    @Argument(description = "执行评估任务")
    boolean evaluate;

    @Argument(description = "模型文件路径")
    String[] model = new String[] {uno.meng.Constants.CWSMODEL, uno.meng.Constants.POSMODEL,
        uno.meng.Constants.NERMODEL};

    @Argument(description = "输入文本路径")
    String input;

    @Argument(description = "结果保存路径")
    String result;

    @Argument(description = "标准分词语料")
    String gold;

    @Argument(description = "训练集")
    String reference;

    @Argument(description = "开发集")
    String development;

    @Argument(description = "迭代次数")
    Integer iter = 5;

    @Argument(description = "模型压缩比率")
    Double compressRatio = 0.0;

    @Argument(description = "线程数")
    Integer thread = Runtime.getRuntime().availableProcessors();
  }

  public static void main(String[] args) {
    // nohup time java -jar averaged-perceptron-segment-1.0.jar -train -model 2014_2w.bin -reference
    // 2014_blank.txt -development 2014_1k.txt > log.txt
    Option option = new Option();
    try {
      Args.parse(option, args);
      PerceptronTrainer trainer = null;
      switch (option.task) {
        case CWS:
          trainer = new CWSTrainer();
          break;
        case POS:
          trainer = new POSTrainer();
          break;
        case NER:
          trainer = new NERTrainer();
          break;
      }
      if (option.train) {
        trainer.train(option.reference, option.development, option.model[0], option.compressRatio,
            option.iter, option.thread);
      } else if (option.evaluate) {
        double[] prf = trainer.evaluate(option.gold, option.model[0]);
        out.printf("Performance - P:%.2f R:%.2f F:%.2f\n", prf[0], prf[1], prf[2]);
      }

    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
      Args.usage(option);
    } catch (IOException e) {
      System.err.println("发生了IO异常，请检查文件路径");
      e.printStackTrace();
    }
  }
}
