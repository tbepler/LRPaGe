
package bepler.lrpage.ebnf.parser.nodes;

import bepler.lrpage.ebnf.parser.Visitor;


/**
 * This class was generated by the LRPaGe parser generator v1.0 using the com.sun.codemodel library.
 * 
 * <P>LRPaGe is available from https://github.com/tbepler/LRPaGe.
 * <P>CodeModel is available from https://codemodel.java.net/.
 * 
 */
public class TokenDecl
    extends TokenDeclAbstractNode
{

    public final IdentifierToken identifier0;
    public final TerminalStringToken terminalstring1;

    public TokenDecl(IdentifierToken identifier0, DefinitionToken definition1, TerminalStringToken terminalstring2, TerminationToken termination3) {
        this.identifier0 = identifier0;
        this.terminalstring1 = terminalstring2;
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

}
