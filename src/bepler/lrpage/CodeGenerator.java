package bepler.lrpage;

import bepler.lrpage.grammar.Terminal;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class CodeGenerator {
	
	private static final String TYPE = "type";
	private static final String ACCEPT = "accept";
	private static final String VISITOR = "Visitor";
	private static final String ABSTRACT_SYNTAX_NODE = "AbstractSyntaxNode";
	private static final String SYMBOLS = "Symbols";
	
	private final JCodeModel model = new JCodeModel();
	private final JDefinedClass iVisitor;
	private final JDefinedClass symbolsEnum;
	private final JDefinedClass syntaxNode;
	
	public CodeGenerator(){
		try {
			iVisitor = model._class(VISITOR, ClassType.INTERFACE);
			symbolsEnum = model._class(SYMBOLS, ClassType.ENUM);
			syntaxNode = model._class(JMod.ABSTRACT + JMod.PUBLIC, ABSTRACT_SYNTAX_NODE, ClassType.CLASS);
			JMethod accept = syntaxNode.method(JMod.ABSTRACT+JMod.PUBLIC, void.class , ACCEPT);
			accept.varParam(iVisitor, "visitor");
			syntaxNode.method(JMod.ABSTRACT+JMod.PUBLIC, symbolsEnum, TYPE);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addToken(Terminal t){
		
	}

}
