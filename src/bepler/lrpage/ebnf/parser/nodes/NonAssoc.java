
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class NonAssoc
    extends AssocAbstractNode
{


    public NonAssoc(NonKeywordToken nonkeyword0) {
    }

    @Override
    public int getLine() {
        return getLine();
    }

    @Override
    public int getPos() {
        return getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ getClass().hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o) {
            return false;
        }
        if (!(o instanceof NonAssoc)) {
            return false;
        }
        NonAssoc castResult = ((NonAssoc) o);
        return true;
    }

}
