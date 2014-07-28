package bepler.lrpage.test;

public class TRUEToken
    implements AbstractSyntaxNode
{

    public final String text;
    public final int line;
    public final int pos;

    public TRUEToken(String text, int line, int pos) {
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
        return Symbols.TRUE;
    }

    @Override
    public String toString() {
        return (((((((this.type()+"(")+ text)+", ")+ line)+":")+ pos)+")");
    }

}
