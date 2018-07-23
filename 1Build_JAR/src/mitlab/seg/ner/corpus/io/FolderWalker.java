package mitlab.seg.ner.corpus.io;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 遍历目录工具类
 */
public class FolderWalker {
  /**
   * 打开一个目录，获取全部的文件名
   * 
   * @param path
   * @return
   */
  public static List<File> open(String path) {
    List<File> fileList = new LinkedList<File>();
    File folder = new File(path);
    handleFolder(folder, fileList);
    return fileList;
  }

  private static void handleFolder(File folder, List<File> fileList) {
    File[] fileArray = folder.listFiles();
    if (fileArray != null) {
      for (File file : fileArray) {
        if (file.isFile() && !file.getName().startsWith(".")) // 过滤隐藏文件
        {
          fileList.add(file);
        } else {
          handleFolder(file, fileList);
        }
      }
    }
  }

  // public static void main(String[] args)
  // {
  // List<File> fileList = FolderWalker.open("D:\\Doc\\语料库\\2014");
  // for (File file : fileList)
  // {
  // System.out.println(file);
  // }
  // }

}
