package language.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import language.compiler.HashSetHashMap;
import language.compiler.ListUtil;
import language.compiler.grammar.Assoc;
import language.compiler.grammar.Grammar;
import language.compiler.grammar.Production;
import language.compiler.grammar.Token;

public class ParserBuilder<V> {
	
	private final Grammar<V> m_Grammar;
	private Set<Class<? extends Token<V>>> m_Nullable;
	private Map<Class<? extends Token<V>>,Set<Class<? extends Token<V>>>> m_First;
	//private final Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> m_Follow;
	private final State<V> m_Start;
	private final Map<State<V>, Map<Class<? extends Token<V>>, Action<V>>> m_Actions;
	
	public ParserBuilder(Grammar<V> g){
		m_Grammar = g;
		m_Nullable = this.computeNullables();
		m_First = this.computeFirstSets();
		//m_Follow = this.computeFollowSets();
		Factory f = new DefaultFactory();
		m_Start = this.computeStartState(f);
		m_Actions = this.constructActionTable(f);
		//assign nullable and first to null to free memory
		m_Nullable = null;
		m_First = null;
	}
	
	public Parser<V> newParser(){
		return new Parser<V>(m_Actions, m_Start);
	}
	
	private State<V> computeStartState(Factory f){
		Set<Item<V>> start = new HashSet<Item<V>>();
		start.add(f.newItem(m_Grammar.getStartProduction(), new ArrayList<Class<? extends Token<V>>>()));
		return this.closure(f, start);
	}
	
	private Map<State<V>,Map<Class<? extends Token<V>>,Action<V>>> constructActionTable(Factory fac){
		Set<State<V>> states = initializeStatesSet();
		Map<State<V>, Map<Class<? extends Token<V>>,Action<V>>> actionTable = new HashMap<State<V>, Map<Class<? extends Token<V>>, Action<V>>>();
		
		Queue<State<V>> stateQ = new LinkedList<State<V>>();
		stateQ.addAll(states);
		
		while(!stateQ.isEmpty()){
			State<V> cur = stateQ.poll();
			
			for(Item<V> item : cur){
				
				if(item.hasNext()){
					Class<? extends Token<V>> next = item.next();
					if(m_Grammar.isEOFSymbol(next)){
						//accept action
						Action<V> a = fac.newAcceptAction();
						if(containsKeys(actionTable, cur, next)){
							a = resolveConflict(a, get(actionTable, cur, next));
						}
						actionTable = add(actionTable, cur, next, a);
					}else{
						State<V> descendent = gotoo(fac, cur, next);
						if(states.add(descendent)){
							stateQ.add(descendent);
						}
						Action<V> a;
						if(m_Grammar.isTerminal(next)){
							a = fac.newShiftAction(descendent, next);
						}else{
							a = fac.newGotoAction(descendent);
						}
						if(containsKeys(actionTable, cur, next)){
							a = resolveConflict(a, get(actionTable, cur, next));
						}
						actionTable = add(actionTable, cur, next, a);
					}
				}else{
					//reduce action
					Action<V> action = fac.newReduceAction(item.getProduction());
					if(item.lookaheadLength() > 0){
						Class<? extends Token<V>> lookahead = item.lookahead(0);
						if(containsKeys(actionTable, cur, lookahead)){
							action = resolveConflict(action, get(actionTable, cur, lookahead));
						}
						if(action == null){
							System.err.println(lookahead);
							System.err.println(cur);
						}
						add(actionTable, cur, lookahead, action);
					}else{
						for(Class<? extends Token<V>> terminal : m_Grammar.getTerminalSymbolTypes()){
							if(containsKeys(actionTable, cur, terminal)){
								action = resolveConflict(action, get(actionTable, cur, terminal));
							}
							add(actionTable, cur, terminal, action);
						}
						add(actionTable, cur, null, action);
					}
				}
			}
		}
		return actionTable;
	}
	
