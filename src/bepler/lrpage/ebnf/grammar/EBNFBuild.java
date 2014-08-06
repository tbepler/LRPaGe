package bepler.lrpage.ebnf.grammar;

import java.io.File;
import java.io.IOException;

import bepler.lrpage.code.generator.CodeGenerator;

public class EBNFBuild {
	
	public static void main(String[] args) throws IOException{
		CodeGenerator gen = new CodeGenerator("bepler.lrpage.ebnf.parser", new EBNFGrammar());
		gen.write(new File("src"));
	}
	
}
