package bepler.lrpage.code.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
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

public class ParsingEngineGenerator {
	
	public static final String PROCEED_METHOD = "proceed";

	private static final String PARSER = "ParsingEngineImpl";
	
	private final ActionTable table;
	private final JCodeModel model;
	private final Framework f;
	private final SymbolsGenerator symbolGen;
	private final NodeGenerator nodes;
	private final JDefinedClass clazz;
	private final JDefinedClass actionsEnum;
	
	private JSwitch getActionSwitch;
	private JVar getActionLookaheadParam;
	
	private final JMethod advance;
	private final JSwitch advanceActionSwitch;
	private final JVar stack;
	private final JVar lookahead;
	private final JSwitch expectedStateSwitch;
	
	private Set<Action> definedActions;
	private Map<State, Integer> stateIndex;
	private Map<Rule, Integer> ruleIndices;

	public ParsingEngineGenerator(Symbols s, String pckg, JCodeModel model,
			Framework f, NodeGenerator nodes, SymbolsGenerator symbolGen)
			throws JClassAlreadyExistsException{
		ActionTableBuilder builder = new ActionTableBuilder(s);
		table = builder.generateActionTable();
		
		this.model = model;
		this.nodes = nodes;
		this.f = f;
		this.symbolGen = symbolGen;
		String name = pckg == null ? PARSER : pckg + "." + PARSER;
		clazz = model._class(name)._implements(
				f.getParsingEngineInterface().narrow(nodes.getVisitorInterface()));
		CodeGenerator.appendJDocHeader(clazz);
		actionsEnum = clazz._enum(JMod.PRIVATE+JMod.STATIC, "Actions");
		
		JMethod getActionMethod = this.createGetActionMethod();
		
		advance = clazz.method(JMod.PUBLIC, f.getStatusEnum(), "advance");
		advance.annotate(Override.class);
		stack = advance.param(f.getStackClass().narrow(nodes.getVisitorInterface()), "stack");
		lookahead = advance.param(f.getNodeInterface().narrow(nodes.getVisitorInterface()), "lookahead");
		JVar action = advance.body().decl(actionsEnum, "action",
				JExpr.invoke(getActionMethod)
				.arg(JExpr.invoke(stack, "curState"))
				.arg(lookahead));
		advanceActionSwitch = advance.body()._switch(action);
		advanceActionSwitch._default().body()._return(f.getStatusEnum().staticRef("ERROR"));
		
		JMethod expected = clazz.method(JMod.PUBLIC, f.getSymbolInterface().array(), "expectedSymbols");
		expected.annotate(Override.class);
		JVar state = expected.param(int.class, "state");
		expectedStateSwitch = expected.body()._switch(state);
		expectedStateSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg("Unknown state"));
		
		
		this.initRuleIndices();
		this.addActions();
	}
	
	public JDefinedClass getParsingEngine(){
		return clazz;
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
		JMethod method = clazz.method(JMod.PRIVATE, actionsEnum,
				"getState"+stateIndex.get(s)+"Action");
		JVar lookahead = method.param(f.getNodeInterface().narrow(nodes.getVisitorInterface()), "lookahead");
		JSwitch sw = method.body()._switch(symbolGen.castTo(JExpr.invoke(lookahead, "symbol")));
		List<JExpression> expected = new ArrayList<JExpression>();
		//add a case statement for each valid lookahead symbol
		for(String symbol : nodes.getAllSymbols()){
			Action a = table.getAction(s, symbol);
			if(a != null){
				JEnumConstant actionConst = this.defineAction(a);
				sw._case(symbolGen.getRef(symbol)).body()._return(actionConst);
				expected.add(symbolGen.getSymbolObj(symbol));
			}
		}
		//the default case should return error action
		sw._default().body()._return(actionsEnum.enumConstant("ERROR"));
		
		JArray array = JExpr.newArray(f.getSymbolInterface());
		for(JExpression exp : expected){
			array.add(exp);
		}
		
		expectedStateSwitch._case(JExpr.lit(stateIndex.get(s)))
			.body()._return(array);
		
		return method;
	}
	
	private JEnumConstant defineAction(Action a){
		String name = this.getActionEnumName(a);
		JEnumConstant actionConst = actionsEnum.enumConstant(name);
		if(definedActions.contains(a)){
			return actionConst;
		}
		JBlock body = advanceActionSwitch._case(JExpr.ref(name)).body();
		switch(a.id()){
		case ACCEPT:
			body._return(f.getStatusEnum().staticRef("COMPLETE"));
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
			fields[i] = body.decl(type, "field"+i, JExpr.cast(type, JExpr.invoke(stack, "pop")));
		}
		JInvocation newNodeClass = JExpr._new(nodeClass);
		for(JVar field : fields){
			newNodeClass = newNodeClass.arg(field);
		}
		JVar node = body.decl(nodes.getNodeInterface(), "reduced", JExpr.invoke(newNodeClass, "replace"));
		body.invoke(JExpr._this(), advance)
				.arg(stack).arg(node);
		body._return(JExpr.invoke(JExpr._this(), advance)
				.arg(stack).arg(lookahead));
	}

	private void defineShiftGotoAction(Action a, JBlock body) {
		body.invoke(stack, "push").arg(lookahead).arg(JExpr.lit(stateIndex.get(a.nextState())));
		body._return(f.getStatusEnum().staticRef("NOMINAL"));
	}
	
	private JMethod createGetActionMethod(){
		JMethod method = clazz.method(JMod.PRIVATE, actionsEnum, "getAction");
		JVar state = method.param(int.class, "state");
		getActionLookaheadParam = method.param(f.getNodeInterface().narrow(nodes.getVisitorInterface()), "lookahead");
		getActionSwitch = method.body()._switch(state);
		getActionSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unknown state.")));
		return method;
	}
	
	
	
	
	
	
	
	
}
