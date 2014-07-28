package bepler.lrpage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JCase;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

public class LexerGenerator {
	
	private static final int PRIVATE_FINAL = JMod.PRIVATE + JMod.FINAL;
	
	private static final String LEXER = "Lexer";
	private static final String HAS_NEXT = "hasNext";
	private static final String NEXT_TOKEN = "nextToken";
	private static final String HAS_MATCH = "hasMatch";

	private static final String CREATE_TOKEN = "createToken";
	
	private static final String BUILD_PATTERN_LIST = "buildPatternList";
	private static final String PATTERN_LIST = "TOKEN_PATTERNS";
	private static final String EOF = "EOF";
	
	private static final String READER = "r";
	private static final String NEXT = "next";
	private static final String LINE = "lineNum";
	private static final String CHAR = "charNum";
	private static final String BUFFER = "buffer";
	
	private final JCodeModel model;
	private final JDefinedClass syntaxNodeClass;
	
	private JMethod buildPatternListMethod;
	private JVar patternList;
	private JFieldVar patternField;
	
	private JFieldVar eofToken;
	
	private JSwitch createTokenSwitch;
	private JVar createTokenLine;
	private JVar createTokenChar;
	private JVar createTokenText;
	
	private int tokenIndex = 0;
	
	private final JDefinedClass lexer;
	private final JDefinedClass eofTokenClass;
	private JFieldVar readerField;
	private JFieldVar nextField;
	
	public LexerGenerator(String lexerName, JCodeModel model, JDefinedClass syntaxNodeClass, JDefinedClass eofTokenClass){
		this.model = model;
		this.syntaxNodeClass = syntaxNodeClass;
		this.eofTokenClass = eofTokenClass;
		try {
			lexer = this.initializeLexer(lexerName);
		} catch (JClassAlreadyExistsException e) {
			throw new RuntimeException(e);
		}
	}
	
	public JDefinedClass getLexerClass(){
		return lexer;
	}
	
	public void addTerminal(String regex, JDefinedClass nodeClass){
		buildPatternListMethod.body().invoke(patternList, "add").arg(model.ref(Pattern.class).staticInvoke("compile").arg(JExpr.lit(regex)));
		JCase c = createTokenSwitch._case(JExpr.lit(tokenIndex++));
		//if nodeClass is null, then this token regex is to be ignored
		if(nodeClass == null){
			c.body()._return(JExpr.invoke(JExpr._this(), NEXT_TOKEN));
		}else{
			//token constructor takes (String text, int line, int c)
			c.body()._return(JExpr._new(nodeClass).arg(createTokenText).arg(createTokenLine).arg(createTokenChar));
		}
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
		//add methods
		this.initializeMethods(lexer);
		
		return lexer;
	}
	
	private JDefinedClass createLexerInterface() throws JClassAlreadyExistsException{
		JDefinedClass iLexer = model._class(LEXER, ClassType.INTERFACE);
		iLexer.method(JMod.PUBLIC, boolean.class, HAS_NEXT);
		iLexer.method(JMod.PUBLIC, syntaxNodeClass, NEXT_TOKEN)._throws(IOException.class);
		return iLexer;
	}
	
	private void initializeFields(JDefinedClass lexer){
		this.initializePatternListField(lexer);
		//eofTokenClass should take no args on the constructor
		eofToken = lexer.field(PRIVATE_FINAL+JMod.STATIC, syntaxNodeClass, EOF, JExpr._new(eofTokenClass));
		readerField = lexer.field(PRIVATE_FINAL, Reader.class, READER);
		nextField = lexer.field(JMod.PRIVATE, boolean.class, NEXT, JExpr.TRUE);
		lexer.field(JMod.PRIVATE, int.class, LINE, JExpr.lit(1));
		lexer.field(JMod.PRIVATE, int.class, CHAR, JExpr.lit(1));
		lexer.field(PRIVATE_FINAL, model.ref(Deque.class).narrow(Character.class), BUFFER, JExpr._new(model.ref(LinkedList.class).narrow(Character.class)));
	}
	
