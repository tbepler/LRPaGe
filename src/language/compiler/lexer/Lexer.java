package language.compiler.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import language.compiler.grammar.Grammar;
import language.compiler.grammar.Token;

public class Lexer<T> {
	
	private final Grammar<T> m_Grammar;
	private final Pattern m_TokenPattern;
	private final Scanner m_Scanner;
	private boolean m_Next = true;
	
	public Lexer(Grammar<T> g, Scanner s){
		m_Grammar = g;
		m_TokenPattern = m_Grammar.getTokenRegex();
		m_Scanner = s;
	}
	
	public Lexer(Grammar<T> g, String s){
		this(g, new Scanner(s));
	}
	
	public Lexer(Grammar<T> g, InputStream in){
		this(g, new Scanner(in));
	}
	
	public boolean hasNext(){
		return m_Next;
	}
	
	public Token<T> nextToken(){
		String match = m_Scanner.findWithinHorizon(m_TokenPattern, 0);
		if(match == null){
			m_Next = false;
			return m_Grammar.getEOFSymbol();
		}
		Token<T> token = this.tokenize(match);
		if(token == null){
			return this.nextToken();
		}
		return token;
	}
	
	public List<Token<T>> lexAllTokens(){
		List<Token<T>> tokens = new ArrayList<Token<T>>();
		String match;
		while((match = m_Scanner.findWithinHorizon(m_TokenPattern, 0)) != null){
			Token<T> token = this.tokenize(match);
			//if token is null, then it is an ignored type
			if(token != null){
				tokens.add(token);
			}
		}
		m_Next = false;
		tokens.add(m_Grammar.getEOFSymbol());
		return tokens;
	}
	
	private Token<T> tokenize(String s){
		try{
			return m_Grammar.tokenize(s);
		}catch(RuntimeException e){
			throw new LexingException(e);
		}
	}

	
}
