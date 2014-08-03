
package bepler.lrpage.code.generator.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer<V> {

	private final Reader r;
	private final TokenFactory<V> fac;
	private final List<Matcher> matchers = new ArrayList<Matcher>();
	private final Deque<Character> buffer = new ArrayDeque<Character>();
	private final List<Token<V>> history = new ArrayList<Token<V>>();
	
	private boolean next = true;
	private int lineNum = 1;
	private int charNum = 1;
	private int mark = -1;
	private int pos = 0;

	public Lexer(Reader r, TokenFactory<V> fac) {
		this.r = r;
		this.fac = fac;
		for(Pattern p : fac.getPatterns()){
			matchers.add(p.matcher(""));
		}
	}

	public Lexer(InputStream in, TokenFactory<V> fac) {
		this(new InputStreamReader(in), fac);
	}

	public Lexer(String str, TokenFactory<V> fac) {
		this(new StringReader(str), fac);
	}

	public TokenFactory<V> getTokenFactory() {
		return fac;
	}

	public boolean isMarked() {
		return mark >= 0;
	}

	public void mark() {
		if (!this.isMarked()) {
			history.subList(0, pos).clear();
			mark = pos = 0;
		}
	}

	public void unmark() {
		mark = -1;
	}

	public void reset() {
		pos = 0;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		if (!this.isMarked()) {
			history.subList(0, pos).clear();
			pos = 0;
		}
		this.pos = pos;
	}

	public int history() {
		return history.size();
	}

	private Token<V> record(Token<V> token) {
		if (this.isMarked()) {
			history.add(token);
			this.setPos(pos + 1);
		}
		return token;
	}

	public boolean hasNext() {
		return next || pos < history.size();
	}

	private Token<V> createToken(int tokenIndex, int line,
			int pos, String text) throws IOException{
		Token<V> t = fac.build(tokenIndex, text, line, pos);
		if(t == null){
			t = this.readNext();
		}
		return t;
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

	private Token<V> readNext() throws IOException {
		if(!next){
			throw new RuntimeException("No tokens remaining."); 
		}
		String cur = nextMatch();
		//if cur is empty, then return eof and mark lexing done
		if(cur.length() == 0){
			next = false;
			return fac.getEOFToken(lineNum, charNum);
		}
		//find the longest match
		for( int end = cur.length() ; end >= 0 ; --end ){
			String sub = cur.substring(0, end);
			for( int i = 0 ; i < matchers.size() ; ++i ){
				Matcher m = matchers.get(i);
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

	private String nextMatch() throws IOException {
		StringBuilder cur = new StringBuilder();
		boolean fin = false;
		while(!fin){
			if(!buffer.isEmpty()){
				//read from the buffer before reading more chars
				//from the reader
				cur.append(buffer.pop());
			}else{
				int read = r.read();
				if(read == -1){
					//the reader is expired, so set fin to true
					fin = true;
				}else{
					cur.append((char) read);
				}
			}
			fin = fin || !hasMatch(cur.toString(), matchers);
		}
		return cur.toString();
	}

	public Token<V> nextToken() throws IOException{
		Token<V> token;
		if (pos < history.size()) {
			token = history.get(pos);
			this.setPos(pos + 1);
		} else {
			token = this.record(this.readNext());
		}
		return token;
	}

}
