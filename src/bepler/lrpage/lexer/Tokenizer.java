package bepler.lrpage.lexer;

import bepler.lrpage.grammar.Symbol;

public interface Tokenizer {
	
	public String getRegex();
	
	public Symbol tokenize(String s);
	
}
