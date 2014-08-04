package bepler.lrpage.code.generator.framework;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Queue;

public class BurkeFischerErrorRepair<V> implements ErrorRepair<V>{
	
	private final int k;
	private final int s;
	
	private Parse<V> parse;
	
	public BurkeFischerErrorRepair(int k, int s){
		this.k = k; this.s = s;
		parse = new Parse<V>();
	}

	@Override
	public void update(Stack<V> s, Token<V> lookahead) {
		parse.add(s.clone(), lookahead);
		while(parse.size() > k){
			parse.remove();
		}
	}

	@Override
	public Stack<V> repair(Stack<V> s, Token<V> lookahead, Lexer<V> lexer,
			ParsingEngine<V> eng) throws IOException {
		
		Stack<V> revert = parse.peek();
		List<Token<V>> history = parse.history();
		history.add(lookahead);
		
		Symbol[] expected = eng.expectedSymbols(s.curState());
		printErrorMessage(lookahead, expected);
		
		Repair<V> best = null;
		Repair<V> deletion = tryDeletions(revert, history, lexer, eng);
		if(meetsStopCriteria(deletion)){
			return acceptRepair(deletion, history, lexer);
		}
		if(deletion.dist >= k && deletion.compareTo(best) > 0){
			best = deletion;
		}
		Repair<V> replacement = tryReplacements(revert, history, lexer, eng);
		if(meetsStopCriteria(replacement)){
			return acceptRepair(replacement, history, lexer);
		}
		if(replacement.dist >= k && replacement.compareTo(best) > 0){
			best = replacement;
		}
		Repair<V> insertion = tryInsertions(revert, history, lexer, eng);
		if(meetsStopCriteria(insertion)){
			return acceptRepair(insertion, history, lexer);
		}
		if(insertion.dist >= k && insertion.compareTo(best) > 0){
			best = insertion;
		}
		
		if(best == null){
			//this should not happen
			throw new RuntimeException("No change can proceed past error.");
		}
		
		return acceptRepair(best, history, lexer);
		
	}

	private void printErrorMessage(Token<V> lookahead, Symbol[] expected) {
		System.err.print("["+lookahead.getLine()+":"+lookahead.getPos()+"] syntax error on "+
				lookahead);
		if(expected.length == 1){
			System.err.println(", expected "+expected[0]);
		}else if(expected.length > 1){
			System.err.print(", expected one of ");
			boolean first = true;
			for(Symbol sym : expected){
				if(first){
					first = false;
				}else{
					System.err.print(", ");
				}
				System.err.print(sym);
			}
			System.err.println();
		}else{
			System.err.println();
		}
	}
	
	private Stack<V> acceptRepair(Repair<V> repair, List<Token<V>> tokens, Lexer<V> lexer){
		printSuggestion(repair, tokens);
		lexer.setPos(repair.pos);
		parse = repair.parse;
		return parse.peekLast();
	}
	
	private void printSuggestion(Repair<V> repair, List<Token<V>> tokens){
		System.err.print("Suggested change: ");
		Modification<V> mod = repair.mod;
		switch(mod.type){
		case DELETION:
			System.err.println("delete ["+mod.token.getLine()+":"+mod.token.getPos()+"] "+mod.token);
			break;
		case INSERTION:
			Token<V> before = tokens.get(mod.index);
			System.err.println("insert "+mod.token.symbol()+" before ["+before.getLine()+":"+before.getPos()+"] "+before);
			break;
		case REPLACEMENT:
			Token<V> replaced = tokens.get(mod.index);
			System.err.println("replace ["+replaced.getLine()+":"+replaced.getPos()+"] "+replaced+" with "+mod.token.symbol());
			break;
		}
	}
	
	private Repair<V> tryInsertions(Stack<V> s, List<Token<V>> tokens, Lexer<V> lexer,
			ParsingEngine<V> eng) throws IOException{
		
		TokenFactory<V> fac = lexer.getTokenFactory();
		Repair<V> best = null;
		Modification<V> mod;
		for( int i = 0 ; i < tokens.size() ; ++i ){
			for( int j = 0 ; j < fac.size() ; ++j ){
				Token<V> insert = fac.build(j);
				if(insert != null){
					tokens.add(i, insert);
					mod = new Modification<V>(ModTypes.INSERTION, i, insert);
					Repair<V> repair = tryModification(s, tokens, mod, lexer, eng);
					tokens.remove(i);
					if(meetsStopCriteria(repair)){
						return repair;
					}
					if(repair.compareTo(best) > 0){
						best = repair;
					}
				}
			}
		}
		return best;
	}
	
