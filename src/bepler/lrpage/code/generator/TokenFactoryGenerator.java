package bepler.lrpage.code.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

public class TokenFactoryGenerator {
	
	private static final String SIZE = "size";

	private static final String TOKEN_FACTORY = "TokenFactory";
	
	private final JCodeModel model;
	private final JDefinedClass fac;
	private final JMethod sizeMethod;
	private final JMethod buildIndex;
	private final JMethod buildIndexTextLinePos;
	
	private JSwitch buildIndexSwitch;
	private JSwitch buildIndexTextLinePosSwitch;
	private JVar text;
	private JVar line;
	private JVar pos;
	private int size = 0;
	
	public TokenFactoryGenerator(String pckg, JCodeModel model, JDefinedClass iNode)
			throws JClassAlreadyExistsException{
		this.model = model;
		String name = pckg == null ? TOKEN_FACTORY : pckg + "." + TOKEN_FACTORY;
		fac = CodeGenerator.appendJDocHeader(model._class(name));
		sizeMethod = this.defineSizeMethod();
		buildIndex = this.defineBuildIndexMethod(iNode);
		buildIndexTextLinePos = this.defineBuildIndexTextLinePosMethod(iNode);
	}
	
	public int appendTokenClass(JDefinedClass tokenClass){
		buildIndexSwitch._case(JExpr.lit(size)).body()
			._return(JExpr._new(tokenClass).arg(JExpr.lit(""))
					.arg(JExpr.lit(0)).arg(JExpr.lit(0)));
		buildIndexTextLinePosSwitch._case(JExpr.lit(size)).body()
			._return(JExpr._new(tokenClass).arg(text)
					.arg(line).arg(pos));
		sizeMethod.body().clear();
		sizeMethod.body()._return(JExpr.lit(size + 1));
		return size++;
	}
	
	public JDefinedClass getTokenFactoryClass(){
		return fac;
	}
	
	public JMethod getBuildIndexTextLinePosMethod(){
		return buildIndexTextLinePos;
	}
	
	public JMethod getBuildIndexMethod(){
		return buildIndex;
	}
	
	public JMethod getSizeMethod(){
		return sizeMethod;
	}
	
	private JMethod defineBuildIndexTextLinePosMethod(JDefinedClass iNode){
		JMethod method = fac.method(JMod.PUBLIC, iNode, "build");
		JVar index = method.param(int.class, "index");
		text = method.param(String.class, "text");
		line = method.param(int.class, "line");
		pos = method.param(int.class, "pos");
		buildIndexTextLinePosSwitch = method.body()._switch(index);
		buildIndexTextLinePosSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class))
				.arg(JExpr.lit("Index out of bounds")));
		return method;
	}
	
	private JMethod defineBuildIndexMethod(JDefinedClass iNode){
		JMethod method = fac.method(JMod.PUBLIC, iNode, "build");
		JVar index = method.param(int.class, "index");
		buildIndexSwitch = method.body()._switch(index);
		buildIndexSwitch._default().body()._throw(JExpr._new(model.ref(RuntimeException.class))
				.arg(JExpr.lit("Index out of bounds.")));
		return method;
	}
	
	private JMethod defineSizeMethod(){
		return fac.method(JMod.PUBLIC, int.class, SIZE);
	}
	
}
