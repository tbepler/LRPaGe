package bepler.lrpage.test;

public class RParenToken
    implements AbstractSyntaxNode
{

    public final String text;
    public final int line;
    public final int pos;

    public RParenToken(String text, int line, int pos) {
        this.text = text;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Symbols type() {
        return Symbols.RParen;
    }

    @Override
    public AbstractSyntaxNode replace() {
        return this;
    }

    @Override
    public String toString() {
        return (((((((this.type()+"(")+ text)+", ")+ line)+":")+ pos)+")");
    }

}
