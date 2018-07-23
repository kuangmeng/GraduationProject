package mitlab.seg.crf_seg.dic;

import java.io.InputStream;
import mitlab.seg.crf_seg.dic.impl.File2Stream;
import mitlab.seg.crf_seg.dic.impl.Jar2Stream;
import mitlab.seg.crf_seg.dic.impl.Url2Stream;

/**
 * 将路径转换为流
 *
 */
public abstract class PathToStream {

  public static InputStream stream(String path) {
    try {
      if (path.startsWith("file://")) {
        return new File2Stream().toStream(path);
      } else if (path.startsWith("jar://")) {
        return new Jar2Stream().toStream(path);
      } else if (path.startsWith("class://")) {
        return ((PathToStream) Class.forName(path.substring(8).split("\\|")[0]).newInstance())
            .toStream(path);
      } else if (path.startsWith("http://") || path.startsWith("https://")) {
        return new Url2Stream().toStream(path);
      } else {
        return new File2Stream().toStream(path);
      }
    } catch (Exception e) {
      System.out.println("Path2Stream Error!");
      return null;
    }
  }

  public abstract InputStream toStream(String path);

}
