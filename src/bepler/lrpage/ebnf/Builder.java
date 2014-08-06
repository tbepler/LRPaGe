package bepler.lrpage.ebnf;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

import bepler.lrpage.ebnf.environment.Environment;
import bepler.lrpage.ebnf.environment.RuleBuilder;
import bepler.lrpage.ebnf.parser.Visitor;
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
import bepler.lrpage.grammar.Rule;

public class Builder implements Visitor{
	
	private final Environment env = new Environment();
	private final Deque<Object> memory = new ArrayDeque<Object>();
	private final List<TerminalImpl> tokens = new ArrayList<TerminalImpl>();
	private final List<Rule> rules = new ArrayList<Rule>();
	
	private int reps = 0;
	
	private static class RHS extends ArrayList<String>{
		private static final long serialVersionUID = 1L;
		public String precId = null;	
	}
	
	/*
	public List<String> getSymbols(){
		return this.symbols;
	}
	
	public List<String> getRegexs(){
		return this.regexs;
	}
	*/
	
	public List<Rule> getRules(){
		return this.rules;
	}
	
	/*
	public Stack<Object> getMemory(){
		return this.memory;
	}
	*/

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
		node.accept(this);
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
		TerminalImpl token = new TerminalImpl(symbol, regex);
		tokens.add(token);
	}

	@Override
	public void visit(IgnoreTokenDecl node) {
		node.terminalstring0.accept(this);
		String regex = (String) memory.pop();
		TerminalImpl token = new TerminalImpl(regex);
		tokens.add(token);
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
		String symbol = "List"+(reps++);
		RuleBuilder start = new RuleBuilder();
		start.appendLHS(symbol);
		start.appendName(symbol+"Start");
		env.appendRule(start);
		RuleBuilder appendElement = new RuleBuilder();
		appendElement.appendLHS(symbol);
		appendElement.appendName(symbol+"Element");
		appendElement.appendRHS(symbol);
		for(RuleBuilder r : list){
			RuleBuilder appendRule = appendElement.clone();
			appendRule.appendRHS(r);
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
	}
	
	//TODO - needs to be implemented in a way that does not remove whitespace - Tristan
	private String trimString(String s){
		String curr;
		String next;
		if(s.length() <2){
			return s;
		}
		
		s = s.substring(1, s.length()-1);  // remove quotations 
		
		for(int i = 0; i <s.length(); i++){  //search through string for \\ replace the first \ with " "
			curr = Character.toString(s.charAt(i));
			if((i + 1) > s.length()){
				next ="";
			}else{
				next = Character.toString(s.charAt(i+1));
			}
			if((curr+next) =="\\"){
				StringBuilder sb = new StringBuilder(s);
				sb.setCharAt(i, ' ');
				s = sb.toString();
			}
		}
		s.replaceAll("\\s+",""); //trim that string of all white spaces.
		
		return s;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(DirectiveList node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PrecDecl node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BothDirective node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AssocDirective node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PriorityDirective node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LeftAssoc node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RightAssoc node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NonAssoc node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SymbolListHead node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SymbolList node) {
		// TODO Auto-generated method stub
		
	}


}