	private Action<V> resolveConflict(Action<V> a1, Action<V> a2){
		if(a1 == null || a2 == null) return null;
		if(a1.equals(a2)) return a1;
		if(a1.id() == Actions.SHIFT && a2.id() == Actions.REDUCE){
			return resolveShiftReduce(a1, a2);
		}
		if(a1.id() == Actions.REDUCE && a2.id() == Actions.SHIFT){
			return resolveShiftReduce(a2, a1);
		}
		System.err.println("Warning: ambiguous grammar ("+m_Grammar+"). Conflict on actions: "+a1+", "+a2);
		System.err.println(a1.production());
		System.err.println(a2.production());
		return null;
	}
	
	private Action<V> resolveShiftReduce(Action<V> shift, Action<V> reduce){
		int shiftPriority = m_Grammar.getPrecedence(shift.shiftToken());
		int reducePriority = reduce.production().getPriority();
		if(shiftPriority < reducePriority){
			//resolve in favor of shifting
			return shift;
		}
		if(reducePriority < shiftPriority){
			//resolve in favor of reducing
			return reduce;
		}
		//priorities are equal, so resolve according to the reduce action's
		//associativity
		Assoc a = reduce.production().getAssoc();
		switch(a){
		case LEFT:
			//resolve in favor of reducing
			return reduce;
		case RIGHT:
			//resolve in favor of shifting
			return shift;
		default:
			//rule is non associative, this is an error state
			System.err.println("Warning: ambiguous grammar ("+m_Grammar+"). Shift-Reduce conflict on equal priority actions: "+shift+", "+reduce);
			return null;
		
		}
	}
	
	private static <K1,K2,V> boolean containsKeys(Map<K1,Map<K2,V>> table, K1 key1, K2 key2){
		if(table.containsKey(key1)){
			return table.get(key1).containsKey(key2);
		}
		return false;
	}
	
	private static <K1,K2,V> V get(Map<K1,Map<K2,V>> table, K1 key1, K2 key2){
		return table.get(key1).get(key2);
	}
	
	private static <K1,K2,V> Map<K1,Map<K2,V>> add(Map<K1,Map<K2,V>> table, K1 key1, K2 key2, V value){
		//System.out.println("Adding: "+value);
		//System.out.println("Key1: "+key1);
		//System.out.println("Key2: "+key2);
		if(table.containsKey(key1)){
			table.get(key1).put(key2, value);
		}else{
			Map<K2,V> map = new HashMap<K2,V>();
			map.put(key2, value);
			table.put(key1, map);
		}
		return table;
	}
	
	private Set<State<V>> initializeStatesSet(){
		Set<State<V>> states = new HashSet<State<V>>();
		states.add(m_Start);
		return states;
	}
	
	private Set<Class<? extends Token<V>>> first(
			List<Class<? extends Token<V>>> symbols
			){
		
		return this.first(0, symbols);
	}
	
	private Set<Class<? extends Token<V>>> first(
			int index,
			List<Class<? extends Token<V>>> symbols
			){
		
		if(index >= symbols.size()){
			return new HashSet<Class<? extends Token<V>>>();
		}
		if(!m_Nullable.contains(symbols.get(index))){
			return m_First.get(symbols.get(index));
		}
		Set<Class<? extends Token<V>>> set = m_First.get(symbols.get(index));
		set.addAll(this.first(index+1, symbols));
		return set;
	}
	
	private State<V> gotoo(Factory fac, State<V> state, Class<? extends Token<V>> symbol){
		Set<Item<V>> shifted = new HashSet<Item<V>>();
		for(Item<V> item : state){
			if(item.hasNext() && symbol.equals(item.next())){
				shifted.add(fac.incrementItem(item));
			}
		}
		return this.closure(fac, shifted);
	}
	
	private State<V> closure(
			Factory fac,
			Set<Item<V>> items
			){
		
		Queue<Item<V>> itemQ = new LinkedList<Item<V>>(items);
		while(!itemQ.isEmpty()){
			Item<V> item = itemQ.poll();
			if(item.hasNext()){
				Class<? extends Token<V>> next = item.next();
				List<Class<? extends Token<V>>> symbols = item.beta();
				symbols.addAll(item.lookahead());
				Set<Class<? extends Token<V>>> firstSet = this.first(symbols);
				for(Production<V> p : m_Grammar.getProductions(next)){
					for(Class<? extends Token<V>> symbol : firstSet){
						Item<V> newItem = fac.newItem(p, ListUtil.<Class<? extends Token<V>>>asList(symbol));
						if(items.add(newItem)){
							itemQ.add(newItem);
						}
					}
				}
			}
		}
		return fac.newState(items);
	}
	
