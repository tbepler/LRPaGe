package bepler.lrpage.code.generator;

import java.io.File;
import java.io.IOException;

import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.parser.Symbols;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public class CodeGenerator {
	
	private static final String EOF = "EOF";
	private final JCodeModel model = new JCodeModel();
	private final NodeGenerator nodeGen;
	private final LexerGenerator lexerGen;
	private final ParserGenerator parserGen;
	
	public CodeGenerator(String name, Grammar g){
		try {
			Symbols symbols = new Symbols(g, EOF);
			nodeGen = new NodeGenerator(symbols, model);
			lexerGen = new LexerGenerator(name, model, nodeGen);
			lexerGen.generate(g.getTokens());
			parserGen = new ParserGenerator(symbols);
			JDefinedClass parser = parserGen.generate(name, model,
					lexerGen.getLexerClass(), nodeGen);
			
			JDefinedClass printVisitor = MainGenerator.generatePrintVisitor(
					null, model, nodeGen.getVisitorInterface(),
					nodeGen.getTokenNodeClasses());
			MainGenerator.generateMain(null, model, lexerGen.getLexerClass(),
					parser, printVisitor,nodeGen.getNodeInterface());
			
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void write(File dir) throws IOException{
		model.build(dir);
	}

}
