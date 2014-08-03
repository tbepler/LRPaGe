
package bepler.lrpage.code.generator.framework;

import java.io.IOException;

public class Parser<V> {
	
	private final ParsingEngine<V> eng;
	
	public Parser(ParsingEngine<V> eng){
		this.eng = eng;
	}

    public Node<V> parse(Lexer<V> lexer, ErrorRepair<V> repair)
    		throws IOException{
    	assert(lexer != null);
    	assert(repair != null);
    	
    	Stack<V> stack = new Stack<V>();
        boolean error = false;
        boolean fin = false;
        while (!fin) {
        	Token<V> lookahead = lexer.nextToken();
        	switch(eng.advance(stack, lookahead)){
			case COMPLETE:
				fin = true;
				break;
			case ERROR:
				error = true;
				stack = repair.repair(stack, lookahead, lexer, eng);
				break;
			default:
				repair.update(stack, lookahead);
				break;
        	}
        }
        if (error) {
            return null;
        }
        return stack.pop();
    }

}
