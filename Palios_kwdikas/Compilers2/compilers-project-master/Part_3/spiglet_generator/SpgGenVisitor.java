package spiglet_generator;

import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import symbol_table.*;
import my_type.*;

public class SpgGenVisitor extends GJDepthFirst<MyType, MyType> {
	public String result;
	private Label L;
	private SymbolTable symTable;

	public SpgGenVisitor(SymbolTable symTable) {
		this.L = new Label();
        this.symTable = symTable;   
	}

	private void initVtables() {
		for (Class_t cl : symTable.ST) {
			if (cl.isMain)
				continue;
			String label = L.new_Class_label(cl.getName());
			String vtable = new String("TEMP " + ++symTable.glob_temp_cnt);
			String temp = new String("TEMP " + ++symTable.glob_temp_cnt);
			this.result += "MOVE " + vtable + " " + label + "\n";
			int offset = cl.meth_cnt;
			this.result += "MOVE " + temp + " HALLOCATE " + offset*4 + "\n";
			this.result += "HSTORE " + vtable + " 0 " + temp + "\n";
			for (int i = 0 ; i < offset ; i++) {
			String newTemp = new String("TEMP " + ++symTable.glob_temp_cnt);
				Method_t meth = cl.classMethods.get(i);
				this.result += "MOVE " + newTemp + " " + meth.comesFrom.getName() + "_" + meth.getName() + "\n";
				this.result += "HSTORE " + temp + " " + i*4 + " " + newTemp + "\n";
			}
			this.result += "\n";
    	}
	}


	/**
	* f0 -> MainClass()
	* f1 -> ( TypeDeclaration() )*
	* f2 -> <EOF>
	*/
	public MyType visit(Goal n, MyType argu) throws Exception {
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	* f0 -> "class"
	* f1 -> Identifier()
	* f2 -> "{"
	* f3 -> "public"
	* f4 -> "static"
	* f5 -> "void"
	* f6 -> "main"
	* f7 -> "("
	* f8 -> "String"
	* f9 -> "["
	* f10 -> "]"
	* f11 -> Identifier()
	* f12 -> ")"
	* f13 -> "{"
	* f14 -> ( VarDeclaration() )*
	* f15 -> ( Statement() )*
	* f16 -> "}"
	* f17 -> "}"
	*/
	public MyType visit(MainClass n, MyType argu) throws Exception {
		this.result = new String("MAIN\n\n");
		String id = n.f1.accept(this, argu).getName();
        Class_t mainclazz = symTable.contains(id);
        Method_t meth = mainclazz.getMethod("main");
        initVtables();
		n.f14.accept(this, meth);
		n.f15.accept(this, meth);
		this.result += new String("END\n");
		return null;
	}

	/**
	* f0 -> ClassDeclaration()
	*       | ClassExtendsDeclaration()
	*/
	public MyType visit(TypeDeclaration n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);	
	}

	/**
	* f0 -> "class"
	* f1 -> Identifier()
	* f2 -> "{"
	* f3 -> ( VarDeclaration() )*
	* f4 -> ( MethodDeclaration() )*
	* f5 -> "}"
	*/
	public MyType visit(ClassDeclaration n, MyType argu) throws Exception {
		n.f0.accept(this, argu);
        String s = n.f1.accept(this, argu).getName();
        Class_t class_id = symTable.contains(s);
        n.f3.accept(this, new Method_t(null, class_id.getName()));
        n.f4.accept(this, class_id);
        return null;
	}

	/**
	* f0 -> "class"
	* f1 -> Identifier()
	* f2 -> "extends"
	* f3 -> Identifier()
	* f4 -> "{"
	* f5 -> ( VarDeclaration() )*
	* f6 -> ( MethodDeclaration() )*
	* f7 -> "}"
	*/
	public MyType visit(ClassExtendsDeclaration n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        String s = n.f1.accept(this, argu).getName();
        Class_t class_id = symTable.contains(s);
        n.f1.accept(this, argu);
        n.f3.accept(this, argu);
        n.f5.accept(this, new Method_t(null, class_id.getName()));
        n.f6.accept(this, class_id);
        return null;
	}

