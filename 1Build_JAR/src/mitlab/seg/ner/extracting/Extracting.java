package mitlab.seg.ner.extracting;

import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import mitlab.seg.crf_seg.util.Graph;
import mitlab.seg.crf_seg.util.TermUtil;
import mitlab.seg.ner.whole_NER;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import mitlab.seg.ner.extracting.domain.ExtractingResult;
import mitlab.seg.ner.extracting.domain.Rule;
import mitlab.seg.ner.extracting.domain.RuleIndex;
import mitlab.seg.ner.library.FinanceDicLibrary;
import mitlab.seg.ner.recognition.real.UserDefineRecognition;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Extracting {

  private RuleIndex ruleIndex = new RuleIndex();

  public Extracting() {

  }

  public Extracting(List<String> lines) {
    addRules(lines);
  }


  public Extracting(InputStream is, String encoding) throws IOException {
    try {
      List<String> lines = IOUtil.readFile2List(IOUtil.getReader(is, encoding));
      addRules(lines);
    } finally {
      is.close();
    }
  }

  public Extracting(Reader reader) throws IOException {
    try {
      List<String> lines = IOUtil.readFile2List(new BufferedReader(reader));
      addRules(lines);
    } finally {
      reader.close();
    }
  }


  public void addRules(List<String> lines) {
    for (String line : lines) {
      addRuleStr(line);
    }
  }

  public void addRule(Rule rule) {
    ruleIndex.add(rule);
  }

  public void addRuleStr(String line) {
    if (StringUtil.isNotBlank(line)) {
      addRule(Lexical.parse(line));
    }
  }

  /**
   * 传入文本分词并抽取
   * 
   * @param content 需要分析的文本
   * @param forests 对文本分词加载的词典
   * @return 抽取结果集
   */
  public ExtractingResult parse(String content, Forest... forests) {
    Forest[] myForests = null;
    if (forests == null) {
      myForests = new Forest[] {ruleIndex.getForest()};
    } else if (forests.length == 0) {
      myForests = new Forest[] {ruleIndex.getForest(), FinanceDicLibrary.get()};
    } else {
      myForests = new Forest[forests.length + 1];
      myForests[0] = ruleIndex.getForest();
      for (int i = 0; i < forests.length; i++) {
        myForests[i + 1] = forests[i];
      }
    }
    Result terms = whole_NER.parse(content, myForests);
    return parse(terms, false);
  }


  /**
   * 对分词后的结果进行抽取
   * 
   * @param terms 分词后的结果
   * @return 抽取结果集
   */

  public ExtractingResult parse(Result terms) {
    return parse(terms, true);
  }

  /**
   * 对分词后的结果进行抽取
   * 
   * @param terms 分词后的结果
   * @param useForest 是否使用词典
   * @return 抽取结果集
   */
  private ExtractingResult parse(Result terms, boolean useForest) {
    if (terms == null || terms.size() == 0) {
      return new ExtractingResult();
    }

    if (useForest) {
      Graph graph = new Graph(terms);
      new UserDefineRecognition(TermUtil.InsertTermType.REPLACE, ruleIndex.getForest())
          .recognition(graph);
      graph.rmLittlePath();
      List<Term> result = new ArrayList<Term>();
      int length = graph.terms.length - 1;
      for (int i = 0; i < length; i++) {
        if (graph.terms[i] != null) {
          result.add(graph.terms[i]);
        }
      }
      terms = new Result(result);
    }


    List<ExtractingTask> tasks = new ArrayList<>();

    ExtractingResult result = new ExtractingResult();

    for (int i = 0; i < terms.size(); i++) {
      Term term = terms.get(i);

      Set<Rule> sets = new HashSet<>();

      Set<Rule> rules = ruleIndex.getRules(term.getName());
      if (rules != null) {
        sets.addAll(rules);
      }

      rules = ruleIndex.getRules(":" + term.getNatureStr());
      if (rules != null) {
        sets.addAll(rules);
      }

      rules = ruleIndex.getRules(":*");
      if (rules != null) {
        sets.addAll(rules);
      }

      for (Rule rule : sets) {
        tasks.add(new ExtractingTask(result, rule, i, terms));
      }
    }

    for (ExtractingTask task : tasks) {
      task.run();
    }

    return result;
  }
}
