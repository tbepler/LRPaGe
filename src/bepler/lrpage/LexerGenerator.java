package bepler.lrpage;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class LexerGenerator {
	
	private static final int PRIVATE_FINAL = JMod.PRIVATE + JMod.FINAL;
	
	private static final String SCANNER = "s";
	private static final String PATTERN = "p";
	private static final String NEXT = "n";
	private static final String TOKEN_TYPES = "TokenTypes";
	
	private final JDefinedClass lexer;
	private final JDefinedClass tokenEnum;
	private final JFieldVar patternField;
	private final JFieldVar scannerField;
	private final JFieldVar nextField;
	
	public LexerGenerator(JDefinedClass lexer){
		this.lexer = lexer;
		try {
			tokenEnum = lexer._class(JMod.PRIVATE+JMod.STATIC, TOKEN_TYPES, ClassType.ENUM);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
		//TODO initialize pattern
		patternField = lexer.field(PRIVATE_FINAL, Pattern.class, PATTERN);
		scannerField = lexer.field(PRIVATE_FINAL+JMod.STATIC, Scanner.class, SCANNER);
		nextField = lexer.field(JMod.PRIVATE, boolean.class, NEXT, JExpr.TRUE);
		this.addScannerConstructor();
		this.addInputStreamConstructor();
		this.addStringConstructor();
	}
	
	private void addScannerConstructor(){
		JMethod cons = lexer.constructor(JMod.PUBLIC);
		JVar param = cons.param(Scanner.class, SCANNER);
		cons.body().assign(JExpr._this().ref(scannerField), param);
	}
	
	private void addInputStreamConstructor(){
		JMethod cons = lexer.constructor(JMod.PUBLIC);
		cons.param(InputStream.class, "in");
		cons.body().directStatement("this(new java.util.Scanner(in));");
	}
	
	private void addStringConstructor(){
		JMethod cons = lexer.constructor(JMod.PUBLIC);
		cons.param(String.class, "str");
		cons.body().directStatement("this(new java.util.Scanner(str));");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
