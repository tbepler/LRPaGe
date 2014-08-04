package bepler.lrpage.code.generator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bepler.lrpage.grammar.Rule;
import bepler.lrpage.parser.Symbols;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

/**
 * This class is responsible for generating the source code for representing
 * symbols as AST nodes.
 * 
 * @author Tristan Bepler, Jennifer Zou
 *
 */
public class NodeGenerator {
	
	private static final String REPLACE = "replace";
	private static final String TYPE = "symbol";
	private static final String ACCEPT = "accept";
	private static final String VISITOR = "Visitor";
	private static final String SYMBOLS = "Symbols";
	private static final String NODES = "nodes";
	
	private final Symbols symbols;
	private final Framework framework;
	private final SymbolsGenerator symbolsGen;
	private final String pckg;
	private final JCodeModel model;
	private final JDefinedClass visitorInterface;
	private final JDefinedClass eofTokenClass;
	private final Map<String, JDefinedClass> tokens;
	private final Map<String, JDefinedClass> abstractNodes;
	private final Map<Rule, JDefinedClass> concreteNodes;
	
	public NodeGenerator(Symbols symbols, String pckg, JCodeModel model, Framework f,
			SymbolsGenerator symbolsGen) throws JClassAlreadyExistsException{
		this.symbols = symbols;
		this.framework = f;
		this.symbolsGen = symbolsGen;
		this.model = model;
		this.pckg = pckg;
		String name = pckg == null ? VISITOR : pckg + "." + VISITOR;
		this.visitorInterface = model._class(name, ClassType.INTERFACE);
		CodeGenerator.appendJDocHeader(visitorInterface);
		name = pckg == null ? SYMBOLS : pckg + "." + SYMBOLS;
		this.eofTokenClass = this.defineEOFToken();
		this.tokens = this.defineTokenNodes();
		this.abstractNodes= this.defineAbstractNodes();
		this.concreteNodes= this.defineConcreteNodes();
	}
	
	/**
	 * Returns the JDefinedClass defining the abstract
	 * AST node representing the given symbol
	 * @param symbol
	 * @return
	 */
	public JDefinedClass getAbstractNode(String symbol){
		JDefinedClass node = tokens.get(symbol);
		if(node == null){
			node = abstractNodes.get(symbol);
		}
		return node;
	}
	
	/**
	 * Returns the JDefinedClass defining the AST node
	 * for the given production rule
	 * @param rule
	 * @return
	 */
	public JDefinedClass getConcreteNode(Rule rule){
		return concreteNodes.get(rule);
	}
	
	/**
	 * Returns the JDefinedClass defining the token node
	 * class representing the given symbol
	 * @param terminalSymbol
	 * @return
	 */
	public JDefinedClass getTokenNode(String terminalSymbol){
		return tokens.get(terminalSymbol);
	}
	
	/**
	 * Returns a set containing all the JDefinedClass that define
	 * a token node class, except for the EOF node class
	 * @return
	 */
	public Set<JDefinedClass> getTokenNodeClasses(){
		Set<JDefinedClass> nodes = new HashSet<JDefinedClass>(tokens.values());
		nodes.remove(this.getEOFTokenNode());
		return nodes;
	}
	
	/**
	 * Returns the JDefinedClass defining the EOF
	 * token node class
	 * @return
	 */
	public JDefinedClass getEOFTokenNode(){
		return eofTokenClass;
	}
	
	/**
	 * Returns the JClass defining the AST
	 * node interface
	 * @return
	 */
	public JClass getNodeInterface(){
		return framework.getNodeInterface().narrow(visitorInterface);
	}
	
	/**
	 * Returns the SymbolsGenerator defining
	 * the symbols constants
	 * @return
	 */
	public SymbolsGenerator getSymbolsClass(){
		return symbolsGen;
	}
	
	/**
	 * Returns the JDefinedClass defining the Visitor
	 * interface
	 * @return
	 */
	public JDefinedClass getVisitorInterface(){
		return visitorInterface;
	}
	
	/**
	 * Returns a list of all the production rules for
	 * this grammar
	 * @return
	 */
	public List<Rule> getRules(){
		return symbols.getRules();
	}
	
	/**
	 * Returns a list of all the symbols
	 * @return
	 */
	public Set<String> getAllSymbols(){
		return symbols.getAllSymbols();
	}
	
	/**
	 * Returns the symbols object used by this class
	 * @return
	 */
	public Symbols getSymbols(){
		return symbols;
	}
	
	/**
	 * Defines the EOF token class
	 * @return the EOF token class
	 * @throws JClassAlreadyExistsException
	 * @author Tristan Bepler
	 */
	private JDefinedClass defineEOFToken() throws JClassAlreadyExistsException{
		String name = pckg == null ? symbols.getEOF() + "Token" : pckg+"."+NODES+"."+symbols.getEOF()+"Token";
		JDefinedClass eofToken = model._class(name)._extends(
				framework.getTokenClass().narrow(visitorInterface));
		CodeGenerator.appendJDocHeader(eofToken);
		
		JMethod cons = eofToken.constructor(JMod.PUBLIC);
		cons.body().invoke("super").arg(JExpr.lit("")).arg(cons.param(int.class, "line"))
			.arg(cons.param(int.class, "pos"));
		
		//override accept method
		JMethod accept = eofToken.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.param(this.visitorInterface, "visitor");
		accept.annotate(Override.class);
		accept.body().directStatement("//do nothing");
		
		//override symbol method
		JMethod type = eofToken.method(JMod.PUBLIC, framework.getSymbolInterface(), TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsGen.getSymbolObj(symbols.getEOF()));
		
		//
		
		//override toString method
		JMethod toString = eofToken.method(JMod.PUBLIC, String.class, "toString");
		toString.annotate(Override.class);
		toString.body()._return(JExpr.invoke(JExpr.invoke(type), "toString"));
		
		return eofToken;
	}
	
