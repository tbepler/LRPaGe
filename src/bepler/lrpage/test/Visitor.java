package bepler.lrpage.test;

public interface Visitor {


    public void visit(TRUEToken node);

    public void visit(IDToken node);

    public void visit(PlusToken node);

    public void visit(TestToken node);

    public void visit(PlusExp node);

    public void visit(IdExp node);

}
