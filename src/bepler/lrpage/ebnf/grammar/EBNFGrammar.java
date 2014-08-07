package bepler.lrpage.ebnf.grammar;

import java.util.List;

import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Rule;
import bepler.lrpage.grammar.Terminal;

/**
 * EBNF grammar implementation.
 * 
 * @author Tristan Bepler
 *
 */
public class EBNFGrammar implements Grammar{

	@Override
	public List<Rule> getRules() {
		return Rules.asList();
	}

	@Override
	public List<Terminal> getTokens() {
		return Terminals.asList();
	}

	@Override
	public int defaultPriority() {
		return 0;
	}

	@Override
	public String getPseudonym(String symbol) {
		return Terminals.pseudonym(symbol);
	}

	@Override
	public String getStartSymbol() {
		return Rules.startSymbol();
	}

}
