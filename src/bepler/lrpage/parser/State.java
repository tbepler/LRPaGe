package bepler.lrpage.parser;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class State implements Iterable<Item> {
	
	private final Set<Item> items;
	private final int hash;
	
	public State(Set<Item> items){
		this.items = new HashSet<Item>(items);
		hash = Arrays.hashCode(items.toArray());
	}
	
	public boolean isEmpty(){
		return items.isEmpty();
	}
	
	public Set<Item> asSet(){
		return Collections.unmodifiableSet(items);
	}
	
	@Override
	public Iterator<Item> iterator() {
		return Collections.unmodifiableSet(items).iterator();
	}
	
	@Override
	public String toString(){
		StringBuilder b = new StringBuilder();
		b.append("{\n  ");
		boolean first = true;
		for(Item item : items){
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
		return hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof State){
			State that = (State) o;
			if(items.size() == that.items.size()){
				for(Object item : that.items){
					if(!items.contains(item)){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}
