
package bepler.lrpage.ebnf.parser;

import bepler.lrpage.ebnf.parser.framework.Node;
import bepler.lrpage.ebnf.parser.framework.ParsingEngine;
import bepler.lrpage.ebnf.parser.framework.Stack;
import bepler.lrpage.ebnf.parser.framework.Status;
import bepler.lrpage.ebnf.parser.framework.Symbol;
import bepler.lrpage.ebnf.parser.nodes.AltRHS;
import bepler.lrpage.ebnf.parser.nodes.AlternationToken;
import bepler.lrpage.ebnf.parser.nodes.AssocAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.AssocDirective;
import bepler.lrpage.ebnf.parser.nodes.BangToken;
import bepler.lrpage.ebnf.parser.nodes.BlockAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.BothDirective;
import bepler.lrpage.ebnf.parser.nodes.ConcatRHS;
import bepler.lrpage.ebnf.parser.nodes.ConcatenationToken;
import bepler.lrpage.ebnf.parser.nodes.DefaultAssocDirective;
import bepler.lrpage.ebnf.parser.nodes.DefaultDirective;
import bepler.lrpage.ebnf.parser.nodes.DefaultKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.DefaultPriorityDirective;
import bepler.lrpage.ebnf.parser.nodes.DefinitionToken;
import bepler.lrpage.ebnf.parser.nodes.DirectiveAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.DirectiveList;
import bepler.lrpage.ebnf.parser.nodes.DirectiveListAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.DirectiveListHead;
import bepler.lrpage.ebnf.parser.nodes.EmptySymbol;
import bepler.lrpage.ebnf.parser.nodes.EndGroupingToken;
import bepler.lrpage.ebnf.parser.nodes.EndOptionToken;
import bepler.lrpage.ebnf.parser.nodes.EndRepetitionToken;
import bepler.lrpage.ebnf.parser.nodes.GrammarAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.GrammarAppendBlock;
import bepler.lrpage.ebnf.parser.nodes.GrammarStartBlock;
import bepler.lrpage.ebnf.parser.nodes.GroupRHS;
import bepler.lrpage.ebnf.parser.nodes.IdSymbol;
import bepler.lrpage.ebnf.parser.nodes.IdentifierToken;
import bepler.lrpage.ebnf.parser.nodes.IgnoreTokenDecl;
import bepler.lrpage.ebnf.parser.nodes.IntToken;
import bepler.lrpage.ebnf.parser.nodes.LeftAssoc;
import bepler.lrpage.ebnf.parser.nodes.LeftKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.LitSymbol;
import bepler.lrpage.ebnf.parser.nodes.NameDecl;
import bepler.lrpage.ebnf.parser.nodes.NameDeclAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.NameKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.NamedRHS;
import bepler.lrpage.ebnf.parser.nodes.NonAssoc;
import bepler.lrpage.ebnf.parser.nodes.NonKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.OptionRHS;
import bepler.lrpage.ebnf.parser.nodes.PrecDecl;
import bepler.lrpage.ebnf.parser.nodes.PrecDeclAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.PrecDirectiveBlock;
import bepler.lrpage.ebnf.parser.nodes.PrecKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.PrecRHS;
import bepler.lrpage.ebnf.parser.nodes.PrecedenceKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.PriorityDirective;
import bepler.lrpage.ebnf.parser.nodes.PseudoDeclAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.PseudoDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.PseudoDeclList;
import bepler.lrpage.ebnf.parser.nodes.PseudoDeclListAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.PseudoDeclListHead;
import bepler.lrpage.ebnf.parser.nodes.PseudonymDecl;
import bepler.lrpage.ebnf.parser.nodes.PseudonymKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.RHSAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.RepRHS;
import bepler.lrpage.ebnf.parser.nodes.RightAssoc;
import bepler.lrpage.ebnf.parser.nodes.RightKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.RuleDecl;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclList;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclListAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.RuleDeclListHead;
import bepler.lrpage.ebnf.parser.nodes.RulesKeywordToken;
import bepler.lrpage.ebnf.parser.nodes.SpecialToken;
import bepler.lrpage.ebnf.parser.nodes.StartBlockToken;
import bepler.lrpage.ebnf.parser.nodes.StartGroupingToken;
import bepler.lrpage.ebnf.parser.nodes.StartOptionToken;
import bepler.lrpage.ebnf.parser.nodes.StartRepetitionToken;
import bepler.lrpage.ebnf.parser.nodes.SymbolAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.SymbolList;
import bepler.lrpage.ebnf.parser.nodes.SymbolListAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.SymbolListHead;
import bepler.lrpage.ebnf.parser.nodes.SymbolRHS;
import bepler.lrpage.ebnf.parser.nodes.TerminalStringToken;
import bepler.lrpage.ebnf.parser.nodes.TerminationToken;
import bepler.lrpage.ebnf.parser.nodes.TokenDecl;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclBlock;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclList;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclListAbstractNode;
import bepler.lrpage.ebnf.parser.nodes.TokenDeclListHead;
import bepler.lrpage.ebnf.parser.nodes.TokensKeywordToken;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class ParsingEngineImpl
    implements ParsingEngine<Visitor>
{


    private ParsingEngineImpl.Actions getAction(int state, Node<Visitor> lookahead) {
        switch (state) {
            case  1 :
                return this.getState1Action(lookahead);
            case  2 :
                return this.getState2Action(lookahead);
            case  3 :
                return this.getState3Action(lookahead);
            case  4 :
                return this.getState4Action(lookahead);
            case  5 :
                return this.getState5Action(lookahead);
            case  6 :
                return this.getState6Action(lookahead);
            case  7 :
                return this.getState7Action(lookahead);
            case  8 :
                return this.getState8Action(lookahead);
            case  9 :
                return this.getState9Action(lookahead);
            case  10 :
                return this.getState10Action(lookahead);
            case  11 :
                return this.getState11Action(lookahead);
            case  12 :
                return this.getState12Action(lookahead);
            case  13 :
                return this.getState13Action(lookahead);
            case  14 :
                return this.getState14Action(lookahead);
            case  15 :
                return this.getState15Action(lookahead);
            case  16 :
                return this.getState16Action(lookahead);
            case  17 :
                return this.getState17Action(lookahead);
            case  18 :
                return this.getState18Action(lookahead);
            case  19 :
                return this.getState19Action(lookahead);
            case  20 :
                return this.getState20Action(lookahead);
            case  21 :
                return this.getState21Action(lookahead);
            case  22 :
                return this.getState22Action(lookahead);
            case  23 :
                return this.getState23Action(lookahead);
            case  24 :
                return this.getState24Action(lookahead);
            case  25 :
                return this.getState25Action(lookahead);
            case  26 :
                return this.getState26Action(lookahead);
            case  27 :
                return this.getState27Action(lookahead);
            case  28 :
                return this.getState28Action(lookahead);
            case  29 :
                return this.getState29Action(lookahead);
            case  30 :
                return this.getState30Action(lookahead);
            case  31 :
                return this.getState31Action(lookahead);
            case  32 :
                return this.getState32Action(lookahead);
            case  33 :
                return this.getState33Action(lookahead);
            case  34 :
                return this.getState34Action(lookahead);
            case  35 :
                return this.getState35Action(lookahead);
            case  36 :
                return this.getState36Action(lookahead);
            case  37 :
                return this.getState37Action(lookahead);
            case  38 :
                return this.getState38Action(lookahead);
            case  39 :
                return this.getState39Action(lookahead);
            case  40 :
                return this.getState40Action(lookahead);
            case  41 :
                return this.getState41Action(lookahead);
            case  42 :
                return this.getState42Action(lookahead);
            case  43 :
                return this.getState43Action(lookahead);
            case  44 :
                return this.getState44Action(lookahead);
            case  45 :
                return this.getState45Action(lookahead);
            case  46 :
                return this.getState46Action(lookahead);
            case  47 :
                return this.getState47Action(lookahead);
            case  48 :
                return this.getState48Action(lookahead);
            case  49 :
                return this.getState49Action(lookahead);
            case  50 :
                return this.getState50Action(lookahead);
            case  51 :
                return this.getState51Action(lookahead);
            case  52 :
                return this.getState52Action(lookahead);
            case  53 :
                return this.getState53Action(lookahead);
            case  54 :
                return this.getState54Action(lookahead);
            case  55 :
                return this.getState55Action(lookahead);
            case  56 :
                return this.getState56Action(lookahead);
            case  57 :
                return this.getState57Action(lookahead);
            case  58 :
                return this.getState58Action(lookahead);
            case  59 :
                return this.getState59Action(lookahead);
            case  60 :
                return this.getState60Action(lookahead);
            case  61 :
                return this.getState61Action(lookahead);
            case  62 :
                return this.getState62Action(lookahead);
            case  63 :
                return this.getState63Action(lookahead);
            case  64 :
                return this.getState64Action(lookahead);
            case  65 :
                return this.getState65Action(lookahead);
            case  66 :
                return this.getState66Action(lookahead);
            case  67 :
                return this.getState67Action(lookahead);
            case  68 :
                return this.getState68Action(lookahead);
            case  69 :
                return this.getState69Action(lookahead);
            case  70 :
                return this.getState70Action(lookahead);
            case  71 :
                return this.getState71Action(lookahead);
            case  72 :
                return this.getState72Action(lookahead);
            case  73 :
                return this.getState73Action(lookahead);
            case  74 :
                return this.getState74Action(lookahead);
            case  75 :
                return this.getState75Action(lookahead);
            case  76 :
                return this.getState76Action(lookahead);
            case  77 :
                return this.getState77Action(lookahead);
            case  78 :
                return this.getState78Action(lookahead);
            case  79 :
                return this.getState79Action(lookahead);
            case  80 :
                return this.getState80Action(lookahead);
            case  81 :
                return this.getState81Action(lookahead);
            case  82 :
                return this.getState82Action(lookahead);
            case  83 :
                return this.getState83Action(lookahead);
            case  84 :
                return this.getState84Action(lookahead);
            case  85 :
                return this.getState85Action(lookahead);
            case  86 :
                return this.getState86Action(lookahead);
            case  87 :
                return this.getState87Action(lookahead);
            case  88 :
                return this.getState88Action(lookahead);
            case  89 :
                return this.getState89Action(lookahead);
            case  90 :
                return this.getState90Action(lookahead);
            case  91 :
                return this.getState91Action(lookahead);
            case  92 :
                return this.getState92Action(lookahead);
            case  93 :
                return this.getState93Action(lookahead);
            case  94 :
                return this.getState94Action(lookahead);
            case  95 :
                return this.getState95Action(lookahead);
            case  96 :
                return this.getState96Action(lookahead);
            case  97 :
                return this.getState97Action(lookahead);
            case  98 :
                return this.getState98Action(lookahead);
            case  99 :
                return this.getState99Action(lookahead);
            case  100 :
                return this.getState100Action(lookahead);
            case  101 :
                return this.getState101Action(lookahead);
            case  102 :
                return this.getState102Action(lookahead);
            case  103 :
                return this.getState103Action(lookahead);
            case  104 :
                return this.getState104Action(lookahead);
            case  105 :
                return this.getState105Action(lookahead);
            case  106 :
                return this.getState106Action(lookahead);
            case  107 :
                return this.getState107Action(lookahead);
            case  108 :
                return this.getState108Action(lookahead);
            case  109 :
                return this.getState109Action(lookahead);
            case  110 :
                return this.getState110Action(lookahead);
            case  111 :
                return this.getState111Action(lookahead);
            case  112 :
                return this.getState112Action(lookahead);
            case  113 :
                return this.getState113Action(lookahead);
            case  114 :
                return this.getState114Action(lookahead);
            case  115 :
                return this.getState115Action(lookahead);
            case  116 :
                return this.getState116Action(lookahead);
            case  117 :
                return this.getState117Action(lookahead);
            case  118 :
                return this.getState118Action(lookahead);
            case  119 :
                return this.getState119Action(lookahead);
            case  120 :
                return this.getState120Action(lookahead);
            case  121 :
                return this.getState121Action(lookahead);
            case  122 :
                return this.getState122Action(lookahead);
            case  123 :
                return this.getState123Action(lookahead);
            case  124 :
                return this.getState124Action(lookahead);
            case  125 :
                return this.getState125Action(lookahead);
            case  126 :
                return this.getState126Action(lookahead);
            case  127 :
                return this.getState127Action(lookahead);
            case  128 :
                return this.getState128Action(lookahead);
            case  129 :
                return this.getState129Action(lookahead);
            case  130 :
                return this.getState130Action(lookahead);
            case  131 :
                return this.getState131Action(lookahead);
            case  0 :
                return this.getState0Action(lookahead);
            case  132 :
                return this.getState132Action(lookahead);
            case  133 :
                return this.getState133Action(lookahead);
            case  134 :
                return this.getState134Action(lookahead);
            case  135 :
                return this.getState135Action(lookahead);
            case  136 :
                return this.getState136Action(lookahead);
            case  137 :
                return this.getState137Action(lookahead);
            case  138 :
                return this.getState138Action(lookahead);
            case  139 :
                return this.getState139Action(lookahead);
            case  140 :
                return this.getState140Action(lookahead);
            case  141 :
                return this.getState141Action(lookahead);
            case  142 :
                return this.getState142Action(lookahead);
            case  143 :
                return this.getState143Action(lookahead);
            case  144 :
                return this.getState144Action(lookahead);
            case  145 :
                return this.getState145Action(lookahead);
            case  146 :
                return this.getState146Action(lookahead);
            case  147 :
                return this.getState147Action(lookahead);
            case  148 :
                return this.getState148Action(lookahead);
            case  149 :
                return this.getState149Action(lookahead);
            case  150 :
                return this.getState150Action(lookahead);
            case  151 :
                return this.getState151Action(lookahead);
            case  152 :
                return this.getState152Action(lookahead);
            case  153 :
                return this.getState153Action(lookahead);
            case  154 :
                return this.getState154Action(lookahead);
            case  155 :
                return this.getState155Action(lookahead);
            case  156 :
                return this.getState156Action(lookahead);
            case  157 :
                return this.getState157Action(lookahead);
            case  158 :
                return this.getState158Action(lookahead);
            case  159 :
                return this.getState159Action(lookahead);
            default:
                throw new RuntimeException("Unknown state.");
        }
    }

    @Override
    public Status advance(Stack<Visitor> stack, Node<Visitor> lookahead) {
        ParsingEngineImpl.Actions action = getAction(stack.curState(), lookahead);
        switch (action) {
            case REDUCE26 :
            {
                IdentifierToken field2 = ((IdentifierToken) stack.pop());
                NameKeywordToken field1 = ((NameKeywordToken) stack.pop());
                SpecialToken field0 = ((SpecialToken) stack.pop());
                Node<Visitor> reduced = new NameDecl(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE39 :
            {
                Node<Visitor> reduced = new EmptySymbol().replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT113 :
                stack.push(lookahead, 113);
                return Status.NOMINAL;
            case SHIFT29 :
                stack.push(lookahead, 29);
                return Status.NOMINAL;
            case SHIFT27 :
                stack.push(lookahead, 27);
                return Status.NOMINAL;
            case SHIFT90 :
                stack.push(lookahead, 90);
                return Status.NOMINAL;
            case GOTO109 :
                stack.push(lookahead, 109);
                return Status.NOMINAL;
            case SHIFT2 :
                stack.push(lookahead, 2);
                return Status.NOMINAL;
            case GOTO127 :
                stack.push(lookahead, 127);
                return Status.NOMINAL;
            case SHIFT115 :
                stack.push(lookahead, 115);
                return Status.NOMINAL;
            case REDUCE17 :
            {
                TerminationToken field3 = ((TerminationToken) stack.pop());
                RHSAbstractNode field2 = ((RHSAbstractNode) stack.pop());
                DefinitionToken field1 = ((DefinitionToken) stack.pop());
                IdentifierToken field0 = ((IdentifierToken) stack.pop());
                Node<Visitor> reduced = new RuleDecl(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT152 :
                stack.push(lookahead, 152);
                return Status.NOMINAL;
            case SHIFT43 :
                stack.push(lookahead, 43);
                return Status.NOMINAL;
            case SHIFT98 :
                stack.push(lookahead, 98);
                return Status.NOMINAL;
            case GOTO117 :
                stack.push(lookahead, 117);
                return Status.NOMINAL;
            case GOTO59 :
                stack.push(lookahead, 59);
                return Status.NOMINAL;
            case REDUCE36 :
            {
                NonKeywordToken field0 = ((NonKeywordToken) stack.pop());
                Node<Visitor> reduced = new NonAssoc(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT37 :
                stack.push(lookahead, 37);
                return Status.NOMINAL;
            case SHIFT112 :
                stack.push(lookahead, 112);
                return Status.NOMINAL;
            case SHIFT33 :
                stack.push(lookahead, 33);
                return Status.NOMINAL;
            case SHIFT67 :
                stack.push(lookahead, 67);
                return Status.NOMINAL;
            case GOTO66 :
                stack.push(lookahead, 66);
                return Status.NOMINAL;
            case SHIFT40 :
                stack.push(lookahead, 40);
                return Status.NOMINAL;
            case GOTO153 :
                stack.push(lookahead, 153);
                return Status.NOMINAL;
            case SHIFT15 :
                stack.push(lookahead, 15);
                return Status.NOMINAL;
            case SHIFT50 :
                stack.push(lookahead, 50);
                return Status.NOMINAL;
            case GOTO128 :
                stack.push(lookahead, 128);
                return Status.NOMINAL;
            case SHIFT99 :
                stack.push(lookahead, 99);
                return Status.NOMINAL;
            case GOTO51 :
                stack.push(lookahead, 51);
                return Status.NOMINAL;
            case SHIFT35 :
                stack.push(lookahead, 35);
                return Status.NOMINAL;
            case REDUCE3 :
            {
                TokenDeclListAbstractNode field3 = ((TokenDeclListAbstractNode) stack.pop());
                StartBlockToken field2 = ((StartBlockToken) stack.pop());
                TokensKeywordToken field1 = ((TokensKeywordToken) stack.pop());
                SpecialToken field0 = ((SpecialToken) stack.pop());
                Node<Visitor> reduced = new TokenDeclBlock(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT31 :
                stack.push(lookahead, 31);
                return Status.NOMINAL;
            case SHIFT34 :
                stack.push(lookahead, 34);
                return Status.NOMINAL;
            case GOTO41 :
                stack.push(lookahead, 41);
                return Status.NOMINAL;
            case REDUCE27 :
            {
                SymbolAbstractNode field2 = ((SymbolAbstractNode) stack.pop());
                PrecKeywordToken field1 = ((PrecKeywordToken) stack.pop());
                SpecialToken field0 = ((SpecialToken) stack.pop());
                Node<Visitor> reduced = new PrecDecl(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE35 :
            {
                RightKeywordToken field0 = ((RightKeywordToken) stack.pop());
                Node<Visitor> reduced = new RightAssoc(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT89 :
                stack.push(lookahead, 89);
                return Status.NOMINAL;
            case SHIFT145 :
                stack.push(lookahead, 145);
                return Status.NOMINAL;
            case SHIFT122 :
                stack.push(lookahead, 122);
                return Status.NOMINAL;
            case SHIFT58 :
                stack.push(lookahead, 58);
                return Status.NOMINAL;
            case SHIFT88 :
                stack.push(lookahead, 88);
                return Status.NOMINAL;
            case SHIFT14 :
                stack.push(lookahead, 14);
                return Status.NOMINAL;
            case GOTO57 :
                stack.push(lookahead, 57);
                return Status.NOMINAL;
            case SHIFT56 :
                stack.push(lookahead, 56);
                return Status.NOMINAL;
            case SHIFT83 :
                stack.push(lookahead, 83);
                return Status.NOMINAL;
            case GOTO87 :
                stack.push(lookahead, 87);
                return Status.NOMINAL;
            case GOTO121 :
                stack.push(lookahead, 121);
                return Status.NOMINAL;
            case SHIFT71 :
                stack.push(lookahead, 71);
                return Status.NOMINAL;
            case SHIFT85 :
                stack.push(lookahead, 85);
                return Status.NOMINAL;
            case SHIFT136 :
                stack.push(lookahead, 136);
                return Status.NOMINAL;
            case SHIFT76 :
                stack.push(lookahead, 76);
                return Status.NOMINAL;
            case SHIFT96 :
                stack.push(lookahead, 96);
                return Status.NOMINAL;
            case SHIFT147 :
                stack.push(lookahead, 147);
                return Status.NOMINAL;
            case SHIFT45 :
                stack.push(lookahead, 45);
                return Status.NOMINAL;
            case GOTO116 :
                stack.push(lookahead, 116);
                return Status.NOMINAL;
            case SHIFT141 :
                stack.push(lookahead, 141);
                return Status.NOMINAL;
            case GOTO61 :
                stack.push(lookahead, 61);
                return Status.NOMINAL;
            case REDUCE10 :
            {
                RuleDeclAbstractNode field0 = ((RuleDeclAbstractNode) stack.pop());
                Node<Visitor> reduced = new RuleDeclListHead(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE20 :
            {
                EndRepetitionToken field2 = ((EndRepetitionToken) stack.pop());
                RHSAbstractNode field1 = ((RHSAbstractNode) stack.pop());
                StartRepetitionToken field0 = ((StartRepetitionToken) stack.pop());
                Node<Visitor> reduced = new RepRHS(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT143 :
                stack.push(lookahead, 143);
                return Status.NOMINAL;
            case REDUCE0 :
            {
                BlockAbstractNode field0 = ((BlockAbstractNode) stack.pop());
                Node<Visitor> reduced = new GrammarStartBlock(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT135 :
                stack.push(lookahead, 135);
                return Status.NOMINAL;
            case SHIFT46 :
                stack.push(lookahead, 46);
                return Status.NOMINAL;
            case SHIFT134 :
                stack.push(lookahead, 134);
                return Status.NOMINAL;
            case GOTO26 :
                stack.push(lookahead, 26);
                return Status.NOMINAL;
            case SHIFT75 :
                stack.push(lookahead, 75);
                return Status.NOMINAL;
            case GOTO86 :
                stack.push(lookahead, 86);
                return Status.NOMINAL;
            case SHIFT81 :
                stack.push(lookahead, 81);
                return Status.NOMINAL;
            case SHIFT18 :
                stack.push(lookahead, 18);
                return Status.NOMINAL;
            case SHIFT124 :
                stack.push(lookahead, 124);
                return Status.NOMINAL;
            case SHIFT7 :
                stack.push(lookahead, 7);
                return Status.NOMINAL;
            case GOTO103 :
                stack.push(lookahead, 103);
                return Status.NOMINAL;
            case SHIFT111 :
                stack.push(lookahead, 111);
                return Status.NOMINAL;
            case GOTO74 :
                stack.push(lookahead, 74);
                return Status.NOMINAL;
            case REDUCE30 :
            {
                TerminationToken field2 = ((TerminationToken) stack.pop());
                IntToken field1 = ((IntToken) stack.pop());
                DefaultKeywordToken field0 = ((DefaultKeywordToken) stack.pop());
                Node<Visitor> reduced = new DefaultPriorityDirective(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE25 :
            {
                NameDeclAbstractNode field1 = ((NameDeclAbstractNode) stack.pop());
                RHSAbstractNode field0 = ((RHSAbstractNode) stack.pop());
                Node<Visitor> reduced = new NamedRHS(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE41 :
            {
                TerminalStringToken field0 = ((TerminalStringToken) stack.pop());
                Node<Visitor> reduced = new LitSymbol(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO79 :
                stack.push(lookahead, 79);
                return Status.NOMINAL;
            case SHIFT21 :
                stack.push(lookahead, 21);
                return Status.NOMINAL;
            case GOTO19 :
                stack.push(lookahead, 19);
                return Status.NOMINAL;
            case GOTO23 :
                stack.push(lookahead, 23);
                return Status.NOMINAL;
            case REDUCE37 :
            {
                SymbolAbstractNode field0 = ((SymbolAbstractNode) stack.pop());
                Node<Visitor> reduced = new SymbolListHead(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT101 :
                stack.push(lookahead, 101);
                return Status.NOMINAL;
            case REDUCE23 :
            {
                RHSAbstractNode field2 = ((RHSAbstractNode) stack.pop());
                ConcatenationToken field1 = ((ConcatenationToken) stack.pop());
                RHSAbstractNode field0 = ((RHSAbstractNode) stack.pop());
                Node<Visitor> reduced = new ConcatRHS(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT119 :
                stack.push(lookahead, 119);
                return Status.NOMINAL;
            case REDUCE21 :
            {
                EndGroupingToken field2 = ((EndGroupingToken) stack.pop());
                RHSAbstractNode field1 = ((RHSAbstractNode) stack.pop());
                StartGroupingToken field0 = ((StartGroupingToken) stack.pop());
                Node<Visitor> reduced = new GroupRHS(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT73 :
                stack.push(lookahead, 73);
                return Status.NOMINAL;
            case REDUCE40 :
            {
                IdentifierToken field0 = ((IdentifierToken) stack.pop());
                Node<Visitor> reduced = new IdSymbol(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE29 :
            {
                TerminationToken field2 = ((TerminationToken) stack.pop());
                AssocAbstractNode field1 = ((AssocAbstractNode) stack.pop());
                DefaultKeywordToken field0 = ((DefaultKeywordToken) stack.pop());
                Node<Visitor> reduced = new DefaultAssocDirective(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO126 :
                stack.push(lookahead, 126);
                return Status.NOMINAL;
            case GOTO63 :
                stack.push(lookahead, 63);
                return Status.NOMINAL;
            case GOTO60 :
                stack.push(lookahead, 60);
                return Status.NOMINAL;
            case REDUCE9 :
            {
                TokenDeclAbstractNode field1 = ((TokenDeclAbstractNode) stack.pop());
                TokenDeclListAbstractNode field0 = ((TokenDeclListAbstractNode) stack.pop());
                Node<Visitor> reduced = new TokenDeclList(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO62 :
                stack.push(lookahead, 62);
                return Status.NOMINAL;
            case GOTO132 :
                stack.push(lookahead, 132);
                return Status.NOMINAL;
            case GOTO30 :
                stack.push(lookahead, 30);
                return Status.NOMINAL;
            case GOTO48 :
                stack.push(lookahead, 48);
                return Status.NOMINAL;
            case GOTO8 :
                stack.push(lookahead, 8);
                return Status.NOMINAL;
            case GOTO95 :
                stack.push(lookahead, 95);
                return Status.NOMINAL;
            case REDUCE11 :
            {
                RuleDeclAbstractNode field1 = ((RuleDeclAbstractNode) stack.pop());
                RuleDeclListAbstractNode field0 = ((RuleDeclListAbstractNode) stack.pop());
                Node<Visitor> reduced = new RuleDeclList(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO32 :
                stack.push(lookahead, 32);
                return Status.NOMINAL;
            case REDUCE24 :
            {
                PrecDeclAbstractNode field1 = ((PrecDeclAbstractNode) stack.pop());
                RHSAbstractNode field0 = ((RHSAbstractNode) stack.pop());
                Node<Visitor> reduced = new PrecRHS(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT69 :
                stack.push(lookahead, 69);
                return Status.NOMINAL;
            case SHIFT158 :
                stack.push(lookahead, 158);
                return Status.NOMINAL;
            case SHIFT42 :
                stack.push(lookahead, 42);
                return Status.NOMINAL;
            case SHIFT91 :
                stack.push(lookahead, 91);
                return Status.NOMINAL;
            case GOTO39 :
                stack.push(lookahead, 39);
                return Status.NOMINAL;
            case GOTO155 :
                stack.push(lookahead, 155);
                return Status.NOMINAL;
            case SHIFT28 :
                stack.push(lookahead, 28);
                return Status.NOMINAL;
            case SHIFT47 :
                stack.push(lookahead, 47);
                return Status.NOMINAL;
            case SHIFT157 :
                stack.push(lookahead, 157);
                return Status.NOMINAL;
            case REDUCE22 :
            {
                RHSAbstractNode field2 = ((RHSAbstractNode) stack.pop());
                AlternationToken field1 = ((AlternationToken) stack.pop());
                RHSAbstractNode field0 = ((RHSAbstractNode) stack.pop());
                Node<Visitor> reduced = new AltRHS(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE18 :
            {
                SymbolAbstractNode field0 = ((SymbolAbstractNode) stack.pop());
                Node<Visitor> reduced = new SymbolRHS(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO108 :
                stack.push(lookahead, 108);
                return Status.NOMINAL;
            case REDUCE19 :
            {
                EndOptionToken field2 = ((EndOptionToken) stack.pop());
                RHSAbstractNode field1 = ((RHSAbstractNode) stack.pop());
                StartOptionToken field0 = ((StartOptionToken) stack.pop());
                Node<Visitor> reduced = new OptionRHS(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO125 :
                stack.push(lookahead, 125);
                return Status.NOMINAL;
            case SHIFT104 :
                stack.push(lookahead, 104);
                return Status.NOMINAL;
            case GOTO17 :
                stack.push(lookahead, 17);
                return Status.NOMINAL;
            case GOTO10 :
                stack.push(lookahead, 10);
                return Status.NOMINAL;
            case REDUCE15 :
            {
                TerminationToken field3 = ((TerminationToken) stack.pop());
                TerminalStringToken field2 = ((TerminalStringToken) stack.pop());
                DefinitionToken field1 = ((DefinitionToken) stack.pop());
                IdentifierToken field0 = ((IdentifierToken) stack.pop());
                Node<Visitor> reduced = new TokenDecl(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT53 :
                stack.push(lookahead, 53);
                return Status.NOMINAL;
            case SHIFT55 :
                stack.push(lookahead, 55);
                return Status.NOMINAL;
            case SHIFT24 :
                stack.push(lookahead, 24);
                return Status.NOMINAL;
            case GOTO151 :
                stack.push(lookahead, 151);
                return Status.NOMINAL;
            case SHIFT3 :
                stack.push(lookahead, 3);
                return Status.NOMINAL;
            case REDUCE7 :
            {
                PseudoDeclAbstractNode field1 = ((PseudoDeclAbstractNode) stack.pop());
                PseudoDeclListAbstractNode field0 = ((PseudoDeclListAbstractNode) stack.pop());
                Node<Visitor> reduced = new PseudoDeclList(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE4 :
            {
                RuleDeclListAbstractNode field3 = ((RuleDeclListAbstractNode) stack.pop());
                StartBlockToken field2 = ((StartBlockToken) stack.pop());
                RulesKeywordToken field1 = ((RulesKeywordToken) stack.pop());
                SpecialToken field0 = ((SpecialToken) stack.pop());
                Node<Visitor> reduced = new RuleDeclBlock(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO49 :
                stack.push(lookahead, 49);
                return Status.NOMINAL;
            case REDUCE32 :
            {
                TerminationToken field2 = ((TerminationToken) stack.pop());
                SymbolListAbstractNode field1 = ((SymbolListAbstractNode) stack.pop());
                AssocAbstractNode field0 = ((AssocAbstractNode) stack.pop());
                Node<Visitor> reduced = new AssocDirective(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE14 :
            {
                TerminationToken field3 = ((TerminationToken) stack.pop());
                TerminalStringToken field2 = ((TerminalStringToken) stack.pop());
                DefinitionToken field1 = ((DefinitionToken) stack.pop());
                SymbolAbstractNode field0 = ((SymbolAbstractNode) stack.pop());
                Node<Visitor> reduced = new PseudonymDecl(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT92 :
                stack.push(lookahead, 92);
                return Status.NOMINAL;
            case SHIFT44 :
                stack.push(lookahead, 44);
                return Status.NOMINAL;
            case SHIFT102 :
                stack.push(lookahead, 102);
                return Status.NOMINAL;
            case SHIFT94 :
                stack.push(lookahead, 94);
                return Status.NOMINAL;
            case GOTO16 :
                stack.push(lookahead, 16);
                return Status.NOMINAL;
            case SHIFT139 :
                stack.push(lookahead, 139);
                return Status.NOMINAL;
            case REDUCE6 :
            {
                Node<Visitor> reduced = new PseudoDeclListHead().replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO114 :
                stack.push(lookahead, 114);
                return Status.NOMINAL;
            case GOTO130 :
                stack.push(lookahead, 130);
                return Status.NOMINAL;
            case GOTO64 :
                stack.push(lookahead, 64);
                return Status.NOMINAL;
            case REDUCE1 :
            {
                BlockAbstractNode field1 = ((BlockAbstractNode) stack.pop());
                GrammarAbstractNode field0 = ((GrammarAbstractNode) stack.pop());
                Node<Visitor> reduced = new GrammarAppendBlock(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT36 :
                stack.push(lookahead, 36);
                return Status.NOMINAL;
            case SHIFT146 :
                stack.push(lookahead, 146);
                return Status.NOMINAL;
            case REDUCE28 :
            {
                TerminationToken field3 = ((TerminationToken) stack.pop());
                IntToken field2 = ((IntToken) stack.pop());
                AssocAbstractNode field1 = ((AssocAbstractNode) stack.pop());
                DefaultKeywordToken field0 = ((DefaultKeywordToken) stack.pop());
                Node<Visitor> reduced = new DefaultDirective(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO52 :
                stack.push(lookahead, 52);
                return Status.NOMINAL;
            case SHIFT105 :
                stack.push(lookahead, 105);
                return Status.NOMINAL;
            case REDUCE13 :
            {
                DirectiveAbstractNode field1 = ((DirectiveAbstractNode) stack.pop());
                DirectiveListAbstractNode field0 = ((DirectiveListAbstractNode) stack.pop());
                Node<Visitor> reduced = new DirectiveList(field0, field1).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO137 :
                stack.push(lookahead, 137);
                return Status.NOMINAL;
            case GOTO150 :
                stack.push(lookahead, 150);
                return Status.NOMINAL;
            case REDUCE2 :
            {
                PseudoDeclListAbstractNode field3 = ((PseudoDeclListAbstractNode) stack.pop());
                StartBlockToken field2 = ((StartBlockToken) stack.pop());
                PseudonymKeywordToken field1 = ((PseudonymKeywordToken) stack.pop());
                SpecialToken field0 = ((SpecialToken) stack.pop());
                Node<Visitor> reduced = new PseudoDeclBlock(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT133 :
                stack.push(lookahead, 133);
                return Status.NOMINAL;
            case SHIFT93 :
                stack.push(lookahead, 93);
                return Status.NOMINAL;
            case GOTO78 :
                stack.push(lookahead, 78);
                return Status.NOMINAL;
            case GOTO77 :
                stack.push(lookahead, 77);
                return Status.NOMINAL;
            case SHIFT84 :
                stack.push(lookahead, 84);
                return Status.NOMINAL;
            case SHIFT80 :
                stack.push(lookahead, 80);
                return Status.NOMINAL;
            case REDUCE8 :
            {
                Node<Visitor> reduced = new TokenDeclListHead().replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO9 :
                stack.push(lookahead, 9);
                return Status.NOMINAL;
            case SHIFT144 :
                stack.push(lookahead, 144);
                return Status.NOMINAL;
            case SHIFT156 :
                stack.push(lookahead, 156);
                return Status.NOMINAL;
            case REDUCE34 :
            {
                LeftKeywordToken field0 = ((LeftKeywordToken) stack.pop());
                Node<Visitor> reduced = new LeftAssoc(field0).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case REDUCE38 :
            {
                SymbolAbstractNode field2 = ((SymbolAbstractNode) stack.pop());
                ConcatenationToken field1 = ((ConcatenationToken) stack.pop());
                SymbolListAbstractNode field0 = ((SymbolListAbstractNode) stack.pop());
                Node<Visitor> reduced = new SymbolList(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT20 :
                stack.push(lookahead, 20);
                return Status.NOMINAL;
            case SHIFT12 :
                stack.push(lookahead, 12);
                return Status.NOMINAL;
            case ACCEPT:
                return Status.COMPLETE;
            case GOTO100 :
                stack.push(lookahead, 100);
                return Status.NOMINAL;
            case SHIFT68 :
                stack.push(lookahead, 68);
                return Status.NOMINAL;
            case SHIFT70 :
                stack.push(lookahead, 70);
                return Status.NOMINAL;
            case SHIFT38 :
                stack.push(lookahead, 38);
                return Status.NOMINAL;
            case SHIFT54 :
                stack.push(lookahead, 54);
                return Status.NOMINAL;
            case GOTO129 :
                stack.push(lookahead, 129);
                return Status.NOMINAL;
            case GOTO22 :
                stack.push(lookahead, 22);
                return Status.NOMINAL;
            case SHIFT149 :
                stack.push(lookahead, 149);
                return Status.NOMINAL;
            case SHIFT72 :
                stack.push(lookahead, 72);
                return Status.NOMINAL;
            case SHIFT140 :
                stack.push(lookahead, 140);
                return Status.NOMINAL;
            case SHIFT65 :
                stack.push(lookahead, 65);
                return Status.NOMINAL;
            case SHIFT25 :
                stack.push(lookahead, 25);
                return Status.NOMINAL;
            case SHIFT1 :
                stack.push(lookahead, 1);
                return Status.NOMINAL;
            case GOTO13 :
                stack.push(lookahead, 13);
                return Status.NOMINAL;
            case SHIFT6 :
                stack.push(lookahead, 6);
                return Status.NOMINAL;
            case SHIFT138 :
                stack.push(lookahead, 138);
                return Status.NOMINAL;
            case SHIFT159 :
                stack.push(lookahead, 159);
                return Status.NOMINAL;
            case GOTO131 :
                stack.push(lookahead, 131);
                return Status.NOMINAL;
            case SHIFT11 :
                stack.push(lookahead, 11);
                return Status.NOMINAL;
            case GOTO154 :
                stack.push(lookahead, 154);
                return Status.NOMINAL;
            case REDUCE16 :
            {
                TerminationToken field2 = ((TerminationToken) stack.pop());
                TerminalStringToken field1 = ((TerminalStringToken) stack.pop());
                BangToken field0 = ((BangToken) stack.pop());
                Node<Visitor> reduced = new IgnoreTokenDecl(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT118 :
                stack.push(lookahead, 118);
                return Status.NOMINAL;
            case REDUCE5 :
            {
                DirectiveListAbstractNode field3 = ((DirectiveListAbstractNode) stack.pop());
                StartBlockToken field2 = ((StartBlockToken) stack.pop());
                PrecedenceKeywordToken field1 = ((PrecedenceKeywordToken) stack.pop());
                SpecialToken field0 = ((SpecialToken) stack.pop());
                Node<Visitor> reduced = new PrecDirectiveBlock(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT142 :
                stack.push(lookahead, 142);
                return Status.NOMINAL;
            case SHIFT82 :
                stack.push(lookahead, 82);
                return Status.NOMINAL;
            case GOTO110 :
                stack.push(lookahead, 110);
                return Status.NOMINAL;
            case SHIFT106 :
                stack.push(lookahead, 106);
                return Status.NOMINAL;
            case SHIFT123 :
                stack.push(lookahead, 123);
                return Status.NOMINAL;
            case GOTO5 :
                stack.push(lookahead, 5);
                return Status.NOMINAL;
            case SHIFT107 :
                stack.push(lookahead, 107);
                return Status.NOMINAL;
            case REDUCE31 :
            {
                TerminationToken field3 = ((TerminationToken) stack.pop());
                SymbolListAbstractNode field2 = ((SymbolListAbstractNode) stack.pop());
                IntToken field1 = ((IntToken) stack.pop());
                AssocAbstractNode field0 = ((AssocAbstractNode) stack.pop());
                Node<Visitor> reduced = new BothDirective(field0, field1, field2, field3).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case SHIFT97 :
                stack.push(lookahead, 97);
                return Status.NOMINAL;
            case SHIFT120 :
                stack.push(lookahead, 120);
                return Status.NOMINAL;
            case SHIFT4 :
                stack.push(lookahead, 4);
                return Status.NOMINAL;
            case REDUCE12 :
            {
                Node<Visitor> reduced = new DirectiveListHead().replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            case GOTO148 :
                stack.push(lookahead, 148);
                return Status.NOMINAL;
            case REDUCE33 :
            {
                TerminationToken field2 = ((TerminationToken) stack.pop());
                SymbolListAbstractNode field1 = ((SymbolListAbstractNode) stack.pop());
                IntToken field0 = ((IntToken) stack.pop());
                Node<Visitor> reduced = new PriorityDirective(field0, field1, field2).replace();
                this.advance(stack, reduced);
                return this.advance(stack, lookahead);
            }
            default:
                return Status.ERROR;
        }
    }

    @Override
    public Symbol[] expectedSymbols(int state) {
        switch (state) {
            case  1 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  2 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  3 :
                return new Symbol[] {Symbols.TERMINALSTRING };
            case  4 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER };
            case  5 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.INT, Symbols.TERMINATION, Symbols.TERMINALSTRING, Symbols.SYMBOLLIST, Symbols.SYMBOL };
            case  6 :
                return new Symbol[] {Symbols.INT, Symbols.TERMINATION };
            case  7 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.ENDGROUPING, Symbols.RHS };
            case  8 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL, Symbols.ENDGROUPING };
            case  9 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.BANG, Symbols.TOKENDECL };
            case  10 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  11 :
                return new Symbol[] {Symbols.INT, Symbols.TERMINATION };
            case  12 :
                return new Symbol[] {Symbols.PSEUDONYMKEYWORD, Symbols.TOKENSKEYWORD, Symbols.PRECEDENCEKEYWORD, Symbols.RULESKEYWORD };
            case  13 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.ENDREPETITION, Symbols.PRECDECL };
            case  14 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  15 :
                return new Symbol[] {Symbols.PRECKEYWORD, Symbols.NAMEKEYWORD };
            case  16 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL, Symbols.ENDGROUPING };
            case  17 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  18 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  19 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER };
            case  20 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  21 :
                return new Symbol[] {Symbols.DEFINITION };
            case  22 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF };
            case  23 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL };
            case  24 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  25 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  26 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  27 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  28 :
                return new Symbol[] {Symbols.RULEDECLLIST, Symbols.IDENTIFIER, Symbols.RULEDECL };
            case  29 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  30 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  31 :
                return new Symbol[] {Symbols.DEFINITION };
            case  32 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL, Symbols.ENDGROUPING };
            case  33 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  34 :
                return new Symbol[] {Symbols.TERMINALSTRING };
            case  35 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  36 :
                return new Symbol[] {Symbols.TERMINATION };
            case  37 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  38 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  39 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.TERMINATION, Symbols.PRECDECL };
            case  40 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  41 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.BANG };
            case  42 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.TERMINALSTRING, Symbols.SYMBOL };
            case  43 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.TERMINALSTRING, Symbols.SYMBOLLIST, Symbols.SYMBOL };
            case  44 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.TERMINALSTRING, Symbols.SYMBOL };
            case  45 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.ENDGROUPING, Symbols.RHS };
            case  46 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  47 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  48 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  49 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER };
            case  50 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.ENDGROUPING, Symbols.RHS };
            case  51 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  52 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  53 :
                return new Symbol[] {Symbols.PRECKEYWORD, Symbols.NAMEKEYWORD };
            case  54 :
                return new Symbol[] {Symbols.PSEUDONYMKEYWORD, Symbols.TOKENSKEYWORD, Symbols.PRECEDENCEKEYWORD, Symbols.RULESKEYWORD };
            case  55 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  56 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  57 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  58 :
                return new Symbol[] {Symbols.STARTBLOCK };
            case  59 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  60 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.ENDREPETITION, Symbols.PRECDECL };
            case  61 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL };
            case  62 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  63 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  64 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL, Symbols.ENDGROUPING };
            case  65 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  66 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  67 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.ENDGROUPING, Symbols.RHS };
            case  68 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  69 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.TERMINALSTRING, Symbols.SYMBOL };
            case  70 :
                return new Symbol[] {Symbols.TERMINATION };
            case  71 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.TERMINALSTRING, Symbols.SYMBOL, Symbols.ENDGROUPING };
            case  72 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.TERMINALSTRING, Symbols.SYMBOL };
            case  73 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.BANG };
            case  74 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.TERMINATION, Symbols.PRECDECL };
            case  75 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  76 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  77 :
                return new Symbol[] {Symbols.DEFINITION };
            case  78 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.TERMINALSTRING, Symbols.DEFINITION };
            case  79 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.RULEDECL };
            case  80 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  81 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  82 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.INT, Symbols.TERMINATION, Symbols.TERMINALSTRING };
            case  83 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  84 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.TERMINALSTRING, Symbols.DEFINITION };
            case  85 :
                return new Symbol[] {Symbols.IDENTIFIER };
            case  86 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  87 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  88 :
                return new Symbol[] {Symbols.PRECKEYWORD, Symbols.NAMEKEYWORD };
            case  89 :
                return new Symbol[] {Symbols.STARTBLOCK };
            case  90 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.ENDGROUPING, Symbols.RHS };
            case  91 :
                return new Symbol[] {Symbols.IDENTIFIER };
            case  92 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  93 :
                return new Symbol[] {Symbols.DEFINITION };
            case  94 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.PSEUDODECLLIST, Symbols.EOF, Symbols.IDENTIFIER, Symbols.TERMINALSTRING, Symbols.DEFINITION };
            case  95 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL };
            case  96 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  97 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  98 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  99 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.ENDGROUPING, Symbols.RHS };
            case  100 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF };
            case  101 :
                return new Symbol[] {Symbols.TERMINALSTRING };
            case  102 :
                return new Symbol[] {Symbols.IDENTIFIER };
            case  103 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  104 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  105 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  106 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.TERMINALSTRING, Symbols.SYMBOLLIST, Symbols.SYMBOL };
            case  107 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.INT, Symbols.TERMINATION, Symbols.TERMINALSTRING };
            case  108 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL, Symbols.ENDGROUPING };
            case  109 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  110 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  111 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  112 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  113 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  114 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.TERMINALSTRING, Symbols.PSEUDODECL, Symbols.SYMBOL, Symbols.DEFINITION };
            case  115 :
                return new Symbol[] {Symbols.TERMINATION };
            case  116 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  117 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  118 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.TOKENDECLLIST, Symbols.EOF, Symbols.IDENTIFIER, Symbols.BANG };
            case  119 :
                return new Symbol[] {Symbols.TERMINATION };
            case  120 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  121 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.ENDREPETITION, Symbols.PRECDECL };
            case  122 :
                return new Symbol[] {Symbols.STARTBLOCK };
            case  123 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.IDENTIFIER, Symbols.INT, Symbols.TERMINATION, Symbols.TERMINALSTRING };
            case  124 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  125 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  126 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  127 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.ENDREPETITION, Symbols.PRECDECL };
            case  128 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDGROUPING };
            case  129 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.BLOCK };
            case  130 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL };
            case  131 :
                return new Symbol[] {Symbols.INT, Symbols.TERMINATION };
            case  0 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.GRAMMAR, Symbols.BLOCK };
            case  132 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  133 :
                return new Symbol[] {Symbols.DEFINITION };
            case  134 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  135 :
                return new Symbol[] {Symbols.PRECKEYWORD, Symbols.NAMEKEYWORD };
            case  136 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  137 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.ENDREPETITION, Symbols.PRECDECL };
            case  138 :
                return new Symbol[] {Symbols.TERMINATION };
            case  139 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  140 :
                return new Symbol[] {Symbols.IDENTIFIER };
            case  141 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.ENDREPETITION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  142 :
                return new Symbol[] {Symbols.NONKEYWORD, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.ASSOC, Symbols.RIGHTKEYWORD };
            case  143 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.IDENTIFIER, Symbols.TERMINATION, Symbols.STARTOPTION, Symbols.TERMINALSTRING, Symbols.STARTGROUPING, Symbols.SYMBOL, Symbols.STARTREPETITION, Symbols.RHS };
            case  144 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.EOF, Symbols.IDENTIFIER, Symbols.BANG };
            case  145 :
                return new Symbol[] {Symbols.STARTBLOCK };
            case  146 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.ENDREPETITION };
            case  147 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.ALTERNATION };
            case  148 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.DIRECTIVE, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.ASSOC, Symbols.RIGHTKEYWORD };
            case  149 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  150 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL };
            case  151 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ENDOPTION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL };
            case  152 :
                return new Symbol[] {Symbols.CONCATENATION, Symbols.TERMINATION };
            case  153 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.PRECDECL, Symbols.ENDGROUPING };
            case  154 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.TERMINATION, Symbols.PRECDECL };
            case  155 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.NAMEDECL, Symbols.ALTERNATION, Symbols.ENDREPETITION, Symbols.PRECDECL };
            case  156 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.DIRECTIVELIST, Symbols.RIGHTKEYWORD };
            case  157 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.CONCATENATION, Symbols.ALTERNATION, Symbols.TERMINATION };
            case  158 :
                return new Symbol[] {Symbols.SPECIAL, Symbols.DEFAULTKEYWORD, Symbols.NONKEYWORD, Symbols.EOF, Symbols.INT, Symbols.LEFTKEYWORD, Symbols.RIGHTKEYWORD };
            case  159 :
                return new Symbol[] {Symbols.INT, Symbols.TERMINATION };
            default:
                throw new RuntimeException("Unknown state");
        }
    }

    private ParsingEngineImpl.Actions getState1Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE26;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState2Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT29;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT90;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO109;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT2;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO127;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState3Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT115;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState4Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE17;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE17;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE17;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState5Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT152;
            case INT:
                return ParsingEngineImpl.Actions.SHIFT43;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT98;
            case SYMBOLLIST:
                return ParsingEngineImpl.Actions.GOTO117;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO59;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState6Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case INT:
                return ParsingEngineImpl.Actions.REDUCE36;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE36;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState7Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT112;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT67;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO66;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO153;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState8Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT15;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT50;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO128;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT99;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO51;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.SHIFT35;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState9Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE3;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE3;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT31;
            case BANG:
                return ParsingEngineImpl.Actions.SHIFT34;
            case TOKENDECL:
                return ParsingEngineImpl.Actions.GOTO41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState10Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE27;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState11Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case INT:
                return ParsingEngineImpl.Actions.REDUCE35;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE35;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState12Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case PSEUDONYMKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT89;
            case TOKENSKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT145;
            case PRECEDENCEKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT122;
            case RULESKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT58;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState13Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT88;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT14;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO57;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT56;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.SHIFT83;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO87;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState14Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT29;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT90;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO109;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT2;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO121;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState15Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case PRECKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT71;
            case NAMEKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT85;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState16Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT15;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT50;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO128;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT99;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO51;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.SHIFT136;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState17Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE27;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE27;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState18Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT96;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT45;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO116;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT141;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO61;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState19Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE10;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE10;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE10;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState20Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE20;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE20;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState21Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case DEFINITION:
                return ParsingEngineImpl.Actions.SHIFT143;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState22Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE0;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE0;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState23Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT135;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT46;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.SHIFT134;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT75;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO86;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState24Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT81;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT18;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT124;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT7;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO103;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT111;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO74;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState25Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE30;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE30;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE30;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE30;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE30;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE30;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE30;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState26Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE25;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState27Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE41;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState28Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case RULEDECLLIST:
                return ParsingEngineImpl.Actions.GOTO79;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT21;
            case RULEDECL:
                return ParsingEngineImpl.Actions.GOTO19;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState29Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT96;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT45;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO116;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT141;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO23;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState30Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE37;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE37;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState31Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case DEFINITION:
                return ParsingEngineImpl.Actions.SHIFT101;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState32Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE23;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO128;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO51;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE23;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState33Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE41;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState34Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT119;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState35Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE21;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState36Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT73;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState37Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE40;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE40;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState38Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE29;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE29;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE29;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE29;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE29;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE29;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE29;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState39Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE23;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO126;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO63;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState40Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT29;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT90;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO109;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT2;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO60;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState41Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE9;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE9;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE9;
            case BANG:
                return ParsingEngineImpl.Actions.REDUCE9;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState42Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT81;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT124;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO62;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState43Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT152;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT98;
            case SYMBOLLIST:
                return ParsingEngineImpl.Actions.GOTO132;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO30;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState44Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO48;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState45Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT112;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT67;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO66;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO8;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState46Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT96;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT45;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO116;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT141;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO95;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState47Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE20;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE20;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState48Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE27;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE27;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState49Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE11;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE11;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE11;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState50Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT112;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT67;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO66;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO32;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState51Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE24;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE24;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState52Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT69;
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT158;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState53Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case PRECKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT42;
            case NAMEKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT91;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState54Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case PSEUDONYMKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT89;
            case TOKENSKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT145;
            case PRECEDENCEKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT122;
            case RULESKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT58;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState55Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT81;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT18;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT124;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT7;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO103;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT111;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO39;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState56Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT29;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT90;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO109;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT2;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO155;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState57Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE25;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE25;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState58Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case STARTBLOCK:
                return ParsingEngineImpl.Actions.SHIFT28;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState59Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE37;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE37;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState60Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT88;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT14;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO57;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT56;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.SHIFT47;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO87;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState61Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT135;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT46;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.SHIFT157;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT75;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO86;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState62Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE27;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE27;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState63Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE24;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState64Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT15;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT50;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO128;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT99;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO51;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE22;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState65Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE20;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState66Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE18;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE18;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState67Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT112;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT67;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO66;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO108;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState68Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE19;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState69Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT152;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT98;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO125;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState70Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT104;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState71Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO17;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState72Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO10;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState73Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE15;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE15;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE15;
            case BANG:
                return ParsingEngineImpl.Actions.REDUCE15;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState74Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT53;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT55;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO126;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT24;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE22;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO63;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState75Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT96;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT45;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO116;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT141;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO151;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState76Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE40;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState77Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case DEFINITION:
                return ParsingEngineImpl.Actions.SHIFT3;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState78Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE7;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE7;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE7;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.REDUCE7;
            case DEFINITION:
                return ParsingEngineImpl.Actions.REDUCE7;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState79Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE4;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE4;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT21;
            case RULEDECL:
                return ParsingEngineImpl.Actions.GOTO49;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState80Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE32;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE32;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE32;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE32;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE32;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE32;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE32;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState81Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE40;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState82Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE36;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE36;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE36;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE36;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.REDUCE36;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState83Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE20;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE20;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE20;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState84Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE14;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE14;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE14;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.REDUCE14;
            case DEFINITION:
                return ParsingEngineImpl.Actions.REDUCE14;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState85Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT92;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState86Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE24;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState87Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE24;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE24;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE24;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState88Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case PRECKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT44;
            case NAMEKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT102;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState89Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case STARTBLOCK:
                return ParsingEngineImpl.Actions.SHIFT94;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState90Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT112;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT67;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO66;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO16;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState91Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT139;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState92Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE26;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE26;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState93Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case DEFINITION:
                return ParsingEngineImpl.Actions.REDUCE41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState94Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE6;
            case PSEUDODECLLIST:
                return ParsingEngineImpl.Actions.GOTO114;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE6;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE6;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.REDUCE6;
            case DEFINITION:
                return ParsingEngineImpl.Actions.REDUCE6;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState95Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE23;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO86;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState96Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT96;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT45;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO116;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT141;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO130;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState97Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE19;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE19;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState98Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState99Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT37;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT112;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT33;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT67;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO66;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT40;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE39;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO64;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState100Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE1;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE1;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState101Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT36;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState102Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT146;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState103Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE18;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState104Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE28;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE28;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE28;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE28;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE28;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE28;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE28;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState105Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE21;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE21;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState106Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT152;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT98;
            case SYMBOLLIST:
                return ParsingEngineImpl.Actions.GOTO52;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO30;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState107Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE35;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE35;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE35;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE35;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.REDUCE35;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState108Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT15;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT50;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO128;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT99;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO51;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.SHIFT105;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState109Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE18;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE18;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState110Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE13;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE13;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE13;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE13;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE13;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE13;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE13;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState111Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT29;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT90;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO109;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT2;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO137;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState112Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT76;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT96;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT147;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT45;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO116;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT141;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO150;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState113Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE40;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE40;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState114Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE2;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE2;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT133;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT93;
            case PSEUDODECL:
                return ParsingEngineImpl.Actions.GOTO78;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO77;
            case DEFINITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState115Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT84;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState116Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE18;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE18;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE18;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState117Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT69;
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT80;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState118Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE8;
            case TOKENDECLLIST:
                return ParsingEngineImpl.Actions.GOTO9;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE8;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE8;
            case BANG:
                return ParsingEngineImpl.Actions.REDUCE8;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState119Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT144;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState120Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE21;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState121Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE23;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO57;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE23;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO87;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState122Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case STARTBLOCK:
                return ParsingEngineImpl.Actions.SHIFT156;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState123Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE34;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE34;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE34;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE34;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.REDUCE34;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState124Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE41;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState125Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE38;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE38;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState126Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE25;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState127Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT88;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT14;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO57;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT56;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.SHIFT20;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO87;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState128Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE25;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE25;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.REDUCE25;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState129Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT12;
            case EOF:
                return ParsingEngineImpl.Actions.ACCEPT;
            case BLOCK:
                return ParsingEngineImpl.Actions.GOTO100;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState130Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT135;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT46;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.SHIFT68;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT75;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO86;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState131Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case INT:
                return ParsingEngineImpl.Actions.SHIFT70;
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT38;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState0Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT54;
            case GRAMMAR:
                return ParsingEngineImpl.Actions.GOTO129;
            case BLOCK:
                return ParsingEngineImpl.Actions.GOTO22;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState132Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT69;
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT149;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState133Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case DEFINITION:
                return ParsingEngineImpl.Actions.REDUCE40;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState134Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE19;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE19;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState135Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case PRECKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT72;
            case NAMEKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT140;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState136Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE21;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE21;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE21;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState137Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT88;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT14;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO57;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT56;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.SHIFT65;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO87;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState138Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT25;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState139Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE26;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState140Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT1;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState141Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT113;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT29;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT27;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT90;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO109;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT2;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO13;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState142Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT6;
            case INT:
                return ParsingEngineImpl.Actions.SHIFT138;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT159;
            case ASSOC:
                return ParsingEngineImpl.Actions.GOTO131;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT11;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState143Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE39;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.SHIFT81;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE39;
            case STARTOPTION:
                return ParsingEngineImpl.Actions.SHIFT18;
            case TERMINALSTRING:
                return ParsingEngineImpl.Actions.SHIFT124;
            case STARTGROUPING:
                return ParsingEngineImpl.Actions.SHIFT7;
            case SYMBOL:
                return ParsingEngineImpl.Actions.GOTO103;
            case STARTREPETITION:
                return ParsingEngineImpl.Actions.SHIFT111;
            case RHS:
                return ParsingEngineImpl.Actions.GOTO154;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState144Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE16;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE16;
            case IDENTIFIER:
                return ParsingEngineImpl.Actions.REDUCE16;
            case BANG:
                return ParsingEngineImpl.Actions.REDUCE16;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState145Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case STARTBLOCK:
                return ParsingEngineImpl.Actions.SHIFT118;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState146Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE26;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE26;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE26;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState147Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE41;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE41;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE41;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState148Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE5;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT142;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT82;
            case DIRECTIVE:
                return ParsingEngineImpl.Actions.GOTO110;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE5;
            case INT:
                return ParsingEngineImpl.Actions.SHIFT106;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT123;
            case ASSOC:
                return ParsingEngineImpl.Actions.GOTO5;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.SHIFT107;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState149Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE31;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE31;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE31;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE31;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE31;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE31;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE31;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState150Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT135;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT46;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.SHIFT97;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT75;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO86;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState151Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT135;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT46;
            case ENDOPTION:
                return ParsingEngineImpl.Actions.REDUCE22;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO26;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT75;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO86;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState152Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE40;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState153Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT15;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT50;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO128;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT99;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO51;
            case ENDGROUPING:
                return ParsingEngineImpl.Actions.SHIFT120;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState154Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT53;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT55;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO126;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT24;
            case TERMINATION:
                return ParsingEngineImpl.Actions.SHIFT4;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO63;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState155Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.SHIFT88;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.SHIFT14;
            case NAMEDECL:
                return ParsingEngineImpl.Actions.GOTO57;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.SHIFT56;
            case ENDREPETITION:
                return ParsingEngineImpl.Actions.REDUCE22;
            case PRECDECL:
                return ParsingEngineImpl.Actions.GOTO87;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState156Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE12;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE12;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE12;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE12;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE12;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE12;
            case DIRECTIVELIST:
                return ParsingEngineImpl.Actions.GOTO148;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE12;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState157Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE19;
            case CONCATENATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case ALTERNATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE19;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState158Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case SPECIAL:
                return ParsingEngineImpl.Actions.REDUCE33;
            case DEFAULTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE33;
            case NONKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE33;
            case EOF:
                return ParsingEngineImpl.Actions.REDUCE33;
            case INT:
                return ParsingEngineImpl.Actions.REDUCE33;
            case LEFTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE33;
            case RIGHTKEYWORD:
                return ParsingEngineImpl.Actions.REDUCE33;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private ParsingEngineImpl.Actions getState159Action(Node<Visitor> lookahead) {
        switch (((Symbols) lookahead.symbol())) {
            case INT:
                return ParsingEngineImpl.Actions.REDUCE34;
            case TERMINATION:
                return ParsingEngineImpl.Actions.REDUCE34;
            default:
                return ParsingEngineImpl.Actions.ERROR;
        }
    }

    private static enum Actions {

        REDUCE26,
        ERROR,
        REDUCE39,
        SHIFT113,
        SHIFT29,
        SHIFT27,
        SHIFT90,
        GOTO109,
        SHIFT2,
        GOTO127,
        SHIFT115,
        REDUCE17,
        SHIFT152,
        SHIFT43,
        SHIFT98,
        GOTO117,
        GOTO59,
        REDUCE36,
        SHIFT37,
        SHIFT112,
        SHIFT33,
        SHIFT67,
        GOTO66,
        SHIFT40,
        GOTO153,
        SHIFT15,
        SHIFT50,
        GOTO128,
        SHIFT99,
        GOTO51,
        SHIFT35,
        REDUCE3,
        SHIFT31,
        SHIFT34,
        GOTO41,
        REDUCE27,
        REDUCE35,
        SHIFT89,
        SHIFT145,
        SHIFT122,
        SHIFT58,
        SHIFT88,
        SHIFT14,
        GOTO57,
        SHIFT56,
        SHIFT83,
        GOTO87,
        GOTO121,
        SHIFT71,
        SHIFT85,
        SHIFT136,
        SHIFT76,
        SHIFT96,
        SHIFT147,
        SHIFT45,
        GOTO116,
        SHIFT141,
        GOTO61,
        REDUCE10,
        REDUCE20,
        SHIFT143,
        REDUCE0,
        SHIFT135,
        SHIFT46,
        SHIFT134,
        GOTO26,
        SHIFT75,
        GOTO86,
        SHIFT81,
        SHIFT18,
        SHIFT124,
        SHIFT7,
        GOTO103,
        SHIFT111,
        GOTO74,
        REDUCE30,
        REDUCE25,
        REDUCE41,
        GOTO79,
        SHIFT21,
        GOTO19,
        GOTO23,
        REDUCE37,
        SHIFT101,
        REDUCE23,
        SHIFT119,
        REDUCE21,
        SHIFT73,
        REDUCE40,
        REDUCE29,
        GOTO126,
        GOTO63,
        GOTO60,
        REDUCE9,
        GOTO62,
        GOTO132,
        GOTO30,
        GOTO48,
        GOTO8,
        GOTO95,
        REDUCE11,
        GOTO32,
        REDUCE24,
        SHIFT69,
        SHIFT158,
        SHIFT42,
        SHIFT91,
        GOTO39,
        GOTO155,
        SHIFT28,
        SHIFT47,
        SHIFT157,
        REDUCE22,
        REDUCE18,
        GOTO108,
        REDUCE19,
        GOTO125,
        SHIFT104,
        GOTO17,
        GOTO10,
        REDUCE15,
        SHIFT53,
        SHIFT55,
        SHIFT24,
        GOTO151,
        SHIFT3,
        REDUCE7,
        REDUCE4,
        GOTO49,
        REDUCE32,
        REDUCE14,
        SHIFT92,
        SHIFT44,
        SHIFT102,
        SHIFT94,
        GOTO16,
        SHIFT139,
        REDUCE6,
        GOTO114,
        GOTO130,
        GOTO64,
        REDUCE1,
        SHIFT36,
        SHIFT146,
        REDUCE28,
        GOTO52,
        SHIFT105,
        REDUCE13,
        GOTO137,
        GOTO150,
        REDUCE2,
        SHIFT133,
        SHIFT93,
        GOTO78,
        GOTO77,
        SHIFT84,
        SHIFT80,
        REDUCE8,
        GOTO9,
        SHIFT144,
        SHIFT156,
        REDUCE34,
        REDUCE38,
        SHIFT20,
        SHIFT12,
        ACCEPT,
        GOTO100,
        SHIFT68,
        SHIFT70,
        SHIFT38,
        SHIFT54,
        GOTO129,
        GOTO22,
        SHIFT149,
        SHIFT72,
        SHIFT140,
        SHIFT65,
        SHIFT25,
        SHIFT1,
        GOTO13,
        SHIFT6,
        SHIFT138,
        SHIFT159,
        GOTO131,
        SHIFT11,
        GOTO154,
        REDUCE16,
        SHIFT118,
        REDUCE5,
        SHIFT142,
        SHIFT82,
        GOTO110,
        SHIFT106,
        SHIFT123,
        GOTO5,
        SHIFT107,
        REDUCE31,
        SHIFT97,
        SHIFT120,
        SHIFT4,
        REDUCE12,
        GOTO148,
        REDUCE33;

    }

}
