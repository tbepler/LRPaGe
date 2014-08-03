package bepler.lrpage.code.generator.parser.repair;

import java.util.Deque;
import java.util.Queue;

import bepler.lrpage.code.generator.parser.ParseStackGenerator;

import com.sun.codemodel.JAssignmentTarget;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
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
	private final JMethod compare;
	
	public Repair(ParseStackGenerator stack, Mod mod,
			JDefinedClass parser, JDefinedClass iNode){
		model = parser.owner();
		this.mod = mod;
		this.iNode = iNode;
		this.stack = stack.getParseStack();
		clazz = initRepairClass(parser);
		initConstructor();
		compare = initCompareToMethod();
	}
	
	public JExpression compare(JExpression _this, JExpression that){
		return JExpr.invoke(_this, compare).arg(that);
	}
	
	public JExpression newRepair(JExpression dist, JExpression tokenQ,
			JExpression stackDeq, JExpression mod, JExpression pos,
			JExpression completed){
		return JExpr._new(clazz).arg(dist).arg(tokenQ)
				.arg(stackDeq).arg(mod).arg(pos).arg(completed);
	}
	
	public JExpression getCompletedField(JVar instance){
		return JExpr.ref(instance, getCompletedField());
	}
	
	public JExpression getLexerPosField(JVar instance){
		return JExpr.ref(instance, getLexerPosField());
	}
	
	public JExpression getModField(JVar instance){
		return JExpr.ref(instance, getModField());
	}
	
	public JExpression getStackDequeField(JVar instance){
		return JExpr.ref(instance, getStackDequeField());
	}
	
	public JExpression getTokenQField(JVar instance){
		return JExpr.ref(instance, getTokenQField());
	}
	
	public JExpression getDistField(JVar instance){
		return JExpr.ref(instance, getDistField());
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
	
	private JMethod initCompareToMethod(){
		JMethod m = clazz.getMethod("compareTo", new JType[]{clazz});
		if(m == null){
			m = clazz.method(JMod.PUBLIC, int.class, "compareTo");
			m.annotate(Override.class);
			JVar other = m.param(clazz, "other");
			m.body()._if(other.eq(JExpr._null()))._then()._return(JExpr.lit(1));
			m.body()._return(JExpr._this().ref(getDistField()).minus(other.ref(getDistField())));
		}
		return m;
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
			JDefinedClass c = parser._class(JMod.PRIVATE+JMod.STATIC, CLASS_NAME);
			c._implements(model.ref(Comparable.class).narrow(c));
			return c;
		} catch (JClassAlreadyExistsException e) {
			JDefinedClass c = model._getClass(parser.fullName() + "." + CLASS_NAME);
			assert(c != null);
			return c;
		}
	}
	
	

}
