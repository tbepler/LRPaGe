package bepler.lrpage.code.generator.parser.repair;

import com.sun.codemodel.ClassType;
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

public class ModClass {
	
	private static final String TOKEN = "token";
	private static final String INDEX = "index";
	private static final String TYPE = "type";

	private static final String CLASS_NAME = "Mod";
	private static final String TYPE_ENUM = "ModTypes";
	
	private static final String DELETION = "DELETION";
	private static final String INSERTION = "INSERTION";
	private static final String REPLACEMENT = "REPLACEMENT";
	
	private static final int PUBLIC_FINAL = JMod.PUBLIC + JMod.FINAL;
	
	private final JCodeModel model;
	private final JDefinedClass iNode;
	private final JDefinedClass clazz;
	private final JDefinedClass typeEnum;
	
	public ModClass(JDefinedClass parser, JDefinedClass iNode){
		model = parser.owner();
		this.iNode = iNode;
		clazz = initModClass(parser);
		typeEnum = initTypeEnum(parser);
		initConstructor();
	}
	
	public JExpression newDeletionMod(JVar index, JVar token){
		return JExpr._new(clazz).arg(typeEnum.enumConstant(DELETION)
				.arg(index).arg(token));
	}
	
	public JExpression newInsertionMod(JVar index, JVar token){
		return JExpr._new(clazz).arg(typeEnum.enumConstant(INSERTION)
				.arg(index).arg(token));
	}
	
	public JExpression newReplacementMod(JVar index, JVar token){
		return JExpr._new(clazz).arg(typeEnum.enumConstant(REPLACEMENT)
				.arg(index).arg(token));
	}
	
	public JExpression getReplacementTypeLabel(){
		typeEnum.enumConstant(REPLACEMENT);
		return JExpr.ref(REPLACEMENT);
	}
	
	public JExpression getInsertionTypeLabel(){
		typeEnum.enumConstant(INSERTION);
		return JExpr.ref(INSERTION);
	}
	
	public JExpression getDeletionTypeLabel(){
		typeEnum.enumConstant(DELETION);
		return JExpr.ref(DELETION);
	}
	
	public JVar getTokenField(){
		JVar field = clazz.fields().get(TOKEN);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL, iNode, TOKEN);
		}
		return field;
	}
	
	public JVar getIndexField(){
		JVar field = clazz.fields().get(INDEX);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL,
					int.class, INDEX);
		}
		return field;
	}
	
	public JVar getTypeField(){
		JVar field = clazz.fields().get(TYPE);
		if(field == null){
			field = clazz.field(PUBLIC_FINAL,
					typeEnum, TYPE);
		}
		return field;
	}
	
	public JType asJType(){
		return clazz;
	}
	
	private void initConstructor(){
		JMethod cons = clazz.constructor(JMod.PUBLIC);
		JVar typeField = getTypeField();
		cons.body().assign(refThis(typeField),
				cons.param(typeField.type(), TYPE));
		JVar indexField = getIndexField();
		cons.body().assign(refThis(indexField),
				cons.param(indexField.type(), INDEX));
		JVar tokenField = getTokenField();
		cons.body().assign(refThis(tokenField),
				cons.param(tokenField.type(), TOKEN));
	}
	
	private static JAssignmentTarget refThis(JVar field){
		return JExpr.ref(JExpr._this(), field);
	}
	
	private JDefinedClass initTypeEnum(JDefinedClass parser){
		try {
			return parser._class(JMod.PRIVATE+JMod.STATIC, TYPE_ENUM, ClassType.ENUM);
		} catch (JClassAlreadyExistsException e) {
			JDefinedClass c = model._getClass(parser.fullName()+"."+TYPE_ENUM);
			assert(c != null);
			return c;
		}
	}
	
	private JDefinedClass initModClass(JDefinedClass parser){
		JDefinedClass c;
		try {
			c = parser._class(JMod.PRIVATE+JMod.STATIC, CLASS_NAME);
		} catch (JClassAlreadyExistsException e) {
			c = model._getClass(parser.fullName()+"."+CLASS_NAME);
		}
		assert(c != null);
		return c;
	}
	
}
