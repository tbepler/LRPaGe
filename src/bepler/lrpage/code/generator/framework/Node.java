
package bepler.lrpage.code.generator.framework;


public interface Node<V> {
	
	public void accept(V visitor);

    public Symbol symbol();

    public Node<V> replace();
    
    public int getLine();
    	
    public int getPos();

}
