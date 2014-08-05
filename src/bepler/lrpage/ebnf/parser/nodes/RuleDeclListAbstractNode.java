
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Symbols;
import bepler.lrpage.ebnf.parser.Visitor;
import bepler.lrpage.ebnf.parser.framework.Node;
import bepler.lrpage.ebnf.parser.framework.Symbol;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public abstract class RuleDeclListAbstractNode
    implements Node<Visitor>
{


    @Override
    public Symbol symbol() {
        return Symbols.RULEDECLLIST;
    }

    @Override
    public RuleDeclListAbstractNode replace() {
        return this;
    }

    @Override
    public String toString() {
        return symbol().toString();
    }

}
