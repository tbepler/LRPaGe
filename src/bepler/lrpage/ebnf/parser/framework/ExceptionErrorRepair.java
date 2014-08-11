package bepler.lrpage.ebnf.parser.framework;

/**
 * This class performs error recovery by simply throwing
 * an exception when a syntax error is detected.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public class ExceptionErrorRepair<V> implements ErrorRepair<V>{

	@Override
	public void update(Stack<V> s, Token<V> lookahead) {
		//do nothing
	}

	@Override
	public Stack<V> repair(Stack<V> s, Token<V> lookahead, Lexer<V> lexer,
			ParsingEngine<V> eng) {
		throw new RuntimeException("Syntax error on token: "+lookahead);
	}

}
