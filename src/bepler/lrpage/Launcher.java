package bepler.lrpage;

import java.io.IOException;

import bepler.lrpage.code.generator.CodeGenerator;
import bepler.lrpage.ebnf.EBNFGrammarParser;
import bepler.lrpage.ebnf.GrammarParser;
import bepler.lrpage.grammar.Grammar;

public class Launcher {
	
	public static void main(String[] args){
		
		CommandLineParams cmdArgs = new CommandLine();
		try {
			cmdArgs.parse(args);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			cmdArgs.printHelp(System.err);
			System.exit(1);
		}
		if(cmdArgs.help()){
			cmdArgs.printHelp(System.err);
			System.exit(0);
		}
		
		GrammarParser parser = new EBNFGrammarParser();
		try {
			Grammar g = parser.parse(cmdArgs.getGrammarInput());
			if(g != null){
				CodeGenerator code = new CodeGenerator(cmdArgs.getRootPackage(), g);
				code.write(cmdArgs.getOutputDir());
			}
		} catch (IOException e) {
			//bad things
			System.err.println(e.getMessage());
			System.exit(2);
		}
		
	}
	
}
