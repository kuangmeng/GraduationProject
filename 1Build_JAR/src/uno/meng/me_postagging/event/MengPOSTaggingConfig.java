package uno.meng.me_postagging.event;

import org.nlpcn.commons.lang.util.FileFinder;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.ObjConver;
import uno.meng.crf_seg.dic.DicReader;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 这个类储存一些公用变量.
 * 
 */
public class MengPOSTaggingConfig {

  public static final Map<String, String> ENV = new HashMap<>();

  static {
    /**
     * 配置文件变量
     */
    ResourceBundle rb = null;
    try {
      rb = ResourceBundle.getBundle("MengPOSTagging");
    } catch (Exception e) {
      try {
        File find = FileFinder.find("MengPOSTagging.properties", 1);
        if (find != null && find.isFile()) {
          rb = new PropertyResourceBundle(
              IOUtil.getReader(find.getAbsolutePath(), System.getProperty("file.encoding")));
        }
      } catch (Exception e1) {
      }
    }
    if (rb == null) {
    } else {

      for (String key : rb.keySet()) {
        ENV.put(key, rb.getString(key));
        try {
          String value = rb.getString(key);

          Field field = MengPOSTaggingConfig.class.getField(key);
          field.set(null, ObjConver.conversion(rb.getString(key), field.getType()));
        } catch (Exception e) {
        }
      }

    }
  }



}
