package uno.meng.ner.corpus.io;

import java.io.*;

/**
 * 基于普通文件系统的IO适配器
 *
 */
public class FileIOAdapter implements IIOAdapter {
  @Override
  public InputStream open(String path) throws FileNotFoundException {
    return new FileInputStream(path);
  }

  @Override
  public OutputStream create(String path) throws FileNotFoundException {
    return new FileOutputStream(path);
  }
}
