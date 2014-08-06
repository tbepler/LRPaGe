package bepler.lrpage.ebnf;

import java.io.IOException;
import java.io.InputStream;

import bepler.lrpage.grammar.Grammar;

public interface GrammarParser {
	
	public Grammar parse(InputStream in) throws IOException;
	
}
