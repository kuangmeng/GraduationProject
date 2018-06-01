package uno.meng.ner;

import uno.meng.crf_seg.util.IOReader;
import uno.meng.crf_seg.util.Graph;
import uno.meng.crf_seg.util.MengSegConfig;
import org.nlpcn.commons.lang.tire.GetWord;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.domain.SmartForest;
import org.nlpcn.commons.lang.util.StringUtil;
import uno.meng.ner.domain.*;
import uno.meng.ner.library.DATDictionary;
import uno.meng.ner.library.FinanceDicLibrary;
import static uno.meng.ner.library.DATDictionary.status;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public abstract class Extract_NER {

  /**
   * 用来记录偏移量
   */
  public int offe;

  /**
   * 分词的类
   */
  private GetWordsImpl gwi = new GetWordsImpl();

  protected Forest[] forests = null;

  /**
   * 文档读取流
   */
  private IOReader br;

  private LinkedList<Term> terms = new LinkedList<Term>();

  /**
   * while 循环调用.直到返回为null则分词结束
   *
   * @return
   * @throws IOException
   */

  public Term next() throws IOException {
    Term term = null;
    if (!terms.isEmpty()) {
      term = terms.poll();
      term.updateOffe(offe);
      return term;
    }

    String temp = br.readLine();
    while (StringUtil.isBlank(temp)) {
      if (temp == null) {
        offe = br.getStart();
        return null;
      } else {
        temp = br.readLine();
      }

    }
    offe = br.getStart();

    // 歧异处理字符串

    fullTerms(temp);

    if (!terms.isEmpty()) {
      term = terms.poll();
      term.updateOffe(offe);
      return term;
    }

    return null;
  }

  /**
   * 填充terms
   */
  private void fullTerms(String temp) {
    List<Term> result = analysisStr(temp);
    terms.addAll(result);
  }

  /**
   * 一整句话分词,用户设置的歧异优先
   *
   * @param temp
   * @return
   */
  private List<Term> analysisStr(String temp) {
    if (temp == null || temp.length() == 0) {
      return Collections.emptyList();
    }

    Graph gp = new Graph(temp);
    int startOffe = 0;

    if (startOffe < gp.chars.length) {
      analysis(gp, startOffe, gp.chars.length);
    }
    gp.rmLittlePath();
    List<Term> result = this.getResult(gp);

    return result;
  }

  private void analysis(Graph gp, int startOffe, int endOffe) {
    int start = 0;
    int end = 0;
    char[] chars = gp.chars;

    String str = null;
    for (int i = startOffe; i < endOffe; i++) {
      switch (status(chars[i])) {
        case 4:
          start = i;
          end = 1;
          while (++i < endOffe && status(chars[i]) == 4) {
            end++;
          }
          str = new String(chars, start, end);
          gp.addTerm(new Term(str, start, TermNatures.EN));
          i--;
          break;
        case 5:
          start = i;
          end = 1;
          while (++i < endOffe && status(chars[i]) == 5) {
            end++;
          }
          str = new String(chars, start, end);
          Term numTerm = new Term(str, start, TermNatures.M_ALB);
          numTerm.termNatures().numAttr = NumNatureAttr.NUM;
          gp.addTerm(numTerm);
          i--;
          break;
        default:
          start = i;
          end = i;

          int status = 0;
          do {
            end = ++i;
            if (i >= endOffe) {
              break;
            }
            status = status(chars[i]);
          } while (status < 4);

          if (status > 3) {
            i--;
          }

          gwi.setChars(chars, start, end);
          int max = start;
          while ((str = gwi.allWords()) != null) {
            Term term = new Term(str, gwi.offe, gwi.getItem());
            int len = term.getOffe() - max;
            if (len > 0) {
              for (; max < term.getOffe();) {
                gp.addTerm(new Term(String.valueOf(chars[max]), max, TermNatures.NULL));
                max++;
              }
            }
            gp.addTerm(term);
            max = term.toValue();
          }

          int len = end - max;
          if (len > 0) {
            for (; max < end;) {
              String temp = String.valueOf(chars[max]);
              SegItem item = DATDictionary.getItem(temp);
              gp.addTerm(new Term(temp, max, item.termNatures));
              max++;
            }
          }

          break;
      }
    }
  }

  /**
   * 将为标准化的词语设置到分词中
   *
   * @param graph
   * @param result
   */
  protected void setRealName(Graph graph, List<Term> result) {

    for (Term term : result) {
      term.setRealName(
          graph.str.substring(term.getOffe(), term.getOffe() + term.getName().length()));
    }
  }

  /**
   * 标记这个词语是否是新词
   *
   * @param term
   */
  protected void setIsNewWord(Term term) {

    if (term.termNatures().id > 0) {
      return;
    }

    int id = DATDictionary.getId(term.getName());

    if (id > 0) {
      return;
    }

    if (forests != null) {
      for (int i = 0; i < forests.length; i++) {
        if (forests[i] == null) {
          continue;
        }

        SmartForest<String[]> branch = forests[i].getBranch(term.getName());

        if (branch == null) {
          continue;
        }

        if (branch.getStatus() > 1) {
          return;
        }
      }
    }

    term.setNewWord(true);

  }

  /**
   * 一句话进行分词并且封装
   *
   * @param temp
   * @return
   */
  public Result parseStr(String temp) {
    return new Result(analysisStr(temp));
  }

  /**
   * 通过构造方法传入的reader直接获取到分词结果
   *
   * @return
   * @throws IOException
   */
  public Result parse() throws IOException {
    List<Term> list = new ArrayList<Term>();
    Term temp = null;
    while ((temp = next()) != null) {
      list.add(temp);
    }
    Result result = new Result(list);
    return result;
  }

  protected abstract List<Term> getResult(Graph graph);

  public abstract class Merger {
    public abstract List<Term> merger();
  }

  /**
   * 重置分词器
   *
   * @param br
   */
  public void resetContent(IOReader br) {
    this.offe = 0;
    this.br = br;
  }

  public void resetContent(Reader reader) {
    this.offe = 0;
    this.br = new IOReader(reader);
  }

  public void resetContent(Reader reader, int buffer) {
    this.offe = 0;
    this.br = new IOReader(reader, buffer);
  }

  public Extract_NER setForests(Forest... forests) {
    this.forests = forests;
    return this;
  }



}
