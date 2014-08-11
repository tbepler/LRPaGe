package bepler.lrpage.ebnf.parser.framework;

import java.io.IOException;

/**
 * This interface describes classes that can be used by the parser
 * to perform error repair.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public interface ErrorRepair<V> {
	
	public void update(Stack<V> s, Token<V> lookahead);
	
	public Stack<V> repair(Stack<V> s, Token<V> lookahead,
			Lexer<V> lexer, ParsingEngine<V> eng) throws IOException;
	
}
