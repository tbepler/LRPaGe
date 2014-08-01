package bepler.lrpage.code.generator.parser.repair;

import java.util.List;

import bepler.lrpage.code.generator.LexerGenerator;
import bepler.lrpage.code.generator.ParseStackGenerator;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

public class TryDeletionsMethod {
	
	private static final String NAME = "tryDeletions";
	
	private final JMethod method;
	
	public TryDeletionsMethod(JDefinedClass parser, JDefinedClass iNode,
			ParseStackGenerator stack, LexerGenerator lexer, Repair repair,
			Mod mod, RepairMethod repairMethod, StopCriteriaMethod stop){
		
		method = initMethod(parser, iNode, stack, lexer, repair, mod, 
				repairMethod, stop);
		
	}
	
	public JExpression invoke(JExpression stack, JExpression tokenList, JExpression lexer){
		return JExpr.invoke(method).arg(stack).arg(tokenList).arg(lexer);
	}
	
	public JExpression invoke(JExpression target, JExpression stack, JExpression tokenList,
			JExpression lexer){
		return JExpr.invoke(target, method).arg(stack).arg(tokenList).arg(lexer);
	}
	
	protected JMethod initMethod(JDefinedClass parser, JDefinedClass iNode,
			ParseStackGenerator stack, LexerGenerator lexer, Repair repair, 
			Mod mod, RepairMethod repairMethod, StopCriteriaMethod stop){
		
		JCodeModel model = parser.owner();
		JClass tokenListType = model.ref(List.class).narrow(iNode);
		JMethod m = parser.getMethod(NAME, new JType[]{stack.getParseStack(),
				tokenListType, lexer.getLexerClass()});
		if(m == null){
			m = defineTryDeletionsMethod(parser, iNode, stack, lexer, repair,
					mod, repairMethod, stop, model, tokenListType);
		}
		return m;
	}

	private JMethod defineTryDeletionsMethod(JDefinedClass parser,
			JDefinedClass iNode, ParseStackGenerator stack,
			LexerGenerator lexer, Repair repair, Mod mod,
			RepairMethod repairMethod, StopCriteriaMethod stop,
			JCodeModel model, JClass tokenListType) {
		
		JMethod m = parser.method(JMod.PRIVATE, repair.asJType(), NAME);
		//initialize parameters
		JVar stackArg = m.param(stack.getParseStack(), "stack");
		JVar tokenList = m.param(tokenListType, "history");
		JVar lexArg = m.param(lexer.getLexerClass(), "lexer");
		//initialize the best repair field and the token list
		JVar best = m.body().decl(repair.asJType(), "best", JExpr._null());
		//try deletions of each position in the token list
		JForLoop loop = m.body()._for();
		JVar index = loop.init(model.INT, "i", JExpr.lit(0));
		loop.test(index.lt(JExpr.invoke(tokenList, "size")));
		loop.update(index.incr());
		//remove token and store it in a variable
		JVar token = loop.body().decl(iNode, "token",
				JExpr.invoke(tokenList, "remove").arg(index));
		//create the mod object
		JVar modVar = loop.body().decl(mod.asJType(), "mode", mod.newDeletionMod(index, token));
		//invoke the repair method and store the result
		JVar repairVar = loop.body().decl(repair.asJType(), "repair",
				repairMethod.invoke(stackArg, tokenList, lexArg, modVar));
		//repair the list
		loop.body().invoke(tokenList, "add").arg(index).arg(token);
		//check the stop condition
		loop.body()._if(stop.invoke(repairVar))._then()._return(repairVar);
		//check if this result should replace best
		loop.body()._if(repair.compare(repairVar, best).gt(JExpr.lit(0)))
			._then().assign(best, repairVar);
		//exit loop
		m.body()._return(best);
		return m;
	}
	
	
	
	
	
	
	
}
