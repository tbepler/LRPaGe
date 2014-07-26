package language.compiler.lexer;

import language.compiler.grammar.Token;

public interface Tokenizer<V> {
	
	public String getRegex();
	
	public Token<V> tokenize(String s);
	
}
