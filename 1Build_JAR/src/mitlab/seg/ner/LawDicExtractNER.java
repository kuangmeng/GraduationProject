package mitlab.seg.ner;

import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.crf_seg.util.IOReader;
import mitlab.seg.crf_seg.util.TermUtil;
import mitlab.seg.crf_seg.util.TermUtil.InsertTermType;
import mitlab.seg.ner.Extract_NER;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.domain.TermNature;
import mitlab.seg.ner.domain.TermNatures;
import mitlab.seg.ner.library.LawDicLibrary;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认用户自定义词性优先
 */

public class LawDicExtractNER extends Extract_NER {

  @Override
  protected List<Term> getResult(final Graph graph) {

    Merger merger = new Merger() {
      @Override
      public List<Term> merger() {
        // 用户自定义词典的识别
        userDefineRecognition(graph, LawDicLibrary.get());
        graph.walkPath();
        return getResult();
      }

      private void userDefineRecognition(final Graph graph, Forest... forests) {
        if (forests == null) {
          return;
        }
        int beginOff = graph.terms[0].getOffe();
        Forest forest = null;
        for (int i = forests.length - 1; i >= 0; i--) {
          forest = forests[i];
          if (forest == null) {
            continue;
          }

          GetWord word = forest.getWord(graph.chars);
          String temp = null;
          int tempFreq = 50;
          while ((temp = word.getAllWords()) != null) {
            Term tempTerm = graph.terms[word.offe];
            tempFreq = getInt(word.getParam()[1], 50);
            if (graph.terms[word.offe] != null && graph.terms[word.offe].getName().equals(temp)) {
              TermNatures termNatures =
                  new TermNatures(new TermNature(word.getParam()[0], tempFreq), tempFreq, -1);
              tempTerm.updateTermNaturesAndNature(termNatures);
            } else {
              Term term = new Term(temp, beginOff + word.offe, word.getParam()[0], tempFreq);
              term.selfScore(-1 * Math.pow(Math.log(tempFreq), temp.length()));
              TermUtil.insertTerm(graph.terms, term, InsertTermType.REPLACE);
            }
          }
        }
        graph.rmLittlePath();
        graph.walkPathByScore();
        graph.rmLittlePath();
      }

      private int getInt(String str, int def) {
        try {
          return Integer.parseInt(str);
        } catch (NumberFormatException e) {
          return def;
        }
      }

      private List<Term> getResult() {

        List<Term> result = new ArrayList<Term>();
        int length = graph.terms.length - 1;
        for (int i = 0; i < length; i++) {
          Term term = graph.terms[i];
          if (term != null) {
            setIsNewWord(term);
            result.add(term);
          }
        }
        setRealName(graph, result);
        return result;
      }
    };
    return merger.merger();
  }

  public LawDicExtractNER() {
    super();
  }

  public LawDicExtractNER(Reader reader) {
    super.resetContent(new IOReader(reader));
  }

  public static Result parse(String str) {
    return new LawDicExtractNER().parseStr(str);
  }

  public static Result parse(String str, Forest... forests) {
    return new LawDicExtractNER().setForests(forests).parseStr(str);
  }
}
