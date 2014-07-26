package language.compiler.lexer;

public class Token<T> {
	
	private String m_Str;
	private T m_Id;
	
	public Token(String s, T id){
		m_Str = s;
		m_Id = id;
	}
	
	public String text(){
		return m_Str;
	}
	
	public T id(){
		return m_Id;
	}
	
	@Override
	public String toString(){
		return m_Id.toString() + "("+m_Str+")";
	}
	
}
