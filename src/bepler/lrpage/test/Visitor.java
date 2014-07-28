package bepler.lrpage.test;

public interface Visitor {


    public void visit(TRUEToken node);

    public void visit(IDToken node);

    public void visit(TestToken node);

}
