
package bepler.lrpage.ebnf.parser;

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


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public interface Visitor {


    public void visit(IntToken node);

    public void visit(IdentifierToken node);

    public void visit(TerminalStringToken node);

    public void visit(ErrorToken node);

    public void visit(GrammarStartBlock node);

    public void visit(GrammarAppendBlock node);

    public void visit(TokenDeclBlock node);

    public void visit(RuleDeclBlock node);

    public void visit(PrecDirectiveBlock node);

    public void visit(TokenDeclListHead node);

    public void visit(TokenDeclList node);

    public void visit(RuleDeclListHead node);

    public void visit(RuleDeclList node);

    public void visit(DirectiveListHead node);

    public void visit(DirectiveList node);

    public void visit(TokenDecl node);

    public void visit(IgnoreTokenDecl node);

    public void visit(RuleDecl node);

    public void visit(SymbolRHS node);

    public void visit(OptionRHS node);

    public void visit(RepRHS node);

    public void visit(GroupRHS node);

    public void visit(AltRHS node);

    public void visit(ConcatRHS node);

    public void visit(PrecRHS node);

    public void visit(PrecDecl node);

    public void visit(BothDirective node);

    public void visit(AssocDirective node);

    public void visit(PriorityDirective node);

    public void visit(LeftAssoc node);

    public void visit(RightAssoc node);

    public void visit(NonAssoc node);

    public void visit(SymbolListHead node);

    public void visit(SymbolList node);

    public void visit(EmptySymbol node);

    public void visit(IdSymbol node);

    public void visit(LitSymbol node);

}
