package bepler.lrpage;

import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException{
		CodeGenerator gen = new CodeGenerator("Test");
		if(args.length > 0){
			gen.write(new File(args[0]));
		}else{
			gen.write(new File(System.getProperty("user.dir")));
		}
	}
	
}
