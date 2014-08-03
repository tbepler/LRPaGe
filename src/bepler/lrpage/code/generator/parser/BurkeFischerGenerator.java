package bepler.lrpage.code.generator.parser;

import java.util.ArrayList;
import java.util.List;

import bepler.lrpage.code.generator.LexerGenerator;
import bepler.lrpage.code.generator.TokenFactoryGenerator;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JVar;

public class BurkeFischerGenerator {
	
	private static final String REPAIR = "Repair";
	private static final String DELETE = "DELETE";
	private static final String INSERT = "INSERT";
	private static final String REPLACE = "REPLACE";
	private static final String NONE = "NONE";
	
	private final JCodeModel model;
	private final JDefinedClass iNode;
	private final LexerGenerator lexer;
	private final TokenFactoryGenerator fac;
	private final JDefinedClass parser;
	private final JDefinedClass parseException;
	private final ParseStackGenerator parseStack;
	private final JMethod parseNext;
	
	private final JDefinedClass modsEnum;
	private final JDefinedClass repairClass;
	
	private final JMethod meetsStopCriteria;
	private final JMethod testRepairMethod;
	private final JMethod tryDeletions;
	
	private JVar dist;
	private JVar mod;
	private JVar index;
	private JVar token;
	
	
	public BurkeFischerGenerator(JCodeModel model, JDefinedClass iNode,
			LexerGenerator lexer, JDefinedClass parser, JDefinedClass parseException,
			ParseStackGenerator parseStack, JMethod parseNext) throws JClassAlreadyExistsException{
		this.model = model;
		this.iNode = iNode;
		this.lexer = lexer;
		this.fac = lexer.getTokenFactoryGenerator();
		this.parser = parser;
		this.parseException = parseException;
		this.parseStack = parseStack;
		this.parseNext = parseNext;
		modsEnum = this.createModsEnum();
		repairClass = this.createRepairClass();
		meetsStopCriteria = this.defineMeetsStopCritieria();
		testRepairMethod = this.defineTestRepairMethod();
		tryDeletions = this.defineTryDeletionsMethod();
	}
	
	private JMethod defineTryDeletionsMethod(){
		JMethod method = parser.method(JMod.PRIVATE, repairClass, "tryDeletions");
		JVar stack = method.param(parseStack.getParseStack(), "stack");
		JVar history = method.param(model.ref(List.class).narrow(iNode), "history");
		JVar lex = method.param(lexer.getLexerClass(), "lexer");
		JVar stop = method.param(int.class, "stop");
		JVar best = initializeBestRepair(method, history);
		JVar test = initializeTestList(method);
		
		JForLoop loop = method.body()._for();
		JVar index = loop.init(model.INT, "i", JExpr.lit(0));
		loop.test(index.lt(JExpr.invoke(history, "size")));
		loop.update(index.incr());
		JBlock body = loop.body();
		
		body.invoke(test, "addAll").arg(history);
		JVar removed = body.decl(iNode, "removed", JExpr.invoke(test, "remove").arg(index));
		JVar stackCopy = copyStack(stack, body);
		JVar dist = computeDistance(lex, stop, test, body, stackCopy);
		JInvocation constRepair = JExpr._new(repairClass).arg(dist)
				.arg(modsEnum.enumConstant(DELETE)).arg(index).arg(removed);
		checkStopCriteria(stop, body, dist, constRepair);
		checkDistanceAndUpdateBest(best, body, dist, constRepair);
		body.invoke(test, "clear");
		method.body()._return(best);
		return method;
	}

	private void checkDistanceAndUpdateBest(JVar best, JBlock body, JVar dist,
			JInvocation newRepair) {
		body._if(dist.gt(best.ref(this.dist)))._then()
			.assign(best, newRepair);
	}

	private void checkStopCriteria(JVar stop, JBlock body, JVar dist,
			JInvocation constRepair) {
		body._if(JExpr.invoke(meetsStopCriteria).arg(stop).arg(dist))
			._then()._return(constRepair);
	}

	private JVar computeDistance(JVar lex, JVar stop, JVar test, JBlock body,
			JVar stackCopy) {
		JVar dist = body.decl(model.INT, "dist",
				JExpr.invoke(testRepairMethod)
					.arg(stackCopy)
					.arg(test)
					.arg(lex)
					.arg(stop));
		return dist;
	}

	private JVar copyStack(JVar stack, JBlock body) {
		JVar stackCopy = body.decl(parseStack.getParseStack(), "stackCopy",
				JExpr.invoke(stack, parseStack.getCloneMethod()));
		return stackCopy;
	}

	private JVar initializeTestList(JMethod method) {
		JVar test = method.body().decl(
				model.ref(List.class).narrow(iNode),
				"test",
				JExpr._new(model.ref(ArrayList.class).narrow(iNode)));
		return test;
	}

