package bepler.lrpage.ebnf.parser.framework;

/**
 * This class defines terminal AST nodes, which are constructed by
 * the {@link TokenFactory} and read as a stream from the {@link Lexer}.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public abstract class Token<V> implements Node<V> {
	
	private final int line;
	private final int pos;
	private final String text;
	
	protected Token(String text, int line, int pos){
		this.text = text;
		this.line = line;
		this.pos = pos;
	}
	
	protected Token(){
		this.text = null;
		this.line = 0;
		this.pos = 0;
	}

	@Override
	public Node<V> replace() {
		return this;
	}
	
	public String getText(){
		return text;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getPos() {
		return pos;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		if(getClass() != o.getClass()) return false;
		@SuppressWarnings("unchecked")
		Token<?> that = (Token<V>) o;
		if(text == null){
			return that.text == null;
		}
		return text.equals(that.text);
	}
	
	@Override
	public int hashCode(){
		int hash = 5;
		hash = hash*37 + getClass().hashCode();
		hash = hash*37 + (text == null ? 0 : text.hashCode());
		return hash;
	}

}
