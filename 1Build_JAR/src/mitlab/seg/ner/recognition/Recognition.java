package mitlab.seg.ner.recognition;

import java.io.Serializable;
import java.util.List;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;

/**
 * 词语结果识别接口,用来通过规则方式识别词语,对结果的二次加工
 */
public interface Recognition extends Serializable {
  public List<Term> recognition(Result result);

  public List<Term> recognition(Graph results);
}
