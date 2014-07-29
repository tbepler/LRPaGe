package bepler.lrpage.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {


    public static void main(String[] args)
        throws IOException
    {
        PrintVisitor visitor = new PrintVisitor();
        TestParser parser = new TestParser();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (((line = reader.readLine()) != null)) {
            if (line.equals("q")) {
                break;
            }
            TestLexer lexer = new TestLexer(line);
            while (lexer.hasNext()) {
                System.out.print((lexer.nextToken()+" "));
            }
            System.out.println();
            lexer = new TestLexer(line);
            AbstractSyntaxNode tree = parser.parse(lexer);
            tree.accept(visitor);
        }
    }

}
