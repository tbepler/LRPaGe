package bepler.lrpage.ebnf.environment;

import java.util.ArrayList;
import java.util.List;

import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Rule;

public class RuleBuilder {
	
	private String lhs = null;
	private List<String> rhs = new ArrayList<String>();
	private String precedence = null;
	private String name = null;
	
	public RuleBuilder appendLHS(String symbol){
		this.lhs = symbol;
		return this;
	}
	
	public String getLHS(){
		return lhs;
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
		this.precedence = symbol;
		return this;
	}
	
	public RuleBuilder appendName(String name){
		this.name = name;
		return this;
	}
	
	public Rule build(Environment env){
		//assert that all rhs symbols are defined
		for(String s : rhs){
			if(!env.isDefined(s)){
				throw new RuntimeException("Undefined symbol: "+s);
			}
		}
		Precedence prec = this.assignPrec(env);
		String n = this.assignName();
		return new ConcreteRule(lhs, rhs.toArray(new String[rhs.size()]), prec.getPriority(), prec.getAssoc(), n);
	}
	
	private String assignName(){
		String s = name;
		if(s == null){
			s = "";
			if(rhs.size() > 0){
				for(String symbol : rhs){
					s += symbol;
				}
			}else{
				s += "Empty";
			}
			s += lhs;
		}
		return s;
	}
	
	private Precedence assignPrec(Environment env){
		Precedence prec = env.getPrecedence(precedence);
		if(prec == null){
			//assign precedence of rightmost terminal symbol
			for( int i = rhs.size() - 1 ; i >= 0 ; --i ){
				String symbol = rhs.get(i);
				if(env.isTerminal(symbol)){
					prec = env.getPrecedence(symbol);
					break;
				}
			}
			//if precedence is still null, assign default
			if(prec == null){
				prec = env.getDefaultPrec();
			}
		}
		if(prec.getAssoc() == null){
			Assoc assoc = null;
			for( int i = rhs.size() - 1 ; i >= 0 ; --i ){
				String symbol = rhs.get(i);
				if(env.isTerminal(symbol)){
					assoc = env.getPrecedence(symbol).getAssoc();
					break;
				}
			}
			if(assoc == null){
				assoc = env.getDefaultAssoc();
			}
			prec = new Precedence(assoc, prec.getPriority());
		}
		if(prec.getPriority() == null){
			Integer priority = null;
			for( int i = rhs.size() - 1 ; i >= 0 ; --i ){
				String symbol = rhs.get(i);
				if(env.isTerminal(symbol)){
					priority = env.getPrecedence(symbol).getPriority();
					break;
				}
			}
			if(priority == null){
				priority = env.getDefaultPriority();
			}
			prec = new Precedence(prec.getAssoc(), priority);
		}
		return prec;
	}
	
	@Override
	public RuleBuilder clone(){
		RuleBuilder copy = new RuleBuilder();
		copy.lhs = lhs;
		copy.rhs = new ArrayList<String>(rhs);
		copy.precedence = precedence;
		copy.name = name;
		return copy;
	}
	
}
