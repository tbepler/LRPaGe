package bepler.lrpage.ebnf.grammar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import bepler.lrpage.grammar.Terminal;

/**
 * Terminals for the EBNF grammar.
 * 
 * @author Tristan Bepler
 *
 */
public enum Terminals implements Terminal{
	
	Definition( "=" , true , "=" ),
	Concatenation( "," , true, "," ),
	Termination( ";" , true , ";" ),
	Alternation( "\\|" , true, "|" ),
	StartOption( "\\[" , true, "[" ),
	EndOption( "\\]" , true, "]" ),
	StartRepetition( "\\{" , true, "{" ),
	EndRepetition( "\\}" , true, "}" ),
	StartGrouping( "\\(" , true, "(" ),
	EndGrouping( "\\)" , true, ")" ),
	Bang( "!", true, "!" ),
	Special( "#" , true, "#" ),
	LeftKeyword ( "LEFT" , true , "Left" ),
	RightKeyword ( "RIGHT",  true, "Right" ),
	NonKeyword ( "NON", true, "Non" ),
	PrecKeyword ( "prec", true, "prec"),
	NameKeyword ( "name", true, "name"),
	DefaultKeyword ( "default" , true, "default" ),
	PrecedenceKeyword( "Precedence", true, "Precedence" ),
	PseudonymKeyword ( "Pseudonyms", true, "Pseudonyms" ), 
	TokensKeyword ( "Tokens" , true, "Tokens" ),
	//IgnoreKeyword ( "Ignore" , true, "Ignore" ),
	RulesKeyword ( "Rules" , true, "Rules" ),
	StartBlock ( ":" ,  true, ":" ),
	Int ( "-?[0-9]+", false ),
	Identifier( "[a-zA-Z][a-zA-Z0-9_]*" , false ),
	TerminalString( "(['\"])(?:\\\\.|(?!\\1).)*\\1" , false ),
	Comment( "(\\(\\*)((?!\\*\\)).)*\\*\\)" , false , true ),
	Whitespace( "\\s+" , false , true ),
	Error( "." , false )
	;
	
	public static List<Terminal> asList(){
		return Collections.unmodifiableList(Arrays.<Terminal>asList(Terminals.values()));
	}
	
	private final String regex;
	private final boolean punctuation;
	private final boolean ignored;
	private final String psuedonym;
	
	private Terminals(String regex, boolean punctuation, boolean ignored, String pseudonym){
		this.regex = regex;
		this.punctuation = punctuation;
		this.ignored = ignored;
		this.psuedonym = pseudonym;
	}
	
	private Terminals(String regex, boolean punctuation, boolean ignored){
		this(regex, punctuation, ignored, null);
	}
	
	private Terminals(String regex, boolean punctuation, String pseudonym){
		this(regex, punctuation, false, pseudonym);
	}
	
	private Terminals(String regex, boolean punctuation){
		this(regex, punctuation, false);
	}
	
	private Terminals(String regex){
		this(regex, false);
	}
	
	public static String pseudonym(String symbol){
		try{
			Terminals t = Terminals.valueOf(symbol);
			return t.psuedonym;
		} catch (IllegalArgumentException e){
			return null;
		}
	}
	
	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public String getSymbol() {
		if(ignored){
			return null;
		}
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
