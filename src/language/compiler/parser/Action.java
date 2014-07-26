package language.compiler.parser;

import java.util.Arrays;

import language.compiler.grammar.Production;
import language.compiler.grammar.Token;

public class Action<S> {
	
	public static <S> Action<S> newAcceptAction(){
		return new Action<S>(Actions.ACCEPT);
	}
	
	public static <S> Action<S> newReduceAction(Production<S> prod){
		return new Action<S>(Actions.REDUCE, prod);
	}
	
	public static <S> Action<S> newShiftAction(State<S> next, Class<? extends Token<S>> symbol){
		return new Action<S>(Actions.SHIFT, next, symbol);
	}
	
	public static <S> Action<S> newGotoAction(State<S> next){
		return new Action<S>(Actions.GOTO, next);
	}
	
	private final State<S> m_Next;
	private final Production<S> m_Prod;
	private final Class<? extends Token<S>> m_Shift;
	private final Actions m_Id;
	private final int m_Hash;
	
	private Action(Actions id){
		m_Id = id;
		m_Next = null;
		m_Prod = null;
		m_Shift = null;
		m_Hash = this.computeHash();
	}
	
	private Action(Actions id, State<S> next){
		m_Id = id;
		m_Next = next;
		m_Prod = null;
		m_Shift = null;
		m_Hash = this.computeHash();
	}
	
	private Action(Actions id, State<S> next, Class<? extends Token<S>> symbolType){
		m_Id = id;
		m_Next = next;
		m_Prod = null;
		m_Shift = symbolType;
		m_Hash = this.computeHash();
	}
	
	private Action(Actions id, Production<S> prod){
		m_Id = id;
		m_Next = null;
		m_Prod = prod;
		m_Shift = null;
		m_Hash = this.computeHash();
	}
	
	private int computeHash(){
		return Arrays.hashCode(new Object[]{m_Id, m_Next, m_Prod, m_Shift});
	}
	
	public Actions id(){
		return m_Id;
	}
	
	public State<S> nextState(){
		return m_Next;
	}
	
	public Class<? extends Token<S>> shiftToken(){
		return m_Shift;
	}
	
	public Production<S> production(){
		return m_Prod;
	}
	
	@Override
	public String toString(){
		return m_Id.toString();
	}
	
	@Override
	public int hashCode(){
		return m_Hash;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Action){
			Action<?> a = (Action<?>) o;
			if(m_Next == null){
				return m_Id.equals(a.m_Id) && m_Prod.equals(a.m_Prod);
			}else if(m_Shift == null){
				return m_Id.equals(a.m_Id) && m_Next.equals(a.m_Next);
			}else{
				return m_Id.equals(a.m_Id) && m_Next.equals(a.m_Next) && m_Shift.equals(a.m_Shift);
			}
		}
		return false;
	}
	
}
