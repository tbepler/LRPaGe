package bepler.lrpage.test;


public interface AbstractSyntaxNode {


    public void accept(Visitor visitor);

    public Symbols type();

}
