package bepler.lrpage.ebnf.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bepler.lrpage.ebnf.parser.nodes.AltRHS;
import bepler.lrpage.ebnf.parser.nodes.ConcatRHS;
import bepler.lrpage.ebnf.parser.nodes.EmptySymbol;
import bepler.lrpage.ebnf.parser.nodes.ErrorToken;
import bepler.lrpage.ebnf.parser.nodes.GrammarAppendBlock;
import bepler.lrpage.ebnf.parser.nodes.GrammarStartBlock;
import bepler.lrpage.ebnf.parser.nodes.IdSymbol;
import bepler.lrpage.ebnf.parser.nodes.IdentifierToken;
import bepler.lrpage.ebnf.parser.nodes.IgnoreTokenDecl;
import bepler.lrpage.ebnf.parser.nodes.LitSymbol;
import bepler.lrpage.ebnf.parser.nodes.RuleDecl;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclList;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclListHead;
import bepler.lrpage.ebnf.parser.nodes.SymbolRHS;
import bepler.lrpage.ebnf.parser.nodes.TerminalStringToken;
import bepler.lrpage.ebnf.parser.nodes.TokenDecl;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclList;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclListHead;
import bepler.lrpage.grammar.Rule;

public class Builder implements Visitor{
	private Stack<Object> memory;
	private List<String> symbols;
	private List<String> regexs;
	private List<Rule> rules;
	
	public Builder(){
		this.memory = new Stack<Object>();
		this.symbols = new ArrayList<String>();
		this.regexs = new ArrayList<String>();
		this.rules = new ArrayList<Rule>();
	}
	
	@SuppressWarnings("serial")
	private static class RHS extends ArrayList<String>{
		@SuppressWarnings("unused")
		public String precToken = null;	
	}
	
	public List<String> getSymbols(){
		return this.symbols;
	}
	
	public List<String> getRegexs(){
		return this.regexs;
	}
	
	public List<Rule> getRules(){
		return this.rules;
	}
	
	public Stack<Object> getMemory(){
		return this.memory;
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
		// TODO Auto-generated method stub
		
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
	}

	@Override
	public void visit(RuleDeclList node) {
		node.ruledecllist0.accept(this);
		node.ruledecl1.accept(this);
	}

	@Override
	public void visit(TokenDecl node) {
		node.identifier0.accept(this); //left 
		node.terminalstring1.accept(this); //right
		String right = (String) memory.pop();
		String left = (String) memory.pop();
		symbols.add(left); 
		regexs.add(trimString(right));
	}

	@Override
	public void visit(IgnoreTokenDecl node) {
		node.terminalstring0.accept(this);
		String s = (String) memory.pop();
		symbols.add(" ");
		regexs.add(s);
	}

	
	@Override
	public void visit(RuleDecl node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SymbolRHS node) {
		node.symbol0.accept(this);
		RHS listSymbols = new RHS();
		listSymbols.add((String) memory.pop());
		memory.push(listSymbols);
	}

	@Override
	public void visit(AltRHS node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ConcatRHS node) {
		node.rhs0.accept(this);
		node.rhs1.accept(this);
		RHS rlist = (RHS) memory.pop();
		RHS llist = (RHS) memory.pop();
		llist.addAll(rlist);
		memory.push(llist);
	}

	@Override
	public void visit(EmptySymbol node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IdSymbol node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LitSymbol node) {
		// TODO Auto-generated method stub
		
	}
	
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

}
