
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class GrammarAppendBlock
    extends GrammarAbstractNode
{

    public final GrammarAbstractNode grammar0;
    public final BlockAbstractNode block1;

    public GrammarAppendBlock(GrammarAbstractNode grammar0, BlockAbstractNode block1) {
        this.grammar0 = grammar0;
        this.block1 = block1;
    }

    @Override
    public int getLine() {
        return grammar0 .getLine();
    }

    @Override
    public int getPos() {
        return grammar0 .getPos();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}