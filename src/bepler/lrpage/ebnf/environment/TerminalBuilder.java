package bepler.lrpage.ebnf.environment;

import java.util.regex.Pattern;

import bepler.lrpage.grammar.Terminal;

public class TerminalBuilder {
	
	private String symbol = null;
	private String regex = null;
	private boolean punctuation = false;
	
	public String getSymbol(){
		return symbol;
	}
	
	public TerminalBuilder appendSymbol(String symbol){
		this.symbol = symbol;
		return this;
	}
	
	public TerminalBuilder appendRegex(String regex){
		this.regex = regex;
		return this;
	}
	
	public TerminalBuilder setPunctuation(){
		this.punctuation = true;
		return this;
	}
	
	public Terminal build(Environment env){
		Precedence prec = env.getPrecedence(symbol);
		if(prec == null){
			prec = env.getDefaultPrec();
		}
		if(punctuation){
			return new ConcreteTerminal(symbol, Pattern.quote(regex), punctuation, prec.getPriority());
		}
		return new ConcreteTerminal(symbol, regex, punctuation, prec.getPriority());
	}
	

}
