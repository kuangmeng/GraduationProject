package uno.meng;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import uno.meng.me_postagging.feature.WordPosContextGenerator;
import uno.meng.me_postagging.feature.WordPosContextGeneratorConf;
import uno.meng.me_postagging.model.WordPos;
import uno.meng.me_postagging.model.WordPosME;
import uno.meng.me_postagging.model.WordPosModel;
import uno.meng.ner.perceptron.PerceptionNERecognizer;

public class MengPOSTagging {
  public static WordPos postagger = null;
  public static PerceptionNERecognizer recognizer = null;

  public MengPOSTagging() throws IOException {
    Properties config = new Properties();
    InputStream configStream =
        MengPOSTagging.class.getClassLoader().getResourceAsStream("MengPOSTagging.properties");
    config.load(configStream);
    WordPosModel model =
        new WordPosModel(new File(config.getProperty("pos.corpus.modelbinary.file")));
    WordPosContextGenerator contextGenerator = new WordPosContextGeneratorConf(config);
    postagger = new WordPosME(model, contextGenerator);
    recognizer = new PerceptionNERecognizer(uno.meng.Constants.NERMODEL);
  }
  
  public static List<String> POSTagging(List<String> segged) throws IOException {
    String[] tmpret = new String[segged.size()];
    List<String> tagged = new ArrayList<String>();
    for (int i = 0; i < segged.size(); i++) {
      tmpret[i] = segged.get(i);
    }
    String[] ress = postagger.wordpos(tmpret);
    for (int i = 0; i < ress.length; i++) {
      tagged.add(ress[i]);
    }
    String[] seg = new String[segged.size()];
       seg = segged.toArray(seg);
    String[] tag = new String[tagged.size()];
      tag = tagged.toArray(tag);
      String[] ret = recognizer.recognize(seg,tag);
      for(String s:ret) {
        System.out.print(s+" ");
      }
      System.out.println();
    return tagged;
  }
}
