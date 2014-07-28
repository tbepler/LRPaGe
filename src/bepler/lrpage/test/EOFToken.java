
public class EOFToken
    implements AbstractSyntaxNode
{


    @Override
    public void accept(Visitor visitor) {
        //do nothing
    }

    @Override
    public Symbols type() {
        return Symbols.EOF;
    }

}
