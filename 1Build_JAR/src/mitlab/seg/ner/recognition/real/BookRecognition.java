package mitlab.seg.ner.recognition.real;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.ner.domain.Nature;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.recognition.Recognition;

/**
 * 基于规则的新词发现
 * 
 * 
 */
public class BookRecognition implements Recognition {

  private static final long serialVersionUID = 1L;

  private static final Nature nature = new Nature("book");

  private static Map<String, String> ruleMap = new HashMap<String, String>();

  static {
    ruleMap.put("《", "》");
  }

  @Override
  public List<Term> recognition(Result result) {
    List<Term> ret = new ArrayList<Term>();
    List<Term> terms = result.getTerms();
    String end = null;
    String name;
    LinkedList<Term> mergeList = null;
    for (Term term : terms) {
      name = term.getName();
      if (end == null) {
        if ((end = ruleMap.get(name)) != null) {
          mergeList = new LinkedList<Term>();
          mergeList.add(term);
        }
      } else {
        mergeList.add(term);
        if (end.equals(name)) {
          Term ft = mergeList.pollFirst();
          for (Term sub : mergeList) {
            ft.merage(sub);
          }
          ft.setNature(nature);
          ret.add(ft);
          mergeList = null;
          end = null;
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
