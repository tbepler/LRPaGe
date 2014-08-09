package bepler.lrpage.ebnf.environment;

import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Rule;

public class ConcreteRule implements Rule{
	
	private final String lhs;
	private final String[] rhs;
	private final int priority;
	private final Assoc assoc;
	private final String name;
	
	public ConcreteRule(String lhs, String[] rhs, int priority, Assoc assoc, String name){
		this.lhs = lhs;
		this.rhs = rhs;
		this.priority = priority;
		this.assoc = assoc;
		this.name = name;
	}

	@Override
	public String leftHandSide() {
		return lhs;
	}

	@Override
	public String[] rightHandSide() {
		return rhs;
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	@Override
	public Assoc getAssoc() {
		return assoc;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
		return lhs + " -> " + rhsToString();
	}
	
	private String rhsToString(){
		String s = "";
		for( int i = 0 ; i < rhs.length ; ++i ){
			if(i != 0){
				s += ", ";
			}
			s += rhs[i];
		}
		return s;
	}

}