	private void initializePatternListField(JDefinedClass lexer){
		JClass patternListClass = model.ref(List.class).narrow(Pattern.class);
		buildPatternListMethod = lexer.method(JMod.PRIVATE+JMod.STATIC, patternListClass, BUILD_PATTERN_LIST);
		patternList = buildPatternListMethod.body().decl(patternListClass, "list", JExpr._new(model.ref(ArrayList.class).narrow(Pattern.class)));
		int pos = buildPatternListMethod.body().pos();
		buildPatternListMethod.body()._return(patternList);
		buildPatternListMethod.body().pos(pos);
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
	
	private void initializeMethods(JDefinedClass lexer){
		//implement the hasNext() method
		JMethod hasNextImpl = lexer.method(JMod.PUBLIC, boolean.class, HAS_NEXT);
		hasNextImpl.annotate(model.ref(Override.class));
		hasNextImpl.body()._return(nextField);
		
		//init the createToken(int tokenIndex, int line, int pos, String text) method
		JMethod createTokenMethod = lexer.method(JMod.PRIVATE, syntaxNodeClass, CREATE_TOKEN);
		createTokenMethod._throws(IOException.class);
		JVar index = createTokenMethod.param(int.class, "tokenIndex");
		createTokenLine = createTokenMethod.param(int.class, "line");
		createTokenChar = createTokenMethod.param(int.class, "pos");
		createTokenText = createTokenMethod.param(String.class, "text");
		createTokenSwitch = createTokenMethod.body()._switch(index);
		createTokenSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unrecognized token index.")));
		
		//implement the hasMatch() helper method
		JMethod hasMatchImpl = lexer.method(JMod.PRIVATE, boolean.class, HAS_MATCH);
		hasMatchImpl.param(String.class, "s");
		hasMatchImpl.param(model.ref(List.class).narrow(Matcher.class), "ms");
		hasMatchImpl.body().directStatement(
				"for(java.util.regex.Matcher m : ms){\n"+
				"	m.reset(s);\n"+
				"	if(m.matches() || m.hitEnd()){\n"+
				"		return true;\n"+
				"	}\n"+
				"}\n"+
				"return false;\n"
				);
		
		//implement the nextToken() method using direct statement, as it is rather complicated
		JMethod nextTokenImpl = lexer.method(JMod.PUBLIC, syntaxNodeClass, NEXT_TOKEN);
		nextTokenImpl.annotate(model.ref(Override.class));
		nextTokenImpl._throws(IOException.class);
		//JBlock body = nextTokenImpl.body();
		//JConditional cond = body._if(nextField.not());
		//cond._then()._throw(JExpr._new(model._ref(RuntimeException.class)).arg("No tokens remaining."));
		//JVar matchers = body.decl(model.ref(List.class).narrow(Matcher.class), "matchers", JExpr._new(model.ref(ArrayList.class).narrow(Matcher.class)));
		//JForEach foreach = body.forEach(model.ref(Pattern.class), "p", patternField);
		//foreach.body().invoke(matchers, "add").arg(JExpr.invoke(foreach.var(), "matcher").arg(JExpr.lit("")));
		nextTokenImpl.body().directStatement(
				"if(!next){\n"+
				"	throw new RuntimeException(\"No tokens remaining.\"); \n" +
				"}\n"+
				"java.util.List<java.util.regex.Matcher> ms = new java.util.ArrayList<java.util.regex.Matcher>();\n" +
				"for(java.util.regex.Pattern p : "+patternField.name()+"){\n" +
				"	ms.add(p.matcher(\"\"));\n" +
				"}\n"+
				"String cur = \"\";\n" +
				"boolean fin = false;\n" +
				"while(!fin){\n" +
				"	if(!buffer.isEmpty()){\n" +
				"		//read from the buffer before reading more chars\n" +
				"		//from the reader\n" +
				"		cur = cur + buffer.pop();\n"+
				"	}else{\n"+
				"		int read = "+READER+".read();\n"+
				"		if(read == -1){\n"+
				"			//the reader is expired, so set fin to true\n"+
				"			fin = true;\n"+
				"		}else{\n"+
				"			cur = cur + (char) read;\n"+
				"		}\n"+
				"	}"+ "\n" +
				"	fin = fin || !"+HAS_MATCH+"(cur, ms);"+"\n" +
				"}"+"\n" +
				"//if cur is empty, then return eof and mark lexing done"+"\n" +
				"if(cur.length() == 0){"+"\n" +
				"	next = false;"+"\n" +
				"	return "+eofToken.name()+";"+"\n" +
				"}"+"\n" +
				"//find the longest match"+"\n" +
				"for( int end = cur.length() ; end >= 0 ; --end ){"+"\n" +
				"	String sub = cur.substring(0, end);"+"\n" +
				"	for( int i = 0 ; i < ms.size() ; ++i ){"+"\n" +
				"		java.util.regex.Matcher m = ms.get(i);"+"\n" +
				"		m.reset(sub);"+"\n" +
				"		if(m.matches()){"+"\n" +
				"			//push the end of cur into the buffer"+"\n" +
				"			for( int j = end ; j < cur.length() ; ++j ){"+"\n" +
				"				buffer.add(cur.charAt(j));"+"\n" +
				"			}"+"\n" +
				"			int line = "+LINE+";"+"\n" +
				"			int cNum = "+CHAR+";"+"\n" +
				"			//update line and char count"+"\n" +
				"			for( int j = 0 ; j < sub.length() ; ++j ){"+"\n" +
				"				if(sub.charAt(j) == '\\n'){"+"\n" +
				"					++"+LINE+";"+"\n" +
				"					"+CHAR+" = 0;"+"\n" +
				"				}"+"\n" +
				"				++"+CHAR+";"+"\n" +
				"			}"+"\n" +
				"			//return the token"+"\n" +
				"			return "+CREATE_TOKEN+"(i, line, cNum, sub);"+"\n" +
				"		}"+"\n" +
				"	}"+"\n" +
				"}"+"\n" +
				"//an error occurred, the string is unmatched" +"\n" +
				"throw new RuntimeException(\"Unmatched token: \"+cur);" + "\n"
				);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
