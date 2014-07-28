package bepler.lrpage;

import java.io.File;
import java.io.IOException;

import bepler.lrpage.grammar.Terminal;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class CodeGenerator {
	
	private static final String TYPE = "type";
	private static final String ACCEPT = "accept";
	private static final String VISITOR = "Visitor";
	private static final String ABSTRACT_SYNTAX_NODE = "AbstractSyntaxNode";
	private static final String EOF_TOKEN = "EOFToken";
	private static final String EOF = "EOF";
	private static final String SYMBOLS = "Symbols";
	
	private final JCodeModel model = new JCodeModel();
	private final JDefinedClass iVisitor;
	private final JDefinedClass symbolsEnum;
	private final JDefinedClass syntaxNode;
	private final JDefinedClass eofToken;
	
	private final LexerGenerator lexerGen;
	
	public CodeGenerator(String name){
		try {
			iVisitor = model._class(VISITOR, ClassType.INTERFACE);
			symbolsEnum = model._class(SYMBOLS, ClassType.ENUM);
			syntaxNode = model._class(ABSTRACT_SYNTAX_NODE, ClassType.INTERFACE);
			JMethod accept = syntaxNode.method(JMod.PUBLIC, void.class , ACCEPT);
			accept.param(iVisitor, "visitor");
			syntaxNode.method(JMod.PUBLIC, symbolsEnum, TYPE);
			eofToken = model._class(EOF_TOKEN)._implements(syntaxNode);
			accept = eofToken.method(JMod.PUBLIC, void.class, ACCEPT);
			accept.param(iVisitor, "visitor");
			accept.annotate(Override.class);
			accept.body().directStatement("//do nothing");
			JMethod type = eofToken.method(JMod.PUBLIC, symbolsEnum, TYPE);
			type.annotate(Override.class);
			type.body()._return(symbolsEnum.enumConstant(EOF));
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
		lexerGen = new LexerGenerator(name, model, syntaxNode, eofToken);
	}
	
	public void write(File dir) throws IOException{
		model.build(dir);
	}
	
	public void addToken(Terminal t) throws JClassAlreadyExistsException{
		JDefinedClass node = model._class(t.getSymbol()+"Token")._implements(syntaxNode);
		JVar text = node.field(JMod.PUBLIC+JMod.FINAL, String.class, "text");
		JVar line = node.field(JMod.PUBLIC+JMod.FINAL, int.class, "line");
		JVar pos = node.field(JMod.PUBLIC+JMod.FINAL, int.class, "pos");
		JMethod cons = node.constructor(JMod.PUBLIC);
		cons.body().assign(text, cons.param(String.class, "text"));
		cons.body().assign(line, cons.param(int.class, "line"));
		cons.body().assign(pos, cons.param(int.class, "pos"));
		JMethod visit = iVisitor.method(JMod.PUBLIC, void.class, "visit");
		visit.param(node, "node");
		//override accept method
		JMethod accept = node.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.annotate(Override.class);
		accept.body().invoke(accept.param(iVisitor, "visitor"), visit);
		//override type method
		JMethod type = node.method(JMod.PUBLIC, symbolsEnum, TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsEnum.enumConstant(t.getSymbol()));
		
		//add to lexer generator
		lexerGen.addTerminal(t.getRegex(), node);
	}

}
