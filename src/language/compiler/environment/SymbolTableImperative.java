package language.compiler.environment;

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class SymbolTableImperative<T> implements SymbolTable<T> {
	
	private static final Object SCOPE_MARKER = new Object();
	
	private final Map<Symbol, Deque<T>> table = new HashMap<Symbol, Deque<T>>();
	private final Deque<Object> stack = new LinkedList<Object>();

	@Override
	public SymbolTable<T> put(Symbol key, T value) {
		Deque<T> binding = table.get(key);
		if(binding == null){
			binding = new LinkedList<T>();
			table.put(key, binding);
		}
		binding.push(value);
		stack.push(value);
		return this;
	}

	@Override
	public T get(Symbol key) {
		Deque<T> binding = table.get(key);
		if(binding == null){
			return null;
		}
		return binding.peek();
	}

	@Override
	public SymbolTable<T> beginScope() {
		stack.push(SCOPE_MARKER);
		return this;
	}

	@Override
	public SymbolTable<T> endScope() {
		while(!stack.isEmpty()){
			Object o = stack.pop();
			if(o == SCOPE_MARKER){
				return this;
			}
			assert(o instanceof Symbol);
			Symbol s = (Symbol) o;
			table.get(s).pop();
		}
		return this;
	}

	@Override
	public Set<Symbol> keySet() {
		return Collections.unmodifiableSet(table.keySet());
	}

}
