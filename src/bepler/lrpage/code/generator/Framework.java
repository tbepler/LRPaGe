package bepler.lrpage.code.generator;

import bepler.lrpage.code.generator.framework.*;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.fmt.JStaticJavaFile;

/**
 * This class loads the framework package classes as static
 * code model JClasses.
 * 
 * @author Tristan Bepler
 *
 */
public class Framework {
	
	private final JClass errorRepair;
	private final JClass excErrorRepair;
	private final JClass lexer;
	private final JClass node;
	private final JClass parser;
	private final JClass parsingEng;
	private final JClass stack;
	private final JClass status;
	private final JClass symbol;
	private final JClass token;
	private final JClass tokenFac;
	
	public Framework(JCodeModel model, String pckg){
		JPackage pack = model._package(pckg == null ? "" : pckg+".framework");
		errorRepair = toStaticJClass(pack, ErrorRepair.class);
		excErrorRepair = toStaticJClass(pack, ExceptionErrorRepair.class);
		lexer = toStaticJClass(pack, Lexer.class);
		node = toStaticJClass(pack, Node.class);
		parser = toStaticJClass(pack, Parser.class);
		parsingEng = toStaticJClass(pack, ParsingEngine.class);
		stack = toStaticJClass(pack, Stack.class);
		status = toStaticJClass(pack, Status.class);
		symbol = toStaticJClass(pack, Symbol.class);
		token = toStaticJClass(pack, Token.class);
		tokenFac = toStaticJClass(pack, TokenFactory.class);
	}
	
	private JClass toStaticJClass(JPackage pckg, Class<?> clazz){
		JStaticJavaFile f = new JStaticJavaFile(pckg, clazz.getSimpleName(),
				clazz.getResource(clazz.getSimpleName()+".java"), null);
		pckg.addResourceFile(f);
		return f.getJClass();
	}
	
	/*
	private String toPackagePath(Class<?> clazz){
		String name = clazz.getCanonicalName();
		name.replace('.', '/');
		return name;
	}
	*/
	
	public JClass getTokenFactoryInterface(){
		return tokenFac;
	}
	
	public JClass getTokenClass(){
		return token;
	}
	
	public JClass getSymbolInterface(){
		return symbol;
	}
	
	public JClass getStatusEnum(){
		return status;
	}
	
	public JClass getStackClass(){
		return stack;
	}
	
	public JClass getParsingEngineInterface(){
		return parsingEng;
	}
	
	public JClass getParserClass(){
		return parser;
	}
	
	public JClass getNodeInterface(){
		return node;
	}
	
	public JClass getLexerClass(){
		return lexer;
	}
	
	public JClass getExceptionErrorRepairClass(){
		return excErrorRepair;
	}
	
	public JClass getErrorRepairInterface(){
		return errorRepair;
	}
	
}
