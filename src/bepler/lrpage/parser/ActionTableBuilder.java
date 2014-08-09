package bepler.lrpage.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import bepler.lrpage.HashSetHashMap;
import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Rule;

public class ActionTableBuilder {
	
	private final Symbols grammar;
	private Set<String> nullables;
	private Map<String,Set<String>> firstSets;
	//private final Map<Class<? extends Symbol>,Set<Class<? extends Symbol>>> m_Follow;
	private final State start;
	private final Map<State, Map<String, Action>> actions;
	
	public ActionTableBuilder(Symbols symbols){
		grammar = symbols;
		nullables = this.computeNullables();
		firstSets = this.computeFirstSets();
		//m_Follow = this.computeFollowSets();
		Factory f = new DefaultFactory(grammar);
		start = this.computeStartState(f);
		actions = this.constructActionTable(f);
		//assign nullable and first to null to free memory
		nullables = null;
		firstSets = null;
	}
	
	public ActionTable generateActionTable(){
		return new ActionTable(actions, start);
	}
	
	private Rule getAuxilStart(){
		final String startSymbol = grammar.getStartSymbol();
		return new Rule(){

			@Override
			public String leftHandSide() {
				return "S'";
			}

			@Override
			public String[] rightHandSide() {
				return new String[]{startSymbol, grammar.getEOF()};
			}

			@Override
			public Integer getPriority() {
				return null;
			}

			@Override
			public Assoc getAssoc() {
				return Assoc.NON;
			}

			@Override
			public String getName() {
				return "S'";
			}
			
			/*
			@Override
			public int[] ignoreSymbols() {
				return new int[]{};
			}

			@Override
			public int replace() {
				return -1;
			}
			*/
		};
	}
	
	private State computeStartState(Factory f){
		Set<Item> start = new HashSet<Item>();
		start.add(f.newItem(this.getAuxilStart(), null));
		return this.closure(f, start);
	}
	
