package language.compiler.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class State<S> implements Iterable<Item<S>> {
	
	private final Set<Item<S>> m_Items;
	private final int m_Hash;
	
	public State(Set<Item<S>> items){
		m_Items = new HashSet<Item<S>>(items);
		m_Hash = Arrays.hashCode(items.toArray());
	}
	
	public boolean isEmpty(){
		return m_Items.isEmpty();
	}
	
	public Set<Item<S>> asSet(){
		return Collections.unmodifiableSet(m_Items);
	}
	
	@Override
	public Iterator<Item<S>> iterator() {
		return this.asSet().iterator();
	}
	
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("{\n  ");
		boolean first = true;
		for(Item<S> item : m_Items){
			if(first){
				first = false;
			}else{
				b.append("\n  ");
			}
			b.append(item);
		}
		b.append("\n}");
		return b.toString();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof State){
			State<?> s = (State<?>) o;
			if(m_Items.size() == s.m_Items.size()){
				for(Object item : s.m_Items){
					if(!m_Items.contains(item)){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
