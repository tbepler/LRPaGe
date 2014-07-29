package bepler.lrpage.test;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class TestParser {


    private TestParser.Actions getAction(int state, AbstractSyntaxNode lookahead) {
        switch (state) {
            case  0 :
                return this.getState0Action(lookahead);
            case  1 :
                return this.getState1Action(lookahead);
            case  2 :
                return this.getState2Action(lookahead);
            case  3 :
                return this.getState3Action(lookahead);
            case  4 :
                return this.getState4Action(lookahead);
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
                case GOTO4 :
                    stack.push(lookaheadStack.pop());
                    states.push(4);
                    break;
                case SHIFT1 :
                    stack.push(lookaheadStack.pop());
                    states.push(1);
                    break;
                case REDUCE1 :
                {
                    IDToken field0 = ((IDToken) stack.pop());
                    states.pop();
                    lookaheadStack.push(new IdExp(field0).replace());
                    break;
                }
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
                case GOTO2 :
                    stack.push(lookaheadStack.pop());
                    states.push(2);
                    break;
                case SHIFT3 :
                    stack.push(lookaheadStack.pop());
                    states.push(3);
                    break;
                case ACCEPT:
                    return stack.peek();
                default:
                    throw new RuntimeException("Unknown action.");
            }
        }
    }

    private TestParser.Actions getState0Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO4;
            case ID:
                return TestParser.Actions.SHIFT1;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState1Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE1;
            case EOF:
                return TestParser.Actions.REDUCE1;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState2Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.REDUCE0;
            case EOF:
                return TestParser.Actions.REDUCE0;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState3Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Exp:
                return TestParser.Actions.GOTO2;
            case ID:
                return TestParser.Actions.SHIFT1;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private TestParser.Actions getState4Action(AbstractSyntaxNode lookahead) {
        switch (lookahead.type()) {
            case Plus:
                return TestParser.Actions.SHIFT3;
            case EOF:
                return TestParser.Actions.ACCEPT;
            default:
                return TestParser.Actions.ERROR;
        }
    }

    private static enum Actions {

        ERROR,
        GOTO4,
        SHIFT1,
        REDUCE1,
        REDUCE0,
        GOTO2,
        SHIFT3,
        ACCEPT;

    }

}
