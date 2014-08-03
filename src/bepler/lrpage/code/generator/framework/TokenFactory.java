
package bepler.lrpage.code.generator.framework;

import java.util.regex.Pattern;

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
    
    public Token<V> getEOFToken();
    
    public Pattern[] getPatterns();

}
