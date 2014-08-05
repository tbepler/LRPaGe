
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
public class EndGroupingToken
    extends Token<Visitor>
{


    public EndGroupingToken(String text, int line, int pos) {
        super(text, line, pos);
    }

    public EndGroupingToken() {
        super();
    }

    @Override
    public void accept(Visitor visitor) {
        //do nothing
    }

    @Override
    public Symbol symbol() {
        return Symbols.ENDGROUPING;
    }

    @Override
    public String toString() {
        return symbol().toString();
    }

}
