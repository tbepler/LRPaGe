package bepler.lrpage.test;

public class PlusExp
    extends ExpAbstractNode
{

    public final ExpAbstractNode f0;
    public final ExpAbstractNode f2;

    public PlusExp(ExpAbstractNode node0, PlusToken node1, ExpAbstractNode node2) {
        this.f0 = node0;
        this.f2 = node2;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
