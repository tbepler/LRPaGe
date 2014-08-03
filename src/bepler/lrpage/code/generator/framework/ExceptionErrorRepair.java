package bepler.lrpage.code.generator.framework;

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
