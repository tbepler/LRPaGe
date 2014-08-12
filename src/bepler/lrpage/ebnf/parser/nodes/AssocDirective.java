
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class AssocDirective
    extends DirectiveAbstractNode
{

    public final AssocAbstractNode assoc0;
    public final SymbolListAbstractNode symbollist1;

    public AssocDirective(AssocAbstractNode assoc0, SymbolListAbstractNode symbollist1, TerminationToken termination2) {
        this.assoc0 = assoc0;
        this.symbollist1 = symbollist1;
    }

    @Override
    public int getLine() {
        return assoc0 .getLine();
    }

    @Override
    public int getPos() {
        return assoc0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ assoc0 .hashCode());
        hash = ((hash* 13)+ symbollist1 .hashCode());
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
        if (!(o instanceof AssocDirective)) {
            return false;
        }
        AssocDirective castResult = ((AssocDirective) o);
        if (!this.assoc0 .equals(castResult.assoc0)) {
            return false;
        }
        if (!this.symbollist1 .equals(castResult.symbollist1)) {
            return false;
        }
        return true;
    }

}