	/*
	private Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> computeFollowSets(){
		Map<Class<? extends Symbol<V>>,Set<Class<? extends Symbol<V>>>> followSets = new HashSetHashMap<Class<? extends Symbol<V>>,Class<? extends Symbol<V>>>();
		boolean changed;
		Class<? extends Symbol<V>> lhs;
		List<Class<? extends Symbol<V>>> rhs;
		Set<Class<? extends Symbol<V>>> follow;
		do{
			changed = false;
			for(Production<V> p : m_Grammar){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				for(int i=0; i<rhs.size()-1; ++i){
					if(!m_Grammar.isTerminal(rhs.get(i))){
						follow = followSets.get(rhs.get(i));
						for(int j=i+1; j<rhs.size(); ++j){
							changed = follow.addAll(m_First.get(rhs.get(j))) || changed;
						}
						followSets.put(rhs.get(i), follow);
					}
				}
				for(int i=rhs.size()-1; i>=0; --i){
					if(!m_Grammar.isTerminal(rhs.get(i)) && nullable(rhs, i+1, rhs.size(), m_Nullable)){
						follow = followSets.get(rhs.get(i));
						changed = follow.addAll(followSets.get(lhs)) || changed;
						followSets.put(rhs.get(i), follow);
					}
				}
			}
		}while(changed);
		return followSets;
	}
	*/
	
	private Map<Class<? extends Token<V>>,Set<Class<? extends Token<V>>>> computeFirstSets(){
		
		Map<Class<? extends Token<V>>,Set<Class<? extends Token<V>>>> firstSets = new HashSetHashMap<Class<? extends Token<V>>,Class<? extends Token<V>>>();
		for(Class<? extends Token<V>> terminal : m_Grammar.getTerminalSymbolTypes()){
			Set<Class<? extends Token<V>>> set = firstSets.get(terminal);
			set.add(terminal);
			firstSets.put(terminal, set);
		}
		boolean changed;
		Class<? extends Token<V>> lhs;
		List<Class<? extends Token<V>>> rhs;
		do{
			changed = false;
			for(Production<V> p : m_Grammar){
				lhs = p.leftHandSide();
				rhs = p.rightHandSide();
				if(rhs.size() > 0){
					Set<Class<? extends Token<V>>> first = firstSets.get(lhs);
					changed = first.addAll(firstSets.get(rhs.get(0))) || changed;
					for(int i=1; i<rhs.size(); ++i){
						if(nullable(rhs, 0, i, m_Nullable)){
							changed = first.addAll(firstSets.get(rhs.get(i))) || changed;
						}
					}
					firstSets.put(lhs, first);
				}
			}
		}while(changed);
		return firstSets;
	}
	
	private Set<Class<? extends Token<V>>> computeNullables(){
		Set<Class<? extends Token<V>>> nullables = new HashSet<Class<? extends Token<V>>>();
		boolean changed;
		Class<? extends Token<V>> lhs;
		List<Class<? extends Token<V>>> rhs;
		do{
			changed = false;
			for(Production<V> p : m_Grammar){
				lhs = p.leftHandSide();
				if(nullables.contains(lhs)){
					continue;
				}
				rhs = p.rightHandSide();
				if(nullable(rhs,nullables)){
					changed = nullables.add(lhs) || changed;
				}
			}
		}while(changed);
		return nullables;
	}
	
	private static <S> boolean nullable(List<S> symbols, int start, int stop, Set<S> nullables){
		for(int i=start; i<stop; ++i){
			if(!nullables.contains(symbols.get(i))){
				return false;
			}
		}
		return true;
	}
	
	private static <S> boolean nullable(List<S> symbols, Set<S> nullables){
		for(S symbol : symbols){
			if(!nullables.contains(symbol)){
				return false;
			}
		}
		return true;
	}
	
}
