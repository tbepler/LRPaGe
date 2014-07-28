package bepler.lrpage.parser;

import java.util.Set;

import bepler.lrpage.grammar.Rule;

public interface Factory {
	
	public  Action newReduceAction(Rule prod);
	public  Action newGotoAction(State next);
	public  Action newShiftAction(State next, String nextToken);
	public  Action newAcceptAction();
	public  Item newItem(Rule production, String lookahead);
	public  Item incrementItem(Item item);
	public  State newState(Set<Item> items);
	
}
