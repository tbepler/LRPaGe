package bepler.lrpage.code.generator;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Terminal;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCase;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

public class LexerGenerator {
	
	private static final String SUBLIST = "subList";

	private static final String RECORD = "record";

	private static final String SET_POS = "setPos";

	private static final String HISTORY = "history";

	private static final String IS_MARKED = "isMarked";

	private static final String GET_POS = "getPos";

	private static final String RESET = "reset";

	private static final String UNMARK = "unmark";

	private static final String MARK = "mark";

	private static final int PRIVATE_FINAL = JMod.PRIVATE + JMod.FINAL;
	
	private static final String LEXER = "Lexer";
	private static final String HAS_NEXT = "hasNext";
	private static final String NEXT_TOKEN = "nextToken";
	private static final String READ_NEXT = "readNext";
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
	private final NodeGenerator nodeGen;
	private final TokenFactoryGenerator tokenGen;
	
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
	private final JMethod isMarked;
	private final JMethod mark;
	private final JMethod unmark;
	private final JMethod reset;
	private final JMethod getPos;
	private final JMethod setPos;
	private final JMethod hasNext;
	private final JMethod readNext;
	private final JMethod nextToken;
	private final JMethod history;
	private final JMethod record;
	private final JMethod getTokenFactory;
	
	private JFieldVar readerField;
	private JFieldVar nextField;
	private JVar facField;
	private JVar markField;
	private JVar posField;
	private JVar historyField;
	
	public LexerGenerator(String pckg, JCodeModel model, NodeGenerator nodeGen, Grammar g)
			throws JClassAlreadyExistsException{
		this.model = model;
		this.nodeGen = nodeGen;
		this.tokenGen = new TokenFactoryGenerator(pckg, model, nodeGen.getNodeInterface());
		lexer = this.initializeLexer(pckg);
		getTokenFactory = this.defineGetTokenFactoryMethod();
		isMarked = this.defineIsMarkedMethod();
		mark = this.defineMarkMethod();
		unmark = this.defineUnmarkMethod();
		reset = this.defineResetMethod();
		getPos = this.defineGetPosMethod();
		setPos = this.defineSetPosMethod();
		history = this.defineHistoryMethod();
		record = this.defineRecordMethod();
		hasNext = this.defineHasNext();
		readNext = this.defineReadNext();
		nextToken = this.defineNextToken();
		this.defineCreateToken(g.getTokens());
		
	}
	
	public TokenFactoryGenerator getTokenFactoryGenerator(){
		return tokenGen;
	}
	
	public JDefinedClass getLexerClass(){
		return lexer;
	}
	
	public JMethod getHashNextMethod(){
		return hasNext;
	}
	
	public JMethod getNextTokenMethod(){
		return nextToken;
	}
	
	public JMethod getHistoryMethod(){
		return history;
	}
	
	public JMethod getIsMarkedMethod(){
		return isMarked;
	}
	
	public JMethod getMarkMethod(){
		return mark;
	}
	
	public JMethod getUnmarkMethod(){
		return unmark;
	}
	
	public JMethod getResetMethod(){
		return reset;
	}
	
	public JMethod getGetPosMethod(){
		return getPos;
	}
	
	public JMethod getSetPosMethod(){
		return setPos;
	}
	
	public JMethod getGetTokenFactoryMethod(){
		return getTokenFactory;
	}
	
	private void defineCreateToken(List<Terminal> terminals){
		for(Terminal t : terminals){
			String regex = t.getRegex();
			String symbol = t.getSymbol();
			if(symbol != null){
				this.addTerminal(regex, nodeGen.getTokenNode(symbol));
			}else{
				this.addTerminal(regex, null);
			}
		}
	}
	
	private void addTerminal(String regex, JDefinedClass nodeClass){
		buildPatternListMethod.body().invoke(patternList, "add").arg(model.ref(Pattern.class).staticInvoke("compile").arg(JExpr.lit(regex)));
		JCase c = createTokenSwitch._case(JExpr.lit(tokenIndex++));
		//if nodeClass is null, then this token regex is to be ignored
		if(nodeClass == null){
			c.body()._return(JExpr.invoke(JExpr._this(), readNext));
		}else{
			//token constructor takes (String text, int line, int c)
			int facIndex = tokenGen.appendTokenClass(nodeClass);
			c.body()._return(JExpr.invoke(facField, tokenGen.getBuildIndexTextLinePosMethod())
					.arg(JExpr.lit(facIndex)).arg(createTokenText).arg(createTokenLine).arg(createTokenChar));
		}
	}
	
