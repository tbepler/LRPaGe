package language.compiler.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import language.compiler.grammar.Production;
import language.compiler.grammar.Token;

public class Item<V> {
	
	private final Production<V> m_Prod;
	private final List<Class<? extends Token<V>>> m_RHS;
	private final Class<? extends Token<V>> m_LHS;
	private final List<Class<? extends Token<V>>> m_Lookahead;
	private final int m_Index;
	private final int m_Hash;
	
	public Item(Production<V> prod, List<Class<? extends Token<V>>> lookahead){
		this(0, prod, lookahead);
	}
	
	public Item(int index, Production<V> prod, List<Class<? extends Token<V>>> lookahead){
		this(index, lookahead, prod, prod.leftHandSide(), prod.rightHandSide());
	}
	
	protected Item(int index, List<Class<? extends Token<V>>> lookahead, Production<V> prod, Class<? extends Token<V>> lhs, List<Class<? extends Token<V>>> rhs){
		m_Index = index;
		m_Lookahead = new ArrayList<Class<? extends Token<V>>>(lookahead);
		m_Prod = prod;
		m_RHS = rhs;
		m_LHS = lhs;
		m_Hash = Arrays.deepHashCode(new Object[]{m_Index, m_Prod, m_Lookahead.toArray()});
	}
	
	public Production<V> getProduction(){
		return m_Prod;
	}
	
	public Item<V> increment(){
		return new Item<V>(m_Index+1, m_Lookahead, m_Prod, m_LHS, m_RHS);
	}
	
	public List<Class<? extends Token<V>>> alpha(){
		List<Class<? extends Token<V>>> alpha = new ArrayList<Class<? extends Token<V>>>();
		for(int i=0; i<m_Index; ++i){
			alpha.add(m_RHS.get(i));
		}
		return alpha;
	}
	
	public List<Class<? extends Token<V>>> beta(){
		List<Class<? extends Token<V>>> beta = new ArrayList<Class<? extends Token<V>>>();
		for(int i=m_Index+1; i<m_RHS.size(); ++i){
			beta.add(m_RHS.get(i));
		}
		return beta;
	}
	
	public boolean hasNext(){
		return m_Index < m_RHS.size();
	}
	
	public Class<? extends Token<V>> next(){
		return m_RHS.get(m_Index);
	}
	
	public boolean hasPrev(){
		return m_Index > 0;
	}
	
	public Class<? extends Token<V>> prev(){
		return m_RHS.get(m_Index-1);
	}
	
	public int lookaheadLength(){
		return m_Lookahead.size();
	}
	
	public Class<? extends Token<V>> lookahead(int index){
		return m_Lookahead.get(index);
	}
	
	public List<Class<? extends Token<V>>> lookahead(){
		return Collections.unmodifiableList(m_Lookahead);
	}
	
	@Override
	public String toString(){
		return m_LHS.getSimpleName() + " -> (" + this.rhsToString() + "), "+this.lookaheadToString();
	}
	
	private String rhsToString(){
		StringBuilder builder = new StringBuilder();
		for( int i = 0; i < m_RHS.size() ; ++i ){
			if(i != 0){
				builder.append(" ");
			}
			if(i == m_Index){
				builder.append(". ");
			}
			builder.append(m_RHS.get(i).getSimpleName());
		}
		if(m_Index == m_RHS.size()){
			builder.append(" .");
		}
		return builder.toString();
	}
	
	private String lookaheadToString(){
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		builder.append("[");
		for(Class<? extends Token<V>> c : m_Lookahead){
			if(first){
				first = false;
			}else{
				builder.append(", ");
			}
			builder.append(c.getSimpleName());
		}
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Item){
			Item<?> i = (Item<?>) o;
			return m_Index == i.m_Index && m_Prod.equals(i.m_Prod) && listEquals(m_Lookahead, i.m_Lookahead);
		}
		return false;
	}
	
	private static boolean listEquals(List<?> l1, List<?> l2){
		if(l1.size() == l2.size()){
			for(int i=0; i<l1.size(); ++i){
				if(!l1.get(i).equals(l2.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
}
