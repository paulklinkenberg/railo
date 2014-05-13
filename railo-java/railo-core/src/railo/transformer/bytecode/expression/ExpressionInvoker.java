package railo.transformer.bytecode.expression;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

import railo.transformer.TransformerException;
import railo.transformer.bytecode.BytecodeContext;
import railo.transformer.bytecode.expression.var.UDF;
import railo.transformer.bytecode.util.ExpressionUtil;
import railo.transformer.bytecode.util.Types;
import railo.transformer.expression.Expression;
import railo.transformer.expression.Invoker;
import railo.transformer.expression.var.DataMember;
import railo.transformer.expression.var.Member;

public final class ExpressionInvoker extends ExpressionBase implements Invoker {

    // Object getCollection (Object,String)
    private final static Method GET_COLLECTION = new Method("getCollection",
			Types.OBJECT,
			new Type[]{Types.OBJECT,Types.STRING}
			);
    
    // Object get (Object,String)
    private final static Method GET = new Method("get",
			Types.OBJECT,
			new Type[]{Types.OBJECT,Types.STRING}
			);

    // Object getFunction (Object,String,Object[])
    private final static Method GET_FUNCTION = new Method("getFunction",
			Types.OBJECT,
			new Type[]{Types.OBJECT,Types.STRING,Types.OBJECT_ARRAY}
			);
    
    // Object getFunctionWithNamedValues (Object,String,Object[])
    private final static Method GET_FUNCTION_WITH_NAMED_ARGS = new Method("getFunctionWithNamedValues",
			Types.OBJECT,
			new Type[]{Types.OBJECT,Types.STRING,Types.OBJECT_ARRAY}
			);
	
    
	private Expression expr;
	private List<Member> members=new ArrayList<Member>();

	public ExpressionInvoker(Expression expr) {
		super(expr.getFactory(),expr.getStart(),expr.getEnd());
		this.expr=expr;
	}

	@Override
	public Type _writeOut(BytecodeContext bc, int mode)	throws TransformerException {

    	GeneratorAdapter adapter = bc.getAdapter();
    	
		Type rtn=Types.OBJECT;
		int count=members.size();
		
		for(int i=0;i<count;i++) {
    		adapter.loadArg(0);
		}
    	
		expr.writeOut(bc, Expression.MODE_REF);
		
		for(int i=0;i<count;i++) {
			Member member=members.get(i);
    		
			// Data Member
			if(member instanceof DataMember)	{
				((DataMember)member).getName().writeOut(bc, MODE_REF);
				adapter.invokeVirtual(Types.PAGE_CONTEXT,((i+1)==count)?GET:GET_COLLECTION);
				rtn=Types.OBJECT;
			}
			
			// UDF
			else if(member instanceof UDF) {
				UDF udf=(UDF) member;
				
				udf.getName().writeOut(bc, MODE_REF);
				ExpressionUtil.writeOutExpressionArray(bc, Types.OBJECT, udf.getArguments());
				
				adapter.invokeVirtual(Types.PAGE_CONTEXT,udf.hasNamedArgs()?GET_FUNCTION_WITH_NAMED_ARGS:GET_FUNCTION);
				rtn=Types.OBJECT;
				
			}
		}
		
		return rtn;
	}

	/**
	 *
	 * @see railo.transformer.expression.Invoker#addMember(railo.transformer.expression.var.Member)
	 */
	@Override
	public void addMember(Member member) {
		members.add(member);
	}

	/**
	 *
	 * @see railo.transformer.expression.Invoker#getMembers()
	 */
	@Override
	public List<Member> getMembers() {
		return members;
	}

	@Override
	public Member removeMember(int index) {
		return members.remove(index);
	}

}
