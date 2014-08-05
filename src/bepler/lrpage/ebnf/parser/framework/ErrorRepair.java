package bepler.lrpage.ebnf.parser.framework;

import java.io.IOException;

public interface ErrorRepair<V> {
	
	public void update(Stack<V> s, Token<V> lookahead);
	
	public Stack<V> repair(Stack<V> s, Token<V> lookahead,
			Lexer<V> lexer, ParsingEngine<V> eng) throws IOException;
	
}
