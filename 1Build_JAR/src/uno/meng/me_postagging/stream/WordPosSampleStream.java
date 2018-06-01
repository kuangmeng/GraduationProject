package uno.meng.me_postagging.stream;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;
import uno.meng.me_postagging.parse.WordPosParseContext;
import uno.meng.me_postagging.parse.WordPosParseNews;

/**
 * 读取文件流，并解析成要的格式返回
 *
 */
public class WordPosSampleStream extends FilterObjectStream<String, WordPosSample> {

  /**
   * 有参构造函数
   * 
   * @param samples 读取的一行未解析的样本流
   */
  public WordPosSampleStream(ObjectStream<String> samples) {
    super(samples);

  }

  /**
   * 读取一行的内容，并解析成词，词性的格式
   * 
   * @return 返回解析之后的结果
   */
  public WordPosSample read() throws IOException {

    String sentence = samples.read();
    WordPosParseContext context = new WordPosParseContext(new WordPosParseNews(), sentence);
    WordPosSample sample = null;
    if (sentence != null) {
      if (sentence.compareTo("") != 0) {
        try {
          // System.out.println(sentences);
          sample = context.parseSample();;
        } catch (Exception e) {
          sample = new WordPosSample(new String[] {}, new String[] {}, new String[] {});
        }

        return sample;
      } else {
        sample = new WordPosSample(new String[] {}, new String[] {}, new String[] {});
        return sample;
      }
    } else {
      return null;
    }

  }

}
