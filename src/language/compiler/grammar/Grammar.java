package language.compiler.grammar;

import java.util.Collection;
import java.util.regex.Pattern;

public interface Grammar<V> extends Iterable<Production<V>>{
	
	public int getPrecedence(Class<? extends Token<V>> symbolType);
	
	public Pattern getTokenRegex();
	
	public Token<V> tokenize(String s);
	
	public Collection<Class<? extends Token<V>>> getTerminalSymbolTypes();
	
	public boolean isTerminal(Token<V> symbol);
	
	public boolean isTerminal(Class<? extends Token<V>> symbolType);
	
	public Collection<Production<V>> getProductions(Class<? extends Token<V>> leftHandSide);
	
	public Collection<Production<V>> getAllProductions();
	
	public Production<V> getStartProduction();
	
	public boolean isStartSymbol(Token<V> symbol);
	
	public boolean isEOFSymbol(Token<V> symbol);
	
	public boolean isEOFSymbol(Class<? extends Token<V>> symbolType);
	
	public Token<V> getEOFSymbol();
	
	
}
