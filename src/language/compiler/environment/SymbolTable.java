package language.compiler.environment;

import java.util.Set;

public interface SymbolTable<T> {
	
	public SymbolTable<T> put(Symbol key, T value);
	public T get(Symbol key);
	public SymbolTable<T> beginScope();
	public SymbolTable<T> endScope();
	public Set<Symbol> keySet();
	
}
