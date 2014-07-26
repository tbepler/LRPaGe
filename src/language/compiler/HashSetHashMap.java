package language.compiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HashSetHashMap<K,V> extends HashMap<K, Set<V>> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Set<V> get(Object k){
		return containsKey(k) ? super.get(k) : new HashSet<V>();
	}
	
}
