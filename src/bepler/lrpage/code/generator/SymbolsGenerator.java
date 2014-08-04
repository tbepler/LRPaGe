package bepler.lrpage.code.generator;

import java.util.HashMap;
import java.util.Map;

import bepler.lrpage.grammar.Grammar;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class SymbolsGenerator {
	
	private static final String NAME = "Symbols";
	
	private final Grammar g;
	private final JDefinedClass clazz;
	private final Map<String, JEnumConstant> enums = new HashMap<String, JEnumConstant>();
	
	public SymbolsGenerator(String pckg, JCodeModel model, Grammar g, Framework f) throws JClassAlreadyExistsException{
		this.g = g;
		clazz = this.defineClass(pckg, model, f);
	}
	
	private JDefinedClass defineClass(String pckg, JCodeModel model, Framework f)
			throws JClassAlreadyExistsException{
		String name = pckg == null ? NAME : pckg + "." + NAME;
		JDefinedClass c = CodeGenerator.appendJDocHeader(
				model._class(JMod.PUBLIC, name, ClassType.ENUM));
		
		JVar str = c.field(JMod.PRIVATE+JMod.FINAL, String.class, "name");
		JMethod cons = c.constructor(JMod.PRIVATE);
		cons.body().assign(JExpr._this().ref(str), cons.param(String.class, "name"));
		JMethod defCons = c.constructor(JMod.PRIVATE);
		defCons.body().assign(JExpr._this().ref(str), JExpr._null());
		
		JMethod toString = c.method(JMod.PUBLIC, String.class, "toString");
		toString.annotate(Override.class);
		toString.body()._if(str.eq(JExpr._null()))._then()._return(JExpr.invoke(JExpr._super(), "toString"));
		toString.body()._return(str);
		
		c._implements(f.getSymbolInterface());
		JMethod type = c.method(JMod.PUBLIC, int.class, "type");
		type.annotate(Override.class);
		type.body()._return(JExpr.invoke(JExpr._this(), "ordinal"));
		
		return c;
	}
	
	public JExpression getSymbolObj(String symbol){
		JEnumConstant obj = enums.get(symbol);
		if(obj == null){
			obj = clazz.enumConstant(symbol.toUpperCase());
			String pseudo = g.getPseudonym(symbol);
			if(pseudo != null){
				obj.arg(JExpr.lit(pseudo));
			}
			enums.put(symbol, obj);
		}
		return obj;
	}
	
	public JExpression getType(String symbol){
		JExpression obj = this.getSymbolObj(symbol);
		return JExpr.invoke(obj, "type");
	}
	
	public JExpression getRef(String symbol){
		this.getSymbolObj(symbol);
		return JExpr.ref(symbol.toUpperCase());
	}
	
	public JExpression castTo(JExpression exp){
		return JExpr.cast(clazz, exp);
	}
	
	public JExpression getName(String symbol){
		JExpression obj = this.getSymbolObj(symbol);
		return JExpr.invoke(obj, "toString");
	}
	
}