	/**
	* f0 -> Type()
	* f1 -> Identifier()
	* f2 -> ";"
	*/
	public MyType visit(VarDeclaration n, MyType argu) throws Exception {
		Method_t meth = (Method_t) argu;
		String varName = n.f1.f0.toString();
		if (meth.comesFrom != null) { 												// is a variable of a function
			String newTemp = new String("TEMP " + ++symTable.glob_temp_cnt);
			if ((meth = meth.comesFrom.getMethod(meth.getName())) != null) {		// if you found method
				meth.addTempToVar(varName, newTemp);
			} else
				throw new Exception("VarDeclaration Errror 1");
			this.result += new String("MOVE " + newTemp + " 0\n");
		} else {																	// is a var (field) of a class
			Class_t cl = symTable.contains(meth.getName());
			if (cl == null)															// do nothing for now
				throw new Exception("VarDeclaration Errror 2");
		}
		return null;
	}

	/**
	* f0 -> "public"
	* f1 -> Type()
	* f2 -> Identifier()
	* f3 -> "("
	* f4 -> ( FormalParameterList() )?
	* f5 -> ")"
	* f6 -> "{"
	* f7 -> ( VarDeclaration() )*
	* f8 -> ( Statement() )*
	* f9 -> "return"
	* f10 -> Expression()
	* f11 -> ";"
	* f12 -> "}"
	*/
	public MyType visit(MethodDeclaration n, MyType argu) throws Exception {
		String methName = n.f2.accept(this, argu).getName();
        Method_t meth = ((Class_t) argu).getMethod(methName);
        this.result += "\n" + ((Class_t) argu).getName()+ "_" + methName + "[" + (meth.par_cnt+1) + "]\nBEGIN\n";
        n.f7.accept(this, meth);
        n.f8.accept(this, meth);
        Variable_t retType = (Variable_t) n.f10.accept(this, meth);
        this.result += "RETURN " + retType.getType() + "\nEND\n";
        return null;
	}

	/**
	* f0 -> FormalParameter()
	* f1 -> FormalParameterTail()
	*/
	public MyType visit(FormalParameterList n, MyType argu) throws Exception {
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return null;
	}

	/**
	* f0 -> Type()
	* f1 -> Identifier()
	*/
	public MyType visit(FormalParameter n, MyType argu) throws Exception {
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return null;
	}

