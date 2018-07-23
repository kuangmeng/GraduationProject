package mitlab.seg;

import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import org.nlpcn.commons.lang.util.logging.Log;
import org.nlpcn.commons.lang.util.logging.LogFactory;
import mitlab.seg.crf_seg.Config;
import mitlab.seg.crf_seg.util.item;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 生成crf训练语聊工具.
 * 
 * 执行:java org.ansj.app.crf.MakeTrainFile [inputPath] [outputPath]
 *
 */
public class MakeTrainFile {


  public static void main(String[] args) {

    String inputPath = "data/seg/train/199802.txt";

    String outputPath = "data/seg/train/train.txt";
    try (BufferedReader reader = IOUtil.getReader(inputPath, "utf-8");
        FileOutputStream fos = new FileOutputStream(outputPath)) {
      String temp = null;
      int i = 0;
      while ((temp = reader.readLine()) != null) {
        StringBuilder sb = new StringBuilder("\n");
        if (StringUtil.isBlank(temp)) {
          continue;
        }
        if (i == 0) {
          temp = StringUtil.trim(temp);
        }
        List<item> list = Config.makeToElementList(temp, "\\s+");
        for (item item : list) {
          sb.append(item.nameStr() + " " + Config.getTagName(item.getTag()));
          sb.append("\n");
        }
        fos.write(sb.toString().getBytes(IOUtil.UTF8));
        System.out.println(++i);
      }
    } catch (FileNotFoundException e) {
      System.out.println("文件没有找到");
    } catch (IOException e) {
      System.out.println("IO异常");
    }
  }

}
