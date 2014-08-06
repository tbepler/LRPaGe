package bepler.lrpage.ebnf.grammar;

import java.util.Arrays;
import java.util.List;

import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Rule;

/**
 * Rules for the EBNF grammar slightly modified to allow precedence declarations.
 * 
 * 
 * @author Tristan Bepler
 *
 */
public enum Rules implements Rule {
	
	GrammarStartBlock ( "Grammar", "Block" ),
	GrammarAppendBlock ( "Grammar", "Grammar", "Block" ),
	
	TokenDeclBlock ( "Block", Terminals.Special.toString(), Terminals.TokensKeyword.toString(),
			Terminals.StartBlock.toString(), "TokenDeclList" ),
			
	RuleDeclBlock ( "Block", Terminals.Special.toString(), Terminals.RulesKeyword.toString(),
			Terminals.StartBlock.toString(), "RuleDeclList" ),
			
	PrecDirectiveBlock ( "Block", Terminals.Special.toString(), Terminals.PrecedenceKeyword.toString(), 
			Terminals.StartBlock.toString(), "DirectiveList" ),
	
	TokenDeclListHead ("TokenDeclList"),
	TokenDeclList ( "TokenDeclList", "TokenDeclList", "TokenDecl" ),
	
	RuleDeclListHead ( "RuleDeclList", "RuleDecl" ),
	RuleDeclList ( "RuleDeclList", "RuleDeclList", "RuleDecl" ),
	
	DirectiveListHead ( "DirectiveList" ),
	DirectiveList ( "DirectiveList", "DirectiveList", "Directive" ),
	
	TokenDecl ( "TokenDecl", Terminals.Identifier.toString(), Terminals.Definition.toString(),
			Terminals.TerminalString.toString(), Terminals.Termination.toString() ),
	IgnoreTokenDecl ( "TokenDecl", Terminals.Bang.toString(), Terminals.TerminalString.toString(),
			Terminals.Termination.toString() ),
	
	RuleDecl ( "RuleDecl", Terminals.Identifier.toString(), Terminals.Definition.toString(),
			"RHS", Terminals.Termination.toString() ),
	
	SymbolRHS ( "RHS", "Symbol" ),
	OptionRHS ( "RHS", Terminals.StartOption.toString(), "RHS", Terminals.EndOption.toString() ),
	RepRHS ( "RHS", Terminals.StartRepetition.toString(), "RHS", Terminals.EndRepetition.toString() ),
	GroupRHS ( "RHS", Terminals.StartGrouping.toString(), "RHS", Terminals.EndGrouping.toString() ),
	AltRHS ( Assoc.RIGHT, "RHS", "RHS", Terminals.Alternation.toString(), "RHS" ),
	ConcatRHS ( Assoc.LEFT, "RHS", "RHS", Terminals.Concatenation.toString(), "RHS" ),
	PrecRHS ( "RHS", "RHS", "PrecDecl" ),
	
	PrecDecl ( "PrecDecl", Terminals.Special.toString(), Terminals.PrecKeyword.toString(), "Symbol" ),
	
	BothDirective ( "Directive", "Assoc", Terminals.Int.toString(),
			"SymbolList", Terminals.Termination.toString() ),
	AssocDirective ( "Directive", "Assoc", "SymbolList", Terminals.Termination.toString() ),
	PriorityDirective ( "Directive", Terminals.Int.toString(), "SymbolList", Terminals.Termination.toString() ),
	
	//AssocDecl ( "AssocDecl", Terminals.Special.toString(), "Assoc" ),
	//PriorityDecl ( "PriorityDecl", Terminals.Special.toString(), Terminals.Int.toString() ),
	
	LeftAssoc ( "Assoc", Terminals.LeftKeyword.toString() ),
	RightAssoc ( "Assoc", Terminals.RightKeyword.toString() ),
	NonAssoc ( "Assoc", Terminals.NonKeyword.toString() ),
	
	SymbolListHead ( "SymbolList", "Symbol" ),
	SymbolList ( "SymbolList", "SymbolList", Terminals.Concatenation.toString(), "Symbol" ),
	
	EmptySymbol (Assoc.RIGHT, "Symbol" ),
	IdSymbol ( "Symbol", Terminals.Identifier.toString() ),
	LitSymbol ( "Symbol", Terminals.TerminalString.toString() )
	;

	private final String lhs;
	private final String[] rhs;
	private final int priority;
	private final Assoc assoc;
	private final String name;
	
	private Rules(String name, int priority, Assoc assoc, String lhs, String ... rhs){
		this.lhs = lhs;
		this.rhs = rhs;
		this.priority = priority;
		this.assoc = assoc;
		this.name = name;
	}
	
	private Rules(String name, Assoc assoc, String lhs, String ... rhs){
		this(name, 0, assoc, lhs, rhs);
	}
	
	private Rules(String name, int priority, String lhs, String ... rhs){
		this(name, priority, Assoc.NON, lhs, rhs);
	}
	
	private Rules(int priority, Assoc assoc, String lhs, String ... rhs){
		this(null, priority, assoc, lhs, rhs);
	}
	
	private Rules(int priority, String lhs, String ... rhs){
		this(priority, Assoc.NON, lhs, rhs);
	}
	
	private Rules(Assoc assoc, String lhs, String ... rhs){
		this(0, assoc, lhs, rhs);
	}
	
	private Rules(String lhs, String ... rhs){
		this(0, lhs, rhs);
	}
	
	public static List<Rule> asList(){
		return Arrays.<Rule>asList(Rules.values());
	}
	
	@Override
	public String leftHandSide() {
		return lhs;
	}

	@Override
	public String[] rightHandSide() {
		return rhs;
	}

	@Override
	public Integer getPriority() {
		return priority;
	}

	@Override
	public Assoc getAssoc() {
		return assoc;
	}

	@Override
	public String getName() {
		if(name == null){
			return this.toString();
		}
		return name;
	}

}