	private Map<State,Map<String,Action>> constructActionTable(Factory fac){
		Set<State> states = initializeStatesSet();
		Map<State, Map<String,Action>> actionTable = new HashMap<State, Map<String, Action>>();
		
		Queue<State> stateQ = new LinkedList<State>();
		stateQ.addAll(states);
		
		while(!stateQ.isEmpty()){
			State cur = stateQ.poll();
			
			for(Item item : cur){
				
				if(item.hasNextSymbol()){
					String next = item.nextSymbol();
					if(grammar.isEOF(next)){
						//accept action
						Action a = fac.newAcceptAction();
						if(containsKeys(actionTable, cur, next)){
							a = resolveConflict(a, get(actionTable, cur, next));
						}
						actionTable = add(actionTable, cur, next, a);
					}else{
						State descendent = gotoo(fac, cur, next);
						if(states.add(descendent)){
							stateQ.add(descendent);
						}
						Action a;
						if(grammar.isTerminal(next)){
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
					Action action = fac.newReduceAction(item.getRule());
					String lookahead = item.lookahead();
					if(lookahead != null){
						if(containsKeys(actionTable, cur, lookahead)){
							action = resolveConflict(action, get(actionTable, cur, lookahead));
						}
						add(actionTable, cur, lookahead, action);
					}else{
						for(String terminal : grammar.getTerminals()){
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
	
	private Action resolveConflict(Action a1, Action a2){
		if(a1 == null || a2 == null) return null;
		if(a1.equals(a2)) return a1;
		if(a1.id() == Actions.SHIFT && a2.id() == Actions.REDUCE){
			return resolveShiftReduce(a1, a2);
		}
		if(a1.id() == Actions.REDUCE && a2.id() == Actions.SHIFT){
			return resolveShiftReduce(a2, a1);
		}
		System.err.println("Warning: ambiguous grammar. Conflict on actions: "+a1+", "+a2);
		int a1index = grammar.indexOf(a1.production());
		int a2index = grammar.indexOf(a2.production());
		Action resolve = a1index < a2index ? a1 : a2;
		System.err.println("Resolving using action: "+resolve);
		return resolve;
		//System.err.println(a1.production());
		//System.err.println(a2.production());
		//return null;
	}
	
	private Action resolveShiftReduce(Action shift, Action reduce){
		int shiftPriority = shift.priority();
		int reducePriority = reduce.priority();
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
			System.err.println("Warning: ambiguous grammar ("+grammar+"). Shift-Reduce conflict on equal priority actions: "+shift+", "+reduce);
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
	
	private Set<State> initializeStatesSet(){
		Set<State> states = new HashSet<State>();
		states.add(start);
		return states;
	}
	
	private Set<String> first(
			List<String> symbols
			){
		
		return this.first(0, symbols);
	}
	
	private Set<String> first(
			int index,
			List<String> symbols
			){
		
		if(index >= symbols.size()){
			return new HashSet<String>();
		}
		if(!nullables.contains(symbols.get(index))){
			return firstSets.get(symbols.get(index));
		}
		Set<String> set = firstSets.get(symbols.get(index));
		set.addAll(this.first(index+1, symbols));
		return set;
	}
	
	private State gotoo(Factory fac, State state, String symbol){
		Set<Item> shifted = new HashSet<Item>();
		for(Item item : state){
			if(item.hasNextSymbol() && symbol.equals(item.nextSymbol())){
				shifted.add(fac.incrementItem(item));
			}
		}
		return this.closure(fac, shifted);
	}
	
	private State closure(
			Factory fac,
			Set<Item> items
			){
		
		Queue<Item> itemQ = new LinkedList<Item>(items);
		while(!itemQ.isEmpty()){
			Item item = itemQ.poll();
			if(item.hasNextSymbol()){
				String next = item.nextSymbol();
				List<Rule> rules = grammar.getRules(next);
				if(rules != null){
					List<String> symbols = new ArrayList<String>(Arrays.asList(item.beta()));
					symbols.add(item.lookahead());
					Set<String> firstSet = this.first(symbols);
					for(Rule r : rules){
						for(String symbol : firstSet){
							Item newItem = fac.newItem(r, symbol);
							if(items.add(newItem)){
								itemQ.add(newItem);
							}
						}
					}
				}
			}
		}
		return fac.newState(items);
	}
	
	/*
	private Map<Class<? extends Symbol>,Set<Class<? extends Symbol>>> computeFollowSets(){
		Map<Class<? extends Symbol>,Set<Class<? extends Symbol>>> followSets = new HashSetHashMap<Class<? extends Symbol>,Class<? extends Symbol>>();
		boolean changed;
		Class<? extends Symbol> lhs;
		List<Class<? extends Symbol>> rhs;
		Set<Class<? extends Symbol>> follow;
		do{
			changed = false;
			for(Rule p : m_Grammar){
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
	
	private Map<String,Set<String>> computeFirstSets(){
		
		Map<String,Set<String>> firstSets = new HashSetHashMap<String,String>();
		for(String symbol : grammar.getTerminals()){
			Set<String> set = firstSets.get(symbol);
			set.add(symbol);
			firstSets.put(symbol, set);
		}
		boolean changed;
		String lhs;
		String[] rhs;
		do{
			changed = false;
			for(Rule r : grammar.getRules()){
				lhs = r.leftHandSide();
				rhs = r.rightHandSide();
				if(rhs.length > 0){
					Set<String> first = firstSets.get(lhs);
					changed = first.addAll(firstSets.get(rhs[0])) || changed;
					for(int i=1; i<rhs.length; ++i){
						if(nullable(rhs, 0, i, nullables)){
							changed = first.addAll(firstSets.get(rhs[i])) || changed;
						}
					}
					firstSets.put(lhs, first);
				}
			}
		}while(changed);
		return firstSets;
	}
	
	private Set<String> computeNullables(){
		Set<String> nullables = new HashSet<String>();
		boolean changed;
		String lhs;
		String[] rhs;
		do{
			changed = false;
			for(Rule r : grammar.getRules()){
				lhs = r.leftHandSide();
				if(nullables.contains(lhs)){
					continue;
				}
				rhs = r.rightHandSide();
				if(nullable(rhs,nullables)){
					changed = nullables.add(lhs) || changed;
				}
			}
		}while(changed);
		return nullables;
	}
	
	private static <S> boolean nullable(S[] symbols, int start, int stop, Set<S> nullables){
		for(int i=start; i<stop; ++i){
			if(!nullables.contains(symbols[i])){
				return false;
			}
		}
		return true;
	}
	
	private static <S> boolean nullable(S[] symbols, Set<S> nullables){
		for(S symbol : symbols){
			if(!nullables.contains(symbol)){
				return false;
			}
		}
		return true;
	}
	
}
