package bepler.lrpage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface CommandLineParams {
	
	public void setRules(String s) throws FileNotFoundException;
	public InputStream getRules();
	
	public void setRootDir(String s);
	public String getRootDir();
	
	public void setOutputDir(String s);
	public String getOutputDir();
	
}
