
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Symbols;
import bepler.lrpage.ebnf.parser.Visitor;
import bepler.lrpage.ebnf.parser.framework.Symbol;
import bepler.lrpage.ebnf.parser.framework.Token;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class EOFToken
    extends Token<Visitor>
{


    public EOFToken(int line, int pos) {
        super("", line, pos);
    }

    @Override
    public void accept(Visitor visitor) {
        //do nothing
    }

    @Override
    public Symbol symbol() {
        return Symbols.EOF;
    }

    @Override
    public String toString() {
        return symbol().toString();
    }

}
