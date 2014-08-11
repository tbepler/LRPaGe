package bepler.lrpage.code.generator.framework;

/**
 * This enum represents the current parsing status and is used
 * by the {@link ParsingEngine} to indicate whether parsing has
 * completed or if an error has occurred.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public enum Status {
	
	NOMINAL,
	COMPLETE,
	ERROR;
	
}
