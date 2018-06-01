package uno.meng.ner.recognition.real;

import uno.meng.crf_seg.util.Graph;
import uno.meng.ner.domain.Result;
import uno.meng.ner.domain.Term;
import uno.meng.ner.domain.TermNature;
import uno.meng.ner.domain.TermNatures;
import uno.meng.ner.extracting.Extracting;
import uno.meng.ner.extracting.domain.ExtractingResult;
import uno.meng.ner.recognition.Recognition;
import java.util.ArrayList;
import java.util.List;


public class URLRecognition implements Recognition {
  private static final Extracting extracting = new Extracting();

  private static final TermNatures URL_T_N = new TermNatures(new TermNature("url", 1));


  static {
    extracting.addRuleStr("(http://|https://)(:en|:m|-|\\.)[[\\\\x00-\\\\xff]+]{1,100}");
  }


  @Override
  public List<Term> recognition(Result result) {
    ExtractingResult parse = extracting.parse(result);
    List<Term> ret = new ArrayList<Term>();
    for (List<Term> list : parse.findAll()) {
      String name = list.get(list.size() - 1).getName();
      while ("-".equals(name) || ".".equals(name)) {
        list.remove(list.size() - 1);
        name = list.get(list.size() - 1).getName();
      }
      if (list.size() == 1) {
        continue;
      }
      int beginOff = list.get(0).getOffe();
      int endOff =
          list.get(list.size() - 1).getOffe() + list.get(list.size() - 1).getName().length();
      List<Term> terms = result.getTerms();
      StringBuilder sb = new StringBuilder();
      StringBuilder sbReal = new StringBuilder();
      for (int i = 0; i < terms.size(); i++) {
        Term term = terms.get(i);
        if (term.getOffe() >= beginOff && term.getOffe() < endOff) {
          sb.append(term.getName());
          if (term.getRealNameIfnull() != null) {
            sbReal.append(term.getRealName());
          }
        } else {
          if (sb != null && sb.length() > 0) {
            Term newTerm = new Term(sb.toString(), beginOff, URL_T_N);
            if (sbReal.length() > 0) {
              newTerm.setRealName(sbReal.toString());
            }
            ret.add(newTerm);
            sb = null;
            sbReal = null;
          }
        }
      }
    }
    return ret;
  }


  @Override
  public List<Term> recognition(Graph results) {
    // TODO Auto-generated method stub
    return null;
  }


}