	private JMethod defineGetTokenFactoryMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, tokenGen.getTokenFactoryClass(), "getTokenFactory");
		meth.body()._return(facField);
		return meth;
	}
	
	private JMethod defineRecordMethod(){
		JMethod meth = lexer.method(JMod.PRIVATE, nodeGen.getNodeInterface(), RECORD);
		JVar token = meth.param(nodeGen.getNodeInterface(), "token");
		JBlock body = meth.body()._if(JExpr.invoke(JExpr._this(), isMarked))._then();
		body.invoke(historyField, "add").arg(token);
		body.invoke(JExpr._this(), setPos).arg(posField.plus(JExpr.lit(1)));
		meth.body()._return(token);
		return meth;
	}
	
	private JMethod defineHistoryMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, int.class, HISTORY);
		meth.body()._return(JExpr.invoke(historyField, "size"));
		return meth;
	}
	
	private JMethod defineSetPosMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, void.class, SET_POS);
		JVar pos = meth.param(int.class, "pos");
		JBlock body = meth.body()._if(JExpr.invoke(JExpr._this(), isMarked).not())._then();
		body.invoke(
				JExpr.invoke(historyField, SUBLIST).arg(JExpr.lit(0)).arg(pos),
				"clear");
		body.assign(pos, JExpr.lit(0));
		meth.body().assign(JExpr._this().ref(posField), pos);
		return meth;
	}
	
	private JMethod defineGetPosMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, int.class, GET_POS);
		meth.body()._return(posField);
		return meth;
	}
	
	private JMethod defineResetMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, void.class, RESET);
		meth.body().assign(posField, JExpr.lit(0));
		return meth;
	}
	
	private JMethod defineUnmarkMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, void.class, UNMARK);
		meth.body().assign(markField, JExpr.lit(-1));
		return meth;
	}
	
	private JMethod defineMarkMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, void.class, MARK);
		JBlock body = meth.body()._if(JExpr.invoke(JExpr._this(), isMarked).not())._then();
		body.invoke(
				JExpr.invoke(historyField, SUBLIST).arg(JExpr.lit(0)).arg(posField),
				"clear");
		body.assign(markField, JExpr.assign(posField, JExpr.lit(0)));
		return meth;
	}
	
	private JMethod defineIsMarkedMethod(){
		JMethod meth = lexer.method(JMod.PUBLIC, boolean.class, IS_MARKED);
		meth.body()._return(markField.gte(JExpr.lit(0)));
		return meth;
	}
	
	private JDefinedClass initializeLexer(String pckg) throws JClassAlreadyExistsException{
		String name = pckg == null ? LEXER : pckg+"."+LEXER;
		JDefinedClass lexer = model._class(name);
		CodeGenerator.appendJDocHeader(lexer);
		//init fields
		this.initializeFields(lexer);
		//add constructors
		this.addReaderConstructor(lexer);
		this.addInputStreamConstructor(lexer);
		this.addStringConstructor(lexer);
		
		return lexer;
	}
	
	private void initializeFields(JDefinedClass lexer){
		this.initializePatternListField(lexer);
		//eofTokenClass should take no args on the constructor
		eofToken = lexer.field(PRIVATE_FINAL+JMod.STATIC, nodeGen.getNodeInterface(), EOF, JExpr._new(nodeGen.getEOFTokenNode()));
		readerField = lexer.field(PRIVATE_FINAL, Reader.class, READER);
		facField = lexer.field(
				JMod.PRIVATE+JMod.FINAL,
				tokenGen.getTokenFactoryClass(),
				"fac",
				JExpr._new(tokenGen.getTokenFactoryClass()));
		nextField = lexer.field(JMod.PRIVATE, boolean.class, NEXT, JExpr.TRUE);
		lexer.field(JMod.PRIVATE, int.class, LINE, JExpr.lit(1));
		lexer.field(JMod.PRIVATE, int.class, CHAR, JExpr.lit(1));
		lexer.field(PRIVATE_FINAL, model.ref(Deque.class).narrow(Character.class), BUFFER, JExpr._new(model.ref(LinkedList.class).narrow(Character.class)));
		markField = lexer.field(JMod.PRIVATE, int.class, MARK, JExpr.lit(-1));
		posField = lexer.field(JMod.PRIVATE, int.class, "pos", JExpr.lit(0));
		historyField = lexer.field(JMod.PRIVATE+JMod.FINAL,
				model.ref(List.class).narrow(nodeGen.getNodeInterface()),
				HISTORY,
				JExpr._new(model.ref(ArrayList.class).narrow(nodeGen.getNodeInterface())));
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
	
	private JMethod defineHasNext(){
		JMethod method = lexer.method(JMod.PUBLIC, boolean.class, HAS_NEXT);
		method.body()._return(nextField);
		return method;
	}
	
	private JMethod defineNextToken(){
		JMethod method = lexer.method(JMod.PUBLIC, nodeGen.getNodeInterface(),
				NEXT_TOKEN);
		method._throws(IOException.class);
		JVar token = method.body().decl(nodeGen.getNodeInterface(), "token");
		JConditional cond = method.body()._if(
				posField.lt(JExpr.invoke(historyField, "size")));
		JBlock body = cond._then();
		body.assign(token, JExpr.invoke(historyField, "get").arg(posField));
		body.invoke(JExpr._this(), setPos).arg(posField.plus(JExpr.lit(1)));
		body = cond._else();
		body.assign(token, JExpr.invoke(JExpr._this(), record)
				.arg(JExpr.invoke(JExpr._this(), readNext)));
		method.body()._return(token);
		return method;
	}
	
	private JMethod defineReadNext(){
		
		//init the createToken(int tokenIndex, int line, int pos, String text) method
		JMethod createTokenMethod = lexer.method(JMod.PRIVATE, nodeGen.getNodeInterface(), CREATE_TOKEN);
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
		
		//implement the readNext() method using direct statement, as it is rather complicated
		JMethod readNext = lexer.method(JMod.PRIVATE, nodeGen.getNodeInterface(), READ_NEXT);
		readNext._throws(IOException.class);
		readNext.body().directStatement(
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
		return readNext;
	}
		
	
	
	
	
	
	
	
	
	
	
}
