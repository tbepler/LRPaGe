package bepler.lrpage.ebnf;

import java.io.IOException;
import java.io.InputStream;

import bepler.lrpage.ebnf.parser.ParsingEngineImpl;
import bepler.lrpage.ebnf.parser.TokenFactoryImpl;
import bepler.lrpage.ebnf.parser.Visitor;
import bepler.lrpage.ebnf.parser.framework.BurkeFischerErrorRepair;
import bepler.lrpage.ebnf.parser.framework.Lexer;
import bepler.lrpage.ebnf.parser.framework.Node;
import bepler.lrpage.ebnf.parser.framework.Parser;
import bepler.lrpage.ebnf.parser.framework.ParsingEngine;
import bepler.lrpage.ebnf.parser.framework.TokenFactory;
import bepler.lrpage.grammar.Grammar;

public class EBNFGrammarParser implements GrammarParser{

	@Override
	public Grammar parse(InputStream in) throws IOException {
		TokenFactory<Visitor> ebnfTokenFac = new TokenFactoryImpl();
		Lexer<Visitor> lexer = new Lexer<Visitor>(in, ebnfTokenFac);
		ParsingEngine<Visitor> eng = new ParsingEngineImpl();
		Parser<Visitor> parser = new Parser<Visitor>(eng);
		Node<Visitor> astRoot = parser.parse(lexer, new BurkeFischerErrorRepair<Visitor>(10, 4));
		if(astRoot == null){
			return null;
		}
		return new Builder().build(astRoot);
	}

}
