package mitlab.seg.ner;

import org.nlpcn.commons.lang.tire.domain.Forest;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.crf_seg.util.IOReader;
import mitlab.seg.crf_seg.util.TermUtil.InsertTermType;
import mitlab.seg.ner.domain.Nature;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.recognition.real.BookRecognition;
import mitlab.seg.ner.recognition.real.EmailRecognition;
import mitlab.seg.ner.recognition.real.FinanceRecognition;
import mitlab.seg.ner.recognition.real.ForeignRecognition;
import mitlab.seg.ner.recognition.real.IDCardRecognition;
import mitlab.seg.ner.recognition.real.KilobitRecognition;
import mitlab.seg.ner.recognition.real.LawRecognition;
import mitlab.seg.ner.recognition.real.MedicineRecognition;
import mitlab.seg.ner.recognition.real.NumRecognition;
import mitlab.seg.ner.recognition.real.OrganizationRecognition;
import mitlab.seg.ner.recognition.real.PersonRecognition;
import mitlab.seg.ner.recognition.real.PlaceRecognition;
import mitlab.seg.ner.recognition.real.TimeRecognition;
import mitlab.seg.ner.recognition.real.URLRecognition;
import mitlab.seg.ner.recognition.real.UserDefineRecognition;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * NER的总类
 */
public class whole_NER extends Extract_NER {

  @Override
  protected List<Term> getResult(final Graph graph) {
    Merger merger = new Merger() {
      @Override
      public List<Term> merger() {
        graph.walkPath();
        // 人名识别
        new PersonRecognition().recognition(graph);
        new NumRecognition().recognition(graph);
        // 用户自定义词典的识别
        userDefineRecognition(graph, forests);
        return getResult();
      }

      private void userDefineRecognition(final Graph graph, Forest... forests) {
        new UserDefineRecognition(InsertTermType.SKIP, forests).recognition(graph);
        graph.rmLittlePath();
        graph.walkPathByScore();
      }

      private List<Term> getResult() {
        List<Term> result = new ArrayList<Term>();
        int length = graph.terms.length - 1;
        for (int i = 0; i < length; i++) {
          if (graph.terms[i] != null) {
            setIsNewWord(graph.terms[i]);
            result.add(graph.terms[i]);
          }
        }
        setRealName(graph, result);
        return result;
      }
    };
    return merger.merger();
  }

  public whole_NER() {
    super();
  }

  public whole_NER(Reader reader) {
    super.resetContent(new IOReader(reader));
  }

  public static Result parse(String str) {
    return new whole_NER().parseStr(str);
  }

  public static Result parse(String str, Forest... forests) {
    return new whole_NER().setForests(forests).parseStr(str);
  }

  public static List<Term> NER(String str, int choose) throws IOException {
    List<Term> rets = new ArrayList<Term>();
    Result ret = parse(str);
    System.out.println(ret);
    String tmp = "";
    for (Term t : ret.getTerms()) {
      if (t.getNatureStr().contains("nr")) {
        if (t.getRealName().length() == 1) {
          tmp += t.getRealName();
          continue;
        }
        t.setNature(new Nature("person"));
        rets.add(t);
      } else if (!tmp.isEmpty()) {
        t.setRealName(tmp + t.getRealName());
        t.setNature(new Nature("person"));
        rets.add(t);
        tmp = "";
      }
    }
    List<Term> book = ret.recognition(new BookRecognition());
    List<Term> email = ret.recognition(new EmailRecognition());
    List<Term> idcard = ret.recognition(new IDCardRecognition());
    List<Term> kilobit = ret.recognition(new KilobitRecognition());
    List<Term> time = ret.recognition(new TimeRecognition());
    List<Term> url = ret.recognition(new URLRecognition());

    rets.addAll(book);
    rets.addAll(email);
    rets.addAll(idcard);
    rets.addAll(kilobit);
    rets.addAll(time);
    rets.addAll(url);
    // 地名识别
    List<Term> place = PlaceRecognition.extractPlace(str);
    rets.addAll(place);

    // 地名识别
    List<Term> org = OrganizationRecognition.extractOrganization(str);
    rets.addAll(org);

    // 地名识别
    List<Term> fore = ForeignRecognition.extractForeign(str);
    rets.addAll(fore);

    switch (choose) {
      case 1:
        List<Term> finance = FinanceRecognition.extractFinance(str);
        rets.addAll(finance);
        break;
      case 2:
        List<Term> law = LawRecognition.extractLaw(str);
        rets.addAll(law);
        break;
      case 3:
        List<Term> medicine = MedicineRecognition.extractSick(str);
        rets.addAll(medicine);
        break;
      default:
        break;
    }
    return rets;
  }
}
