package bepler.lrpage.test;

public abstract class ExpAbstractNode
    implements AbstractSyntaxNode
{


    @Override
    public Symbols type() {
        return Symbols.Exp;
    }

    @Override
    public AbstractSyntaxNode replace() {
        return this;
    }

    @Override
    public String toString() {
        return type().toString();
    }

}