	/**
	* f0 -> ( FormalParameterTerm() )*
	*/
	public MyType visit(FormalParameterTail n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> ","
	* f1 -> FormalParameter()
	*/
	public MyType visit(FormalParameterTerm n, MyType argu) throws Exception {
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return null;
	}

	/**
	* f0 -> ArrayType()
	*       | BooleanType()
	*       | IntegerType()
	*       | Identifier()
	*/
	public MyType visit(Type n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> "int"
	* f1 -> "["
	* f2 -> "]"
	*/
	public MyType visit(ArrayType n, MyType argu) throws Exception {
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return null;
	}

	/**
	* f0 -> "boolean"
	*/
	public MyType visit(BooleanType n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> "int"
	*/
	public MyType visit(IntegerType n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> Block()
	*       | AssignmentStatement()
	*       | ArrayAssignmentStatement()
	*       | IfStatement()
	*       | WhileStatement()
	*       | PrintStatement()
	*/
	public MyType visit(Statement n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> "{"
	* f1 -> ( Statement() )*
	* f2 -> "}"
	*/
	public MyType visit(Block n, MyType argu) throws Exception {
		n.f1.accept(this, argu);
		return null;
	}

	/**
	* f0 -> Identifier()
	* f1 -> "="
	* f2 -> Expression()
	* f3 -> ";"
	*/
	public MyType visit(AssignmentStatement n, MyType argu) throws Exception {
		String id = n.f0.accept(this, argu).getName();
		String expr = ((Variable_t) n.f2.accept(this, argu)).getType();
		Variable_t var;
		// if a local var
		Method_t meth = (Method_t) argu;
		if (meth != null) { 
			var = meth.methContainsVar(id);
			if (var == null) { // didnt find the var in method, so its a field of the class
				Class_t cl = symTable.contains(meth.comesFrom.getName());
				if (cl == null) {  throw new Exception("something went wrong at AssignmentStatement 2"); }
				var = cl.classContainsVarReverse(id);
				if (var != null)							// class field
				this.result += "HSTORE " + " TEMP 0 " + var.getVarNum()*4 + " " + expr +"\n";
				return null;
			}
			this.result += "MOVE " +  var.getTemp() + " " + expr +"\n";
		}
		else { // if a field of a class
			Class_t cl = symTable.contains(meth.comesFrom.getName());
			if (cl == null) {  throw new Exception("something went wrong at AssignmentStatement 2"); }
			var = cl.classContainsVarReverse(id);
			if (var != null)							// class field
				this.result += "HSTORE " + " TEMP 0 " + var.getVarNum()*4 + " " + expr +"\n";
		}
		return null;
	}

	/**
	* f0 -> Identifier()
	* f1 -> "["
	* f2 -> Expression()
	* f3 -> "]"
	* f4 -> "="
	* f5 -> Expression()
	* f6 -> ";"
	*/
	public MyType visit(ArrayAssignmentStatement n, MyType argu) throws Exception {
		String length = new String("TEMP " + ++symTable.glob_temp_cnt);
		String cond = new String("TEMP " + ++symTable.glob_temp_cnt);
		String error = L.new_label();
		String noerror = L.new_label();
		String array = ((Variable_t) n.f0.accept(this, argu)).getType();
		this.result += "HLOAD " + length + " " + array + " 0\n"; 			// load real size to length
		String pos = ((Variable_t) n.f2.accept(this, argu)).getType();		
		this.result += "MOVE " + cond + " LT " + pos + " " + length + "\n";	// if pos < arr.length 
		this.result += "CJUMP " + cond + " " + error + "\n";
		this.result += "MOVE " + cond + " LT " + pos + " 0\n";				// if arr.length > 0 g
		String one = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + one + " 1\n";
		this.result += "MOVE " + cond + " MINUS " + one + " " + cond + "\n";
		this.result += "CJUMP " + cond + " " + error + "\n";
		this.result += "JUMP " + noerror + "\n";
		this.result += error + " NOOP\n";
		this.result += "ERROR\n" + noerror + " NOOP\n";						
		String temp_array = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + temp_array + " " + array + "\n";			// temp_array = &array
		this.result += "MOVE " + temp_array + " PLUS " + temp_array + " 4\n";
		String temp = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + temp + " TIMES " + pos + " 4\n";
		this.result += "MOVE " + temp_array + " PLUS " + temp_array + " " + temp + "\n";
		String expr = ((Variable_t) n.f5.accept(this, argu)).getType();
		this.result += "HSTORE " + temp_array + " 0 " + expr + "\n";
		return new Variable_t(temp_array, null);
	}

	/**
	* f0 -> "if"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> Statement()
	* f5 -> "else"
	* f6 -> Statement()
	*/
	public MyType visit(IfStatement n, MyType argu) throws Exception {
		String elselabel = L.new_label();
		String endlabel = L.new_label();
		String cond = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += "CJUMP " + cond + " " + elselabel + "\n"; //if cond not true go to elselabel
		n.f4.accept(this, argu);
		this.result += "JUMP " + endlabel + "\n" + elselabel + " NOOP\n";
		n.f6.accept(this, argu);
		this.result += endlabel + " NOOP\n";
		return null;
	}

	/**
	* f0 -> "while"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> Statement()
	*/
	public MyType visit(WhileStatement n, MyType argu) throws Exception {
		String lstart = L.new_label();
		String lend = L.new_label();
		String cond = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += lstart + " NOOP\n";
		String expr = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += "MOVE " + cond + " " + expr + "\n";
		this.result += "CJUMP " + cond + " " + lend + "\n";
		n.f4.accept(this, argu);
		this.result += "JUMP " + lstart + "\n" + lend + " NOOP\n";
		return null;
	}

	/**
	* f0 -> "System.out.println"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> ";"
	*/
	public MyType visit(PrintStatement n, MyType argu) throws Exception {
		String t = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += "PRINT " + t + "\n";
		return null;
	}

	/**
	* f0 -> AndExpression()
	*       | CompareExpression()
	*       | PlusExpression()
	*       | MinusExpression()
	*       | TimesExpression()
	*       | ArrayLookup()
	*       | ArrayLength()
	*       | MessageSend()
	*       | Clause()
	*/
	public MyType visit(Expression n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> Clause()
	* f1 -> "&&"
	* f2 -> Clause()
	*/
	public MyType visit(AndExpression n, MyType argu) throws Exception {
		String label = L.new_label();
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t1 = ((Variable_t) n.f0.accept(this, argu)).getType();
		this.result += new String("MOVE " + ret + " " + t1 + "\n" + "CJUMP " + t1 + " " + label + "\n");
		String t2 = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += new String("MOVE " + ret + " " + t2 + "\n" + label + " NOOP\n");
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "<"
	* f2 -> PrimaryExpression()
	*/
	public MyType visit(CompareExpression n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t1 = ((Variable_t) n.f0.accept(this, argu)).getType();
		String t2 = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += new String("MOVE " + ret + " LT " +  t1 + " " + t2 + "\n");
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "+"
	* f2 -> PrimaryExpression()
	*/
	public MyType visit(PlusExpression n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t1 = ((Variable_t) n.f0.accept(this, argu)).getType();
		String t2 = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += new String("MOVE " + ret + " PLUS " + t1 + " " + t2 + "\n");
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "-"
	* f2 -> PrimaryExpression()
	*/
	public MyType visit(MinusExpression n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t1 = ((Variable_t) n.f0.accept(this, argu)).getType();
		String t2 = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += new String("MOVE " + ret + " MINUS " +  t1 + " " + t2 + "\n");
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "*"
	* f2 -> PrimaryExpression()
	*/
	public MyType visit(TimesExpression n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t1 = ((Variable_t) n.f0.accept(this, argu)).getType();
		String t2 = ((Variable_t) n.f2.accept(this, argu)).getType();
		this.result += new String("MOVE " + ret + " TIMES " + t1 + " " + t2 + "\n");
		return new Variable_t(ret, null);
		
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "["
	* f2 -> PrimaryExpression()
	* f3 -> "]"
	*/
	public MyType visit(ArrayLookup n, MyType argu) throws Exception {
		String length = new String("TEMP " + ++symTable.glob_temp_cnt);
		String cond = new String("TEMP " + ++symTable.glob_temp_cnt);
		String error = L.new_label();
		String noerror = L.new_label();
		String array = ((Variable_t) n.f0.accept(this, argu)).getType();
		this.result += "HLOAD " + length + " " + array + " 0\n"; 			// load real size to length
		String pos = ((Variable_t) n.f2.accept(this, argu)).getType();		
		this.result += "MOVE " + cond + " LT " + pos + " " + length + "\n";	// if pos < arr.length 
		this.result += "CJUMP " + cond + " " + error + "\n";
		this.result += "MOVE " + cond + " LT " + pos + " 0\n";				// if arr.length > 0 g
		String one = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + one + " 1\n";
		this.result += "MOVE " + cond + " MINUS " + one + " " + cond + "\n";
		this.result += "CJUMP " + cond + " " + error + "\n";
		this.result += "JUMP " + noerror + "\n";
		this.result += error + " NOOP\n";
		this.result += "ERROR\n" + noerror + " NOOP\n";						
		String temp_array = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + temp_array + " " + array + "\n";			// temp_array = &array
		this.result += "MOVE " + temp_array + " PLUS " + temp_array + " 4\n";
		String temp = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + temp + " TIMES " + pos + " 4\n";
		this.result += "MOVE " + temp_array + " PLUS " + temp_array + " " + temp + "\n";
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "HLOAD " + ret + " " + temp_array + " 0\n";
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "."
	* f2 -> "length"
	*/
	public MyType visit(ArrayLength n, MyType argu) throws Exception {
		String len = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t = ((Variable_t) n.f0.accept(this, argu)).getType();
		this.result += "HLOAD " + len + " " + t + " 0\n"; 
		return new Variable_t(len, null);
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "."
	* f2 -> Identifier()
	* f3 -> "("
	* f4 -> ( ExpressionList() )?
	* f5 -> ")"
	*/
	public MyType visit(MessageSend n, MyType argu) throws Exception {
		Variable_t obj = (Variable_t) n.f0.accept(this, argu);	// obj.temp is type
        Variable_t func = (Variable_t) n.f2.accept(this, argu);
        String className = obj.getTemp();
        String objTemp = obj.getType();
        Class_t cl = symTable.contains(className);
        Method_t meth = cl.getMethod(func.getName());
        int offset = meth.meth_num - 1;
		String vtable_addr = new String("TEMP " + ++symTable.glob_temp_cnt);
		String thisTemp = new String("TEMP " + ++symTable.glob_temp_cnt);
		String methTemp = new String("TEMP " + ++symTable.glob_temp_cnt);
        this.result += "MOVE " + thisTemp + " " + objTemp + "\n"; 	// load the address of vtable
        this.result += "HLOAD " + vtable_addr + " " + thisTemp + " 0\n"; 	// load the address of vtable
        this.result += "HLOAD " + methTemp + " " + vtable_addr + " " + (offset*4) + "\n";	// load the right method from vtable
        // add params to method call
		Method_t params = (Method_t) n.f4.accept(this, argu);
		String parStr = new String(" ");
        if (n.f4.present()) {														// if meth has params
	        for (int i = 0 ; i < params.methodParams.size() ; i++) {				// for every par
	        	Variable_t var = ((Variable_t) params.methodParams.get(i));
				String parTemp = new String("TEMP " + ++symTable.glob_temp_cnt);	
	        	this.result += "MOVE " + parTemp + " " + var.getType() + "\n";
	        	parStr += parTemp + " ";
	        }
	    }
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
        this.result += "MOVE " + ret + " CALL " + methTemp + "( " + thisTemp + parStr + ")\n";
        Variable_t v = new Variable_t(ret, null);
        v.var_temp = meth.getType();
		return v; 
	}

 /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    public MyType visit(ExpressionList n, MyType argu) throws Exception {
        Variable_t expr =  (Variable_t) n.f0.accept(this, argu); //na tsekarw th seira 
        Method_t meth = (Method_t) n.f1.accept(this, argu);
        meth.methodParams.addLast(expr);
        return meth;
    }

    /**
    * f0 -> ( ExpressionTerm() )*
    */
    public MyType visit(ExpressionTail n, MyType argu) throws Exception {
        Method_t meth = new Method_t(null, null);
        if (n.f0.present())                 // create a linked list of variables. (parameters list)
            for (int i = 0 ; i < n.f0.size() ; i++)
                meth.methodParams.addLast( (Variable_t)n.f0.nodes.get(i).accept(this, argu) );
        return meth;
    }

    /**
    * f0 -> ","
    * f1 -> Expression()
    */
    public MyType visit(ExpressionTerm n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        return (Variable_t) n.f1.accept(this, argu);
    }



	/**
	* f0 -> NotExpression()
	*       | PrimaryExpression()
	*/
	public MyType visit(Clause n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> IntegerLiteral()
	*       | TrueLiteral()
	*       | FalseLiteral()
	*       | Identifier()
	*       | ThisExpression()
	*       | ArrayAllocationExpression()
	*       | AllocationExpression()
	*       | BracketExpression()
	*/
	public MyType visit(PrimaryExpression n, MyType argu) throws Exception {
		return n.f0.accept(this, argu);
	}

	/**
	* f0 -> <INTEGER_LITERAL>
	*/
	public MyType visit(IntegerLiteral n, MyType argu) throws Exception {
		String ret = "TEMP " + ++symTable.glob_temp_cnt;
		this.result += "MOVE " + ret + " " + n.f0.toString() + "\n";
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> "true"
	*/
	public MyType visit(TrueLiteral n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += new String("MOVE " + ret + " 1\n");
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> "false"
	*/
	public MyType visit(FalseLiteral n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += new String("MOVE " + ret + " 0\n");
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> <IDENTIFIER>
	*/
	public MyType visit(Identifier n, MyType argu) throws Exception {
		String id = n.f0.toString();
		if (argu == null) 
			return new Variable_t(null, id);
		Class_t cl = symTable.contains(argu.getName());
		Variable_t var;
		if (cl != null) {									// if argu is a class name
			var = cl.classContainsVarReverse(id);
			if (var != null) {								// and id is a field of that class
				Variable_t v = new Variable_t(var.getTemp(), id);
				v.var_temp = cl.getName();
				return v;
			} else {										// is a method
				Method_t meth = cl.getMethod(id);
				if (meth == null) { throw new Exception("something went wrong 1"); }
				return new Variable_t(null, id);
			}
		} else {											// if argu is a method name 
			Method_t meth = (Method_t) argu;
			var = meth.methContainsVar(id);				
			if (var != null) {								// if a parameter or a local var
				Variable_t v = new Variable_t(var.getTemp(), id);
				v.var_temp = var.getType();
				return v;
			} else {										// a field of class
				cl = symTable.contains(meth.comesFrom.getName());
				if (cl == null) {  throw new Exception("something went wrong 2"); }
				var = cl.classContainsVarReverse(id);
				if (var == null) {  return new Variable_t(null, id);  }
				String newTemp = "TEMP " + ++symTable.glob_temp_cnt;
				this.result += "HLOAD " + newTemp + " TEMP 0 " + var.getVarNum()*4 + "\n";
				Variable_t v = new Variable_t(newTemp, id);
				v.var_temp = var.getType();
				return v;
			}
		}
	}

	/**
	* f0 -> "this"
	*/
	public MyType visit(ThisExpression n, MyType argu) throws Exception {
		Variable_t var = new Variable_t("TEMP 0", "this");
		Class_t cl = ((Method_t) argu).comesFrom;
		var.var_temp = cl.getName();
		return var;
	}

	/**
	* f0 -> "new"
	* f1 -> "int"
	* f2 -> "["
	* f3 -> Expression()
	* f4 -> "]"
	*/
	public MyType visit(ArrayAllocationExpression n, MyType argu) throws Exception {
		String lstart = L.new_label();
		String lend = L.new_label();
		String noerror = L.new_label();
		String expr = ((Variable_t) n.f3.accept(this, argu)).getType();
		String zero = new String("TEMP " + ++symTable.glob_temp_cnt);
		String cnt = new String("TEMP " + ++symTable.glob_temp_cnt);
		String cond = new String("TEMP " + ++symTable.glob_temp_cnt);
		String size = new String("TEMP " + ++symTable.glob_temp_cnt);
		String alloc_sz = new String("TEMP " + ++symTable.glob_temp_cnt);
		String array = new String("TEMP " + ++symTable.glob_temp_cnt);
		String array_addr = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + cond + " LT " + expr + " 0\n"; 						// check if given length > 0
		this.result += "CJUMP " + cond + " " + noerror + "\n"; 
		this.result += "ERROR\n" + noerror + " NOOP\n";									
		this.result += "MOVE " + size + " PLUS " + expr + " 1\n"; 						// create room for arraylength 
		this.result += "MOVE " + alloc_sz + " TIMES " + size + " 4\n";					// *4 for bytes
		this.result += "MOVE " + array + " HALLOCATE " + alloc_sz + "\n"; 				// allocate
		this.result += "HSTORE " + array + " 0 " + expr + "\n";							// store array length in first position
		this.result += "MOVE " + array_addr + " " + array + "\nMOVE " + cnt + " 4\n";	// keep array address and init a counter 
		this.result += lstart + " NOOP\nMOVE " + cond + " LT " + cnt + " " + alloc_sz + "\n";
		this.result += "CJUMP " + cond + " " + lend + "\n"; 							// if !cond goto end 				
		this.result += "MOVE " + array + " PLUS " + array + " 4\n";						// &array++
		this.result += "MOVE " + zero + " 0\n";
		this.result += "HSTORE " + array + " 0 " + zero + "\n";
		this.result += "MOVE " + cnt + " PLUS " + cnt + " 4\n";							// cnt++
		this.result += "JUMP " + lstart + "\n" + lend + " NOOP\n";						// loop
		return new Variable_t(array_addr, null);
	}

	/**
	* f0 -> "new"
	* f1 -> Identifier()
	* f2 -> "("
	* f3 -> ")"
	*/
	public MyType visit(AllocationExpression n, MyType argu) throws Exception {
		String id = n.f1.accept(this, argu).getName();
        Class_t cl = symTable.contains(id);
		String t = new String("TEMP " + ++symTable.glob_temp_cnt);
		String vtable = new String("TEMP " + ++symTable.glob_temp_cnt);
		String vtable_addr = new String("TEMP " + ++symTable.glob_temp_cnt);
		String label = L.new_Class_label(cl.getName());
        this.result += "MOVE " + t + " HALLOCATE " + (1+cl.var_cnt)*4 + "\n";
        this.result += "MOVE " + vtable_addr + " " + label + "\n";
        this.result += "HLOAD " + vtable + " " + vtable_addr + " 0\n";
        this.result += "HSTORE " + t + " 0 " + vtable + "\n";
		String zero = new String("TEMP " + ++symTable.glob_temp_cnt);
        this.result += "MOVE " + zero + " 0\n";
        for (int i = 1 ; i <= cl.var_cnt ; i++)
        	this.result += "HSTORE " + t + " " + i*4 + " " + zero + "\n";
        this.result += "\n";
        Variable_t var = new Variable_t(t, id);
        var.var_temp = id;
		return var;
	}

	/**
	* f0 -> "!"
	* f1 -> Clause()
	*/
	public MyType visit(NotExpression n, MyType argu) throws Exception {
		String ret = new String("TEMP " + ++symTable.glob_temp_cnt);
		String t = ((Variable_t) n.f1.accept(this, argu)).getType();
		String one = new String("TEMP " + ++symTable.glob_temp_cnt);
		this.result += "MOVE " + one + " 1\n";
		this.result += "MOVE " + ret + " MINUS " + one + " " + t + "\n";
		return new Variable_t(ret, null);
	}

	/**
	* f0 -> "("
	* f1 -> Expression()
	* f2 -> ")"
	*/
	public MyType visit(BracketExpression n, MyType argu) throws Exception {
		return n.f1.accept(this, argu);
	}

}
