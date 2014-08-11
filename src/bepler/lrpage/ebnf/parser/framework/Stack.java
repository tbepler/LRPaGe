package bepler.lrpage.ebnf.parser.framework;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class represents a parse stack, containing both the current
 * {@link Node}s and states.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public class Stack<V> {
	
	private final Deque<Node<V>> nodeStack;
    private final Deque<Integer> stateStack;

    public Stack() {
        nodeStack = new ArrayDeque<Node<V>>();
        stateStack = new ArrayDeque<Integer>();
    }
    
    //clone constructor
    private Stack(Deque<Node<V>> nodes, Deque<Integer> states) {
        nodeStack = new ArrayDeque<Node<V>>(nodes);
        stateStack = new ArrayDeque<Integer>(states);
    }

    public int curState() {
        if (stateStack.isEmpty()) {
            return  0;
        }
        return stateStack.peek();
    }

    public Node<V> pop() {
        stateStack.pop();
        return nodeStack.pop();
    }

    public void push(Node<V> node, int state) {
        nodeStack.push(node);
        stateStack.push(state);
    }

    @Override
    public Stack<V> clone() {
        return new Stack<V>(nodeStack, stateStack);
    }
	
}
