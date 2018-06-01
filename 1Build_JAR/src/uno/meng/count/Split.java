package uno.meng.count;

import java.util.ArrayList;
import java.util.List;

public class Split {
  public List<SplitUtil> ret = new ArrayList<SplitUtil>();

  public static List<SplitUtil> splitWord(String str) {
    List<SplitUtil> ret_tmp = new ArrayList<SplitUtil>();
    for (int i = 0; i < str.length() - 1; i++) {
      SplitUtil tmp = new SplitUtil(str.substring(i, i + 2), i);
      ret_tmp.add(tmp);
    }
    return ret_tmp;
  }

}
