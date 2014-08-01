package bepler.lrpage.code.generator.parser.repair;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

/**
 * Defines the method for checking if a distance meets the stopping
 * criteria.
 * 
 * @author Tristan Bepler
 *
 */
public class StopCriteriaMethod {
	
	private static final String NAME = "meetsStopCriteria";
	
	private final JMethod method;
	
	public StopCriteriaMethod(JDefinedClass parser, JExpression k, JExpression r){
		method = initMethod(parser, k, r);
	}
	
	public JExpression invoke(JExpression target, JExpression distArg){
		return JExpr.invoke(target, method).arg(distArg);
	}
	
	public JExpression invoke(JExpression distArg){
		return JExpr.invoke(method).arg(distArg);
	}
	
	public JType returnType(){
		return method.type();
	}
	
	protected JMethod initMethod(JDefinedClass parser, JExpression k, JExpression r){
		JMethod m = parser.getMethod(NAME, new JType[]{parser.owner().INT});
		if(m == null){
			m = parser.method(JMod.PRIVATE, boolean.class, NAME);
			m.body()._return(r.gte(JExpr.lit(0)).cand(m.param(int.class, "dist").gte(k.plus(r))));
		}
		return m;
	}
	
}
