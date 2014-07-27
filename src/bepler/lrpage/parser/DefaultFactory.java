package bepler.lrpage.parser;

import java.util.List;
import java.util.Set;

import bepler.lrpage.grammar.Production;
import bepler.lrpage.grammar.Terminal;

public class DefaultFactory implements Factory {

	@Override
	public <V> Action<V> newReduceAction(Production<V> prod) {
		return Action.newReduceAction(prod);
	}

	@Override
	public <V> Action<V> newGotoAction(State<V> next) {
		return Action.newGotoAction(next);
	}

	@Override
	public <V> Action<V> newShiftAction(State<V> next, Class<? extends Terminal<V>> nextToken) {
		return Action.newShiftAction(next, nextToken);
	}

	@Override
	public <V> Action<V> newAcceptAction() {
		return Action.newAcceptAction();
	}

	@Override
	public <V> Item<V> newItem(Production<V> production,
			List<Class<? extends Terminal<V>>> lookahead) {
		return new Item<V>(production, lookahead);
	}

	@Override
	public <V> Item<V> incrementItem(Item<V> item) {
		return item.increment();
	}

	@Override
	public <V> State<V> newState(Set<Item<V>> items) {
		return new State<V>(items);
	}

	

}
