package mitlab.seg.crf_seg.model;

import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.IOUtil;
import mitlab.seg.crf_seg.Config;
import mitlab.seg.crf_seg.Model;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * 加载crfmodel,目前此model格式是通过crf++ 或者wapiti生成的
 */

public class CRFModel extends Model {
  @Override
  public CRFModel loadModel(String modelPath) throws Exception {
    try (InputStream is = IOUtil.getInputStream(modelPath)) {
      loadModel(is);
      return this;
    }
  }

  @Override
  public CRFModel loadModel(InputStream is) throws Exception {
    try (ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(is))) {
      ois.readUTF();
      this.status = (float[][]) ois.readObject();
      int[][] template = (int[][]) ois.readObject();
      this.config = new Config(template);
      int win = 0;
      int size = 0;
      String name = null;
      featureTree = new SmartForest<float[]>();
      float[] value = null;
      do {
        win = ois.readInt();
        size = ois.readInt();
        for (int i = 0; i < size; i++) {
          name = ois.readUTF();
          value = new float[win];
          for (int j = 0; j < value.length; j++) {
            value[j] = ois.readFloat();
          }
          featureTree.add(name, value);
        }
      } while (win == 0 || size == 0);
      //System.out.println("加载CRF模型成功，用时：" + (System.currentTimeMillis() - start) + "毫秒！");
    }
    return this;
  }

  @Override
  public boolean checkModel(String modelPath) {
    try (FileInputStream fis = new FileInputStream(modelPath)) {
      ObjectInputStream inputStream = new ObjectInputStream(new GZIPInputStream(fis));
      String version = inputStream.readUTF();
      if (version.equals("ansj1")) { // model
        return true;
      }
    } catch (ZipException ze) {
      System.out.println("解压异常");
    } catch (FileNotFoundException e) {
      System.out.println("文件没有找到");
    } catch (IOException e) {
      System.out.println("IO异常");
    }
    return false;
  }

}
