package bepler.lrpage.test;

public class PrintVisitor
    implements Visitor
{

    private final static String DELIM = "  ";
    private int depth = 0;

    private void print(Object obj) {
        for (int i = 0; (i<depth); i ++) {
            System.out.print(DELIM);
        }
        System.out.println(obj);
    }

    @Override
    public void visit(TRUEToken node) {
        this.print(node);
    }

    @Override
    public void visit(IDToken node) {
        this.print(node);
    }

    @Override
    public void visit(PlusToken node) {
        this.print(node);
    }

    @Override
    public void visit(TestToken node) {
        this.print(node);
    }

    @Override
    public void visit(PlusExp node) {
        this.print(node);
        this.print("{");
        depth = (depth + 1);
        node.f0 .accept(this);
        node.f2 .accept(this);
        depth = (depth- 1);
        this.print("}");
    }

    @Override
    public void visit(IdExp node) {
        this.print(node);
        this.print("{");
        depth = (depth + 1);
        node.f0 .accept(this);
        depth = (depth- 1);
        this.print("}");
    }

}
