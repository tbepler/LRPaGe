package bepler.lrpage.code.generator;

import java.util.ArrayList;
import java.util.List;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class EqualsGenerator {
	private List<JVar> fields;
	private List<JInvocation> methods;
	
	public EqualsGenerator(){
		fields= new ArrayList<JVar>();
		methods= new ArrayList<JInvocation>();
	}
	
	public void appendField(JVar field){
		fields.add(field);
	}
	
	public void appendMethod(JInvocation method){
		methods.add(method);
	}
	
	
	public JMethod define(JDefinedClass clazz){
		JCodeModel model= clazz.owner();
		JMethod equals= clazz.method(JMod.PUBLIC, boolean.class, "equals");
		equals.annotate(Override.class);
		JVar o = equals.param(Object.class, "o");
		
		JBlock jBlock = equals.body();
	    JVar ret= jBlock.decl(model.BOOLEAN, "ret", JExpr.TRUE);
	    
		//if objects are the same, return true
		JConditional sameObj= jBlock._if(JExpr._this().eq(o));
		sameObj._then()._return(JExpr.lit(true));
		//if object is null, return false
		JConditional nullObj= jBlock._if(o.eq(null));
		nullObj._then()._return(JExpr.lit(false));
		//if object is not instance of same class, return false
		JConditional notInstOf = jBlock._if(JExpr.lit(JExpr._this().getClass().equals(o.getClass())).not());
		notInstOf._then()._return(JExpr.lit(false));
		
		//compare field values
		for(JVar f:fields){
			JConditional notEq= jBlock._if(JExpr.lit(JExpr._this().ref(f).equals(o.ref(f).not())));
			notEq._then()._return(JExpr.lit(false));
		}
		
		equals.body()._return(JExpr.lit(true));
		
		return equals;
	}
}
