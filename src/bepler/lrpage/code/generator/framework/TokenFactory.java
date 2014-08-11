
package bepler.lrpage.code.generator.framework;

import java.util.regex.Pattern;

/**
 * This interface defines classes used by the {@link Lexer} to read
 * a stream of {@link Token}s from an input stream.
 * 
 * <P>
 * 
 * Part of the LRPaGe parser generator. Available at https://github.com/tbepler/LRPaGe
 * 
 * @author Tristan Bepler
 *
 * @param <V> Visitor on which AST nodes are generified.
 */
public interface TokenFactory<V> {


    public int size() ;

    /**
     * Null is returned if the given index is an ignored token.
     * @param index
     * @return
     */
    public Token<V> build(int index) ;

    /**
     * Null is returned if the given index is an ignored token.
     * @param index
     * @param text
     * @param line
     * @param pos
     * @return
     */
    public Token<V> build(int index, String text, int line, int pos) ;
    
    public Token<V> getEOFToken(int line, int pos);
    
    public Pattern[] getPatterns();

}
