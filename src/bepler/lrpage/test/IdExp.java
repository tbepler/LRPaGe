package bepler.lrpage.test;

public class IdExp
    extends ExpAbstractNode
{

    public final IDToken f0;

    public IdExp(IDToken node0) {
        this.f0 = node0;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
