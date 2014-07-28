package bepler.lrpage.test;
import java.io.IOException;

public interface Lexer {


    public boolean hasNext();

    public AbstractSyntaxNode nextToken()
        throws IOException
    ;

}
