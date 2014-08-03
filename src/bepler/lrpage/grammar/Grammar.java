package bepler.lrpage.grammar;

import java.util.List;

public interface Grammar{
	
	public List<Rule> getRules();
	
	public List<Terminal> getTokens();
	
	public int defaultPriority();
	
	public String getPseudonym(String symbol);
	
	
}
