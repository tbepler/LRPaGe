package bepler.lrpage;

import java.io.File;
import java.io.IOException;

import com.sun.codemodel.JClassAlreadyExistsException;

import bepler.lrpage.grammar.Terminal;

public class Main {
	
	public static class True implements Terminal{

		@Override
		public String getRegex() {
			return "true";
		}

		@Override
		public String getSymbol() {
			return "TRUE";
		}

		@Override
		public int getPriority() {
			return 0;
		}
		
	}
	
	public static class Id implements Terminal{

		@Override
		public String getRegex() {
			return "[A-Za-z_]+[A-Za-z0-9_]*";
		}

		@Override
		public String getSymbol() {
			return "ID";
		}

		@Override
		public int getPriority() {
			return 0;
		}
		
	}
	
	public static class TerminalTest implements Terminal{

		@Override
		public String getRegex() {
			return ".";
		}

		@Override
		public String getSymbol() {
			return "Test";
		}

		@Override
		public int getPriority() {
			return 0;
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
	}
	
	public static void main(String[] args) throws IOException, JClassAlreadyExistsException{
		CodeGenerator gen = new CodeGenerator("Test");
		gen.addToken(new True());
		gen.addToken(new Id());
		gen.addToken(new IgnoreTest());
		gen.addToken(new TerminalTest());
		if(args.length > 0){
			gen.write(new File(args[0]));
		}else{
			gen.write(new File(System.getProperty("user.dir")));
		}
	}
	
}
