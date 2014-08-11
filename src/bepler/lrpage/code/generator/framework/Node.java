
package bepler.lrpage.code.generator.framework;

/**
 * Interface describing an abstract syntax tree (AST) node.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public interface Node<V> {
	
	public void accept(V visitor);

    public Symbol symbol();

    public Node<V> replace();
    
    public int getLine();
    	
    public int getPos();

}
