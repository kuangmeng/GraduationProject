package uno.meng.ner.recognition.real;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import uno.meng.crf_seg.util.Graph;
import uno.meng.crf_seg.util.TermUtil;
import uno.meng.ner.domain.Result;
import uno.meng.ner.domain.Term;
import uno.meng.ner.domain.TermNatures;
import uno.meng.ner.recognition.Recognition;

public class NumRecognition implements Recognition {

  public static final Set<Character> j_NUM = new HashSet<>();
  public static final Set<Character> f_NUM = new HashSet<>();

  static {
    j_NUM.add('零');
    j_NUM.add('一');
    j_NUM.add('两');
    j_NUM.add('二');
    j_NUM.add('三');
    j_NUM.add('四');
    j_NUM.add('五');
    j_NUM.add('六');
    j_NUM.add('七');
    j_NUM.add('八');
    j_NUM.add('九');
    j_NUM.add('十');
    j_NUM.add('百');
    j_NUM.add('千');
    j_NUM.add('万');
    j_NUM.add('亿');

    f_NUM.add('零');
    f_NUM.add('壹');
    f_NUM.add('贰');
    f_NUM.add('叁');
    f_NUM.add('肆');
    f_NUM.add('伍');
    f_NUM.add('陆');
    f_NUM.add('柒');
    f_NUM.add('捌');
    f_NUM.add('玖');
    f_NUM.add('拾');
    f_NUM.add('佰');
    f_NUM.add('仟');
    f_NUM.add('万');
    f_NUM.add('亿');

  };

  /**
   * 数字+数字合并
   *
   * @param graph
   */
  @Override
  public List<Term> recognition(Graph graph) {
    List<Term> ret = new ArrayList<Term>();
    Term[] terms = graph.terms;
    int length = terms.length - 1;
    Term to = null;
    Term temp = null;
    for (int i = 0; i < length; i++) {
      temp = terms[i];
      if (temp == null) {
        continue;
      }
      if (!temp.termNatures().numAttr.isNum()) {
        continue;
      }

      if (temp.getName().length() == 1) {
        if (j_NUM.contains(temp.getName().charAt(0))) {
          to = temp.to();
          while (to.getName().length() == 1 && j_NUM.contains(to.getName().charAt(0))) {
            temp.setName(temp.getName() + to.getName());
            terms[to.getOffe()] = null;
            TermUtil.termLink(temp, to.to());
            to = to.to();
          }
        }
        if (temp.getName().length() > 1) {
          i--;
          continue;
        }
        if (f_NUM.contains(temp.getName().charAt(0))) {
          to = temp.to();
          while (to.getName().length() == 1 && f_NUM.contains(to.getName().charAt(0))) {
            temp.setName(temp.getName() + to.getName());
            terms[to.getOffe()] = null;
            TermUtil.termLink(temp, to.to());
            to = to.to();
          }
        }
        if (temp.getName().length() > 1) {
          i--;
          continue;
        }
      }

      if (temp.termNatures() == TermNatures.M_ALB) { // 阿拉伯数字
        if (!temp.from().getName().equals(".") && temp.to().getName().equals(".")
            && temp.to().to().termNatures() == TermNatures.M_ALB
            && !temp.to().to().to().getName().equals(".")) {
          temp.setName(temp.getName() + temp.to().getName() + temp.to().to().getName());
          terms[temp.to().getOffe()] = null;
          terms[temp.to().to().getOffe()] = null;
          TermUtil.termLink(temp, temp.to().to().to());
          i = temp.getOffe() - 1;
        }
      }
      to = temp.to();
      if (to.termNatures().numAttr.isQua()) {
        temp.setName(temp.getName() + to.getName());
        terms[to.getOffe()] = null;
        TermUtil.termLink(temp, to.to());
        temp.setNature(to.termNatures().numAttr.nature);
        ret.add(temp);
        if ("m".equals(to.termNatures().numAttr.nature.natureStr)) {
          i = temp.getOffe() - 1;
        } else {
          i = to.getOffe();
        }
      }
    }
    return ret;

  }

  @Override
  public List<Term> recognition(Result result) {
    // TODO Auto-generated method stub
    return null;
  }


}
