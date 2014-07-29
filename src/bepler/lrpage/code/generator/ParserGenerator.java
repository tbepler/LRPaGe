package bepler.lrpage.code.generator;

import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

import bepler.lrpage.grammar.Rule;
import bepler.lrpage.parser.Action;
import bepler.lrpage.parser.ActionTable;
import bepler.lrpage.parser.ActionTableBuilder;
import bepler.lrpage.parser.State;
import bepler.lrpage.parser.Symbols;

public class ParserGenerator {
	
	private static final String PARSER = "Parser";
	
	private final ActionTable table;

	public ParserGenerator(Symbols s){
		ActionTableBuilder builder = new ActionTableBuilder(s);
		table = builder.generateActionTable();
	}
	
	private JCodeModel model;
	private NodeGenerator nodes;
	private JDefinedClass parserClass;
	private JDefinedClass actionsEnum;
	private JSwitch getActionSwitch;
	private JVar getActionLookaheadParam;
	private JSwitch parseActionSwitch;
	private JVar statesStack;
	private JVar nodeStack;
	private JVar lookaheadStack;
	private JVar lookaheadNode;
	private Set<Action> definedActions;
	private Map<State, Integer> stateIndex;
	private Map<Rule, Integer> ruleIndices;
	
	/**
	 * Warning: this method stores temporary internal state and is
	 * therefore not thread safe.
	 * @param prefix
	 * @param model
	 * @param lexer
	 * @param syntaxNode
	 * @throws JClassAlreadyExistsException
	 */
	public JDefinedClass generate(String prefix, JCodeModel model,
			JDefinedClass lexer, NodeGenerator nodes)
			throws JClassAlreadyExistsException{
		this.model = model;
		this.nodes = nodes;
		parserClass = model._class(prefix + PARSER);
		actionsEnum = parserClass._enum(JMod.PRIVATE+JMod.STATIC, "Actions");
		JMethod getActionMethod = this.createGetActionMethod();
		this.createParseMethod(lexer, getActionMethod);
		this.initRuleIndices();
		this.addActions();
		return parserClass;
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
		JEnumConstant error = actionsEnum.enumConstant("ERROR");
		this.defineErrorAction(error);
		JMethod method = this.createStateActionMethod(s, error);
		getActionSwitch._case(JExpr.lit(stateIndex.get(s))).body()._return(JExpr.invoke(JExpr._this(), method).arg(getActionLookaheadParam));
	}
	
	private void defineErrorAction(JEnumConstant error){
		parseActionSwitch._case(error).body()._throw(
				JExpr._new(model.ref(RuntimeException.class))
						.arg(JExpr.lit("Syntax error on token: ").plus(lookaheadNode)));
	}
	
	private JMethod createStateActionMethod(State s, JEnumConstant error){
		JMethod method = parserClass.method(JMod.PRIVATE, actionsEnum, "getState"+stateIndex.get(s)+"Action");
		JVar lookahead = method.param(nodes.getNodeInterface(), "lookahead");
		JSwitch sw = method.body()._switch(JExpr.invoke(lookahead, "type"));
		sw._default().body()._return(error);
		for(String symbol : nodes.getAllSymbols()){
			Action a = table.getAction(s, symbol);
			if(a != null){
				JEnumConstant actionConst = this.defineAction(a);
				sw._case(nodes.getSymbolsEnumeration().enumConstant(symbol)).body()._return(actionConst);
			}
		}
		return method;
	}
	
	private JEnumConstant defineAction(Action a){
		String name = this.getActionEnumName(a);
		JEnumConstant actionConst = actionsEnum.enumConstant(name);
		if(definedActions.contains(a)){
			return actionConst;
		}
		JBlock body = parseActionSwitch._case(actionConst).body();
		switch(a.id()){
		case ACCEPT:
			body._return(JExpr.invoke(nodeStack, "peek"));
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
			fields[i] = body.decl(type, "field"+i, JExpr.cast(type, JExpr.invoke(nodeStack, "pop")));
			body.invoke(statesStack, "pop");
		}
		JInvocation newNodeClass = JExpr._new(nodeClass);
		for(JVar field : fields){
			newNodeClass = newNodeClass.arg(field);
		}
		body.invoke(lookaheadStack, "push").arg(newNodeClass);
		body._break();
	}

	private void defineShiftGotoAction(Action a, JBlock body) {
		body.invoke(nodeStack, "push").arg(JExpr.invoke(lookaheadStack, "pop"));
		body.invoke(statesStack, "push").arg(JExpr.lit(stateIndex.get(a.nextState())));
		body._break();
	}
	
	private void createParseMethod(JDefinedClass lexerClass, JMethod getActionMethod){
		JDefinedClass syntaxNodeClass = nodes.getNodeInterface();
		JMethod method = parserClass.method(JMod.PUBLIC, syntaxNodeClass, "parse");
		method._throws(IOException.class);
		JVar lexer = method.param(lexerClass, "lexer");
		JBlock body = method.body();
		//initialize the parsing stacks
		statesStack = body.decl(
				model.ref(Deque.class).narrow(Integer.class),
				"states",
				JExpr._new(model.ref(LinkedList.class).narrow(Integer.class)));
		//push the first state (0) onto the state stack
		body.invoke(statesStack, "push").arg(JExpr.lit(0));
		nodeStack = body.decl(
				model.ref(Deque.class).narrow(syntaxNodeClass),
				"stack",
				JExpr._new(model.ref(LinkedList.class).narrow(syntaxNodeClass)));
		lookaheadStack = body.decl(
				model.ref(Deque.class).narrow(syntaxNodeClass),
				"lookaheadStack",
				JExpr._new(model.ref(LinkedList.class).narrow(syntaxNodeClass)));
		//add the first token from the lexer to the lookaheadStack
		body.invoke(lookaheadStack, "add").arg(JExpr.invoke(lexer, "nextToken"));
		//add the while loop to the body and set its body to the body
		body = body._while(JExpr.TRUE).body();
		//if the lookahead stack is empty, put the next token from the lexer into it
		body._if(JExpr.invoke(lookaheadStack, "isEmpty"))
			._then().invoke(lookaheadStack, "add").arg(JExpr.invoke(lexer, "nextToken"));		
		//set the current state and lookahead
		JVar state = body.decl(model.ref(int.class), "state", JExpr.invoke(statesStack, "peek"));
		lookaheadNode = body.decl(syntaxNodeClass, "lookahead", JExpr.invoke(lookaheadStack, "peek"));
		//lookup the action corresponding to this state, lookahead pair
		JVar action = body.decl(actionsEnum, "action", JExpr.invoke(JExpr._this(), getActionMethod).arg(state).arg(lookaheadNode));
		//switch over the action
		parseActionSwitch = body._switch(action);
		parseActionSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unknown action.")));
	}
	
	private JMethod createGetActionMethod(){
		JMethod method = parserClass.method(JMod.PRIVATE, actionsEnum, "getAction");
		JVar state = method.param(int.class, "state");
		getActionLookaheadParam = method.param(nodes.getNodeInterface(), "lookahead");
		getActionSwitch = method.body()._switch(state);
		getActionSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unknown state.")));
		return method;
	}
	
	
	
	
	
	
	
	
}