	/**
	 * Defines all the terminal node classes
	 * @return a mapping of symbols to their terminal node classes
	 * @throws JClassAlreadyExistsException
	 * @author Tristan Bepler
	 */
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
	
	/**
	 * Defines a terminal node class for the symbol
	 * @param symbol
	 * @return JDefinedClass for the symbol
	 * @throws JClassAlreadyExistsException
	 * @author Tristan Bepler
	 */
	private JDefinedClass defineTokenNode(String symbol) throws JClassAlreadyExistsException{
		String name = pckg == null ? symbol + "Token" : pckg+"."+NODES+"."+symbol+"Token";
		JDefinedClass node = model._class(name)._extends(
				framework.getTokenClass().narrow(visitorInterface));
		CodeGenerator.appendJDocHeader(node);
		
		//add method to the visitor for visiting this node
		JMethod visit = this.addVisitableNode(node);
		
		//add constructor that defines the text, line, and pos fields
		JMethod cons = node.constructor(JMod.PUBLIC);
		cons.body().invoke("super").arg(cons.param(String.class, "text"))
			.arg(cons.param(int.class, "line")).arg(cons.param(int.class, "pos"));
		
		//add a no args constructor for the burke-fischer algorithm to use
		cons = node.constructor(JMod.PUBLIC);
		cons.body().invoke("super");
		
		//override accept method to call visitor.visit(this)
		JMethod accept = node.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.annotate(Override.class);
		accept.body().invoke(accept.param(this.visitorInterface, "visitor"), visit).arg(JExpr._this());
		
		//override type method to return the enumerated type of this symbol
		JMethod type = node.method(JMod.PUBLIC, framework.getSymbolInterface(), TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsGen.getSymbolObj(symbol));
		
		//define toString() for this class
		JMethod toString = node.method(JMod.PUBLIC, String.class, "toString");
		toString.annotate(Override.class);
		//if this is punctuation, only return the name
		if(symbols.isPunctuation(symbol)){
			toString.body()._return(JExpr.invoke(JExpr.invoke(type), "toString"));
		}else{
			toString.body()._return(JExpr.invoke(JExpr.invoke(type), "toString")
					.plus(JExpr.lit("("))
					.plus(JExpr.invoke("getText"))
					.plus(JExpr.lit(")")));
		}
		
		return node;
	}
	
	/**
	 * Adds a visit method to the visitor for the node class.
	 * @param node
	 * @return the created method
	 * @author Tristan Bepler
	 */
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
		String name = pckg == null ? s + "AbstractNode" : pckg+"."+NODES+"."+s+"AbstractNode";
		JDefinedClass asn= model._class(JMod.PUBLIC+JMod.ABSTRACT, name, ClassType.CLASS);
		CodeGenerator.appendJDocHeader(asn);
		
		asn._implements(framework.getNodeInterface().narrow(visitorInterface));
		
		//declare the type method
		JMethod type= asn.method(JMod.PUBLIC, framework.getSymbolInterface(), TYPE);
		type.annotate(Override.class);
		type.body()._return(symbolsGen.getSymbolObj(s));
		//define replace method
		JMethod replace= asn.method(JMod.PUBLIC, asn, REPLACE);
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
		String name = pckg == null ? r.getName() : pckg+"."+NODES+"."+r.getName();
		JDefinedClass concreteNode= model._class(name);
		CodeGenerator.appendJDocHeader(concreteNode);
		concreteNode._extends(asn);
		
		//define constructor and fields
		JMethod cons= concreteNode.constructor(JMod.PUBLIC);
		String[] rhs= r.rightHandSide();
		Map<Integer, JVar> fieldInd= new HashMap<Integer, JVar>();
		int[] ignore= r.ignoreSymbols();
		JVar firstField = null;
		for(int i=0;i<rhs.length;i++){
			JDefinedClass clazz= lookupNodeClass(rhs[i]);
			JVar param= cons.param(clazz, "node"+i);
			if(!contains(ignore, i)){
				JVar field= concreteNode.field(JMod.PUBLIC+JMod.FINAL, clazz, "f"+i);
				if(firstField == null){
					firstField = field;
				}
				fieldInd.put(i, field);
				cons.body().assign(JExpr._this().ref(field), param);
			}
		}
		
		//define getLine and getPos methods
		JMethod line = concreteNode.method(JMod.PUBLIC, int.class, "getLine");
		line.annotate(Override.class);
		line.body()._return(JExpr.invoke(firstField, "getLine"));
		
		JMethod pos = concreteNode.method(JMod.PUBLIC, int.class, "getPos");
		pos.annotate(Override.class);
		pos.body()._return(JExpr.invoke(firstField, "getPos"));
		
		//define accept method
		JMethod accept= concreteNode.method(JMod.PUBLIC, void.class, ACCEPT);
		accept.annotate(Override.class);
		JVar visitor= accept.param(visitorInterface, "visitor");
		if(r.replace() < 0){ //if not replaced, define visit method on visitor and accept to call that method
			JMethod visit= addVisitableNode(concreteNode);
			accept.body().invoke(visitor, visit).arg(JExpr._this());
		}else{ //if replaced, make accept do nothing
			accept.body().directStatement("//do nothing");
		}
		
		//if replace is specified, define replace method
		if(r.replace()>=0){
			JMethod replace= concreteNode.method(JMod.PUBLIC, asn, REPLACE);
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
