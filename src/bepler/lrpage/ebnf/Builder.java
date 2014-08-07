package bepler.lrpage.ebnf;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

import bepler.lrpage.ebnf.environment.Environment;
import bepler.lrpage.ebnf.environment.Precedence;
import bepler.lrpage.ebnf.environment.RuleBuilder;
import bepler.lrpage.ebnf.environment.TerminalBuilder;
import bepler.lrpage.ebnf.parser.Visitor;
import bepler.lrpage.ebnf.parser.framework.Node;
import bepler.lrpage.ebnf.parser.nodes.AltRHS;
import bepler.lrpage.ebnf.parser.nodes.AssocDirective;
import bepler.lrpage.ebnf.parser.nodes.BothDirective;
import bepler.lrpage.ebnf.parser.nodes.ConcatRHS;
import bepler.lrpage.ebnf.parser.nodes.DirectiveList;
import bepler.lrpage.ebnf.parser.nodes.DirectiveListHead;
import bepler.lrpage.ebnf.parser.nodes.EmptySymbol;
import bepler.lrpage.ebnf.parser.nodes.ErrorToken;
import bepler.lrpage.ebnf.parser.nodes.GrammarAppendBlock;
import bepler.lrpage.ebnf.parser.nodes.GrammarStartBlock;
import bepler.lrpage.ebnf.parser.nodes.GroupRHS;
import bepler.lrpage.ebnf.parser.nodes.IdSymbol;
import bepler.lrpage.ebnf.parser.nodes.IdentifierToken;
import bepler.lrpage.ebnf.parser.nodes.IgnoreTokenDecl;
import bepler.lrpage.ebnf.parser.nodes.IntToken;
import bepler.lrpage.ebnf.parser.nodes.LeftAssoc;
import bepler.lrpage.ebnf.parser.nodes.LitSymbol;
import bepler.lrpage.ebnf.parser.nodes.NameDecl;
import bepler.lrpage.ebnf.parser.nodes.NamedRHS;
import bepler.lrpage.ebnf.parser.nodes.NonAssoc;
import bepler.lrpage.ebnf.parser.nodes.OptionRHS;
import bepler.lrpage.ebnf.parser.nodes.PrecDecl;
import bepler.lrpage.ebnf.parser.nodes.PrecDirectiveBlock;
import bepler.lrpage.ebnf.parser.nodes.PrecRHS;
import bepler.lrpage.ebnf.parser.nodes.PriorityDirective;
import bepler.lrpage.ebnf.parser.nodes.RepRHS;
import bepler.lrpage.ebnf.parser.nodes.RightAssoc;
import bepler.lrpage.ebnf.parser.nodes.RuleDecl;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclList;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclListHead;
import bepler.lrpage.ebnf.parser.nodes.SymbolList;
import bepler.lrpage.ebnf.parser.nodes.SymbolListHead;
import bepler.lrpage.ebnf.parser.nodes.SymbolRHS;
import bepler.lrpage.ebnf.parser.nodes.TerminalStringToken;
import bepler.lrpage.ebnf.parser.nodes.TokenDecl;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclList;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclListHead;
import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Rule;

public class Builder implements Visitor{
	
	private final Environment env = new Environment();
	private final Deque<Object> memory = new ArrayDeque<Object>();
	
	private int reps = 0;
	
	/*
	public List<String> getSymbols(){
		return this.symbols;
	}
	
	public List<String> getRegexs(){
		return this.regexs;
	}
	*/
	
	/*
	public Stack<Object> getMemory(){
		return this.memory;
	}
	*/
	
	public Grammar build(Node<Visitor> root){
		root.accept(this);
		return env.build();
	}

	@Override
	public void visit(IdentifierToken node) {
		memory.push(node.getText());
	}

	@Override
	public void visit(TerminalStringToken node) {
		memory.push(trimString(node.getText()));
	}

	@Override
	public void visit(ErrorToken node) {
		throw new RuntimeException("Error on tokens: "+node);
	}

	@Override
	public void visit(GrammarStartBlock node) {
		node.block0.accept(this);
	}

	@Override
	public void visit(GrammarAppendBlock node) {
		node.block1.accept(this);
		node.grammar0.accept(this);
	}

	@Override
	public void visit(TokenDeclBlock node) {
		node.tokendecllist0.accept(this);
	}

