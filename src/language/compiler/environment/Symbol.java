package language.compiler.environment;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

public class Symbol {
	
	private static final Map<String, Symbol> CACHE = new HashMap<String, Symbol>();
	
	private final String name;
	private Symbol(String s){ name = s; }
	
	public static Symbol asSymbol(String s){
		String in = s.intern();
		Symbol sym;
		synchronized(CACHE){
			sym = CACHE.get(in);
			if(sym == null){
				sym = new Symbol(in);
				CACHE.put(in, sym);
			}
		}
		return sym;
	}
	
	//enforces flyweight pattern on deserialization
	private Object readResolve() throws ObjectStreamException{
		return asSymbol(name);
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Symbol){
			Symbol that = (Symbol) o;
			return equals(this.name, that.name);
		}
		return false;
	}
	
	public static boolean equals(Object o1, Object o2){
		if(o1 == o2) return true;
		if(o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}
	
	
	
}
