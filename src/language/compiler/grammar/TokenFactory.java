package language.compiler.grammar;

import java.util.List;

public interface TokenFactory<V> {
	
	public Token<V> newEOFToken();
	public Production<V> newStartProduction();
	public List<Production<V>> newProductions();
	public List<? extends Token<V>> newTerminals();
	
}
