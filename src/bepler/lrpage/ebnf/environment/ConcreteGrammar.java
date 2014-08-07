package bepler.lrpage.ebnf.environment;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Rule;
import bepler.lrpage.grammar.Terminal;

public class ConcreteGrammar implements Grammar{
	
	private final List<Rule> rules;
	private final List<Terminal> tokens;
	private final int defaultPriority;
	private final Map<String, String> pseudonyms;
	private final String startSymbol;
	
	public ConcreteGrammar(List<Rule> rules, List<Terminal> tokens, int defaultPriority, 
			Map<String, String> pseudonyms, String startSymbol){
		
		this.rules = rules;
		this.tokens = tokens;
		this.defaultPriority = defaultPriority;
		this.pseudonyms = pseudonyms;
		this.startSymbol = startSymbol;
		
	}

	@Override
	public List<Rule> getRules() {
		return Collections.unmodifiableList(rules);
	}

	@Override
	public List<Terminal> getTokens() {
		return Collections.unmodifiableList(tokens);
	}

	@Override
	public int defaultPriority() {
		return defaultPriority;
	}

	@Override
	public String getPseudonym(String symbol) {
		return pseudonyms.get(symbol);
	}

	@Override
	public String getStartSymbol() {
		return startSymbol;
	}

}
