package bepler.lrpage.code.generator.parser.repair;

import java.util.Deque;
import java.util.Queue;

import bepler.lrpage.code.generator.ParseStackGenerator;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpressionImpl;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

/**
 * This class generates the code for and models
 * a repair inner class of the parser.
 * 
 * @author Tristan Bepler
 *
 */
public class Repair {
	
	private static final String COMPLETED = "completed";
	private static final String POS = "pos";
	private static final String MOD = "mod";
	private static final String STACK_DEQ = "stackDeq";
	private static final String TOKEN_Q = "tokenQ";
	private static final String CLASS_NAME = "Repair";
	private static final String DIST = "dist";
	
	private static final int PUBLIC_FINAL = JMod.PUBLIC + JMod.FINAL;
	
	private final JCodeModel model;
	private final Mod mod;
	private final JDefinedClass iNode;
	private final JDefinedClass stack;
	private final JDefinedClass clazz;
	
	public Repair(ParseStackGenerator stack, Mod mod,
			JDefinedClass parser, JDefinedClass iNode){
		model = parser.owner();
		this.mod = mod;
		this.iNode = iNode;
		this.stack = stack.getParseStack();
		clazz = initRepairClass(parser);
		initConstructor();
	}
	
	public JExpressionImpl newRepair(JVar dist, JVar tokenQ,
			JVar stackDeq, JVar mod, JVar pos, JVar completed){
		return JExpr._new(clazz).arg(dist).arg(tokenQ)
				.arg(stackDeq).arg(mod).arg(pos).arg(completed);
	}
	
	public JVar getCompletedField(){
		JVar field = clazz.fields().get(COMPLETED);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL, boolean.class, COMPLETED);
		}
		return field;
	}
	
	public JVar getLexerPosField(){
		JVar field = clazz.fields().get(POS);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL, int.class, POS);
		}
		return field;
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
	
	public JType asJType(){
		return clazz;
	}
	
	private void initConstructor(){
		JMethod cons = clazz.constructor(JMod.PUBLIC);
		JVar f = getDistField();
		cons.body().assign(refThis(f), cons.param(f.type(), DIST));
		f = getTokenQField();
		cons.body().assign(refThis(f), cons.param(f.type(), TOKEN_Q));
		f = getStackDequeField();
		cons.body().assign(refThis(f), cons.param(f.type(), STACK_DEQ));
		f = getModField();
		cons.body().assign(refThis(f), cons.param(f.type(), MOD));
		f = getLexerPosField();
		cons.body().assign(refThis(f), cons.param(f.type(), POS));
		f = getCompletedField();
		cons.body().assign(refThis(f), cons.param(f.type(), COMPLETED));
	}
	
	private static JAssignmentTarget refThis(JVar field){
		return JExpr.ref(JExpr._this(), field);
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