	@Override
	public void visit(RuleDeclBlock node) {
		node.ruledecllist0.accept(this);
	}

	@Override
	public void visit(TokenDeclListHead node) {
		//do nothing - this node is empty
	}

	@Override
	public void visit(TokenDeclList node) {
		node.tokendecllist0.accept(this);
		node.tokendecl1.accept(this);
	}

	@Override
	public void visit(RuleDeclListHead node) {
		node.ruledecl0.accept(this);
		String start = (String) memory.pop();
		env.setStartSymbol(start);
	}

	@Override
	public void visit(RuleDeclList node) {
		node.ruledecllist0.accept(this);
		node.ruledecl1.accept(this);
		memory.pop();
	}

	@Override
	public void visit(TokenDecl node) {
		node.identifier0.accept(this); //left 
		node.terminalstring1.accept(this); //right
		String regex = (String) memory.pop();
		String symbol = (String) memory.pop();
		env.appendTerminal(new TerminalBuilder().appendSymbol(symbol).appendRegex(regex));
	}

	@Override
	public void visit(IgnoreTokenDecl node) {
		node.terminalstring0.accept(this);
		String regex = (String) memory.pop();
		env.appendTerminal(new TerminalBuilder().appendRegex(regex));
	}

	
	@Override
	public void visit(RuleDecl node) {
		node.identifier0.accept(this);
		node.rhs1.accept(this);
		List<RuleBuilder> rhs = (List<RuleBuilder>) memory.pop();
		String symbol = (String) memory.peek();
		for(RuleBuilder r : rhs){
			r.appendLHS(symbol);
			env.appendRule(r);
		}
	}

	@Override
	public void visit(SymbolRHS node) {
		node.symbol0.accept(this);
		RuleBuilder rule = new RuleBuilder();
		String symbol = (String) memory.pop();
		if(symbol != null){
			rule.appendRHS(symbol);
		}
		List<RuleBuilder> list =  new ArrayList<RuleBuilder>();
		list.add(rule);
		memory.push(list);
	}

	@Override
	public void visit(AltRHS node) {
		node.rhs0.accept(this);
		node.rhs1.accept(this);
		List<RuleBuilder> right = (List<RuleBuilder>) memory.pop();
		List<RuleBuilder> left = (List<RuleBuilder>) memory.peek();
		left.addAll(right);
	}

	@Override
	public void visit(ConcatRHS node) {
		node.rhs0.accept(this);
		node.rhs1.accept(this);
		
		List<RuleBuilder> right = (List<RuleBuilder>) memory.pop();
		List<RuleBuilder> left = (List<RuleBuilder>) memory.pop();
		
		List<RuleBuilder> combined = new ArrayList<RuleBuilder>();
		for(RuleBuilder rule : left){
			for(RuleBuilder append : right){
				RuleBuilder copy = rule.clone();
				copy.appendRHS(append);
				combined.add(copy);
			}
		}
		
		memory.push(combined);
		
	}
	
	@Override
	public void visit(OptionRHS node) {
		node.rhs0.accept(this);
		List<RuleBuilder> list = (List<RuleBuilder>) memory.peek();
		list.add(new RuleBuilder());
	}

	@Override
	public void visit(RepRHS node) {
		node.rhs0.accept(this);
		List<RuleBuilder> list = (List<RuleBuilder>) memory.pop();
		String symbol = "";
		boolean first = true;
		for(RuleBuilder r : list){
			if(first){
				first = false;
			}else{
				symbol += "Or";
			}
			symbol += r.getName();
		}
		symbol += "List";
		//String symbol = "List"+(reps++);
		RuleBuilder start = new RuleBuilder();
		start.appendLHS(symbol);
		start.appendName(symbol+"Start");
		env.appendRule(start);
		RuleBuilder appendElement = new RuleBuilder();
		appendElement.appendLHS(symbol);
		appendElement.appendRHS(symbol);
		for(RuleBuilder r : list){
			RuleBuilder appendRule = appendElement.clone();
			appendRule.appendRHS(r);
			appendRule.appendName(symbol+"Append"+r.getName());
			env.appendRule(appendRule);
		}
		RuleBuilder substRule = new RuleBuilder();
		substRule.appendRHS(symbol);
		list.clear();
		list.add(substRule);
		memory.push(list);
	}

