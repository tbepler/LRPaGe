package bepler.lrpage;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

public interface CommandLineParams {
	
	public void parse(String ... args) throws Exception;
	
	public void printHelp(PrintStream out);

	public InputStream getGrammarInput();
	public String getRootPackage();
	public File getOutputDir();
	public boolean help();
	
}
