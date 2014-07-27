package bepler.lrpage.parser;

import java.util.List;
import java.util.Set;

import bepler.lrpage.grammar.Production;
import bepler.lrpage.grammar.Terminal;

public interface Factory {
	
	public <V> Action<V> newReduceAction(Production<V> prod);
	public <V> Action<V> newGotoAction(State<V> next);
	public <V> Action<V> newShiftAction(State<V> next, Class<? extends Terminal<V>> nextToken);
	public <V> Action<V> newAcceptAction();
	public <V> Item<V> newItem(Production<V> production, List<Class<? extends Terminal<V>>> lookahead);
	public <V> Item<V> incrementItem(Item<V> item);
	public <V> State<V> newState(Set<Item<V>> items);
	
}
