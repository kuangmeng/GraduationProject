package mitlab.seg.ner.recognition.real;

import java.util.List;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.ner.domain.Nature;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.library.FinanceDicLibrary;
import mitlab.seg.ner.recognition.Recognition;

/**
 * 用户自定义词典的词性优先
 */
public class UserDicNatureRecognition implements Recognition {

  private static final long serialVersionUID = 1L;
  private Forest[] forests = null;

  public UserDicNatureRecognition() {
    forests = new Forest[] {FinanceDicLibrary.get()};
  }

  /**
   * 传入多本词典，后面的会覆盖前面的结果
   * 
   */
  public UserDicNatureRecognition(Forest... forests) {
    this.forests = forests;
  }

  @Override
  public List<Term> recognition(Result result) {
    for (Term term : result) {
      for (int i = forests.length - 1; i > -1; i--) {
        String[] params = getParams(forests[i], term.getName());
        if (params != null) {
          term.setNature(new Nature(params[0]));
          break;
        }
      }
    }
    return null;
  }

  public static String[] getParams(Forest forest, String word) {
    SmartForest<String[]> temp = forest;
    for (int i = 0; i < word.length(); i++) {
      temp = temp.get(word.charAt(i));
      if (temp == null) {
        return null;
      }
    }
    if (temp.getStatus() > 1) {
      return temp.getParam();
    } else {
      return null;
    }
  }

  @Override
  public List<Term> recognition(Graph results) {
    return null;
  }
}
