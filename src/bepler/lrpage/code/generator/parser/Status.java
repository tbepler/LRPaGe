package bepler.lrpage.code.generator.parser;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

public class Status {
	
	private static final String ERROR = "ERROR";
	private static final String COMPLETE = "COMPLETE";
	private static final String NOMINAL = "NOMINAL";
	
	private static final int PRIVATE_STATIC_FINAL = JMod.PRIVATE+JMod.STATIC+JMod.FINAL;
	
	private final JCodeModel model;
	private final JDefinedClass parser;
	
	public Status(JDefinedClass parser){
		this.model = parser.owner();
		this.parser = parser;
	}
	
	public JExpression getErrorLabel(){
		JVar field = parser.fields().get(ERROR);
		if(field == null){
			field = parser.field(PRIVATE_STATIC_FINAL,
					int.class, ERROR, JExpr.lit(-1));
		}
		return field;
	}
	
	public JExpression getNominalLabel(){
		JVar field = parser.fields().get(NOMINAL);
		if(field == null){
			field = parser.field(PRIVATE_STATIC_FINAL,
					int.class, NOMINAL, JExpr.lit(1));
		}
		return field;
	}
	
	public JExpression getCompletedLabel(){
		JVar field = parser.fields().get(COMPLETE);
		if(field == null){
			field = parser.field(PRIVATE_STATIC_FINAL,
					int.class, COMPLETE, JExpr.lit(0));
		}
		return field;
	}
	
	public JType asJType(){
		return model.INT;
	}
	

	
}
