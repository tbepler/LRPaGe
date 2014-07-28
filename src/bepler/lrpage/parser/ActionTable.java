package bepler.lrpage.parser;

import java.util.*;

public class ActionTable {
	
	private final Map<State, Map<String, Action>> table;
	private final State start;
	
	public ActionTable(Map<State, Map<String, Action>> table, State start){
		this.table = table;
		this.start = start;
	}
	
	public State getStartState(){
		return start;
	}
	
	public Set<State> getStates(){
		return Collections.unmodifiableSet(table.keySet());
	}
	
	/**
	 * MAY RETURN NULL
	 * @param s
	 * @param lookahead
	 * @return
	 */
	public Action getAction(State s, String lookahead){
		Map<String, Action> map = table.get(s);
		if(map != null){
			return map.get(lookahead);
		}
		return null;
	}
	
	
	
	

}
