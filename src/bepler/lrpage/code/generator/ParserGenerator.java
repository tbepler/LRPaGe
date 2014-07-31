package bepler.lrpage.code.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JArray;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JVar;

import bepler.lrpage.grammar.Rule;
import bepler.lrpage.parser.Action;
import bepler.lrpage.parser.ActionTable;
import bepler.lrpage.parser.ActionTableBuilder;
import bepler.lrpage.parser.State;
import bepler.lrpage.parser.Symbols;

public class ParserGenerator {

	private static final int BURKE_FISCHER_K = 15;
	private static final String PARSING_EXCEPTION = "ParsingException";
	private static final String EXCEPTION_HANDLER = "ParsingErrorHandler";
	public static final String PROCEED_METHOD = "proceed";

	private static final String PARSER = "Parser";
	
	private final ActionTable table;
	private final JCodeModel model;
	private final NodeGenerator nodes;
	private final JDefinedClass parsingException;
	private final JDefinedClass exceptionHandler;
	private final JDefinedClass parserClass;
	private final JVar burkeFischerK;
	private final JDefinedClass actionsEnum;
	private final ParseStackGenerator parseStack;
	
	private JSwitch getActionSwitch;
	private JVar getActionLookaheadParam;
	private JMethod parseNextMethod;
	private JSwitch parseActionSwitch;
	private JVar stack;
	private JVar lookaheadNode;
	private Set<Action> definedActions;
	private Map<State, Integer> stateIndex;
	private Map<Rule, Integer> ruleIndices;

	public ParserGenerator(Symbols s, String pckg, JCodeModel model,
			JDefinedClass lexer, NodeGenerator nodes)
			throws JClassAlreadyExistsException{
		ActionTableBuilder builder = new ActionTableBuilder(s);
		table = builder.generateActionTable();
		
		this.model = model;
		this.nodes = nodes;
		parsingException = this.createParsingExceptionClass(pckg);
		exceptionHandler = this.createExceptionHandlerInterface(pckg);
		String name = pckg == null ? PARSER : pckg + "." + PARSER;
		parserClass = model._class(name);
		CodeGenerator.appendJDocHeader(parserClass);
		burkeFischerK = parserClass.field(JMod.PRIVATE+JMod.STATIC+JMod.FINAL,
				int.class, "BURKE_FISCHER_K", JExpr.lit(BURKE_FISCHER_K));
		actionsEnum = parserClass._enum(JMod.PRIVATE+JMod.STATIC, "Actions");
		parseStack = new ParseStackGenerator(model, parserClass, nodes.getNodeInterface());
		JMethod getActionMethod = this.createGetActionMethod();
		this.createParseMethod(lexer, getActionMethod);
		this.initRuleIndices();
		this.addActions();
	}
	
	public JDefinedClass getParserClass(){
		return parserClass;
	}
	
	public JDefinedClass getParsingExceptionClass(){
		return parsingException;
	}
	
	public JDefinedClass getExceptionHandlerInterface(){
		return exceptionHandler;
	}
	
	private JDefinedClass createExceptionHandlerInterface(String pckg)
			throws JClassAlreadyExistsException{
		String name = pckg == null ? EXCEPTION_HANDLER
				: pckg + "." + EXCEPTION_HANDLER;
		JDefinedClass iface = model._class(
				JMod.PUBLIC, name, ClassType.INTERFACE);
		CodeGenerator.appendJDocHeader(iface);
		//add method for handling exceptions that takes a parsing exception
		//and returns a boolean indicating whether the parser should proceed
		//or terminate
		iface.method(JMod.PUBLIC, boolean.class, PROCEED_METHOD)
			.param(parsingException, "e");
		return iface;
	}
	
	private JDefinedClass createParsingExceptionClass(String pckg)
			throws JClassAlreadyExistsException{
		String name = pckg == null ? PARSING_EXCEPTION
				: pckg + "." + PARSING_EXCEPTION;
		JDefinedClass clazz = model._class(name);
		CodeGenerator.appendJDocHeader(clazz);
		clazz._extends(Exception.class);
		
		//add static method that takes received node and var args of expected symbols
		//and returns a message constructed from them
		JMethod msg = createGenerateMessageMethod(clazz);
		
		//add constructor that takes received node and var args of expected symbols
		JMethod cons = clazz.constructor(JMod.PUBLIC);
		JVar received = cons.param(nodes.getNodeInterface(),
				"received");
		JVar expected = cons.varParam(nodes.getSymbolsEnumeration(),
				"expected");
		cons.body().invoke("super").arg(JExpr.invoke(msg)
				.arg(received).arg(expected));
		
		return clazz;
	}

