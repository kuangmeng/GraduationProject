package uno.meng.me_postagging.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.util.InputStreamFactory;

/**
 * 获取输入文件流的工厂类
 *
 */
public class FileInputStreamFactory implements InputStreamFactory {

  File file;

  /**
   * 获取样本流
   * 
   * @return 样本流
   * @throws FileNotFoundException
   */
  public FileInputStreamFactory(File file) throws FileNotFoundException {
    if (!file.exists()) {
      throw new FileNotFoundException("File '" + file + "' cannot be found");
    }
    this.file = file;
  }

  /**
   * 获取样本流
   * 
   * @return 样本流
   */
  public InputStream createInputStream() throws IOException {

    return new FileInputStream(file);
  }
}
