package bepler.lrpage.ebnf.parser.framework;

/**
 * This interface describes node symbols.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public interface Symbol {
	
	public int type();
	
}
