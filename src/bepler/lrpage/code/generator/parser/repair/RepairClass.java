package bepler.lrpage.code.generator.parser.repair;

import java.util.Deque;
import java.util.Queue;

import bepler.lrpage.code.generator.ParseStackGenerator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class RepairClass {
	
	private static final String MOD = "mod";
	private static final String STACK_DEQ = "stackDeq";
	private static final String TOKEN_Q = "tokenQ";
	private static final String CLASS_NAME = "Repair";
	private static final String DIST = "dist";
	
	private static final int PUBLIC_FINAL = JMod.PUBLIC + JMod.FINAL;
	
	private final JCodeModel model;
	private final ModClass mod;
	private final JDefinedClass iNode;
	private final JDefinedClass stack;
	private final JDefinedClass clazz;
	
	public RepairClass(ParseStackGenerator stack, ModClass mod, JDefinedClass parser, JDefinedClass iNode){
		model = parser.owner();
		this.mod = mod;
		this.iNode = iNode;
		this.stack = stack.getParseStack();
		clazz = initRepairClass(parser);
		
	}
	
	public JVar getModField(){
		JVar field = clazz.fields().get(MOD);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL, mod.asJType(), MOD);
		}
		return field;
	}
	
	public JVar getStackDequeField(){
		JVar field = clazz.fields().get(STACK_DEQ);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL,
					model.ref(Deque.class).narrow(stack), STACK_DEQ);
		}
		return field;
	}
	
	public JVar getTokenQField(){
		JVar field = clazz.fields().get(TOKEN_Q);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL,
					model.ref(Queue.class).narrow(iNode), TOKEN_Q);
		}
		return field;
	}
	
	public JVar getDistField(){
		JVar field = clazz.fields().get(DIST);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL, int.class, DIST);
		}
		return field;
	}
	
	public JDefinedClass getRepairClass(){
		return clazz;
	}
	
	private JDefinedClass initRepairClass(JDefinedClass parser){
		try {
			return parser._class(JMod.PRIVATE+JMod.STATIC, CLASS_NAME);
		} catch (JClassAlreadyExistsException e) {
			JDefinedClass c = model._getClass(parser.fullName() + "." + CLASS_NAME);
			assert(c != null);
			return c;
		}
	}
	
	

}
