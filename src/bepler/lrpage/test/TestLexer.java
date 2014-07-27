package bepler.lrpage.test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

import bepler.lrpage.lexer.Lexer;
import bepler.lrpage.lexer.Token;
import bepler.lrpage.lexer.TokenTypes;

public class TestLexer extends junit.framework.TestCase{
	
	private final String s = "_identifier 0.0001 \" a string\\\" 56 literal\"  -5  (a,b) ;   \"  ";
	private final Lexer<TokenTypes> lexer = new Lexer<TokenTypes>(TokenTypes.values());
	
	public void testLexerString(){
		List<Terminal<TokenTypes>> tokens = lexer.lex(s);
		
		checkTokens(tokens);
		
	}
	
	public void testLexerScanner(){

		List<Terminal<TokenTypes>> tokens = lexer.lex(new Scanner(s));
		
		checkTokens(tokens);
	}
	
	public void testLexerInputStream() throws UnsupportedEncodingException{
		
		List<Terminal<TokenTypes>> tokens = lexer.lex(new ByteArrayInputStream(s.getBytes("UTF-8")));
		
		checkTokens(tokens);
	}

	private void checkTokens(List<Terminal<TokenTypes>> tokens) {
		assertEquals(11, tokens.size());
		
		Terminal<TokenTypes> token0 = tokens.get(0);
		assertEquals(TokenTypes.ID, token0.id());
		assertEquals("_identifier", token0.text());
		
		Terminal<TokenTypes> token1 = tokens.get(1);
		assertEquals(TokenTypes.NUM, token1.id());
		assertEquals("0.0001", token1.text());
		
		Terminal<TokenTypes> token2 = tokens.get(2);
		assertEquals(TokenTypes.STR, token2.id());
		assertEquals("\" a string\\\" 56 literal\"", token2.text());
		
		Terminal<TokenTypes> token3 = tokens.get(3);
		assertEquals(TokenTypes.NUM, token3.id());
		assertEquals("-5", token3.text());
		
		Terminal<TokenTypes> token = tokens.get(4);
		assertEquals(TokenTypes.L_PAREN, token.id());
		assertEquals("(", token.text());
		
		token = tokens.get(5);
		assertEquals(TokenTypes.ID, token.id());
		assertEquals("a", token.text());
		
		token = tokens.get(6);
		assertEquals(TokenTypes.COMMA, token.id());
		assertEquals(",", token.text());
		
		token = tokens.get(7);
		assertEquals(TokenTypes.ID, token.id());
		assertEquals("b", token.text());
		
		token = tokens.get(8);
		assertEquals(TokenTypes.R_PAREN, token.id());
		assertEquals(")", token.text());
		
		token = tokens.get(9);
		assertEquals(TokenTypes.SEMICOLON, token.id());
		assertEquals(";", token.text());
		
		token = tokens.get(10);
		assertEquals(TokenTypes.ERROR, token.id());
		assertEquals("\"", token.text());
	}
	
	
	
}
