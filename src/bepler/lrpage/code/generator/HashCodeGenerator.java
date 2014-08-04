package bepler.lrpage.code.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class HashCodeGenerator {
	private int initPrime;
	private int incPrime;
	private List<JVar> fields= new ArrayList<JVar>();
	private List<JInvocation> methods= new ArrayList<JInvocation>();
	
	public HashCodeGenerator(){
		initPrime=7;
		incPrime=13;
	}
	
	public HashCodeGenerator(int p1, int p2){
		initPrime= p1;
		incPrime= p2;
	}
	
	public void appendField(JVar field){
		fields.add(field);
	}
	
	public void appendMethod(JInvocation method){
		methods.add(method);
	}
	
	public JMethod define(JDefinedClass clazz){
		JCodeModel model= clazz.owner();
		JMethod hashCode= clazz.method(JMod.PUBLIC, int.class, "hashCode");
		hashCode.annotate(Override.class);
		JBlock jBlock = hashCode.body();
		//define variable for hash value to return
	    JVar hash= jBlock.decl(model.INT, "hash", JExpr.lit(initPrime));
	    for(JVar f:fields){
    		jBlock.assign(hash, hash.mul(JExpr.lit(incPrime).plus(JExpr.invoke(f, "hashCode"))));
	    }
    	for(JInvocation m:methods){
    		jBlock.assign(hash, hash.mul(JExpr.lit(incPrime).plus(JExpr.invoke(m, "hashCode"))));
    	}
		hashCode.body()._return(hash);
		return hashCode;

	}
	
}
