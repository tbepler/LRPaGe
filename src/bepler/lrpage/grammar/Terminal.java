package bepler.lrpage.grammar;

public interface Terminal{

	/**
	 * The regular expression used to match this terminal symbol.
	 * @return
	 */
	public String getRegex();
	
	/**
	 * String name of the symbol represented by this terminal. A symbol
	 * of null indicates that this terminal's regex should be ignored.
	 * @return
	 */
	public String getSymbol();
	
	/**
	 * Boolean indicating whether this terminal represents punctuation.
	 * If true, then the text contained by tokens created for this 
	 * terminal will be ignored, and those tokens will be removed 
	 * from the abstract syntax tree.
	 * @return
	 */
	public boolean isPunctuation();
	
	/**
	 * The priority of this terminal symbol. Lower priorities are
	 * favored when resolving parser action conflicts.
	 * @return
	 */
	public int getPriority();
	

}
