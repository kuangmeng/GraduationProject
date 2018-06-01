package uno.meng.crf_seg.dic.impl;

import uno.meng.crf_seg.dic.DicReader;
import uno.meng.crf_seg.dic.PathToStream;
import java.io.InputStream;

/**
 * 从系统jar包中读取文件，你们不能用，只有我能用 jar://uno.meng.crf_seg.dic.DicReader|/crf.model
 */
public class Jar2Stream extends PathToStream {

  @Override
  public InputStream toStream(String path) {
    if (path.contains("|")) {
      String[] split = path.split("\\|");
      try {
        return Class.forName(split[0].substring(6)).getResourceAsStream(split[1].trim());
      } catch (ClassNotFoundException e) {
        System.out.print("Jar2Stream Error!");
        return null;
      }
    } else {
      return DicReader.getInputStream(path.substring(6));
    }
  }

}
