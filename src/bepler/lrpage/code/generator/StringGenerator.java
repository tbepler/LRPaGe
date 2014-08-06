package bepler.lrpage.code.generator;

import java.util.ArrayList;
import java.util.List;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class StringGenerator {
	private List<JVar> fields;
	private List<JInvocation> methods;
	
	public StringGenerator(){
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
		JMethod toString= clazz.method(JMod.PUBLIC, int.class, "toString");
		toString.annotate(Override.class);
		JBlock jBlock = toString.body();
		JVar string= jBlock.decl(model._ref(String.class), "string", JExpr.lit(""));
	    for(JVar f:fields){
    		jBlock.assign(string, string.plus(JExpr.lit(" ").plus(JExpr.invoke(f, "toString"))));
	    }
    	for(JInvocation m:methods){
    		jBlock.assign(string, string.plus(JExpr.lit(" ").plus(JExpr.invoke(m, "toString"))));
    	}
		toString.body()._return(JExpr.invoke(string, "trim"));
		return toString;
	}
}
