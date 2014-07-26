package language.compiler.grammar;

import java.util.List;

public interface Production<V> {
	
	public Class<? extends Token<V>> leftHandSide();
	
	public List<Class<? extends Token<V>>> rightHandSide();
	
	public Token<V> reduce(List<Token<V>> symbols);
	
	public int getPriority();
	
	public Assoc getAssoc();
	
}
