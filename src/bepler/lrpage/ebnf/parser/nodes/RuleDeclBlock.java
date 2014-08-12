
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class RuleDeclBlock
    extends BlockAbstractNode
{

    public final RuleDeclListAbstractNode ruledecllist0;

    public RuleDeclBlock(SpecialToken special0, RulesKeywordToken ruleskeyword1, StartBlockToken startblock2, RuleDeclListAbstractNode ruledecllist3) {
        this.ruledecllist0 = ruledecllist3;
    }

    @Override
    public int getLine() {
        return ruledecllist0 .getLine();
    }

    @Override
    public int getPos() {
        return ruledecllist0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = ((hash* 13)+ ruledecllist0 .hashCode());
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
        if (!(o instanceof RuleDeclBlock)) {
            return false;
        }
        RuleDeclBlock castResult = ((RuleDeclBlock) o);
        if (!this.ruledecllist0 .equals(castResult.ruledecllist0)) {
            return false;
        }
        return true;
    }

}