	private JMethod createGenerateMessageMethod(JDefinedClass clazz) {
		JMethod msg = clazz.method(JMod.PRIVATE+JMod.STATIC, String.class, "generateMessage");
		JVar received = msg.param(nodes.getNodeInterface(), "received");
		JVar expected = msg.varParam(nodes.getSymbolsEnumeration(), "expected");
		//initialize the message TODO - add the line and pos at the start of the message
		JVar str = msg.body().decl(model.ref(String.class), "msg",
				JExpr.lit("syntax error on token: ").plus(received).plus(JExpr.lit(".")));
		//switch on the length of the expected array
		JSwitch sw = msg.body()._switch(JExpr.ref(expected, "length"));
		//if length == 0 then suggest to delete the token
		JBlock body = sw._case(JExpr.lit(0)).body();
		body.assign(str, str.plus(JExpr.lit(" Delete this token.")));
		body._break();
		//if length == 1 then present the expected symbol
		body = sw._case(JExpr.lit(1)).body();
		body.assign(str, str.plus(JExpr.lit(" Expected: ")).plus(expected.component(JExpr.lit(0))));
		body._break();
		//if length > 1 then present all possible expected symbols
		body = sw._default().body();
		body.assign(str, str.plus(JExpr.lit(" Expected one of: ")));
		JForLoop loop = body._for();
		JVar index = loop.init(model.INT, "i", JExpr.lit(0));
		loop.test(index.lt(JExpr.ref(expected, "length")));
		loop.update(index.incr());
		JConditional cond = loop.body()._if(index.eq(JExpr.lit(0)));
		cond._then().assign(str, str.plus(expected.component(index)));
		cond._else().assign(str, str.plus(JExpr.lit(", ")).plus(expected.component(index)));
		//return the string
		msg.body()._return(str);
		return msg;
	}
	
	private void initRuleIndices(){
		ruleIndices = new HashMap<Rule, Integer>();
		List<Rule> rules = nodes.getRules();
		for( int i = 0 ; i < rules.size() ; ++i ){
			ruleIndices.put(rules.get(i), i);
		}
	}
	
	private void addActions(){
		stateIndex = new HashMap<State, Integer>();
		definedActions = new HashSet<Action>();
		Set<State> states = new HashSet<State>(table.getStates());
		State start = table.getStartState();
		//initialize the state indices
		int index = 0;
		stateIndex.put(start, index);
		states.remove(start);
		for(State s : states){
			stateIndex.put(s, ++index);
		}
		//generate the action code for all the states
		states.add(start);
		for(State s : states){
			this.addActionsForState(s);
		}
	}
	
	private void addActionsForState(State s){
		JMethod method = this.createStateActionMethod(s);
		getActionSwitch._case(JExpr.lit(stateIndex.get(s))).body()._return(JExpr.invoke(JExpr._this(), method).arg(getActionLookaheadParam));
	}
	
	private JMethod createStateActionMethod(State s){
		JMethod method = parserClass.method(JMod.PRIVATE, actionsEnum,
				"getState"+stateIndex.get(s)+"Action");
		method._throws(parsingException);
		JVar lookahead = method.param(nodes.getNodeInterface(), "lookahead");
		JSwitch sw = method.body()._switch(JExpr.invoke(lookahead, "type"));
		List<JEnumConstant> expected = new ArrayList<JEnumConstant>();
		//add a case statement for each valid lookahead symbol
		for(String symbol : nodes.getAllSymbols()){
			Action a = table.getAction(s, symbol);
			if(a != null){
				JEnumConstant actionConst = this.defineAction(a);
				sw._case(JExpr.ref(symbol)).body()._return(actionConst);
				expected.add(nodes.getSymbolsEnumeration().enumConstant(symbol));
			}
		}
		//the default case should throw a parsing exception with the
		//invalid lookahead node and an array of expected symbols
		JArray array = JExpr.newArray(nodes.getSymbolsEnumeration());
		for(JEnumConstant exp : expected){
			array.add(exp);
		}
		sw._default().body()._throw(JExpr._new(parsingException)
				.arg(lookahead).arg(array));
		return method;
	}
	
	private JEnumConstant defineAction(Action a){
		String name = this.getActionEnumName(a);
		JEnumConstant actionConst = actionsEnum.enumConstant(name);
		if(definedActions.contains(a)){
			return actionConst;
		}
		JBlock body = parseActionSwitch._case(JExpr.ref(name)).body();
		switch(a.id()){
		case ACCEPT:
			body._return(JExpr.TRUE);
			break;
		case GOTO:
			this.defineShiftGotoAction(a, body);
			break;
		case REDUCE:
			this.defineReduceAction(a, body);
			break;
		case SHIFT:
			this.defineShiftGotoAction(a, body);
			break;
		default:
			throw new Error();
		}
		definedActions.add(a);
		return actionConst;
	}

