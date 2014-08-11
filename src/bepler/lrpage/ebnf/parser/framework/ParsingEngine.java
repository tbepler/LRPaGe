package bepler.lrpage.ebnf.parser.framework;

/**
 * Interface describing a ParsingEngine used by the {@link Parser} to
 * construct ASTs.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public interface ParsingEngine<V> {
	
	public Status advance(Stack<V> s, Node<V> lookahead);
	
	public Symbol[] expectedSymbols(int state);
	
}
