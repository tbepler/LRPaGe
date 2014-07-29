package bepler.lrpage.test;

public class ParenExp
    extends ExpAbstractNode
{

    public final LParenToken f0;
    public final ExpAbstractNode f1;
    public final RParenToken f2;

    public ParenExp(LParenToken node0, ExpAbstractNode node1, RParenToken node2) {
        this.f0 = node0;
        this.f1 = node1;
        this.f2 = node2;
    }

    @Override
    public void accept(Visitor visitor) {
        //do nothing
    }

    @Override
    public AbstractSyntaxNode replace() {
        return f1;
    }

}
