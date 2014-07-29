package bepler.lrpage.code.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bepler.lrpage.grammar.Rule;
import bepler.lrpage.parser.Symbols;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class NodeGenerator {
	
	private static final String REPLACE = "replace";
	private static final String TYPE = "type";
	private static final String ACCEPT = "accept";
	private static final String VISITOR = "Visitor";
	private static final String ABSTRACT_SYNTAX_NODE = "AbstractSyntaxNode";
	private static final String SYMBOLS = "Symbols";
	
	private final Symbols symbols;
	private final JCodeModel model;
	private final JDefinedClass visitorInterface;
	private final JDefinedClass symbolsEnumeration;
	private final JDefinedClass nodeInterface;
	private final JDefinedClass eofTokenClass;
	private final Map<String, JDefinedClass> tokens;
	
	public NodeGenerator(Symbols symbols, JCodeModel model) throws JClassAlreadyExistsException{
		this.symbols = symbols;
		this.model = model;
		this.visitorInterface = model._class(VISITOR, ClassType.INTERFACE);
		this.symbolsEnumeration = model._class(SYMBOLS, ClassType.ENUM);
		this.nodeInterface = this.defineNodeInterface();
		this.eofTokenClass = this.defineEOFToken();
		this.tokens = this.defineTokenNodes();
	}
	
	public JDefinedClass getAbstractNode(String symbol){
		//TODO
		return null;
	}
	
	public JDefinedClass getConcreteNode(Rule rule){
		//TODO
		return null;
	}
	
	public JDefinedClass getTokenNode(String terminalSymbol){
		return tokens.get(terminalSymbol);
	}
	
	public JDefinedClass getEOFTokenNode(){
		return eofTokenClass;
	}
	
	public JDefinedClass getNodeInterface(){
		return nodeInterface;
	}
	
	public JDefinedClass getSymbolsEnumeration(){
		return symbolsEnumeration;
	}
	
	public JDefinedClass getVisitorInterface(){
		return visitorInterface;
	}
	
	public List<Rule> getRules(){
		return symbols.getRules();
	}
	
	public Set<String> getAllSymbols(){
		return symbols.getAllSymbols();
	}
	
	public Symbols getSymbols(){
		return symbols;
	}
	
	private JDefinedClass defineNodeInterface() throws JClassAlreadyExistsException{
		JDefinedClass nodeInterface = model._class(ABSTRACT_SYNTAX_NODE, ClassType.INTERFACE);
		
		//declare the accept(Visitor) method
		JMethod accept = nodeInterface.method(JMod.PUBLIC, void.class , ACCEPT);
		accept.param(visitorInterface, "visitor");
		
		//declare the type() method
		nodeInterface.method(JMod.PUBLIC, symbolsEnumeration, TYPE);
		
		//declare the replace() method
		nodeInterface.method(JMod.PUBLIC, nodeInterface, REPLACE);
		
		return nodeInterface;
	}
	
	private JDefinedClass defineEOFToken() throws JClassAlreadyExistsException{
		JDefinedClass eofToken = model._class(symbols.getEOF()+"Token")._implements(this.nodeInterface);
		
		//override accept method
		JMethod accept = eofToken.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.param(this.visitorInterface, "visitor");
		accept.annotate(Override.class);
		accept.body().directStatement("//do nothing");
		
		//override type method
		JMethod type = eofToken.method(JMod.PUBLIC, this.symbolsEnumeration, TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsEnumeration.enumConstant(this.symbols.getEOF()));
		
		//override the replace method
		JMethod replace = eofToken.method(JMod.PUBLIC, this.nodeInterface, REPLACE);
		replace.annotate(Override.class);
		replace.body()._return(JExpr._this());
		
		//override toString method
		JMethod toString = eofToken.method(JMod.PUBLIC, String.class, "toString");
		toString.annotate(Override.class);
		toString.body()._return(JExpr.invoke(JExpr.invoke(JExpr._this(), type), "toString"));
		
		return eofToken;
	}
	
	private Map<String, JDefinedClass> defineTokenNodes() throws JClassAlreadyExistsException{
		Map<String, JDefinedClass> terminalNodes = new HashMap<String, JDefinedClass>();
		for(String symbol : symbols.getTerminals()){
			if(symbols.isEOF(symbol)){
				terminalNodes.put(symbol, eofTokenClass);
			}else{
				terminalNodes.put(symbol, this.defineTokenNode(symbol));
			}
		}
		return terminalNodes;
	}
	
	private JDefinedClass defineTokenNode(String symbol) throws JClassAlreadyExistsException{
		JDefinedClass node = model._class(symbol+"Token")._implements(this.nodeInterface);
		
		//add method to the visitor for visiting this node
		JMethod visit = this.addVisitableNode(node);
		
		//tokens should have fields for text, line and char positions
		JVar text = node.field(JMod.PUBLIC+JMod.FINAL, String.class, "text");
		JVar line = node.field(JMod.PUBLIC+JMod.FINAL, int.class, "line");
		JVar pos = node.field(JMod.PUBLIC+JMod.FINAL, int.class, "pos");
		
		//add constructor that defines the text, line, and pos fields
		JMethod cons = node.constructor(JMod.PUBLIC);
		cons.body().assign(JExpr._this().ref(text), cons.param(String.class, "text"));
		cons.body().assign(JExpr._this().ref(line), cons.param(int.class, "line"));
		cons.body().assign(JExpr._this().ref(pos), cons.param(int.class, "pos"));
		
		//override accept method to call visitor.visit(this)
		JMethod accept = node.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.annotate(Override.class);
		accept.body().invoke(accept.param(this.visitorInterface, "visitor"), visit).arg(JExpr._this());
		
		//override type method to return the enumerated type of this symbol
		JMethod type = node.method(JMod.PUBLIC, symbolsEnumeration, TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsEnumeration.enumConstant(symbol)); //this will add symbol to the enumeration if not already defined
		
		//override the replace method to return this
		JMethod replace = node.method(JMod.PUBLIC, nodeInterface, REPLACE);
		replace.annotate(Override.class);
		replace.body()._return(JExpr._this());
		
		//define toString() for this class
		JMethod toString = node.method(JMod.PUBLIC, String.class, "toString");
		toString.annotate(Override.class);
		toString.body()._return(JExpr.invoke(JExpr._this(), type)
				.plus(JExpr.lit("("))
				.plus(text)
				.plus(JExpr.lit(", "))
				.plus(line)
				.plus(JExpr.lit(":"))
				.plus(pos)
				.plus(JExpr.lit(")")));
		
		return node;
	}
	
	private JMethod addVisitableNode(JDefinedClass node){
		JMethod visit = this.visitorInterface.method(JMod.PUBLIC, void.class, "visit");
		visit.param(node, "node");
		
		return visit;
	}

	
}
