package bepler.lrpage.ebnf;

import bepler.lrpage.grammar.Terminal;

public class TerminalImpl implements Terminal{
	
	private final String symbol;
	private final String regex;
	private final boolean punc;
	
	private int priority;
	
	public TerminalImpl(String symbol, String regex, boolean isPunctuation, int priority){
		this.symbol = symbol;
		this.regex = regex;
		this.punc = isPunctuation;
		this.priority = priority;
	}
	
	public TerminalImpl(String symbol, String regex, boolean isPunctuation){
		this(symbol, regex, isPunctuation, 0);
	}
	
	public TerminalImpl(String symbol, String regex){
		this(symbol, regex, false);
	}
	
	public TerminalImpl(String regex, boolean isPunctuation){
		this(null, regex, isPunctuation);
	}
	
	public TerminalImpl(String regex){
		this(regex, true);
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
	
	public void setPriority(int priority){
		this.priority = priority;
	}

	@Override
	public int getPriority() {
		return priority;
	}

}
