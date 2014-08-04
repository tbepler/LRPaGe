package bepler.lrpage.code.generator.framework;

public interface ParsingEngine<V> {
	
	public Status advance(Stack<V> s, Node<V> lookahead);
	
	public Symbol[] expectedSymbols(int state);
	
}
