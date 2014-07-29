package bepler.lrpage.test;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class TestParser {


    private TestParser.Actions getAction(int state, AbstractSyntaxNode lookahead) {
        switch (state) {
            case  1 :
                return this.getState1Action(lookahead);
            case  2 :
                return this.getState2Action(lookahead);
            case  3 :
                return this.getState3Action(lookahead);
            case  4 :
                return this.getState4Action(lookahead);
            case  0 :
                return this.getState0Action(lookahead);
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
            default:
                throw new RuntimeException("Unknown state.");
        }
    }

    public AbstractSyntaxNode parse(TestLexer lexer)
        throws IOException
    {
        Deque<Integer> states = new LinkedList<Integer>();
        states.push(0);
        Deque<AbstractSyntaxNode> stack = new LinkedList<AbstractSyntaxNode>();
        Deque<AbstractSyntaxNode> lookaheadStack = new LinkedList<AbstractSyntaxNode>();
        lookaheadStack.add(lexer.nextToken());
        while (true) {
            if (lookaheadStack.isEmpty()) {
                lookaheadStack.add(lexer.nextToken());
            }
            int state = states.peek();
            AbstractSyntaxNode lookahead = lookaheadStack.peek();
            TestParser.Actions action = this.getAction(state, lookahead);
            switch (action) {
                case ERROR:
                    throw new RuntimeException(("Syntax error on token: "+ lookahead));
                case REDUCE0 :
                {
                    ExpAbstractNode field2 = ((ExpAbstractNode) stack.pop());
                    states.pop();
                    PlusToken field1 = ((PlusToken) stack.pop());
                    states.pop();
                    ExpAbstractNode field0 = ((ExpAbstractNode) stack.pop());
                    states.pop();
                    lookaheadStack.push(new PlusExp(field0, field1, field2).replace());
                    break;
                }
                case REDUCE1 :
                {
                    IDToken field0 = ((IDToken) stack.pop());
                    states.pop();
                    lookaheadStack.push(new IdExp(field0).replace());
                    break;
                }
                case SHIFT13 :
                    stack.push(lookaheadStack.pop());
                    states.push(13);
                    break;
                case SHIFT7 :
                    stack.push(lookaheadStack.pop());
                    states.push(7);
                    break;
                case SHIFT6 :
                    stack.push(lookaheadStack.pop());
                    states.push(6);
                    break;
                case ACCEPT:
                    return stack.peek();
                case GOTO4 :
                    stack.push(lookaheadStack.pop());
                    states.push(4);
                    break;
                case SHIFT9 :
                    stack.push(lookaheadStack.pop());
                    states.push(9);
                    break;
                case SHIFT11 :
                    stack.push(lookaheadStack.pop());
                    states.push(11);
                    break;
                case GOTO8 :
                    stack.push(lookaheadStack.pop());
                    states.push(8);
                    break;
                case SHIFT5 :
                    stack.push(lookaheadStack.pop());
                    states.push(5);
                    break;
                case SHIFT2 :
                    stack.push(lookaheadStack.pop());
                    states.push(2);
                    break;
                case GOTO10 :
                    stack.push(lookaheadStack.pop());
                    states.push(10);
                    break;
                case REDUCE2 :
                {
                    RParenToken field2 = ((RParenToken) stack.pop());
                    states.pop();
                    ExpAbstractNode field1 = ((ExpAbstractNode) stack.pop());
                    states.pop();
                    LParenToken field0 = ((LParenToken) stack.pop());
                    states.pop();
                    lookaheadStack.push(new ParenExp(field0, field1, field2).replace());
                    break;
                }
                case SHIFT12 :
                    stack.push(lookaheadStack.pop());
                    states.push(12);
                    break;
                case GOTO3 :
                    stack.push(lookaheadStack.pop());
                    states.push(3);
                    break;
                case GOTO1 :
                    stack.push(lookaheadStack.pop());
                    states.push(1);
                    break;
                default:
                    throw new RuntimeException("Unknown action.");
            }
        }
    }

    private TestParser.Actions getState1Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE0;
            case RParen:
                return TestParser.Actions.REDUCE0;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState2Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE1;
            case RParen:
                return TestParser.Actions.REDUCE1;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState3Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.SHIFT13;
            case RParen:
                return TestParser.Actions.SHIFT7;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState4Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.SHIFT6;
            case EOF:
                return TestParser.Actions.ACCEPT;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState0Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO4;
            case LParen:
                return TestParser.Actions.SHIFT9;
            case ID:
                return TestParser.Actions.SHIFT11;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState5Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO8;
            case LParen:
                return TestParser.Actions.SHIFT5;
            case ID:
                return TestParser.Actions.SHIFT2;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState6Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO10;
            case LParen:
                return TestParser.Actions.SHIFT9;
            case ID:
                return TestParser.Actions.SHIFT11;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState7Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE2;
            case EOF:
                return TestParser.Actions.REDUCE2;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState8Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.SHIFT13;
            case RParen:
                return TestParser.Actions.SHIFT12;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState9Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO3;
            case LParen:
                return TestParser.Actions.SHIFT5;
            case ID:
                return TestParser.Actions.SHIFT2;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState10Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE0;
            case EOF:
                return TestParser.Actions.REDUCE0;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState11Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE1;
            case EOF:
                return TestParser.Actions.REDUCE1;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState12Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE2;
            case RParen:
                return TestParser.Actions.REDUCE2;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState13Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO1;
            case LParen:
                return TestParser.Actions.SHIFT5;
            case ID:
                return TestParser.Actions.SHIFT2;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private static enum Actions {

        ERROR,
        REDUCE0,
        REDUCE1,
        SHIFT13,
        SHIFT7,
        SHIFT6,
        ACCEPT,
        GOTO4,
        SHIFT9,
        SHIFT11,
        GOTO8,
        SHIFT5,
        SHIFT2,
        GOTO10,
        REDUCE2,
        SHIFT12,
        GOTO3,
        GOTO1;

    }

}
