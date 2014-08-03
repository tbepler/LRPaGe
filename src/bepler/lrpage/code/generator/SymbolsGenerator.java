package bepler.lrpage.code.generator;

import java.util.HashMap;
import java.util.Map;

import bepler.lrpage.grammar.Grammar;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class SymbolsGenerator {
	
	private static final String NAME = "Symbols";
	
	private static final int MOD = JMod.PUBLIC+JMod.STATIC+JMod.FINAL;
	
	private final Grammar g;
	private final JDefinedClass clazz;
	private final Map<String, JVar> types = new HashMap<String, JVar>();
	private final Map<String, JVar> names = new HashMap<String, JVar>();
	
	private int count = 0;
	
	public SymbolsGenerator(String pckg, JCodeModel model, Grammar g) throws JClassAlreadyExistsException{
		this.g = g;
		String name = pckg == null ? NAME : pckg + "." + NAME;
		clazz = CodeGenerator.appendJDocHeader(model._class(name));
	}
	
	public JExpression getType(String symbol){
		JVar type = types.get(symbol);
		if(type == null){
			type = clazz.field(MOD, int.class, symbol.toUpperCase(), JExpr.lit(count++));
			types.put(symbol, type);
		}
		return clazz.staticRef(type);
	}
	
	public JExpression getName(String symbol){
		JVar name = names.get(symbol);
		if(name == null){
			String str = g.getPseudonym(symbol);
			if(str == null){
				str = symbol;
			}
			name = clazz.field(MOD, String.class, symbol.toUpperCase()+"_NAME", JExpr.lit(str));
			names.put(symbol, name);
		}
		return clazz.staticRef(name);
	}
	
}
