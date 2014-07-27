package bepler.lrpage.grammar;

import java.util.List;

public interface Grammar{
	
	public List<AbstractSyntaxNode> getRules();
	
	public List<Token> getTokens();

	
	
}
