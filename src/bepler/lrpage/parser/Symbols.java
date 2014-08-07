package bepler.lrpage.parser;

import java.util.*;

import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Rule;
import bepler.lrpage.grammar.Terminal;

public class Symbols{
	
	private final String start;
	
	private final List<Rule> rules;
	private final Map<String, List<Rule>> producers = new HashMap<String, List<Rule>>();
	
	private final Set<String> punctuation = new HashSet<String>();
	private final List<String> terminals = new ArrayList<String>();
	private final Map<String, Terminal> terminalSymbols = new HashMap<String, Terminal>();
	
	private final Set<String> allSymbols = new HashSet<String>();
	
	private final int defaultPriority;
	private final String eof;
	
	public Symbols(Grammar g, String eof){
		assert(g != null && eof != null);
		this.start = g.getStartSymbol();
		defaultPriority = g.defaultPriority();
		this.eof = eof;
		this.rules = Collections.unmodifiableList(g.getRules());
		for(Rule r : rules){
			String lhs = r.leftHandSide();
			allSymbols.add(lhs);
			List<Rule> rules = producers.get(lhs);
			if(rules == null){
				rules = new ArrayList<Rule>();
				producers.put(lhs, rules);
			}
			rules.add(r);
		}
		
		for(Terminal t : g.getTokens()){
			String symbol = t.getSymbol();
			if(symbol != null){
				allSymbols.add(symbol);
				terminals.add(symbol);
				terminalSymbols.put(symbol, t);
			}
			if(t.isPunctuation()){
				punctuation.add(symbol);
			}
		}
		allSymbols.add(eof);
		terminals.add(eof);
	}
	
	public String getStartSymbol(){
		return start;
	}
	
	public Set<String> getAllSymbols(){
		return Collections.unmodifiableSet(allSymbols);
	}
	
	public int getPriority(String symbol){
		Terminal t = terminalSymbols.get(symbol);
		if(t != null){
			return t.getPriority();
		}
		return defaultPriority;
	}
	
	public int getDefaultPriority(){
		return defaultPriority;
	}
	
	public String getEOF(){
		return eof;
	}
	
	public boolean isEOF(String symbol){
		return eof.equals(symbol);
	}
	
	public boolean isTerminal(String symbol){
		return terminalSymbols.containsKey(symbol) || eof.equals(symbol);
	}
	
	public List<String> getTerminals(){
		return Collections.unmodifiableList(terminals);
	}
	
	public Terminal getTerminal(String symbol){
		return terminalSymbols.get(symbol);
	}
	
	public boolean isPunctuation(String symbol){
		return punctuation.contains(symbol);
	}
	
	/**
	 * MAY RETURN NULL
	 * @param lhs
	 * @return
	 */
	public List<Rule> getRules(String lhs){
		return producers.get(lhs);
	}

	public List<Rule> getRules() {
		return rules;
	}
	
}
