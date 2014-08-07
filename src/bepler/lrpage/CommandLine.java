package bepler.lrpage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;

public class CommandLine implements CommandLineParams{
	
	private static final String DEFAULT_OUTPUT_DIR = "lrpage_output";
	private static final String DEFAULT_PACKAGE = "bepler.lrgpage.output";
	
	private static final String GRAMMAR_TAG = "-g";
	private static final String OUTPUT_TAG = "-o";
	private static final String PACKAGE_TAG = "-p";
	private static final String HELP_TAG = "-h";

	private String root = DEFAULT_PACKAGE;
	private File output = new File(DEFAULT_OUTPUT_DIR);
	private InputStream grammar = System.in;
	private boolean help = false;
	
	@Override
	public void parse(String ... args) throws Exception{
		for( int i = 0 ; i < args.length ; ++i ){
			String cur = args[i];
			switch(cur){
			case GRAMMAR_TAG: 
				assertNextArg(GRAMMAR_TAG, i, args);
				grammar = parseGrammarArg(args[++i]);
				break;
			case OUTPUT_TAG:
				assertNextArg(OUTPUT_TAG, i, args);
				output = new File(args[++i]);
				break;
			case PACKAGE_TAG:
				assertNextArg(PACKAGE_TAG, i, args);
				root = args[++i];
				break;
			case HELP_TAG:
				help = true;
				return;
			default:
				throw new Exception("Unrecognized flag: "+cur);
			}
		}
	}
	
	private void assertNextArg(String flag, int index, String ... args) throws Exception{
		if( index + 1 >= args.length){
			throw new Exception("Flag `"+flag+"' requires an argument.");
		}
	}
	
	@Override
	public void printHelp(PrintStream out){
		out.println("Usage:\nlrpage [-g] [-o] [-p] [-h]\n");
		out.println("Options:");
		out.println("-g\tEBNF grammar file");
		out.println("-p\tpath to output package");
		out.println("-h\thelp statement");
	}
	
	private InputStream parseGrammarArg(String s) throws FileNotFoundException {
		return new BufferedInputStream( new FileInputStream(new File(s)));
	}
	
	@Override
	public InputStream getGrammarInput() {
		return grammar;
	}
	
	@Override
	public File getOutputDir() {
		if(!output.exists()){
			output.mkdirs();
		}
		return output;
	}

	@Override
	public String getRootPackage() {
		return root;
	}

	@Override
	public boolean help() {
		return help;
	}


	
}
