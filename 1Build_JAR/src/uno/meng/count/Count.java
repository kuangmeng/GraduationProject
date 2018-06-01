package uno.meng.count;

import java.util.ArrayList;
import java.util.List;
import uno.meng.ner.domain.Term;

public class Count {
  public static List<SplitUtil> Cut(List<SplitUtil> ret) {
    List<SplitUtil> amountWord = new ArrayList<SplitUtil>();
    for (SplitUtil string : ret) {
      if (amountWord.isEmpty()) {
        amountWord.add(string);
      } else {
        boolean flag = false;
        for (SplitUtil s : amountWord) {
          if (s.name.equals(string.name)) {
            s.addOffset(string.offset[0]);
            flag = true;
          }
        }
        if (!flag) {
          amountWord.add(string);
        }
      }
    }
    return amountWord;
  }

  public static List<Term> FrequentStr(String str) {
    List<Term> ret_term = new ArrayList<Term>();
    Tmp_Term[] tmp_term = new Tmp_Term[1000];
    int len = 0;
    List<SplitUtil> ret = Cut(Split.splitWord(str));
    for (SplitUtil su : ret) {
      if (su.getNum() >= 6) {
       // System.out.println(su.getNum() + su.name);

        for (int i = 0; i < su.getNum(); i++) {
          if (len == 0) {
            tmp_term[0] = new Tmp_Term(su.getName(), su.offset[i]);
            len++;
          } else {
            int j = 0;
            for (j = 0; j < len; j++) {
              if (su.offset[i] < tmp_term[j].offset - 1) {
                for (int t = len; t > j; t--) {
                  tmp_term[t] = new Tmp_Term(tmp_term[t - 1].str, tmp_term[t - 1].offset);
                }
                tmp_term[j] = new Tmp_Term(su.getName(), su.offset[i]);
                len++;
                break;
              }
            }
            if (j == len) {
              tmp_term[len] = new Tmp_Term(su.getName(), su.offset[i]);
              len++;
            }
          }
        }
      }
    }
    for (int t = 0; t < len; t++) {
      ret_term.add(new Term(tmp_term[t].str, tmp_term[t].offset));
    }
    return ret_term;
  }

}
