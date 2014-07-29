package bepler.lrpage.code.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
	private final Map<String, JDefinedClass> abstractNodes;
	private final Map<Rule, JDefinedClass> concreteNodes;
	
	public NodeGenerator(Symbols symbols, JCodeModel model) throws JClassAlreadyExistsException{
		this.symbols = symbols;
		this.model = model;
		this.visitorInterface = model._class(VISITOR, ClassType.INTERFACE);
		this.symbolsEnumeration = model._class(SYMBOLS, ClassType.ENUM);
		this.nodeInterface = this.defineNodeInterface();
		this.eofTokenClass = this.defineEOFToken();
		this.tokens = this.defineTokenNodes();
		this.abstractNodes= this.defineAbstractNodes();
		this.concreteNodes= this.defineConcreteNodes();
		
	}
	
	public JDefinedClass getAbstractNode(String symbol){
		return abstractNodes.get(symbol);
	}
	
	public JDefinedClass getConcreteNode(Rule rule){
		return concreteNodes.get(rule);
	}
	
	public JDefinedClass getTokenNode(String terminalSymbol){
		return tokens.get(terminalSymbol);
	}
	
	public Set<JDefinedClass> getTokenNodeClasses(){
		Set<JDefinedClass> nodes = new HashSet<JDefinedClass>(tokens.values());
		nodes.remove(this.getEOFTokenNode());
		return nodes;
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

	/**
	 * @author Jennifer Zou
	 * @return
	 * @throws JClassAlreadyExistsException
	 */
	private Map<String, JDefinedClass> defineAbstractNodes() throws JClassAlreadyExistsException{
		Map<String, JDefinedClass> nodes = new HashMap<String, JDefinedClass>();
		Set<String> sym= new HashSet<String>(symbols.getAllSymbols());
		sym.removeAll(symbols.getTerminals());
		sym.remove(symbols.getEOF());
		for(String symbol : sym){
			nodes.put(symbol, defineAbstractNode(symbol));
		}
		return nodes;
	}
	
	/**
	 * @author Jennifer Zou
	 * @param s
	 * @return
	 * @throws JClassAlreadyExistsException
	 */
	private JDefinedClass defineAbstractNode(String s) throws JClassAlreadyExistsException{
		JDefinedClass asn= model._class(JMod.PUBLIC+JMod.ABSTRACT, s+"AbstractNode", ClassType.CLASS);
		asn._implements(nodeInterface);
		//declare the type method
		JMethod type= asn.method(JMod.PUBLIC, symbolsEnumeration, TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsEnumeration.enumConstant(s));
		//define replace method
		JMethod replace= asn.method(JMod.PUBLIC, nodeInterface, REPLACE);
		replace.annotate(Override.class);
		replace.body()._return(JExpr._this());
		//define toString()
		JMethod toString= asn.method(JMod.PUBLIC, String.class, "toString");
		toString.annotate(Override.class);
		toString.body()._return(JExpr.invoke(JExpr.invoke(type), "toString"));
		return asn;
	}
	
	/**
	 * @author Jennifer Zou
	 * @return
	 * @throws JClassAlreadyExistsException
	 */
	private Map<Rule, JDefinedClass> defineConcreteNodes() throws JClassAlreadyExistsException{
		Map<Rule, JDefinedClass> ret= new HashMap<Rule, JDefinedClass>();
		for(Rule r:symbols.getRules()){
			ret.put(r, defineConcreteNode(r));
		}
		return ret;
	}
	
	/**
	 * @author Jennifer Zou
	 * @param r
	 * @return
	 * @throws JClassAlreadyExistsException
	 */
	private JDefinedClass defineConcreteNode(Rule r) throws JClassAlreadyExistsException{
		String lhs= r.leftHandSide();
		JDefinedClass asn= abstractNodes.get(lhs);
		JDefinedClass concreteNode= model._class(r.getName());
		concreteNode._extends(asn);
		
		//define constructor and fields
		JMethod cons= concreteNode.constructor(JMod.PUBLIC);
		String[] rhs= r.rightHandSide();
		Map<Integer, JVar> fieldInd= new HashMap<Integer, JVar>();
		int[] ignore= r.ignoreSymbols();
		for(int i=0;i<rhs.length;i++){
			JDefinedClass clazz= lookupNodeClass(rhs[i]);
			JVar param= cons.param(clazz, "node"+i);
			if(!contains(ignore, i)){
				JVar field= concreteNode.field(JMod.PUBLIC+JMod.FINAL, clazz, "f"+i);
				fieldInd.put(i, field);
				cons.body().assign(JExpr._this().ref(field), param);
			}
		}
		
		//define accept method
		JMethod accept= concreteNode.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.annotate(Override.class);
		JVar visitor= accept.param(visitorInterface, "visitor");
		JMethod visit= addVisitableNode(concreteNode);
		accept.body().invoke(visitor, visit).arg(JExpr._this());
		
		//if replace is specified, define replace method
		if(r.replace()>=0){
			JMethod replace= concreteNode.method(JMod.PUBLIC, nodeInterface, REPLACE);
			replace.annotate(Override.class);
			JVar field= fieldInd.get(r.replace());
			if(field==null){
				throw new NullPointerException("Replace index not found: "+r.replace()+", "+r);
			}
			replace.body()._return(field);
		}
		
		return concreteNode;
		
	}
	
	/**
	 * @author Jennifer Zou
	 * @param symbol
	 * @return
	 */
	private JDefinedClass lookupNodeClass(String symbol){
		JDefinedClass clazz= tokens.get(symbol);
		if(clazz==null){
			clazz= abstractNodes.get(symbol);
		}
		if(clazz==null){
			throw new RuntimeException("No class for symbol: "+symbol);
		}
		return clazz;
	}


	/**
	 * @author Jennifer Zou
	 * @param array
	 * @param i
	 * @return
	 */
	private boolean contains(int[] array, int i){
		for(int a:array){
			if(a==i){
				return true;
			}
		}
		return false;
	}

}
