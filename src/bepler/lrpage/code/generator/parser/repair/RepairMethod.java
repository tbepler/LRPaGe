package bepler.lrpage.code.generator.parser.repair;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

import bepler.lrpage.code.generator.LexerGenerator;
import bepler.lrpage.code.generator.ParseStackGenerator;
import bepler.lrpage.code.generator.parser.AdvanceMethod;
import bepler.lrpage.code.generator.parser.Status;

import com.sun.codemodel.JCase;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JWhileLoop;

public class RepairMethod {
	
	private static final String NAME = "repair";
	
	private final JMethod method;
	
	public RepairMethod(JDefinedClass parser, ParseStackGenerator stack,
			JDefinedClass iNode, LexerGenerator lexer, Status status, Mod mod,
			Repair repair, StopCriteriaMethod stop, AdvanceMethod advance){
		
		method = initMethod(parser, stack, iNode, lexer, status, mod, repair,
				stop, advance);
		
	}
	
	public JExpression invoke(JExpression stack, JExpression tokenList, JExpression lexer,
			JExpression mod){
		return JExpr.invoke(method).arg(stack).arg(tokenList).arg(lexer).arg(mod);
	}
	
	public JExpression invoke(JExpression target, JExpression stack, JExpression tokenList, 
			JExpression lexer, JExpression mod){
		return JExpr.invoke(target, method).arg(stack).arg(tokenList).arg(lexer).arg(mod);
	}
	
	public JType returnType(){
		return method.type();
	}
	
	protected JMethod initMethod(JDefinedClass parser, ParseStackGenerator stack,
			JDefinedClass iNode, LexerGenerator lexer, Status status, Mod mod,
			Repair repair, StopCriteriaMethod stop, AdvanceMethod advance){
		
		JCodeModel model = parser.owner();
		JClass tokenListType = model.ref(List.class).narrow(iNode);
		JMethod m = parser.getMethod(NAME, new JType[]{stack.getParseStack(),
				tokenListType, lexer.getLexerClass(), mod.asJType()});
		if(m == null){
			m = defineRepairMethod(parser, stack, iNode, lexer, status, mod,
					repair, stop, advance, model, tokenListType);
			
		}
		return m;
	}

	private JMethod defineRepairMethod(JDefinedClass parser,
			ParseStackGenerator stack, JDefinedClass iNode,
			LexerGenerator lexer, Status status, Mod mod, Repair repair,
			StopCriteriaMethod stop, AdvanceMethod advance, JCodeModel model,
			JClass tokenListType) {
		JMethod m;
		m = parser.method(JMod.PRIVATE, repair.asJType(), NAME);
		//initialize the parameters
		JVar stackArg = m.param(stack.getParseStack(), "stack");
		JVar tokenList = m.param(tokenListType, "tokenList");
		JVar lexerArg = m.param(lexer.getLexerClass(), "lexer");
		JVar modArg = m.param(mod.asJType(), "mod");
		//initialize the stack deque
		JVar stacks = initializeStackDeque(stack, model, m, stackArg);
		//initialize the token queue
		JVar tokenQ = m.body().decl(
				model.ref(Queue.class).narrow(iNode),
				"tokenQ",
				JExpr._new(model.ref(ArrayDeque.class).narrow(iNode)));
		//mark the lexer
		m.body().invoke(lexerArg, lexer.getMarkMethod());
		//initialize the completed, fin, and dist variables
		JVar dist = m.body().decl(model.INT, "dist", JExpr.lit(0));
		JVar compl = m.body().decl(model.BOOLEAN, "completed", JExpr.FALSE);
		JVar fin = m.body().decl(model.BOOLEAN, "fin", JExpr.FALSE);
		//build the while loop
		JWhileLoop loop = m.body()._while(fin.not());
		//get the next token
		JVar token = getNextToken(iNode, lexer, tokenList, lexerArg, dist,
				loop);
		//increment the distance
		loop.body().assign(dist, dist.plus(JExpr.lit(1)));
		//advance the stack with the next token and switch on the result status
		advanceAndSwitchOnStatus(lexer, status, advance, lexerArg, stacks,
				tokenQ, compl, fin, loop, token);
		//check the stop criteria
		loop.body()._if(stop.invoke(dist))._then().assign(fin, JExpr.TRUE);
		//exit loop
		//record the lexer's position, reset it, and unmark it
		JVar pos = recordLexerPosResetAndUnmark(lexer, model, m, lexerArg);
		//return a repair object constructed from the results of this parse
		m.body()._return(repair.newRepair(dist, tokenQ, stacks, modArg, pos, compl));
		return m;
	}

	private JVar recordLexerPosResetAndUnmark(LexerGenerator lexer,
			JCodeModel model, JMethod m, JVar lexerArg) {
		JVar pos = m.body().decl(model.INT, "pos", JExpr.invoke(lexerArg, lexer.getGetPosMethod()));
		m.body().invoke(lexerArg, lexer.getResetMethod());
		m.body().invoke(lexerArg, lexer.getUnmarkMethod());
		return pos;
	}

	private void advanceAndSwitchOnStatus(LexerGenerator lexer, Status status,
			AdvanceMethod advance, JVar lexerArg, JVar stacks, JVar tokenQ,
			JVar compl, JVar fin, JWhileLoop loop, JVar token) {
		
		JSwitch sw = loop.body()._switch(advance.invoke(stacks, tokenQ, token));
		JCase completed = sw._case(status.getCompletedLabel());
		completed.body().assign(fin, JExpr.assign(compl, JExpr.TRUE));
		completed.body()._break();
		//if an error occurred, set fin to true and move the lexer back one position
		JCase error = sw._case(status.getErrorLabel());
		error.body().assign(fin, JExpr.TRUE);
		error.body().invoke(lexerArg, lexer.getSetPosMethod())
			.arg(JExpr.invoke(lexerArg, lexer.getGetPosMethod()).minus(JExpr.lit(1)));
		error.body()._break();
		//if the status was neither complete nor error, then proceed normally
		sw._default().body()._break();
	}

	private JVar getNextToken(JDefinedClass iNode, LexerGenerator lexer,
			JVar tokenList, JVar lexerArg, JVar dist, JWhileLoop loop) {
		JVar token = loop.body().decl(iNode, "next");
		JConditional cond = loop.body()._if(dist.lt(JExpr.invoke(tokenList, "size")));
		cond._then().assign(token, JExpr.invoke(tokenList, "get").arg(dist));
		cond._else().assign(token, JExpr.invoke(lexerArg, lexer.getNextTokenMethod()));
		return token;
	}

	private JVar initializeStackDeque(ParseStackGenerator stack,
			JCodeModel model, JMethod m, JVar stackArg) {
		JVar stacks = m.body().decl(
				model.ref(Deque.class).narrow(stack.getParseStack()),
				"stacks",
				JExpr._new(model.ref(ArrayDeque.class).narrow(stack.getParseStack())));
		m.body().invoke(stacks, "add").arg(stackArg);
		return stacks;
	}

}
