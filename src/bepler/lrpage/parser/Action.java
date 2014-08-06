package bepler.lrpage.parser;

import java.util.Arrays;

import bepler.lrpage.grammar.Rule;

public class Action {
	
	public static  Action newAcceptAction(Symbols s){
		return new Action(s, Actions.ACCEPT);
	}
	
	public static  Action newReduceAction(Symbols s, Rule rule){
		return new Action(s, Actions.REDUCE, rule);
	}
	
	public static  Action newShiftAction(Symbols s, State next, String shift){
		return new Action(s, Actions.SHIFT, next, shift);
	}
	
	public static  Action newGotoAction(Symbols s, State next){
		return new Action(s, Actions.GOTO, next);
	}
	
	private final Symbols symbols;
	private final State next;
	private final Rule rule;
	private final String shift;
	private final Actions id;
	private final int hash;
	
	private Action(Symbols symbols, State next, Rule rule, String shift, Actions id){
		this.symbols = symbols;
		this.next = next;
		this.rule = rule;
		this.shift = shift;
		this.id = id;
		this.hash = Arrays.hashCode(new Object[]{symbols, next, rule, shift, id});
	}
	
	private Action(Symbols symbols, Actions id){
		this(symbols, null, null, null, id);
	}
	
	private Action(Symbols symbols, Actions id, State next){
		this(symbols, next, null, null, id);
	}
	
	private Action(Symbols symbols, Actions id, State next, String shift){
		this(symbols, next, null, shift, id);
	}
	
	private Action(Symbols symbols, Actions id, Rule rule){
		this(symbols, null, rule, null, id);
	}
	
	public int priority(){
		if(rule != null){
			Integer p = rule.getPriority();
			if(p == null){
				//assign the rule's priority according to the priority
				//of its last terminal symbol
				String[] rhs = rule.rightHandSide();
				for( int i = rhs.length - 1 ; i >= 0 ; --i ){
					String symbol = rhs[i];
					if(symbols.isTerminal(symbol)){
						p = symbols.getPriority(symbol);
						break;
					}
				}
			}
			//this means that there were no terminal symbols
			//in the rhs of this rule, so assign it the default
			//priority
			if(p == null){
				p = symbols.getDefaultPriority();
			}
			return p;
		}
		//rule is null so check shift symbol
		if(shift != null){
			return symbols.getPriority(shift);
		}
		//this is neither a reduce nor shift rule, return the default priority
		return symbols.getDefaultPriority();
	}
	
	public Actions id(){
		return id;
	}
	
	public State nextState(){
		return next;
	}
	
	public String shiftToken(){
		return shift;
	}
	
	public Rule production(){
		return rule;
	}
	
	@Override
	public String toString(){
		switch(id){
		case ACCEPT: return id.toString();
		case GOTO: return id.toString();
		case REDUCE: return id.toString() + " " +rule;
		case SHIFT: return id.toString() + " " +shift;
		}
		return id.toString();
	}
	
	@Override
	public int hashCode(){
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Action){
			Action that = (Action) o;
			return equals(this.symbols, that.symbols) &&
					equals(this.next, that.next) &&
					equals(this.rule, that.rule) &&
					equals(this.shift, that.shift) &&
					equals(this.id, that.id);
		}
		return false;
	}
	
	private static boolean equals(Object o1, Object o2){
		if(o1 == o2) return true;
		if(o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}
	
}
