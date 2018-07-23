package mitlab.seg.ner.domain;

import org.nlpcn.commons.lang.util.StringUtil;
import mitlab.seg.ner.recognition.Recognition;
import mitlab.seg.ner.recognition.real.PersonRecognition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 分词结果的一个封装
 * 
 *
 */
public class Result implements Iterable<Term> {

  private char[] chars;

  private List<Term> terms = null;


  public Result(List<Term> terms) {
    this.terms = terms;
  }

  public List<Term> getTerms() {
    return terms;
  }

  public List<String> getString() {
    List<String> ret = new ArrayList<String>();
    List<Term> tmp = getTerms();
    for (Term t : tmp) {
      ret.add(t.getRealName());
    }
    return ret;
  }


  public String[] getStrings() {
    List<String> ret = new ArrayList<String>();
    List<Term> tmp = getTerms();
    for (Term t : tmp) {
      ret.add(t.getRealName());
    }
    return (String[]) ret.toArray();
  }

  public void setTerms(List<Term> terms) {
    this.terms = terms;
  }

  @Override
  public Iterator<Term> iterator() {
    return terms.iterator();
  }

  public int size() {
    return terms.size();
  }

  public Term get(int index) {
    return terms.get(index);
  }

  /**
   * 调用一个发现引擎
   * 
   * @return
   */
  public List<Term> recognition(Recognition re) {
    List<Term> ret = re.recognition(this);
    return ret;
  }


  @Override
  public String toString() {
    return toString(",");
  }


  public String toString(String split) {
    return StringUtil.joiner(this.terms, split);
  }

  /**
   * 返回没有词性的切分结果
   * 
   * @return
   */
  public String toStringWithOutNature() {
    return toStringWithOutNature(",");
  }

  /**
   * 返回没有词性的切分结果
   * 
   * @return
   */
  public String toStringWithOutNature(String split) {

    if (terms == null || terms.size() == 0) {
      return "";
    }

    Iterator<Term> iterator = terms.iterator();

    StringBuilder sb = new StringBuilder(iterator.next().getRealName());

    while (iterator.hasNext()) {
      sb.append(split);
      sb.append(iterator.next().getRealName());
    }

    return sb.toString();
  }

}
