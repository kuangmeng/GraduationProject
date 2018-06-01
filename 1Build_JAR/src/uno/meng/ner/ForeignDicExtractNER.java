package uno.meng.ner;

import uno.meng.ner.domain.Result;
import uno.meng.ner.domain.Term;
import uno.meng.ner.domain.TermNature;
import uno.meng.ner.domain.TermNatures;
import uno.meng.ner.library.ForeignDicLibrary;
import uno.meng.ner.library.PlaceDicLibrary;
import uno.meng.ner.Extract_NER;
import uno.meng.crf_seg.util.IOReader;
import uno.meng.crf_seg.util.Graph;
import uno.meng.crf_seg.util.TermUtil;
import uno.meng.crf_seg.util.TermUtil.InsertTermType;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认用户自定义词性优先
 */

public class ForeignDicExtractNER extends Extract_NER {

  @Override
  protected List<Term> getResult(final Graph graph) {

    Merger merger = new Merger() {
      @Override
      public List<Term> merger() {
        // 用户自定义词典的识别
        userDefineRecognition(graph, ForeignDicLibrary.get());
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

  public ForeignDicExtractNER() {
    super();
  }

  public ForeignDicExtractNER(Reader reader) {
    super.resetContent(new IOReader(reader));
  }

  public static Result parse(String str) {
    return new ForeignDicExtractNER().parseStr(str);
  }

  public static Result parse(String str, Forest... forests) {
    return new ForeignDicExtractNER().setForests(forests).parseStr(str);
  }
}
