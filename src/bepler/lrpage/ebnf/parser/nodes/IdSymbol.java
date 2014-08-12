
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class IdSymbol
    extends SymbolAbstractNode
{

    public final IdentifierToken identifier0;

    public IdSymbol(IdentifierToken identifier0) {
        this.identifier0 = identifier0;
    }

    @Override
    public int getLine() {
        return identifier0 .getLine();
    }

    @Override
    public int getPos() {
        return identifier0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ identifier0 .hashCode());
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
        if (!(o instanceof IdSymbol)) {
            return false;
        }
        IdSymbol castResult = ((IdSymbol) o);
        if (!this.identifier0 .equals(castResult.identifier0)) {
            return false;
        }
        return true;
    }

}
