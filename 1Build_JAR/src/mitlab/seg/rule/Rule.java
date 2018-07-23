package mitlab.seg.rule;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	private List<String> rule = new ArrayList<String>();

	public List<String> getRule() {
		return rule;
	}

	public void setRule(List<String> rule) {
		for(String s : rule){
			this.rule.add(s);
		}
	}
	
	public String toString(){
		if(rule == null){
			return "";
		}
		String txt = "";
		for(String s : rule){
			txt += s;
		}
		return txt;
	}
	
}