	private String getActionEnumName(Action a) throws Error {
		String name;
		switch(a.id()){
		case ACCEPT:
			name = "ACCEPT";
			break;
		case GOTO:
			name = "GOTO" + stateIndex.get(a.nextState());
			break;
		case REDUCE:
			name = "REDUCE" + ruleIndices.get(a.production());
			break;
		case SHIFT:
			name = "SHIFT" + stateIndex.get(a.nextState());
			break;
		default:
			throw new Error();
		}
		return name;
	}

	private void defineReduceAction(Action a, JBlock body) {
		Rule r = a.production();
		JDefinedClass nodeClass = nodes.getConcreteNode(r);
		String[] rhs = r.rightHandSide();
		JVar[] fields = new JVar[rhs.length];
		for( int i = fields.length - 1 ; i >= 0 ; --i ){
			JDefinedClass type = nodes.getAbstractNode(rhs[i]);
			if(type == null){
				throw new NullPointerException("Node for: "+rhs[i]+" is null.");
			}
			fields[i] = body.decl(type, "field"+i, JExpr.cast(type, JExpr.invoke(stack, parseStack.getPopMethod())));
		}
		JInvocation newNodeClass = JExpr._new(nodeClass);
		for(JVar field : fields){
			newNodeClass = newNodeClass.arg(field);
		}
		JVar node = body.decl(nodes.getNodeInterface(), "reduced", JExpr.invoke(newNodeClass, "replace"));
		body.invoke(JExpr._this(), parseNextMethod)
				.arg(stack).arg(node);
		body._return(JExpr.invoke(JExpr._this(), parseNextMethod)
				.arg(stack).arg(lookaheadNode));
	}

	private void defineShiftGotoAction(Action a, JBlock body) {
		body.invoke(stack, parseStack.getPushMethod()).arg(lookaheadNode).arg(JExpr.lit(stateIndex.get(a.nextState())));
		body._return(JExpr.FALSE);
	}
	
	private void createParseMethod(JDefinedClass lexerClass, JMethod getActionMethod){
		JDefinedClass syntaxNodeClass = nodes.getNodeInterface();
		//initialize the parseNext method
		createParseNextMethod(getActionMethod, syntaxNodeClass);
		//create this method
		JMethod method = parserClass.method(JMod.PUBLIC, syntaxNodeClass, "parse");
		method._throws(IOException.class);
		method._throws(parsingException);
		JVar lexer = method.param(lexerClass, "lexer");
		JVar errorHandler = method.param(exceptionHandler, "errHandler");
		JBlock body = method.body();
		JDefinedClass stack = parseStack.getParseStack();
		//initialize the stack queue
		JVar stackQ = body.decl(
				model.ref(Deque.class).narrow(stack),
				"stackQ",
				JExpr._new(model.ref(LinkedList.class).narrow(stack)));
		//push the start state stack onto the q
		body.invoke(stackQ, "add").arg(JExpr._new(stack));
		JVar tokenQ = body.decl(
				model.ref(Queue.class).narrow(syntaxNodeClass),
				"tokenQ",
				JExpr._new(model.ref(LinkedList.class).narrow(syntaxNodeClass)));
		JVar err = body.decl(model.BOOLEAN, "error", JExpr.FALSE);
		JVar done = body.decl(model.BOOLEAN, "fin", JExpr.FALSE);
		//add the while loop to the body and set its body to the body
		body = body._while(done.not()).body();
		JVar cur = body.decl(
				stack,
				"cur",
				JExpr.invoke(JExpr.invoke(stackQ, "peekLast"), parseStack.getCloneMethod()));
		JVar lookahead = body.decl(syntaxNodeClass, "lookahead", JExpr.invoke(lexer, "nextToken"));
		JTryBlock tryb = body._try();
		tryb.body().assign(done, JExpr.invoke(JExpr._this(), parseNextMethod)
				.arg(cur).arg(lookahead));
		JCatchBlock catchb = tryb._catch(parsingException);
		JVar exc = catchb.param("e");
		//do error handling code here - TODO
		catchb.body().assign(err, JExpr.TRUE);
		JConditional cond = catchb.body()._if(errorHandler.ne(JExpr._null()));
		cond._else()._throw(exc);
		JConditional errCodeBlock = cond._then()._if(JExpr.invoke(errorHandler, PROCEED_METHOD).arg(exc));
		//if error handler says to proceed, then apply the burke-fischer error repair algorithm
		JBlock errRepair = errCodeBlock._then();
		//need to try every single token insertion, deletion, and replacement
		//of the token q starting from the first state of the nodes q - TODO
		
		errCodeBlock._else()._return(JExpr._null());
		
		//update the queues
		body.invoke(tokenQ, "add").arg(lookahead);
		body.invoke(stackQ, "add").arg(cur);
		body = body._if(JExpr.invoke(tokenQ, "size").gt(burkeFischerK))._then();
		body.invoke(tokenQ, "remove");
		body.invoke(stackQ, "remove");
		
		//return the first node on the last node stack
		method.body()._if(err)._then()._return(JExpr._null());
		method.body()._return(JExpr.invoke(JExpr.invoke(stackQ, "peekLast"), parseStack.getPopMethod()));
		
	}
	
