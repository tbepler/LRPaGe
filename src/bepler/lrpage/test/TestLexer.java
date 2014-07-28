package bepler.lrpage.test;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestLexer
    implements Lexer
{

    private final static List<Pattern> TOKEN_PATTERNS = buildPatternList();
    private final static AbstractSyntaxNode EOF = new EOFToken();
    private final Reader r;
    private boolean next = true;
    private int lineNum = 1;
    private int charNum = 1;
    private final Deque<Character> buffer = new LinkedList<Character>();

    public TestLexer(Reader r) {
        this.r = r;
    }

    public TestLexer(InputStream in) {
        this(new java.io.InputStreamReader(in));
    }

    public TestLexer(String str) {
        this(new java.io.StringReader(str));
    }

    private static List<Pattern> buildPatternList() {
        List<Pattern> list = new ArrayList<Pattern>();
        list.add(Pattern.compile("true"));
        list.add(Pattern.compile("[A-Za-z_]+[A-Za-z0-9_]*"));
        list.add(Pattern.compile("\\s+"));
        list.add(Pattern.compile("."));
        return list;
    }

    @Override
    public boolean hasNext() {
        return next;
    }

    private AbstractSyntaxNode createToken(int tokenIndex, int line, int pos, String text)
        throws IOException
    {
        switch (tokenIndex) {
            case  0 :
                return new TRUEToken(text, line, pos);
            case  1 :
                return new IDToken(text, line, pos);
            case  2 :
                return this.nextToken();
            case  3 :
                return new TestToken(text, line, pos);
            default:
                throw new RuntimeException("Unrecognized token index.");
        }
    }

    private boolean hasMatch(String s, List<Matcher> ms) {
        for(java.util.regex.Matcher m : ms){
	m.reset(s);
	if(m.matches() || m.hitEnd()){
		return true;
	}
}
return false;

    }

    @Override
    public AbstractSyntaxNode nextToken()
        throws IOException
    {
        if(!next){
	throw new RuntimeException("No tokens remaining."); 
}
java.util.List<java.util.regex.Matcher> ms = new java.util.ArrayList<java.util.regex.Matcher>();
for(java.util.regex.Pattern p : TOKEN_PATTERNS){
	ms.add(p.matcher(""));
}
String cur = "";
boolean fin = false;
while(!fin){
	if(!buffer.isEmpty()){
		//read from the buffer before reading more chars
		//from the reader
		cur = cur + buffer.pop();
	}else{
		int read = r.read();
		if(read == -1){
			//the reader is expired, so set fin to true
			fin = true;
		}else{
			cur = cur + (char) read;
		}
	}
	fin = fin || !hasMatch(cur, ms);
}
//if cur is empty, then return eof and mark lexing done
if(cur.length() == 0){
	next = false;
	return EOF;
}
//find the longest match
for( int end = cur.length() ; end >= 0 ; --end ){
	String sub = cur.substring(0, end);
	for( int i = 0 ; i < ms.size() ; ++i ){
		java.util.regex.Matcher m = ms.get(i);
		m.reset(sub);
		if(m.matches()){
			//push the end of cur into the buffer
			for( int j = end ; j < cur.length() ; ++j ){
				buffer.add(cur.charAt(j));
			}
			int line = lineNum;
			int cNum = charNum;
			//update line and char count
			for( int j = 0 ; j < sub.length() ; ++j ){
				if(sub.charAt(j) == '\n'){
					++lineNum;
					charNum = 0;
				}
				++charNum;
			}
			//return the token
			return createToken(i, line, cNum, sub);
		}
	}
}
//an error occurred, the string is unmatched
throw new RuntimeException("Unmatched token: "+cur);

    }

}
