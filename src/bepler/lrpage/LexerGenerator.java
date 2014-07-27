package bepler.lrpage;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class LexerGenerator {
	
	private static final int PRIVATE_FINAL = JMod.PRIVATE + JMod.FINAL;
	
	private static final String LEXER = "Lexer";
	private static final String HAS_NEXT = "hasNext";
	private static final String NEXT_TOKEN = "nextToken";
	
	private static final String BUILD_PATTERN_LIST = "buildPatternList";
	private static final String PATTERN_LIST = "TOKEN_PATTERNS";
	
	private static final String READER = "r";
	private static final String NEXT = "next";
	private static final String LINE = "lineNum";
	private static final String CHAR = "charNum";
	private static final String BUFFER = "buffer";
	private static final String TOKEN_TYPES = "TokenTypes";
	
	private final JCodeModel model;
	private final JDefinedClass syntaxNode;
	
	private JMethod buildPatternListMethod;
	private JVar patternList;
	private JFieldVar patternField;
	
	private final JDefinedClass lexer;
	private final JDefinedClass tokenEnum;
	private final JFieldVar readerField;
	private final JFieldVar nextField;
	
	public LexerGenerator(String lexerName, JCodeModel model, JDefinedClass syntaxNode){
		this.model = model;
		this.syntaxNode = syntaxNode;
		try {
			JDefinedClass iLexer = this.createLexerInterface();
			lexer = model._class(lexerName + LEXER);
			lexer._implements(iLexer);
			tokenEnum = lexer._class(JMod.PRIVATE+JMod.STATIC, TOKEN_TYPES, ClassType.ENUM);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
		//TODO initialize pattern
		patternField = lexer.field(PRIVATE_FINAL + JMod.STATIC, Pattern.class, PATTERN_LIST);
		readerField = lexer.field(PRIVATE_FINAL+JMod.STATIC, Reader.class, READER);
		nextField = lexer.field(JMod.PRIVATE, boolean.class, NEXT, JExpr.TRUE);
		this.addReaderConstructor();
		this.addInputStreamConstructor();
		this.addStringConstructor();
	}
	
	private JDefinedClass initializeLexer(String lexerName) throws JClassAlreadyExistsException{
		JDefinedClass iLexer = this.createLexerInterface();
		JDefinedClass lexer = model._class(lexerName + LEXER);
		lexer._implements(iLexer);
		//init fields
		this.initializeFields(lexer);
		//add constructors
		this.addReaderConstructor(lexer);
		this.addInputStreamConstructor(lexer);
		this.addStringConstructor(lexer);
		
	}
	
	private JDefinedClass createLexerInterface() throws JClassAlreadyExistsException{
		JDefinedClass iLexer = model._class(LEXER, ClassType.INTERFACE);
		iLexer.method(JMod.PUBLIC, boolean.class, HAS_NEXT);
		iLexer.method(JMod.PUBLIC, syntaxNode, NEXT_TOKEN);
		return iLexer;
	}
	
	private void initializeFields(JDefinedClass lexer){
		this.initializePatternListField(lexer);
		lexer.field(PRIVATE_FINAL, Reader.class, READER);
		lexer.field(JMod.PRIVATE, boolean.class, NEXT, JExpr.TRUE);
		lexer.field(JMod.PRIVATE, int.class, LINE, JExpr.lit(1));
		lexer.field(JMod.PRIVATE, int.class, CHAR, JExpr.lit(1));
		lexer.field(PRIVATE_FINAL, model.ref(Deque.class).narrow(Character.class), BUFFER, JExpr._new(model.ref(LinkedList.class).narrow(Character.class)));
	}
	
	private void initializePatternListField(JDefinedClass lexer){
		JClass patternListClass = model.ref(List.class).narrow(Pattern.class);
		buildPatternListMethod = lexer.method(JMod.PRIVATE+JMod.STATIC, patternListClass, BUILD_PATTERN_LIST);
		patternList = buildPatternListMethod.body().decl(patternListClass, "list", JExpr._new(model.ref(ArrayList.class).narrow(Pattern.class)));
		patternField = lexer.field(PRIVATE_FINAL+JMod.STATIC, patternListClass, PATTERN_LIST, JExpr.invoke(buildPatternListMethod));
	}
	
	private void addReaderConstructor(JDefinedClass lexer){
		JMethod cons = lexer.constructor(JMod.PUBLIC);
		JVar param = cons.param(Reader.class, READER);
		cons.body().assign(JExpr._this().ref(readerField), param);
	}
	
	private void addInputStreamConstructor(JDefinedClass lexer){
		JMethod cons = lexer.constructor(JMod.PUBLIC);
		cons.param(InputStream.class, "in");
		cons.body().directStatement("this(new java.io.InputStreamReader(in));");
	}
	
	private void addStringConstructor(JDefinedClass lexer){
		JMethod cons = lexer.constructor(JMod.PUBLIC);
		cons.param(String.class, "str");
		cons.body().directStatement("this(new java.io.StringReader(str));");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
