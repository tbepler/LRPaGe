package bepler.lrpage.ebnf.environment;

import java.util.ArrayList;
import java.util.List;

public class Environment {
	
	private String startSymbol = null;
	private final List<RuleBuilder> rules = new ArrayList<RuleBuilder>();
	
	public Environment setStartSymbol(String symbol){
		startSymbol = symbol;
		return this;
	}
	
	public Environment appendRule(RuleBuilder r){
		rules.add(r);
		return this;
	}
	
	public Environment removeRule(RuleBuilder r){
		rules.remove(r);
		return this;
	}
	
}
