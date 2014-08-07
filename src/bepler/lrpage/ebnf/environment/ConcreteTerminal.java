package bepler.lrpage.ebnf.environment;

import bepler.lrpage.grammar.Terminal;

public class ConcreteTerminal implements Terminal{
	
	private final String symbol;
	private final String regex;
	private final boolean punc;
	private final int priority;
	
	public ConcreteTerminal(String symbol, String regex, boolean punc,
			int priority){
		
		this.symbol = symbol;
		this.regex = regex;
		this.punc = punc;
		this.priority = priority;
		
	}

	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public String getSymbol() {
		return symbol;
	}

	@Override
	public boolean isPunctuation() {
		return punc;
	}

	@Override
	public int getPriority() {
		return priority;
	}
	
	
	
}
