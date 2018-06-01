package uno.meng.crf_seg.dic.impl;

import org.nlpcn.commons.lang.util.IOUtil;
import uno.meng.crf_seg.dic.PathToStream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

/**
 * 将文件转换为流 file://c:/dic.txt
 */
public class File2Stream extends PathToStream {

  @Override
  public InputStream toStream(String path) {
    if (path.startsWith("file://")) {
      path = path.substring(7);
    }

    File file = new File(path);

    if (file.exists() && file.canRead()) {

      try {
        if (file.isDirectory()) {
          return multiple(path);
        } else {
          return new FileInputStream(file);
        }
      } catch (Exception e) {
        System.out.println("File2Stream Error!");
        return null;
      }
    }
    System.out.println("File2Stream Error!");
    return null;

  }

  private InputStream multiple(String path) throws FileNotFoundException {
    File[] libs = new File[0];

    File file = new File(path);

    if (file.exists() && file.canRead()) {
      if (file.isFile()) {
        libs = new File[1];
        libs[0] = file;
      } else if (file.isDirectory()) {

        File[] files = file.listFiles(new FileFilter() {
          @Override
          public boolean accept(File file) {
            return file.canRead() && !file.isHidden() && !file.isDirectory();
          }
        });

        if (files != null && files.length > 0) {
          libs = files;
        }
      }
    }

    if (libs.length == 0) {
      System.out.println("File2Stream Error!");
    }

    if (libs.length == 1) {
      return new FileInputStream(libs[0]);
    }

    Vector<InputStream> vector = new Vector<>(libs.length);

    for (int i = 0; i < libs.length; i++) {
      vector.add(new FileInputStream(libs[i]));

      if (i < libs.length - 1) {
        vector.add(new ByteArrayInputStream(IOUtil.LINE.getBytes(StandardCharsets.UTF_8)));
      }
    }

    return new SequenceInputStream(vector.elements());
  }

}
