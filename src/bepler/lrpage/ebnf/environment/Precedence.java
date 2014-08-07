package bepler.lrpage.ebnf.environment;

import bepler.lrpage.grammar.Assoc;

public class Precedence {
	
	private final Assoc assoc;
	private final Integer priority;
	
	public Precedence(Assoc assoc, Integer priority){
		this.assoc = assoc;
		this.priority = priority;
	}
	
	public Assoc getAssoc(){
		return assoc;
	}
	
	public Integer getPriority(){
		return priority;
	}
	
}
