package mitlab.seg.ner.recognition.real;

import org.nlpcn.commons.lang.tire.domain.Forest;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.crf_seg.util.TermUtil;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.recognition.Recognition;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 用户自定词典识别 ,对于结果后的二次处理
 */

public class DicRecognition implements Recognition {

  private static final long serialVersionUID = 7487741700410080896L;

  private Forest[] forests = null;

  private TermUtil.InsertTermType type = TermUtil.InsertTermType.REPLACE;

  public DicRecognition(Forest... forests) {
    this.forests = forests;
  }

  public DicRecognition(TermUtil.InsertTermType type, Forest... forests) {
    this.type = type;
    this.forests = forests;
  }

  @Override
  public List<Term> recognition(Result result) {
    Graph graph = new Graph(result);
    new UserDefineRecognition(type, forests).recognition(graph);
    graph.rmLittlePath();
    graph.walkPathByScore();

    List<Term> terms = new ArrayList<Term>();
    int length = graph.terms.length - 1;
    for (int i = 0; i < length; i++) {
      if (graph.terms[i] != null) {
        terms.add(graph.terms[i]);
      }
    }
    result.setTerms(terms);
    return null;
  }

  @Override
  public List<Term> recognition(Graph results) {
    // TODO Auto-generated method stub
    return null;
  }

}
