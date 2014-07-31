package bepler.lrpage.code.generator;

import java.util.Deque;
import java.util.LinkedList;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

/**
 * This class generates a parse stack inner class
 * of the parser class.
 * 
 * @author Tristan Bepler
 *
 */
public class ParseStackGenerator {
	
	private static final String PARSE_STACK = "ParseStack";
	private static final String CUR_STATE = "curState";
	private static final String POP = "pop";
	private static final String PUSH = "push";
	private static final String CLONE = "clone";
	
	private final JCodeModel model;
	private final JDefinedClass parser;
	private final JDefinedClass iNode;
	private final JDefinedClass parseStack;
	private final JVar nodeStack;
	private final JVar stateStack;
	private final JMethod curStateMethod;
	private final JMethod popMethod;
	private final JMethod pushMethod;
	private final JMethod cloneMethod;
	
	public ParseStackGenerator(JCodeModel model, JDefinedClass parser, JDefinedClass iNode)
			throws JClassAlreadyExistsException{
		this.model = model;
		this.parser = parser;
		this.iNode = iNode;
		parseStack = this.generateParseStackClass();
		nodeStack = this.appendNodeStackField();
		stateStack = this.appendStateStackField();
		this.defineDefaultConstructor();
		curStateMethod = this.defineCurStateMethod();
		popMethod = this.definePopMethod();
		pushMethod = this.definePushMethod();
		cloneMethod = this.defineCloneMethod();
	}
	
	public JDefinedClass getParseStack(){
		return parseStack;
	}
	
	public JMethod getCurStateMethod(){
		return curStateMethod;
	}
	
	public JMethod getPopMethod(){
		return popMethod;
	}
	
	public JMethod getPushMethod(){
		return pushMethod;
	}
	
	public JMethod getCloneMethod(){
		return cloneMethod;
	}
	
	private JMethod defineCloneMethod(){
		//define a clone constructor
		JMethod cons = parseStack.constructor(JMod.PRIVATE);
		cons.body().assign(nodeStack,
				JExpr._new(model.ref(LinkedList.class).narrow(iNode))
					.arg(cons.param(model.ref(Deque.class).narrow(iNode), "nodes")));
		cons.body().assign(stateStack,
				JExpr._new(model.ref(LinkedList.class).narrow(Integer.class))
					.arg(cons.param(model.ref(Deque.class).narrow(Integer.class), "states")));
		JMethod method = parseStack.method(JMod.PUBLIC, parseStack, CLONE);
		method.annotate(Override.class);
		method.body()._return(JExpr._new(parseStack).arg(nodeStack).arg(stateStack));
		return method;
	}
	
	private JMethod definePushMethod(){
		JMethod method = parseStack.method(JMod.PUBLIC, void.class, PUSH);
		method.body().invoke(nodeStack, "push").arg(method.param(iNode, "node"));
		method.body().invoke(stateStack, "push").arg(method.param(int.class, "state"));
		return method;
	}
	
	private JMethod definePopMethod(){
		JMethod method = parseStack.method(JMod.PUBLIC, iNode, POP);
		method.body().invoke(stateStack, "pop");
		method.body()._return(JExpr.invoke(nodeStack, "pop"));
		return method;
	}
	
	private JMethod defineCurStateMethod(){
		JMethod method = parseStack.method(JMod.PUBLIC, int.class, CUR_STATE);
		method.body()._if(JExpr.invoke(stateStack, "isEmpty"))
			._then()._return(JExpr.lit(0));
		method.body()._return(JExpr.invoke(stateStack, "peek"));
		return method;
	}
	
	private JMethod defineDefaultConstructor(){
		JMethod cons = parseStack.constructor(JMod.PUBLIC);
		cons.body().assign(nodeStack,
				JExpr._new(model.ref(LinkedList.class).narrow(iNode)));
		cons.body().assign(stateStack, 
				JExpr._new(model.ref(LinkedList.class).narrow(Integer.class)));
		return cons;
	}
	
	private JVar appendStateStackField(){
		JVar field = parseStack.field(
				JMod.PRIVATE+JMod.FINAL,
				model.ref(Deque.class).narrow(Integer.class),
				"stateStack");
		return field;
	}
	
	private JVar appendNodeStackField(){
		JVar field = parseStack.field(
				JMod.PRIVATE+JMod.FINAL,
				model.ref(Deque.class).narrow(iNode),
				"nodeStack");
		return field;
	}
	
	private JDefinedClass generateParseStackClass() throws JClassAlreadyExistsException{
		return CodeGenerator.appendJDocHeader(parser._class(JMod.PRIVATE+JMod.STATIC, PARSE_STACK));
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
