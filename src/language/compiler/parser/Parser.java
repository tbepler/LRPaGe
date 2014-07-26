package language.compiler.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;
import language.compiler.lexer.Lexer;

public class Parser<V> {
	
	private final Map<State<V>, Map<Class<? extends Token<V>>, Action<V>>> m_Actions;
	private final State<V> m_Start;
	
	public Parser(Map<State<V>, Map<Class<? extends Token<V>>, Action<V>>> actionTable, State<V> startState){
		m_Actions = actionTable;
		m_Start = startState;
	}
	
	public Token<V> parse(List<Token<V>> tokens){
		Deque<State<V>> states = new LinkedList<State<V>>();
		states.push(m_Start);
		Deque<Token<V>> symbols = new LinkedList<Token<V>>();
		Deque<Token<V>> remaining = new LinkedList<Token<V>>(tokens);
		while(true){
			State<V> state = states.peek();
			Token<V> lookahead = remaining.peek();
			Action<V> action = this.getAction(state, lookahead);
			if(action == null){
				//ERROR //TODO
				throw new RuntimeException("Error on token: "+lookahead);
			}
			switch(action.id()){
			case ACCEPT:
				return symbols.peek();
			case GOTO:
				symbols.push(lookahead);
				remaining.pop();
				states.push(action.nextState());
				break;
			case REDUCE:
				remaining.push(this.reduce(symbols, states, action.production()));
				break;
			case SHIFT:
				symbols.push(lookahead);
				remaining.pop();
				states.push(action.nextState());
				break;
			default:
				throw new RuntimeException("Unknown action: "+action.id());
			}
		}
	}
	
	public Token<V> parse(Lexer<V> lexer){
		Deque<State<V>> states = new LinkedList<State<V>>();
		states.push(m_Start);
		Deque<Token<V>> symbols = new LinkedList<Token<V>>();
		Deque<Token<V>> lookaheadStack = new LinkedList<Token<V>>();
		lookaheadStack.add(lexer.nextToken());
		while(true){
			Token<V> lookahead = lookaheadStack.peek();
			State<V> state = states.peek();
			Action<V> action = this.getAction(state, lookahead);
			System.out.println("Lookahead: "+lookahead);
			//System.out.println("Stack: "+stackToString(symbols));
			System.out.println("Action: "+action);
			//System.out.println("State: "+state);
			if(action == null){
				//ERROR //TODO
				throw new RuntimeException("Error on token: " + lookahead +
						"\nStack: "+stackToString(symbols) +
						"\nState: "+state);
			}
			switch(action.id()){
			case ACCEPT:
				return symbols.peek();
			case GOTO:
				symbols.push(lookahead);
				lookaheadStack.pop();
				states.push(action.nextState());
				break;
			case REDUCE:
				lookaheadStack.push(this.reduce(symbols, states, action.production()));
				break;
			case SHIFT:
				symbols.push(lookahead);
				lookaheadStack.pop();
				if(!lexer.hasNext() && lookaheadStack.isEmpty()){
					throw new RuntimeException("All symbols exhausted and no accept action reached.");
				}
				lookaheadStack.add(lexer.nextToken());
				states.push(action.nextState());
				break;
			default:
				throw new RuntimeException("Unknown action: "+action.id());
			}
		}
	}
	
	private static <V> String stackToString(Iterable<Token<V>> stack){
		StringBuilder b = new StringBuilder();
		b.append("[");
		boolean first = true;
		for(Token<?> s : stack){
			if(first){
				first = false;
			}else{
				b.append(", ");
			}
			b.append(s);
		}
		b.append("]");
		return b.toString();
	}
	
	
	private Token<V> reduce(Deque<Token<V>> stack, Deque<State<V>> states, Production<V> rule){
		List<Token<V>> lhs = new ArrayList<Token<V>>();
		int len = rule.rightHandSide().size();
		for(int i=0; i<len; ++i){
			lhs.add(0, stack.pop());
			states.pop();
		}
		return rule.reduce(lhs);
	}
	
	public Action<V> getAction(State<V> state, Token<V> symbol){
		return this.getAction(state, symbol.getSymbolType());
	}
	
	public Action<V> getAction(State<V> state, Class<? extends Token<V>> symbolType){
		if(m_Actions.containsKey(state)){
			return m_Actions.get(state).get(symbolType);
		}
		return null;
	}
	
	public Collection<State<V>> getStates(){
		return Collections.unmodifiableSet(m_Actions.keySet());
	}


	
	
	
	
	
	
	
	
	
	
	
	
}
