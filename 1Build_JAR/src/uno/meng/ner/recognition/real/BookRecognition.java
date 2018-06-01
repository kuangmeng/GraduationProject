package uno.meng.ner.recognition.real;

import uno.meng.crf_seg.util.Graph;
import uno.meng.ner.domain.Nature;
import uno.meng.ner.domain.Result;
import uno.meng.ner.domain.Term;
import uno.meng.ner.recognition.Recognition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
