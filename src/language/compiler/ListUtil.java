package language.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListUtil {
	
	public static String toString(List<?> list){
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		builder.append("[");
		for(Object o : list){
			if(first){
				first = false;
			}else{
				builder.append(", ");
			}
			builder.append(o);
		}
		builder.append("]");
		return builder.toString();
	}
	
	public static <T> List<T> asList(){
		return new ArrayList<T>();
	}
	
	public static <T> List<T> asList(T e0){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		return list;
	}
	
	public static <T> List<T> asList(T e0, T e1){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		return list;
	}
	
	public static <T> List<T> asList(T e0, T e1, T e2){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		list.add(e2);
		return list;
	}
	
	public static <T> List<T> asList(T e0, T e1, T e2, T e3){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		return list;
	}
	
	public static <T> List<T> asList(T e0, T e1, T e2, T e3, T e4){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		return list;
	}
	
	public static <T> List<T> asUnmodifiableList(T ... es){
		return Collections.unmodifiableList(Arrays.asList(es));
	}
	
	public static <T> List<T> asUnmodifiableList(){
		return Collections.unmodifiableList(new ArrayList<T>());
	}
	
	public static <T> List<T> asUnmodifiableList(T e0){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		return Collections.unmodifiableList(list);
	}
	
	public static <T> List<T> asUnmodifiableList(T e0, T e1){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		return Collections.unmodifiableList(list);
	}
	
	public static <T> List<T> asUnmodifiableList(T e0, T e1, T e2){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		list.add(e2);
		return Collections.unmodifiableList(list);
	}
	
	public static <T> List<T> asUnmodifiableList(T e0, T e1, T e2, T e3){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		return Collections.unmodifiableList(list);
	}
	
	public static <T> List<T> asUnmodifiableList(T e0, T e1, T e2, T e3, T e4){
		List<T> list = new ArrayList<T>();
		list.add(e0);
		list.add(e1);
		list.add(e2);
		list.add(e3);
		list.add(e4);
		return Collections.unmodifiableList(list);
	}
	
	
}
