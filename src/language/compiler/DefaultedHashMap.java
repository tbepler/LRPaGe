package language.compiler;

import java.util.HashMap;
import java.util.Map;

public class DefaultedHashMap<K,V> extends HashMap<K,V> {
	private static final long serialVersionUID = 1L;
	
	private V m_Default = null;
	
	public DefaultedHashMap(V defaultValue){
		super();
		m_Default = defaultValue;
	}

	public DefaultedHashMap() {
		super();
		m_Default = null;
	}

	public DefaultedHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public DefaultedHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	public DefaultedHashMap(Map<? extends K, ? extends V> m) {
		super(m);
	}
	
	public V getDefault(){
		return m_Default;
	}
	
	public void setDefault(V defaultValue){
		m_Default = defaultValue;
	}
	
	@Override
	public V get(Object key){
		return containsKey(key) ? super.get(key) : m_Default;
	}
	
	
}
