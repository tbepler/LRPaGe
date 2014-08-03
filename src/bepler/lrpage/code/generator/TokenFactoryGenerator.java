package bepler.lrpage.code.generator;

import java.util.regex.Pattern;

import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Terminal;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JVar;

public class TokenFactoryGenerator {
	
	private static final String NAME = "TokenFactoryImpl";
	
	private final JCodeModel model;
	private final NodeGenerator nodes;
	private final JDefinedClass clazz;
	private final JSwitch buildIndexSw;
	private final JSwitch buildIndexTextLinePosSw;
	private final JVar text;
	private final JVar line;
	private final JVar pos;
	private final JArray patternArray;
	
	private int index = 0;
	
	public TokenFactoryGenerator(String pckg, JCodeModel model,
			NodeGenerator nodes, SymbolsGenerator symbols, Grammar g,
			Framework f) throws JClassAlreadyExistsException{
		
		this.model = model;
		this.nodes = nodes;
		String name = pckg == null ? NAME : pckg + "." + NAME;
		clazz = model._class(name);
		CodeGenerator.appendJDocHeader(clazz);
		clazz._implements(f.getTokenFactoryInterface()
				.narrow(nodes.getVisitorInterface()));
		
		JExpression exc = JExpr._new(model.ref(RuntimeException.class))
				.arg(JExpr.lit("Index out of bounds"));
		
		//define the build(index) method
		JMethod build = clazz.method(JMod.PUBLIC,
				f.getTokenClass().narrow(nodes.getVisitorInterface()), "build");
		build.annotate(Override.class);
		buildIndexSw = build.body()._switch(build.param(int.class, "index"));
		buildIndexSw._default().body()._throw(exc);
		
		//define the build(index, text, line, pos) method
		build = clazz.method(JMod.PUBLIC,
				f.getTokenClass().narrow(nodes.getVisitorInterface()), "build");
		build.annotate(Override.class);
		JVar index = build.param(int.class, "index");
		text = build.param(String.class, "text");
		line = build.param(int.class, "line");
		pos = build.param(int.class, "pos");
		buildIndexTextLinePosSw = build.body()._switch(index);
		buildIndexTextLinePosSw._default().body()._throw(exc);
		
		//define the getPatterns() method
		patternArray = JExpr.newArray(model.ref(Pattern.class));
		JVar patterns = clazz.field(JMod.PRIVATE+JMod.STATIC+JMod.FINAL, Pattern[].class, "PATTERNS", patternArray);
		JMethod getPats = clazz.method(JMod.PUBLIC, Pattern[].class, "getPatterns");
		getPats.annotate(Override.class);
		getPats.body()._return(patterns);
		
		//define the getEOFToken() method
		JMethod getEOF = clazz.method(JMod.PUBLIC,
				f.getTokenClass().narrow(nodes.getVisitorInterface()), "getEOFToken");
		getEOF.annotate(Override.class);
		getEOF.body()._return(JExpr._new(nodes.getEOFTokenNode())
				.arg(getEOF.param(int.class, "line")).arg(getEOF.param(int.class, "pos")));
		
		//define the size() method;
		this.defineSize(g);
		
		//add the terminals to this token factory
		for(Terminal t : g.getTokens()){
			this.appendTerminal(t);
		}
		
	}
	
	private JMethod defineSize(Grammar g){
		JMethod size = clazz.method(JMod.PUBLIC, int.class, "size");
		size.annotate(Override.class);
		size.body()._return(JExpr.lit(g.getTokens().size()));
		return size;
	}
	
	private void appendTerminal(Terminal t){
		//add to pattern array
		patternArray.add(model.ref(Pattern.class)
				.staticInvoke("compile").arg(JExpr.lit(t.getRegex())));
		//add to build by index method
		String symbol = t.getSymbol();
		JExpression build;
		if(symbol == null){
			build = JExpr._null();
		}else{
			build = JExpr._new(nodes.getTokenNode(symbol));
		}
		buildIndexSw._case(JExpr.lit(index)).body()._return(build);
		if(symbol != null){
			build = JExpr._new(nodes.getTokenNode(symbol)).arg(text)
					.arg(line).arg(pos);
		}
		buildIndexTextLinePosSw._case(JExpr.lit(index)).body()._return(build);
		
		++index;
	}
	
	public JExpression newTokenFactory(){
		return JExpr._new(clazz);
	}
	
	
}
