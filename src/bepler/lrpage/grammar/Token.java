package bepler.lrpage.grammar;

public abstract class Token implements Symbol{

	public abstract String getRegex();
	
	@Override
	public Class<? extends Symbol> getSymbol() {
		return this.getClass();
	}


}
