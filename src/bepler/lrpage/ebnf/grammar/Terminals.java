package bepler.lrpage.ebnf.grammar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bepler.lrpage.grammar.Terminal;

public enum Terminals implements Terminal{
	
	Definition( "=" , true ),
	Concatenation( "," , true ),
	Termination( ";" , true ),
	Alternation( "|" , true ),
	StartOption( "[" , true ),
	EndOption( "]" , true ),
	StartRepetition( "{" , true ),
	EndRepetition( "}" , true ),
	StartGrouping( "\\(" , true ),
	EndGrouping( "\\)" , true ),
	TerminalString( "(['\"])" , false); //TODO
	
	public static List<Terminal> asList(){
		return Collections.unmodifiableList(Arrays.<Terminal>asList(Terminals.values()));
	}
	
	private final String regex;
	private final boolean punctuation;
	
	private Terminals(String regex, boolean punctuation){
		this.regex = regex;
		this.punctuation = punctuation;
	}
	
	private Terminals(String regex){
		this(regex, false);
	}
	
	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public String getSymbol() {
		return this.toString();
	}

	@Override
	public boolean isPunctuation() {
		return punctuation;
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
