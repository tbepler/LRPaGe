
package bepler.lrpage.code.generator.framework;


public interface Node<V> {
	
	public void accept(V visitor);

    public int type();

    public Node<V> replace();
    
    public int getLine();
    	
    public int getPos();

}
