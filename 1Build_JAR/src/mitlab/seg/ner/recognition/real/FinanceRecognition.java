package mitlab.seg.ner.recognition.real;

import org.nlpcn.commons.lang.util.IOUtil;
import org.nlpcn.commons.lang.util.StringUtil;
import mitlab.seg.ner.FinanceDicExtractNER;
import mitlab.seg.ner.domain.Result;
import mitlab.seg.ner.domain.Term;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceRecognition {

  public static Result segment(String str) {
    return FinanceDicExtractNER.parse(str);
  }

  public static List<String> segment(List<String> strs) {
    return strs.stream().map(str -> segment(str).toString()).collect(Collectors.toList());
  }

  public static List<String> segmentFile(String filePath) {
    List<String> result = new ArrayList<>();
    List<String> fileStrs = new ArrayList<>();
    try {
      BufferedReader br = IOUtil.getReader(filePath, "utf-8");
      String temp;
      while ((temp = br.readLine()) != null) {
        if (StringUtil.isNotBlank(temp)) {
          // temp = StringUtil.trim(temp);
          fileStrs.add(temp);
        }
      }
    } catch (UnsupportedEncodingException e) {
      System.out.println("unsupport encoding exception" + e);
    } catch (IOException e) {
      System.out.println("IO exception file :" + e.getMessage() + ",path:" + filePath);
    }

    return segment(fileStrs);
  }

  public static List<Term> extractFinance(String str) {
    List<Term> result = new ArrayList<>();
    List<Term> list = segment(str).getTerms();
    if (list.size() > 0)
      result.addAll(list.stream().filter(term -> term.getNatureStr().equals("finance"))
          .collect(Collectors.toList()));
    return result;
  }

}
