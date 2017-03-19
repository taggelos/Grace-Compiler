import syntaxtree.*;
import java.util.*;
import visitor.GJDepthFirst;
import attribute.*;
import symboltable.*;


public class TypeCheckerVisitor extends GJDepthFirst<Attribute, Attribute> {

	SymbolTable symbolTable;

    
	public TypeCheckerVisitor(SymbolTable symbolTable){
		this.symbolTable = symbolTable;
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
    * f8 -> "Attribute"
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
	public Attribute visit(MainClass n, Attribute argu) throws Exception {
		String identifier = n.f1.accept(this,argu).name;
		classType classObject = symbolTable.getClassByName(identifier); 

		methodType method = classObject.getMethodByName("main");

		n.f15.accept(this,method);

		return null;
	}

   
   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
	public Attribute visit(ClassDeclaration n, Attribute argu) throws Exception {
		String identifier = n.f1.accept(this,argu).name;
		classType classObject = symbolTable.getClassByName(identifier); 
		n.f4.accept(this,classObject);
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
	public Attribute visit(ClassExtendsDeclaration n, Attribute argu) throws Exception {
		String identifier = n.f1.accept(this,argu).name;
		classType classObject = symbolTable.getClassByName(identifier); 
		n.f6.accept(this,classObject);
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
	public Attribute visit(MethodDeclaration n, Attribute argu) throws Exception {
		String identifier = n.f2.accept(this,argu).name;
		classType ownerClass = (classType) argu;
		methodType method = ownerClass.getMethodByName(identifier);

		String returnType = ((variableType)n.f10.accept(this,method)).getType(symbolTable, method);
		if(!variableType.matchType(symbolTable,method.getType(),returnType)){
			throw new Exception("Error: return type doesn't match method's declaration.");
		}     
		n.f8.accept(this,method);
		return null;
	}

  
   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
	public Attribute visit(ArrayType n, Attribute argu) throws Exception {
		return new variableType("int[]",null);
	}

   /**
    * f0 -> "boolean"
    */
	public Attribute visit(BooleanType n, Attribute argu) throws Exception {
		return new variableType("boolean",null);
	}

   /**
    * f0 -> "int"
    */
	public Attribute visit(IntegerType n, Attribute argu) throws Exception {
		return new variableType("int",null);
	}


   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
	public Attribute visit(Block n, Attribute argu) throws Exception {
		n.f1.accept(this,argu);
		return null;
	}



   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
	public Attribute visit(AssignmentStatement n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType = ((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if(!variableType.matchType(symbolTable, leftType, rightType)){
			throw new Exception("Error: Invalid assignment. Not applicable to given types: "+leftType+" and "+rightType);
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
   public Attribute visit(ArrayAssignmentStatement n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String middleType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f5.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int[]") || !middleType.equals("int") || !rightType.equals("int")){
			throw new Exception("Error: Invalid array assignmet. Not applicable to given types: "+leftType+", "+middleType+" and "+rightType);
		}
		return null;
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
	public Attribute visit(IfStatement n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String type = ((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if(!type.equals("boolean")){
			throw new Exception("Error: condition of if statement must be of type boolean. Not applicable to "+type);
		}
		n.f4.accept(this,argu);
		n.f6.accept(this,argu);
		return null;
	}

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
	public Attribute visit(WhileStatement n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String type = ((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if(!type.equals("boolean")){
			throw new Exception("Error: condition of while statement must be of type boolean. Not applicable to "+type);
		}
		n.f4.accept(this,argu);
		return null;
	}

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
	public Attribute visit(PrintStatement n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String type = ((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if(!type.equals("boolean") && !type.equals("int")){
			throw new Exception("Error: Printing only applies to int and boolean types. Not applicable to "+type);
		}
		n.f2.accept(this,argu);
		return null;
	}

  
   /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
	public Attribute visit(AndExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("boolean",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("boolean") || !rightType.equals("boolean")){
			throw new Exception("Error: Invalid use of && operator. Not applicable to given types: "+leftType+" and "+rightType);
		}
		return _ret;
	}

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
	public Attribute visit(CompareExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("boolean",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int") || !rightType.equals("int")){
			throw new Exception("Error: Invalid use of < operator. Not applicable to given types: "+leftType+" and "+rightType);
		}
		return _ret;
	}

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
	public Attribute visit(PlusExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("int",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int") || !rightType.equals("int")){
			throw new Exception("Error: Invalid use of + operator. Not applicable to given types: "+leftType+" and "+rightType);
		}
		return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
	public Attribute visit(MinusExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("int",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int") || !rightType.equals("int")){
			throw new Exception("Error: Invalid use of - operator. Not applicable to given types: "+leftType+" and "+rightType);
		}
		return _ret;
	}

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public Attribute visit(TimesExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("int",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int") || !rightType.equals("int")){
			throw new Exception("Error: Invalid use of * operator. Not applicable to given types: "+leftType+" and "+rightType);
		}
		return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
	public Attribute visit(ArrayLookup n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("int",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		String rightType =((variableType)n.f2.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int[]") || !rightType.equals("int")){
			throw new Exception("Error: Invalid use of [] operator. Not applicable to given types: "+leftType+" and "+rightType);
		}
		return _ret;
	}

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public Attribute visit(ArrayLength n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("int",null);
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable, method);
		if( !leftType.equals("int[]") ){
			throw new Exception("Error: Invalid use of length keyword. Not applicable to type: "+leftType);
		}
		return _ret;
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
	public Attribute visit(MessageSend n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String leftType = ((variableType)n.f0.accept(this,argu)).getType(symbolTable,method);
		classType classObject = symbolTable.getClassByName(leftType);	
		String identifier = n.f2.accept(this,argu).name;

		methodType methodCall = classObject.getInheritedMethodByName(identifier);
		if(methodCall == null){
			throw new Exception("Error: method "+identifier+" not declared in this scope.");
		}
		methodType dummyMethod;
		if(n.f4.present()){
			dummyMethod = (methodType)n.f4.accept(this,argu);
		}else {
			dummyMethod = new methodType("dummy");
		}
		if(!methodCall.matchParameters(dummyMethod, method, symbolTable)){
			throw new Exception("Error: parameter list doesn't match in call of method "+identifier);
		}

		Attribute _ret = new variableType(methodCall.getType(),null);
		return _ret;
	}

   /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
	public Attribute visit(ExpressionList n, Attribute argu) throws Exception {
		methodType dummyMethod = new methodType("dummy");
		variableType parameter = (variableType)n.f0.accept(this,argu);
		dummyMethod.addParameter(parameter);

		methodType method2 = (methodType)n.f1.accept(this,argu);
		dummyMethod.stealParametersFrom(method2);
		return dummyMethod;
		}

   /**
    * f0 -> ( ExpressionTerm() )*
    */
   public Attribute visit(ExpressionTail n, Attribute argu) throws Exception {
		methodType dummyMethod = new methodType("dummy");
		if(n.f0.present()){
			for(int i=0; i< n.f0.size(); i++){
				variableType parameter = (variableType)n.f0.nodes.elementAt(i).accept(this,argu);
				dummyMethod.addParameter(parameter);
			}
		}
		return dummyMethod;
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
	public Attribute visit(ExpressionTerm n, Attribute argu) throws Exception {
		return n.f1.accept(this,argu);
	}


   /**
    * f0 -> <INTEGER_LITERAL>
    */
	public Attribute visit(IntegerLiteral n, Attribute argu) throws Exception {
		return new variableType("int",null);
	}

   /**
    * f0 -> "true"
    */
	public Attribute visit(TrueLiteral n, Attribute argu) throws Exception {
		return new variableType("boolean",null);
	}

   /**
    * f0 -> "false"
    */
	public Attribute visit(FalseLiteral n, Attribute argu) throws Exception {
		return new variableType("boolean",null);
	}

   /**
    * f0 -> <IDENTIFIER>
    */
	public Attribute visit(Identifier n, Attribute argu) throws Exception {
		return new variableType(null,n.f0.toString());
	}

   /**
    * f0 -> "this"
    */
	public Attribute visit(ThisExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		classType classObject = method.getOwnerClass();
		return new variableType(classObject.name, null);
	}

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
	public Attribute visit(ArrayAllocationExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		String type = ((variableType)n.f3.accept(this,argu)).getType(symbolTable, method);
		if(!type.equals("int")){
			throw new Exception("Error: Invalid array allocation. Not applicable to type "+type);
		}
		return new variableType("int[]",null);
	}

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
	public Attribute visit(AllocationExpression n, Attribute argu) throws Exception {
	   	methodType method = (methodType) argu;
		String className = ((variableType)n.f1.accept(this,argu)).name;			 // Use .name just for this case
		classType classObject = symbolTable.getClassByName(className);
		if(classObject == null){
			throw new Exception("Error: Invalid type: "+className);
		}
		return new variableType(className,null);
	}

   /**
    * f0 -> "!"
    * f1 -> Clause()
    */
	public Attribute visit(NotExpression n, Attribute argu) throws Exception {
		methodType method = (methodType) argu;
		variableType _ret = new variableType("boolean",null);
		String type = ((variableType)n.f1.accept(this,argu)).getType(symbolTable, method);
		if( !type.equals("boolean")){
			throw new Exception("Error: Invalid use of ! operator. Not applicable to type "+type);
		}
		return _ret;
	}

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
	public Attribute visit(BracketExpression n, Attribute argu) throws Exception {
		return n.f1.accept(this,argu);
	}
}
