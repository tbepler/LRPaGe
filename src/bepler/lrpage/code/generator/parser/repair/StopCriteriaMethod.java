package bepler.lrpage.code.generator.parser.repair;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

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
	
	public StopCriteriaMethod(JDefinedClass parser, JExpression k, JExpression r,
			Repair repair){
		method = initMethod(parser, k, r, repair);
	}
	
	public JExpression invoke(JExpression target, JExpression arg){
		return JExpr.invoke(target, method).arg(arg);
	}
	
	public JExpression invoke(JExpression arg){
		return JExpr.invoke(method).arg(arg);
	}
	
	public JType returnType(){
		return method.type();
	}
	
	protected JMethod initMethod(JDefinedClass parser, JExpression k, JExpression r,
			Repair repair){
		JMethod m = parser.getMethod(NAME, new JType[]{parser.owner().INT});
		if(m == null){
			m = parser.method(JMod.PRIVATE, boolean.class, NAME);
			m.body()._return(r.gte(JExpr.lit(0)).cand(m.param(int.class, "dist").gte(k.plus(r))));
		}
		m = parser.getMethod(NAME, new JType[]{repair.asJType()});
		if(m == null){
			m = parser.method(JMod.PRIVATE, boolean.class, NAME);
			JVar repArg = m.param(repair.asJType(), "repair");
			m.body()._if(repair.getCompletedField(repArg))._then()._return(JExpr.TRUE);
			m.body()._return(this.invoke(repair.getDistField(repArg)));
		}
		return m;
	}
	
}
