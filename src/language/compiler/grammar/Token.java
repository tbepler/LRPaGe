package language.compiler.grammar;

import java.io.Serializable;

public abstract class Token<V> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public abstract void accept(V visitor);
	
	public abstract Class<? extends Token<V>> getSymbolType();
	
	@Override
	public String toString(){ return this.getClass().getSimpleName(); }
	
}
