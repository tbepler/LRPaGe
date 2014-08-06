package bepler.lrpage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

public class CommandLine implements CommandLineParams{

	public String root;
	public String output;
	public InputStream rules;
	
	CommandLine(String root, String output, String rules) throws FileNotFoundException{
		setRules(rules);
		setRootDir(root);
		setOutputDir(output);
		
	}
	
	public void printHelp(){
		System.out.println("Argument input:\nRoot path <STRING>\nOutput path <STRING>\nRules file <STRING>\n");
	}
	
	@Override
	public void setRules(String s) throws FileNotFoundException {
		InputStream fis;
		try {
			System.out.println(s);
			fis = new BufferedInputStream( new FileInputStream(new File(s)));
			rules=fis;
		} catch (FileNotFoundException e) {
			printHelp();
			e.printStackTrace();
			System.exit(1);
		}
	}
	@Override
	public InputStream getRules() {
		return rules;
	}
	
	@Override
	public void setRootDir(String s) {

		try {
			System.out.println(s);
			InputStream f= new FileInputStream(new File(s+"test.txt"));
			root=s;
		} catch (FileNotFoundException e) {
			printHelp();
			e.printStackTrace();
			System.exit(2);
		}
	
	}

	@Override
	public String getRootDir() {
		return root;
	}

	@Override
	public void setOutputDir(String s) {
		try {
			System.out.println(s);
			InputStream f = new FileInputStream(new File(s+"test.txt"));
			output=s;
		} catch (FileNotFoundException e) {
			printHelp();
			e.printStackTrace();
			System.exit(3);
		}
		
	}
	
	@Override
	public String getOutputDir() {
		return output;
	}

	
}
