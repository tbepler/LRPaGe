package language.compiler.parser;

import java.util.List;
import java.util.Set;

import language.compiler.grammar.Production;
import language.compiler.grammar.Token;

public interface Factory {
	
	public <V> Action<V> newReduceAction(Production<V> prod);
	public <V> Action<V> newGotoAction(State<V> next);
	public <V> Action<V> newShiftAction(State<V> next, Class<? extends Token<V>> nextToken);
	public <V> Action<V> newAcceptAction();
	public <V> Item<V> newItem(Production<V> production, List<Class<? extends Token<V>>> lookahead);
	public <V> Item<V> incrementItem(Item<V> item);
	public <V> State<V> newState(Set<Item<V>> items);
	
}
