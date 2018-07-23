package mitlab.seg.rule;

import java.util.ArrayList;
import java.util.List;

public class Match {

	public static List<String> match(String str, List<String> tmp, Rule area, int loc) {
	    List<String> ret = new ArrayList<String>();
	    int index = 0;
	    int tmp_index = 0;
	    int tmp_i = 0;
	    int i = 0;
	    for (i = 0; i < tmp.size(); i++) {
	      if (index == loc) {
	        ret.addAll(area.getRule());
	        index += area.toString().length();
	        i--;
	        break;
	      } else if (index > tmp_index) {
	        if (tmp_index + tmp.get(i).length() <= index) {
	          tmp_index += tmp.get(i).length();
	        } else {
	          ret.add(tmp.get(i).substring(index - tmp_index));
	          tmp_index += tmp.get(i).length();
	          index = tmp_index;
	        }
	      } else if (index + tmp.get(i).length() <= loc) {
	        ret.add(tmp.get(i));
	        index += tmp.get(i).length();
	        tmp_index += tmp.get(i).length();

	      } else if (index + tmp.get(i).length() > loc
	          && index < loc) {
	        ret.add(tmp.get(i).substring(0, loc - index));
	        index += loc - index;
	        tmp_index += tmp.get(i).length();
	      }
	    }
	    // 从上一个位置break
	    for (int j = i + 1; j < tmp.size(); j++) {
	      if (index > tmp_index) {
	        if (tmp_index + tmp.get(j).length() <= index) {
	          tmp_index += tmp.get(j).length();
	        } else {
	          ret.add(tmp.get(j).substring(index - tmp_index));
	          tmp_index += tmp.get(j).length();
	          index = tmp_index;
	        }
	      } else {
	        ret.add(tmp.get(j));
	      }
	    }
	    return ret;
	  }

	
	public static List<Integer> getLocation(String ori, String ru){
		List<Integer> ret = new ArrayList<Integer>();
		int first = ori.indexOf(ru);
		ret.add(first);
		if(first == -1){
			return ret;
		}
		while(first+ru.length() <= ori.length()){
			int tmp = ori.substring(first + ru.length()).indexOf(ru);
			if(tmp >= 0){
				first = tmp + first + ru.length();
				ret.add(first);
			}else{
				break;
			}
		}
		return ret;
	}
}