	@Override
	public void visit(GroupRHS node) {
		node.rhs0.accept(this);
	}
	
	@Override
	public void visit(PrecRHS node) {
		node.rhs0.accept(this);
		node.precdecl1.accept(this);
		String precSymbol = (String) memory.pop();
		List<RuleBuilder> list = (List<RuleBuilder>) memory.peek();
		for(RuleBuilder r : list){
			r.appendPrecedence(precSymbol);
		}
	}
	
	@Override
	public void visit(NamedRHS node) {
		node.rhs0.accept(this);
		node.namedecl1.accept(this);
		String name = (String) memory.pop();
		List<RuleBuilder> list = (List<RuleBuilder>) memory.peek();
		if(list.size() == 1){
			list.get(0).appendName(name);
		}else{
			for( int i = 0 ; i < list.size() ; ++i ){
				list.get(i).appendName(name+i);
			}
		}
	}

	@Override
	public void visit(NameDecl node) {
		node.identifier0.accept(this);
	}

	@Override
	public void visit(EmptySymbol node) {
		memory.push(null);
	}

	@Override
	public void visit(IdSymbol node) {
		node.identifier0.accept(this);
	}

	@Override
	public void visit(LitSymbol node) {
		node.terminalstring0.accept(this);
		String regex = (String) memory.pop();
		String symbol = env.getPunctuationSymbol(regex);
		memory.push(symbol);
	}
	
	private String trimString(String s){
		if(s.length() <2){
			return s;
		}
		
		StringBuilder b = new StringBuilder();
		for(int i = 1 ; i < s.length() - 1 ; ++i ){
			char cur = s.charAt(i);
			if(cur == '\\'){
				cur = s.charAt(++i);
				assert(i < s.length() - 1);
			}
			b.append(cur);
		}
		
		return b.toString();
	}

	@Override
	public void visit(IntToken node) {
		memory.push(Integer.parseInt(node.getText()));
	}

	@Override
	public void visit(PrecDirectiveBlock node) {
		node.directivelist0.accept(this);
	}

	@Override
	public void visit(DirectiveListHead node) {
		//do nothing - node is empty
	}

	@Override
	public void visit(DirectiveList node) {
		node.directivelist0.accept(this);
		node.directive1.accept(this);
	}

	@Override
	public void visit(PrecDecl node) {
		node.symbol0.accept(this);
	}

	@Override
	public void visit(BothDirective node) {
		node.assoc0.accept(this);
		node.int1.accept(this);
		node.symbollist2.accept(this);
		List<String> symbols = (List<String>) memory.pop();
		Integer priority = (Integer) memory.pop();
		Assoc assoc = (Assoc) memory.pop();
		Precedence prec = new Precedence(assoc, priority);
		for(String symbol : symbols){
			env.putPrecedence(symbol, prec);
		}
	}

	@Override
	public void visit(AssocDirective node) {
		node.assoc0.accept(this);
		node.symbollist1.accept(this);
		List<String> symbols = (List<String>) memory.pop();
		Assoc assoc = (Assoc) memory.pop();
		Precedence prec = new Precedence(assoc, null);
		for(String symbol : symbols){
			env.putPrecedence(symbol, prec);
		}
	}

	@Override
	public void visit(PriorityDirective node) {
		node.int0.accept(this);
		node.symbollist1.accept(this);
		List<String> symbols = (List<String>) memory.pop();
		Integer priority = (Integer) memory.pop();
		Precedence prec = new Precedence(null, priority);
		for(String symbol : symbols){
			env.putPrecedence(symbol, prec);
		}
	}

	@Override
	public void visit(LeftAssoc node) {
		memory.push(Assoc.LEFT);
	}

	@Override
	public void visit(RightAssoc node) {
		memory.push(Assoc.RIGHT);
	}

	@Override
	public void visit(NonAssoc node) {
		memory.push(Assoc.NON);
	}

	@Override
	public void visit(SymbolListHead node) {
		node.symbol0.accept(this);
		List<String> symbols = new ArrayList<String>();
		symbols.add((String) memory.pop());
		memory.push(symbols);
	}

	@Override
	public void visit(SymbolList node) {
		node.symbollist0.accept(this);
		node.symbol1.accept(this);
		String symbol = (String) memory.pop();
		List<String> symbols = (List<String>) memory.peek();
		symbols.add(symbol);
	}


}
