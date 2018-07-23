package mitlab.seg.ner.recognition.real;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.ner.domain.Nature;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.recognition.Recognition;

/**
 * 基于规则的新词发现，身份证号码识别
 */
public class IDCardRecognition implements Recognition {
  /**
   * 
   */
  private static final long serialVersionUID = -32133440735240290L;
  private static final Nature ID_CARD_NATURE = new Nature("idcard");

  @Override
  public List<Term> recognition(Result result) {
    List<Term> ret = new ArrayList<Term>();
    List<Term> terms = result.getTerms();
    for (Term term : terms) {
      if ("m".equals(term.getNatureStr())) {
        if (term.getName().length() == 18) {
          term.setNature(ID_CARD_NATURE);
          ret.add(term);
        } else if (term.getName().length() == 17) {
          Term to = term.to();
          if ("x".equals(to.getName())) {
            term.merage(to);
            to.setName(null);
            term.setNature(ID_CARD_NATURE);
            ret.add(term);
          }
        }
      }
    }
    for (Iterator<Term> iterator = terms.iterator(); iterator.hasNext();) {
      Term term = iterator.next();
      if (term.getName() == null) {
        iterator.remove();
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