	private JVar initializeBestRepair(JMethod method, JVar history) {
		JVar best = method.body().decl(
				repairClass,
				"best",
				JExpr._new(repairClass)
					.arg(JExpr.invoke(history, "size")
					.arg(modsEnum.enumConstant(NONE))
					.arg(JExpr.lit(-1))
					.arg(JExpr._null())));
		return best;
	}
	
	/**
	 * Defines a method which takes the parse stack, a list of history tokens,
	 * the lexer, and a stopping criteria and tests how many tokens can be parse
	 * without error.
	 * @return
	 */
	private JMethod defineTestRepairMethod(){
		JMethod method = parser.method(JMod.PRIVATE, int.class, "testRepair");
		JVar stack = method.param(parseStack.getParseStack(), "stack");
		JVar history = method.param(model.ref(List.class).narrow(iNode), "history");
		JVar lex = method.param(lexer.getLexerClass(), "lexer");
		JVar stop = method.param(int.class, "stop");
		JVar dist = method.body().decl(model.INT, "dist", JExpr.lit(0));
		method.body().invoke(lex, lexer.getMarkMethod());
		JVar fin = method.body().decl(model.BOOLEAN, "fin", JExpr.FALSE);
		JBlock body = method.body()._while(fin.not()).body();
		JVar token = body.decl(iNode, "token");
		JConditional cond = body._if(dist.lt(JExpr.invoke(history, "size")));
		cond._then().assign(token, JExpr.invoke(history, "get").arg(dist));
		cond._else().assign(token, JExpr.invoke(lex, lexer.getNextTokenMethod()));
		body.assign(dist, dist.plus(JExpr.lit(1)));
		body._if(JExpr.invoke(meetsStopCriteria).arg(stop).arg(dist))
			._then()._break();
		JTryBlock tryb = body._try();
		tryb.body().assign(fin, JExpr.invoke(parseNext).arg(stack).arg(token));
		tryb._catch(parseException).body().assign(fin, JExpr.TRUE);
		method.body().invoke(lex, lexer.getResetMethod());
		method.body().invoke(lex, lexer.getUnmarkMethod());
		method.body()._return(dist);
		return method;
	}
	
	/**
	 * Defines a method which tests whether a given distance meets a given
	 * stopping criteria
	 * @return
	 */
	private JMethod defineMeetsStopCritieria(){
		JMethod method = parser.method(JMod.PRIVATE+JMod.STATIC, boolean.class, "meetsStopCriteria");
		JVar stop = method.param(int.class, "stop");
		JVar dist = method.param(int.class, "dist");
		method.body()._return(stop.gte(JExpr.lit(0)).cand(dist.gte(stop)));
		return method;
	}
	
	/**
	 * Defines the repair object class. Constructor takes the distance, 
	 * the modification type, the index of the modification, and the
	 * inserted/replacement/deleted token.
	 * @return
	 * @throws JClassAlreadyExistsException
	 */
	private JDefinedClass createRepairClass() throws JClassAlreadyExistsException{
		JDefinedClass clazz = parser._class(JMod.PRIVATE, REPAIR);
		dist = clazz.field(JMod.PUBLIC+JMod.FINAL, int.class, "dist");
		mod = clazz.field(JMod.PUBLIC+JMod.FINAL, modsEnum, "mod");
		index = clazz.field(JMod.PUBLIC+JMod.FINAL, int.class, "index");
		token = clazz.field(JMod.PUBLIC+JMod.FINAL, iNode, "token");
		JMethod cons = clazz.constructor(JMod.PUBLIC);
		cons.body().assign(JExpr._this().ref(dist), cons.param(int.class, "dist"));
		cons.body().assign(JExpr._this().ref(mod), cons.param(modsEnum, "mod"));
		cons.body().assign(JExpr._this().ref(index), cons.param(int.class, "index"));
		cons.body().assign(JExpr._this().ref(token), cons.param(int.class, "token"));
		return clazz;
	}
	
	private JDefinedClass createModsEnum() throws JClassAlreadyExistsException{
		JDefinedClass clazz = parser._class(JMod.PRIVATE+JMod.STATIC, "Mods", ClassType.ENUM);
		clazz.enumConstant(DELETE);
		clazz.enumConstant(INSERT);
		clazz.enumConstant(REPLACE);
		clazz.enumConstant(NONE);
		return clazz;
	}
	
	public JMethod getBurkeFischerMethod(){
		//TODO
		return null;
	}
	
	public JDefinedClass getRepairClass(){
		return repairClass;
	}
	
	public JVar getRecoveryTokenField(){
		return token;
	}
	
	public JVar getRecoveryIndexField(){
		return index;
	}
	
	public JVar getRecoveryModField(){
		return mod;
	}
	
	public String getNoneMod(){
		return NONE;
	}
	
	public String getReplaceMod(){
		return REPLACE;
	}
	
	public String getInsertMod(){
		return INSERT;
	}
	
	public String getDeleteMod(){
		return DELETE;
	}

}
