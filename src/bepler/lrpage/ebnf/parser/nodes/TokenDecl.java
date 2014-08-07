
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (hash*(13 + identifier0 .hashCode()));
        hash = (hash*(13 + terminalstring1 .hashCode()));
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this.equals(o)) {
            return true;
        }
        if (o.equals(null)) {
            return false;
        }
        if (!(o instanceof TokenDecl)) {
            return false;
        }
        TokenDecl castResult = ((TokenDecl) o);
        if (!this.identifier0 .equals(castResult.identifier0)) {
            return false;
        }
        if (!this.terminalstring1 .equals(castResult.terminalstring1)) {
            return false;
        }
        return true;
    }

}
