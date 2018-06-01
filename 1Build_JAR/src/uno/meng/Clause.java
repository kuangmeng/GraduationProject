package uno.meng;

import java.util.ArrayList;
import java.util.List;
import uno.meng.clause.SentenceSplit;

public class Clause {
  public static List<String> getClause(String raw) {
    List<String> tmpClaused = new ArrayList<String>();
    int lastEOS = 0;
    do {
      // 找下一个切分点
      int nextEOS = SentenceSplit.nextPoint(raw, lastEOS);
      // 根据上一个切分点和下一个切分点分出句子
      String subSentence = raw.substring(lastEOS, nextEOS);
      tmpClaused.add(subSentence);
      lastEOS = nextEOS;
    } while (lastEOS < raw.length());
    return tmpClaused;
  }

}
