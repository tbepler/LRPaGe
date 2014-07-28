package bepler.lrpage.code.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JWhileLoop;

public class MainGenerator {
	
	private static final String MAIN = "Main";
	
	public static void generateMain(String pckg, JCodeModel model, JDefinedClass lexer) throws JClassAlreadyExistsException{
		String name = pckg == null ? MAIN : pckg+"."+MAIN;
		JDefinedClass mainClass = model._class(name);
		JMethod main = mainClass.method(JMod.PUBLIC + JMod.STATIC, void.class, "main");
		main._throws(IOException.class);
		main.param(String[].class, "args");
		JBlock body = main.body();
		JVar str = body.decl(model.ref(String.class), "line");
		body.decl(model.ref(BufferedReader.class), "reader", JExpr._new(model.ref(BufferedReader.class))
				.arg(JExpr._new(model.ref(InputStreamReader.class)).arg(model.ref(System.class).staticRef("in"))));
		JWhileLoop loop = body._while(JExpr.direct("(line = reader.readLine()) != null"));
		loop.body()._if(JExpr.invoke(str, "equals").arg(JExpr.lit("q")))._then()._break();
		JVar lex = loop.body().decl(lexer, "lexer", JExpr._new(lexer).arg(str));
		loop.body()._while(JExpr.invoke(lex, "hasNext")).body()
			.invoke(model.ref(System.class).staticRef("out"), "print").arg(JExpr.invoke(lex, "nextToken").plus(JExpr.lit(" ")));
		loop.body().invoke(model.ref(System.class).staticRef("out"), "println");
		
	}
	
}
