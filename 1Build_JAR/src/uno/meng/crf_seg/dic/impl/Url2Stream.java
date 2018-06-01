package uno.meng.crf_seg.dic.impl;

import uno.meng.crf_seg.dic.PathToStream;
import java.io.InputStream;
import java.net.URL;

/**
 * url://http://maven.nlpcn.org/down/library/default.dic
 */
public class Url2Stream extends PathToStream {

  @Override
  public InputStream toStream(String path) {
    try {
      URL url = new URL(path);
      return url.openStream();
    } catch (Exception e) {
      System.out.println("Url2Stream Error!");
      return null;
    }

  }

}
