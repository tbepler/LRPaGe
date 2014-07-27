package bepler.lrpage.grammar;

public abstract class AbstractSyntaxNode implements Symbol{
	
	public abstract Assoc getAssoc();
	
	public AbstractSyntaxNode replace(){
		return this;
	}
	
}
