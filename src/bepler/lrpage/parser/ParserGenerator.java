package bepler.lrpage.parser;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

import bepler.lrpage.grammar.Grammar;

public class ParserGenerator {
	
	private static final String PARSER = "Parser";
	
	private final ActionTable table;

	public ParserGenerator(Grammar g, String eofSymbol){
		Symbols s = new Symbols(g, eofSymbol);
		ActionTableBuilder builder = new ActionTableBuilder(s);
		table = builder.generateActionTable();
	}
	
	private JCodeModel model;
	private JSwitch getActionSwitch;
	private JVar getActionLookaheadParam;
	private JSwitch parseActionSwitch;
	
	/**
	 * Warning: this method stores temporary internal state and is
	 * therefore not thread safe.
	 * @param prefix
	 * @param model
	 * @param lexer
	 * @param syntaxNode
	 * @throws JClassAlreadyExistsException
	 */
	public void generate(String prefix, JCodeModel model,
			JDefinedClass lexer, JDefinedClass syntaxNode)
			throws JClassAlreadyExistsException{
		this.model = model;
		JDefinedClass parserClass = model._class(prefix + PARSER);
		JDefinedClass actionsEnum = parserClass._enum(JMod.PRIVATE+JMod.STATIC, "Actions");
		JMethod getActionMethod = this.createGetActionMethod(parserClass, actionsEnum, syntaxNode);
		
	}
	
	private void createParseMethod(JDefinedClass parserClass,
			JDefinedClass syntaxNodeClass, JDefinedClass lexerClass,
			JMethod getActionMethod, JDefinedClass actionsEnum){
		JMethod method = parserClass.method(JMod.PUBLIC, syntaxNodeClass, "parse");
		method._throws(IOException.class);
		JVar lexer = method.param(lexerClass, "lexer");
		JBlock body = method.body();
		//initialize the parsing stacks
		JVar statesStack = body.decl(
				model.ref(Deque.class).narrow(Integer.class),
				"states",
				JExpr._new(model.ref(LinkedList.class).narrow(Integer.class)));
		//push the first state (0) onto the state stack
		body.invoke(statesStack, "push").arg(JExpr.lit(0));
		JVar nodeStack = body.decl(
				model.ref(Deque.class).narrow(syntaxNodeClass),
				"stack",
				JExpr._new(model.ref(LinkedList.class).narrow(syntaxNodeClass)));
		JVar lookaheadStack = body.decl(
				model.ref(Deque.class).narrow(syntaxNodeClass),
				"lookaheadStack",
				JExpr._new(model.ref(LinkedList.class).narrow(syntaxNodeClass)));
		//add the first token from the lexer to the lookaheadStack
		body.invoke(lookaheadStack, "add").arg(JExpr.invoke(lexer, "nextToken"));
		//add the while loop to the body and set its body to the body
		body = body._while(JExpr.TRUE).body();
		//set the current state and lookahead
		JVar state = body.decl(model.ref(int.class), "state", JExpr.invoke(statesStack, "peek"));
		JVar lookahead = body.decl(syntaxNodeClass, "lookahead", JExpr.invoke(lookaheadStack, "peek"));
		//lookup the action corresponding to this state, lookahead pair
		JVar action = body.decl(actionsEnum, "action", JExpr.invoke(JExpr._this(), getActionMethod).arg(state).arg(lookahead));
		//switch over the action
		
	}
	
	private JMethod createGetActionMethod(JDefinedClass parserClass,
			JDefinedClass actionsEnum, JDefinedClass syntaxNodeClass){
		JMethod method = parserClass.method(JMod.PRIVATE, actionsEnum, "getAction");
		JVar state = method.param(int.class, "state");
		getActionLookaheadParam = method.param(syntaxNodeClass, "lookahead");
		getActionSwitch = method.body()._switch(state);
		getActionSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class)).arg(JExpr.lit("Unknown state.")));
		return method;
	}
	
	
	
	
	
	
	
	
}
