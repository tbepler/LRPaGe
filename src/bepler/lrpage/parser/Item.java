package bepler.lrpage.parser;

import java.util.Arrays;
import bepler.lrpage.grammar.Rule;

public class Item {
	
	private final Rule rule;
	private final String lookahead;
	private final int index;
	
	public Item(Rule rule, String lookahead){
		this(0, rule, lookahead);
	}
	
	public Item(int index, Rule rule, String lookahead){
		this.rule = rule;
		this.lookahead = lookahead;
		this.index = index;
	}
	
	public Rule getRule(){
		return rule;
	}
	
	public Item increment(){
		return new Item(index+1, rule, lookahead);
	}
	
	public String[] alpha(){
		String[] alpha = new String[index];
		for( int i = 0 ; i < index ; ++i ){
			alpha[i] = rule.rightHandSide()[i];
		}
		return alpha;
	}
	
	public String[] beta(){
		String[] rhs = rule.rightHandSide();
		String[] beta = new String[rhs.length - index - 1];
		for( int i = index + 1 ; i < rhs.length ; ++i ){
			beta[i-index-1] = rhs[i];
		}
		return beta;
	}
	
	public boolean hasNextSymbol(){
		return index < rule.rightHandSide().length;
	}
	
	public String nextSymbol(){
		return rule.rightHandSide()[index];
	}
	
	public boolean hasPrevSymbol(){
		return index > 0;
	}
	
	public String prevSymbol(){
		return rule.rightHandSide()[index-1];
	}
	
	public String lookahead(){
		return lookahead;
	}
	
	@Override
	public String toString(){
		return rule.leftHandSide() + " -> (" + this.rhsToString() + "), ["+lookahead+"]";
	}
	
	private String rhsToString(){
		String[] rhs = rule.rightHandSide();
		StringBuilder builder = new StringBuilder();
		for( int i = 0; i < rhs.length ; ++i ){
			if(i != 0){
				builder.append(" ");
			}
			if(i == index){
				builder.append(". ");
			}
			builder.append(rhs[i]);
		}
		if(index == rhs.length){
			builder.append(" .");
		}
		return builder.toString();
	}
	
	@Override
	public int hashCode(){
		return Arrays.deepHashCode(new Object[]{rule, lookahead, index});
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Item){
			Item that = (Item) o;
			return equals(this.rule, that.rule) && this.index == that.index && equals(this.lookahead, that.lookahead);
		}
		return false;
	}
	
	private static boolean equals(Object o1, Object o2){
		if(o1 == o2) return true;
		if(o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}
	
}
