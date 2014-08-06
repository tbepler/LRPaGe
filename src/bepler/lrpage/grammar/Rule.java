package bepler.lrpage.grammar;

public interface Rule {
	
	/**
	 * A string representing the symbol of the left
	 * hand side of this rule.
	 * @return
	 */
	public String leftHandSide();
	
	/**
	 * A string array indicating the symbols of the
	 * right hand side of this rule.
	 * @return
	 */
	public String[] rightHandSide();
	
	/**
	 * By default a rule should use the priority of its
	 * right most terminal symbol, indicated by a value of null.
	 * Otherwise, the specified priority is used for this rule.
	 * @return
	 */
	public Integer getPriority();
	
	/**
	 * The associativity of this rule.
	 * @return
	 */
	public Assoc getAssoc();
	
	/**
	 * The name of this rule. This will be used to name
	 * the abstract syntax tree class created for this 
	 * rule.
	 * @return
	 */
	public String getName();
	
	/**
	 * An int array indicating the indices of right hand side
	 * symbols of this rule that should be ignored when creating
	 * an abstract syntax tree node for this rule. The node class
	 * will contain fields for all right hand side symbols that are
	 * not ignored.
	 * @return
	 */
	//public int[] ignoreSymbols();
	
	
	/**
	 * An int indicating the index of a right hand side symbol
	 * for this rule that should be used to replace this rule when
	 * constructing the abstract syntax tree. This indicates that
	 * no node should be created for this rule, and that the specified
	 * right hand side symbol will replace it. A value less than 0
	 * indicates that no replacement should occur.
	 * @return
	 */
	//public int replace();

}
