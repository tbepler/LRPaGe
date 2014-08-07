package bepler.lrpage.ebnf.environment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Rule;
import bepler.lrpage.grammar.Terminal;

public class Environment {
	
	private String startSymbol = null;
	private final List<RuleBuilder> rules = new ArrayList<RuleBuilder>();
	private final Deque<TerminalBuilder> terminals = new ArrayDeque<TerminalBuilder>();
	private final Map<String, String> punctuation = new HashMap<String, String>();
	private final Set<String> terminalSymbols = new HashSet<String>();
	private final Map<String, Precedence> precedence = new HashMap<String, Precedence>();
	private Precedence defaultPrec = new Precedence(Assoc.NON, Integer.MAX_VALUE);
	private final Set<String> symbols = new HashSet<String>();
	private final Map<String, String> pseudonyms = new HashMap<String, String>();
	
	public Grammar build(){
		assert(startSymbol != null);
		List<Terminal> tokens = new ArrayList<Terminal>();
		for(TerminalBuilder b : terminals){
			tokens.add(b.build(this));
		}
		List<Rule> ruleList = new ArrayList<Rule>();
		for(RuleBuilder b : rules){
			ruleList.add(b.build(this));
		}
		return new ConcreteGrammar(ruleList, tokens, this.getDefaultPriority(),
				pseudonyms, startSymbol);
	}
	
	public Environment putPseudonym(String symbol, String pseudonym){
		pseudonyms.put(symbol, pseudonym);
		return this;
	}
	
	public boolean isDefined(String symbol){
		return symbols.contains(symbol);
	}
	
	public boolean isTerminal(String symbol){
		return terminalSymbols.contains(symbol);
	}
	
	public Environment putPrecedence(String symbol, Precedence prec){
		if(symbol == null){
			return this.setDefaultPrecedence(prec);
		}
		precedence.put(symbol, prec);
		return this;
	}
	
	public Environment setDefaultPrecedence(Precedence prec){
		assert(prec != null);
		assert(prec.getAssoc() != null && prec.getPriority() != null);
		defaultPrec = prec;
		return this;
	}
	
	public Precedence getPrecedence(String symbol){
		return precedence.get(symbol);
	}
	
	public Precedence getDefaultPrec(){
		return defaultPrec;
	}
	
	public int getDefaultPriority(){
		return getDefaultPrec().getPriority();
	}
	
	public Assoc getDefaultAssoc(){
		return getDefaultPrec().getAssoc();
	}
	
	public Environment appendTerminal(TerminalBuilder t){
		terminals.add(t);
		symbols.add(t.getSymbol());
		terminalSymbols.add(t.getSymbol());
		return this;
	}
	
	public String getPunctuationSymbol(String regex){
		String name = punctuation.get(regex);
		if(name == null){
			name = "_Punctuation"+punctuation.size();
			terminals.push(new TerminalBuilder().appendSymbol(name)
					.appendRegex(regex).setPunctuation());
			punctuation.put(regex, name);
			symbols.add(name);
			terminalSymbols.add(name);
			this.putPseudonym(name, "`"+regex+"'");
		}
		return name;
	}
	
	public Environment setStartSymbol(String symbol){
		startSymbol = symbol;
		return this;
	}
	
	public Environment appendRule(RuleBuilder r){
		rules.add(r);
		symbols.add(r.getLHS());
		return this;
	}
	
	public Environment removeRule(RuleBuilder r){
		rules.remove(r);
		return this;
	}
	
}
