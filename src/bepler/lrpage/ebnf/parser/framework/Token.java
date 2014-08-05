package bepler.lrpage.ebnf.parser.framework;

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

}
