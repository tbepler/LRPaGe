package bepler.lrpage.templates;


public class LexerTemplate {
	
	private final java.io.Reader r;
	private boolean next = true;
	private int lineNum = 1;
	private int charNum = 1;
	private final java.util.Deque<Character> buffer = new java.util.LinkedList<Character>();
	
	public LexerTemplate(java.io.Reader r){
		this.r = r;
	}
	
	public LexerTemplate(java.io.InputStream in){
		this(new java.io.InputStreamReader(in));
	}
	
	public boolean hasNext(){
		return next;
	}
	
	public AbstractSyntaxNodeTemplate nextToken() throws java.io.IOException{
		if(!next){
			throw new RuntimeException("No tokens remaining.");
		}
		java.util.List<java.util.regex.Pattern> pats = getRegexes();
		java.util.List<java.util.regex.Matcher> ms = new java.util.ArrayList<java.util.regex.Matcher>();
		for(java.util.regex.Pattern p : pats){
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
			return eofToken();
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
		//an error occured, the string is unmatched
		throw new RuntimeException("Unmatched token: "+cur);
	}
	
	private boolean hasMatch(String s, java.util.List<java.util.regex.Matcher> ms){
		for(java.util.regex.Matcher m : ms){
			m.reset(s);
			if(m.matches() || m.hitEnd()){
				return true;
			}
		}
		return false;
	}
	
	//TODO - must be implemented by the code generator
	protected AbstractSyntaxNodeTemplate createToken(int tokenIndex, int line, int pos, String text){
		//STUB
		return null;
	}

	//TODO - must be implemented by the code generator
	protected java.util.List<java.util.regex.Pattern> getRegexes(){
		//STUB
		return null;
	}

	//TODO - must be implemented by the code generator
	protected AbstractSyntaxNodeTemplate eofToken(){
		//STUB	
		return null;
	}
	
}
