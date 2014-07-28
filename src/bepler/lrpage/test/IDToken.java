package bepler.lrpage.test;

public class IDToken
    implements AbstractSyntaxNode
{

    public final String text;
    public final int line;
    public final int pos;

    public IDToken(String text, int line, int pos) {
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
        return Symbols.ID;
    }

    @Override
    public String toString() {
        return (((((((this.type()+"(")+ text)+", ")+ line)+":")+ pos)+")");
    }

}