	private Repair<V> tryReplacements(Stack<V> s, List<Token<V>> tokens, Lexer<V> lexer,
			ParsingEngine<V> eng) throws IOException{
		
		TokenFactory<V> fac = lexer.getTokenFactory();
		Repair<V> best = null;
		Modification<V> mod;
		for( int i = 0 ; i < tokens.size() ; ++i ){
			for( int j = 0 ; j < fac.size() ; ++j ){
				Token<V> insert = fac.build(j);
				if(insert != null){
					Token<V> repl = tokens.set(i, insert);
					mod = new Modification<V>(ModTypes.REPLACEMENT, i, insert);
					Repair<V> repair = tryModification(s, tokens, mod, lexer, eng);
					tokens.set(i, repl);
					if(meetsStopCriteria(repair)){
						return repair;
					}
					if(repair.compareTo(best) > 0){
						best = repair;
					}
				}
			}
		}
		return best;
		
	}
	
	private Repair<V> tryDeletions(Stack<V> s, List<Token<V>> tokens, Lexer<V> lexer,
			ParsingEngine<V> eng) throws IOException{
	
		Repair<V> best = null;
		Modification<V> mod;
		for( int i = 0 ; i < tokens.size() ; ++i ){
			Token<V> t = tokens.remove(i);
			mod = new Modification<V>(ModTypes.DELETION, i, t);
			Repair<V> repair = tryModification(s, tokens, mod, lexer, eng);
			tokens.add(i, t);
			if(meetsStopCriteria(repair)){
				return repair;
			}
			if(repair.compareTo(best) > 0){
				best = repair;
			}
		}
		return best;
		
	}
	
	private Repair<V> tryModification(Stack<V> s, List<Token<V>> tokens, Modification<V> mod,
			Lexer<V> lexer, ParsingEngine<V> eng) throws IOException{
		
		Parse<V> parse = new Parse<V>(s);
		int dist = 0;
		boolean complete = false;
		boolean fin = false;
		lexer.mark();
		while(!fin){
			Token<V> next = dist < tokens.size() ? tokens.get(dist) : lexer.nextToken();
			++dist;
			Stack<V> cur = parse.peekLast().clone();
			int pos;
			switch(eng.advance(cur, next)){
			case COMPLETE:
				complete = fin = true;
				pos = lexer.getPos();
				if(pos > 0){
					lexer.setPos(pos - 1);
				}
				break;
			case ERROR:
				fin = true;
				pos = lexer.getPos();
				if(pos > 0){
					lexer.setPos(pos - 1);
				}
				break;
			default:
				parse.add(cur, next);
				if(meetsStopCriteria(dist)){
					fin = true;
				}
				break;
			}
		}
		int pos = lexer.getPos();
		lexer.reset();
		lexer.unmark();
		return new Repair<V>(parse, dist, complete, pos, mod);
	}
	
	private boolean meetsStopCriteria(Repair<V> repair){
		return repair.complete || meetsStopCriteria(repair.dist);
	}
	
	private boolean meetsStopCriteria(int dist){
		return s >= 0 && dist >= (s+k);
	}
	
	private static class Repair<V> implements Comparable<Repair<V>>{
		
		public final Parse<V> parse;
		public final int dist;
		public final boolean complete;
		public final int pos;
		public final Modification<V> mod;
		
		public Repair(Parse<V> parse, int dist, boolean complete,
				int pos, Modification<V> mod){
			this.parse = parse;
			this.dist = dist;
			this.complete = complete;
			this.pos = pos;
			this.mod = mod;
		}

		@Override
		public int compareTo(Repair<V> arg0) {
			if(arg0 == null){
				return 1;
			}
			return dist - arg0.dist;
		}
		
	}
	
	private static class Modification<V>{
		public final ModTypes type;
		public final int index;
		public final Token<V> token;
		
		public Modification(ModTypes type, int index, Token<V> token){
			this.type = type;
			this.index = index;
			this.token = token;
		}
	}
	
	private static enum ModTypes{
		
		DELETION,
		INSERTION,
		REPLACEMENT;
		
	}
	
	private static class Parse<V>{
		
		private final Queue<Token<V>> tokenQ = new ArrayDeque<Token<V>>();
		private final Deque<Stack<V>> stackQ = new ArrayDeque<Stack<V>>();
		
		public Parse(){
			stackQ.add(new Stack<V>());
		}
		
		public Parse(Stack<V> start){
			stackQ.add(start);
		}
		
		public int size(){
			return tokenQ.size();
		}
		
		public void add(Stack<V> s, Token<V> t){
			tokenQ.add(t);
			stackQ.add(s);
		}
		
		public void remove(){
			tokenQ.remove();
			stackQ.remove();
		}
		
		public Stack<V> peek(){
			return stackQ.peek();
		}
		
		public Stack<V> peekLast(){
			return stackQ.peekLast();
		}
		
		public List<Token<V>> history(){
			return new ArrayList<Token<V>>(tokenQ);
		}
		
	}

}