	private JMethod createBurkeFischerMethod(){
		JDefinedClass iNode = nodes.getNodeInterface();
		JMethod bk = parserClass.method(JMod.PRIVATE, void.class, "burkeFischerRepair");
		JVar tokenQ = bk.param(model.ref(Queue.class).narrow(model.ref(Deque.class).narrow(iNode)), "tokenQ");
		JVar nodeStackQ = bk.param(model.ref(Queue.class).narrow(model.ref(Deque.class).narrow(iNode)), "nodeStackQ");
		//TODO
		
		return bk;
	}
	

	private void createParseNextMethod(JMethod getActionMethod,
			JDefinedClass syntaxNodeClass) {
		parseNextMethod = parserClass.method(JMod.PRIVATE, boolean.class, "parseNext");
		parseNextMethod._throws(parsingException);
		stack = parseNextMethod.param(
				parseStack.getParseStack(),
				"stack");
		lookaheadNode = parseNextMethod.param(syntaxNodeClass, "lookahead");
		JVar state = parseNextMethod.body().decl(model.INT, "state", JExpr.invoke(stack, parseStack.getCurStateMethod()));
		JVar action = parseNextMethod.body().decl(actionsEnum, "action", JExpr.invoke(JExpr._this(), getActionMethod).arg(state).arg(lookaheadNode));
		parseActionSwitch = parseNextMethod.body()._switch(action);
		parseActionSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unknown action.")));
	}

	/*
	private JTryBlock initializeErrorHandlingCode(JVar errorHandler, JBlock body) {
		JTryBlock tryblock = body._try();
		JCatchBlock catchblock = tryblock._catch(parsingException);
		JVar except = catchblock.param("e");
		JConditional cond = catchblock.body()._if(
				JExpr.invoke(nodeStack, "isEmpty").not()
				.cand(JExpr.invoke(JExpr.invoke(nodeStack, "peek"), "type").eq(nodes.getErrorSymbol())));
		cond._then().directStatement("//top of the stack is an error, so discard the lookahead and continue");
		cond._then().invoke(lookaheadStack, "pop");
		cond = cond._elseif(
				JExpr.invoke(lookaheadStack, "isEmpty").not()
				.cand(
						JExpr.invoke(JExpr.invoke(lookaheadStack, "peek"), "type")
						.eq(nodes.getErrorSymbol())
				));
		JConditional innercond = cond._then()._if(JExpr.invoke(nodeStack, "isEmpty"));
		innercond._then().directStatement("//the stack is empty and the lookahead is error, "
				+ "so return null");
		innercond._then()._return(JExpr._null());;
		innercond._else().directStatement("//pop the stack to look for a state from which"
				+ " the error can be resolved");
		innercond._else().invoke(nodeStack, "pop");
		innercond._else().invoke(statesStack, "pop");
		cond._else().directStatement("//this is a fresh error");
		innercond = cond._else()._if(
				errorHandler.ne(JExpr._null())
				.cand(JExpr.invoke(errorHandler, PROCEED_METHOD).arg(except)));
		JVar errNode = innercond._then().decl(nodes.getErrorNode(), "errorNode",
				JExpr._new(nodes.getErrorNode()));
		innercond._then().invoke(lookaheadStack, "push").arg(errNode);
		innercond._else()._throw(except);
		return tryblock;
	}
	*/
	
	private JMethod createGetActionMethod(){
		JMethod method = parserClass.method(JMod.PRIVATE, actionsEnum, "getAction");
		method._throws(parsingException);
		JVar state = method.param(int.class, "state");
		getActionLookaheadParam = method.param(nodes.getNodeInterface(), "lookahead");
		getActionSwitch = method.body()._switch(state);
		getActionSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unknown state.")));
		return method;
	}
	
	
	
	
	
	
	
	
}
