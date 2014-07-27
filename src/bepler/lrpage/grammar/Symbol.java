package bepler.lrpage.grammar;

public interface Symbol {
	
	public Class<? extends Symbol> getSymbol();
	public int getPriority();
	
}
