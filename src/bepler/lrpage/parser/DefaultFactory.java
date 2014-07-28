package bepler.lrpage.parser;

import java.util.Set;

import bepler.lrpage.grammar.Rule;

public class DefaultFactory implements Factory {
	
	private final Symbols symbols;
	
	public DefaultFactory(Symbols symbols){
		this.symbols = symbols;
	}

	@Override
	public  Action newReduceAction(Rule prod) {
		return Action.newReduceAction(symbols, prod);
	}

	@Override
	public  Action newGotoAction(State next) {
		return Action.newGotoAction(symbols, next);
	}

	@Override
	public  Action newShiftAction(State next, String nextToken) {
		return Action.newShiftAction(symbols, next, nextToken);
	}

	@Override
	public  Action newAcceptAction() {
		return Action.newAcceptAction(symbols);
	}

	@Override
	public  Item newItem(Rule production, String lookahead) {
		return new Item(production, lookahead);
	}

	@Override
	public  Item incrementItem(Item item) {
		return item.increment();
	}

	@Override
	public  State newState(Set<Item> items) {
		return new State(items);
	}

	

}
