package bepler.lrpage.ebnf.environment;

import java.util.ArrayList;
import java.util.List;

public class RuleBuilder {
	
	private String lhs = null;
	private List<String> rhs = new ArrayList<String>();
	private String precSymbol = null;
	private String name = null;
	
	public RuleBuilder appendLHS(String symbol){
		this.lhs = symbol;
		return this;
	}
	
	public RuleBuilder appendRHS(String symbol){
		rhs.add(symbol);
		return this;
	}
	
	public RuleBuilder appendRHS(RuleBuilder other){
		rhs.addAll(other.rhs);
		return this;
	}
	
	public RuleBuilder appendPrecedence(String symbol){
		this.precSymbol = symbol;
		return this;
	}
	
	public RuleBuilder appendName(String name){
		this.name = name;
		return this;
	}
	
	@Override
	public RuleBuilder clone(){
		RuleBuilder copy = new RuleBuilder();
		copy.lhs = lhs;
		copy.rhs = new ArrayList<String>(rhs);
		copy.precSymbol = precSymbol;
		copy.name = name;
		return copy;
	}
	
}
