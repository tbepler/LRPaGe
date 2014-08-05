package bepler.lrpage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.sun.codemodel.JClassAlreadyExistsException;

import bepler.lrpage.code.generator.CodeGenerator;
import bepler.lrpage.grammar.Assoc;
import bepler.lrpage.grammar.Grammar;
import bepler.lrpage.grammar.Rule;
import bepler.lrpage.grammar.Terminal;

public class Main {
	
	public static class True implements Terminal{

		@Override
		public String getRegex() {
			return "true";
		}

		@Override
		public String getSymbol() {
			return "True";
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean isPunctuation() {
			return false;
		}
		
	}
	
	public static class Plus implements Terminal{
		@Override public String getRegex(){ return "\\+"; }
		@Override public String getSymbol(){ return "Plus"; }
		@Override public int getPriority(){ return 0; }
		@Override public boolean isPunctuation() { return true; }
	}
	
	public static class LParen implements Terminal{
		@Override public String getRegex(){ return "\\("; }
		@Override public String getSymbol(){ return "LParen"; }
		@Override public int getPriority(){ return 0; }
		@Override public boolean isPunctuation() { return true; }
	}
	
	public static class RParen implements Terminal{
		@Override public String getRegex(){ return "\\)"; }
		@Override public String getSymbol(){ return "RParen"; }
		@Override public int getPriority(){ return 0; }
		@Override public boolean isPunctuation() { return true; }
	}
	
	public static class Semicolon implements Terminal{
		@Override public String getRegex(){ return ";"; }
		@Override public String getSymbol(){ return "Semicolon"; }
		@Override public int getPriority(){ return 0; }
		@Override public boolean isPunctuation() { return true; }
	}
	
	public static class StrLit implements Terminal{
		@Override public String getRegex(){ return "(['\"])(?:\\\\.|(?!\\1).)*\\1"; }
		@Override public String getSymbol(){ return "Str"; }
		@Override public int getPriority(){ return 0; }
		@Override public boolean isPunctuation() { return false; }
	}
	
	public static class Id implements Terminal{

		@Override
		public String getRegex() {
			return "[A-Za-z_]+[A-Za-z0-9_]*";
		}

		@Override
		public String getSymbol() {
			return "Id";
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean isPunctuation() {
			return false;
		}
		
	}
	
	public static class ErrorChar implements Terminal{

		@Override
		public String getRegex() {
			return ".";
		}

		@Override
		public String getSymbol() {
			return "ErrorChar";
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean isPunctuation() {
			return false;
		}
		
	}
	
	public static class IgnoreTest implements Terminal{
		@Override
		public String getRegex(){
			return "\\s+";
		}

		@Override
		public String getSymbol() {
			return null;
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		public boolean isPunctuation() {
			return false;
		}
	}
	
	public static class IdExpRule implements Rule{

		@Override
		public String leftHandSide() {
			return "Exp";
		}

		@Override
		public String[] rightHandSide() {
			return new String[]{"Id"};
		}

		@Override
		public Integer getPriority() {
			return null;
		}

		@Override
		public Assoc getAssoc() {
			return Assoc.NON;
		}

		@Override
		public String getName() {
			return "IdExp";
		}
		/*
		@Override
		public int[] ignoreSymbols() {
			return new int[]{};
		}

		@Override
		public int replace() {
			return -1;
		}
		*/
	}
	
	public static class StrExpRule implements Rule{
		@Override public String leftHandSide(){ return "Exp"; }
		@Override public String[] rightHandSide(){ return new String[]{"Str"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.NON; }
		@Override public String getName(){ return "StrExp"; }
	}
	
	public static class PlusExpRule implements Rule{
		@Override public String leftHandSide(){ return "Exp"; }
		@Override public String[] rightHandSide(){ return new String[]{"Exp","Plus","Exp"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.LEFT; }
		@Override public String getName(){ return "PlusExp"; }
		//@Override public int[] ignoreSymbols(){ return new int[]{1}; }
		//@Override public int replace(){ return -1; }
	}
	
	public static class ParenExpRule implements Rule{
		@Override public String leftHandSide(){ return "Exp"; }
		@Override public String[] rightHandSide(){ return new String[]{"LParen", "Exp", "RParen"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.NON; }
		@Override public String getName(){ return "ParenExp"; }
	}
	
	public static class ExpStmt implements Rule{
		@Override public String leftHandSide(){ return "Stmt"; }
		@Override public String[] rightHandSide(){ return new String[]{"Exp", "Semicolon"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.NON; }
		@Override public String getName(){ return "ExpStmt"; }
	}
	
	public static class StmtListHead implements Rule{
		@Override public String leftHandSide(){ return "StmtList"; }
		@Override public String[] rightHandSide(){ return new String[]{"Stmt"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.NON; }
		@Override public String getName(){ return "StmtListHead"; }
	}
	
	public static class StmtList implements Rule{
		@Override public String leftHandSide(){ return "StmtList"; }
		@Override public String[] rightHandSide(){ return new String[]{"StmtList","Stmt"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.NON; }
		@Override public String getName(){ return "StmtList"; }
	}
	
	/*
	public static class ErrorSynchRule implements Rule{
		@Override public String leftHandSide(){ return "Exp"; }
		@Override public String[] rightHandSide(){ return new String[]{"LParen", "ERROR", "RParen"}; }
		@Override public Integer getPriority(){ return null; }
		@Override public Assoc getAssoc(){ return Assoc.NON; }
		@Override public String getName(){ return "ErrorExp"; }
		@Override public int[] ignoreSymbols(){ return new int[]{}; }
		@Override public int replace(){ return -1; }
	}
	*/
	
	public static class TestGrammar implements Grammar {

		@Override
		public List<Rule> getRules() {
			return Arrays.asList(
					new StmtList(),
					new StmtListHead(),
					new ExpStmt(),
					new PlusExpRule(),
					new IdExpRule(),
					new StrExpRule(),
					new ParenExpRule()
					);
		}

		@Override
		public List<Terminal> getTokens() {
			return Arrays.asList(
					new True(),
					new LParen(),
					new RParen(),
					new StrLit(),
					new Id(),
					new Semicolon(),
					new Plus(),
					new IgnoreTest(),
					new ErrorChar()
					);
		}

		@Override
		public int defaultPriority() {
			return 0;
		}

		@Override
		public String getPseudonym(String symbol) {
			switch(symbol){
			case "Plus": return "'+'";
			case "LParen": return "'('";
			case "RParen": return "')'";
			case "Semicolon": return "';'";
			default: return null;
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException, JClassAlreadyExistsException{
		CodeGenerator gen = new CodeGenerator("bepler.lrpage.test", new TestGrammar());
		if(args.length > 0){
			gen.write(new File(args[0]));
		}else{
			gen.write(new File(System.getProperty("user.dir")));
		}
	}
	
}
