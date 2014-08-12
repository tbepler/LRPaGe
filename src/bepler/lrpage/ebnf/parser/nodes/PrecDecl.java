
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class PrecDecl
    extends PrecDeclAbstractNode
{

    public final SymbolAbstractNode symbol0;

    public PrecDecl(SpecialToken special0, PrecKeywordToken preckeyword1, SymbolAbstractNode symbol2) {
        this.symbol0 = symbol2;
    }

    @Override
    public int getLine() {
        return symbol0 .getLine();
    }

    @Override
    public int getPos() {
        return symbol0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ symbol0 .hashCode());
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
        if (!(o instanceof PrecDecl)) {
            return false;
        }
        PrecDecl castResult = ((PrecDecl) o);
        if (!this.symbol0 .equals(castResult.symbol0)) {
            return false;
        }
        return true;
    }

}
