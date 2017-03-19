import syntaxtree.*;
import java.util.*;
import visitor.GJDepthFirst;
import attribute.*;
import symboltable.*;


public class LoweringVisitor extends GJDepthFirst<Attribute, Attribute> {

	SymbolTable symbolTable;
	String finalCode;
	int tempCount;
	int labelCount;

    
	public LoweringVisitor(SymbolTable symbolTable){
		this.symbolTable = symbolTable;
		finalCode = "";
		tempCount = symbolTable.getMaxTempCount() + 1;
		labelCount = 0;
	}




	public Attribute visit(Goal n, Attribute argu) throws Exception {
	  Attribute _ret=null;
	  n.f0.accept(this, argu);
	  n.f1.accept(this, argu);
	  n.f2.accept(this, argu);
	  return _ret;
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
	public Attribute visit(MainClass n, Attribute argu) throws Exception {
		finalCode += "\nMAIN";
		buildVTables();
		String identifier = n.f1.f0.toString();
		ClassType classObject = symbolTable.getClassByName(identifier);
		MethodType method = classObject.getMethodByName("main");
		n.f14.accept(this, method);
		n.f15.accept(this, method);
		finalCode += "\nEND\n";
		Attribute _ret=null;
		return _ret;
	}

	/**
	* f0 -> ClassDeclaration()
	*       | ClassExtendsDeclaration()
	*/
	public Attribute visit(TypeDeclaration n, Attribute argu) throws Exception {
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
	public Attribute visit(ClassDeclaration n, Attribute argu) throws Exception {
		Attribute _ret=null;
		String identifier = n.f1.accept(this,argu).name;
		ClassType classObject = symbolTable.getClassByName(identifier);
		n.f3.accept(this, classObject);
		n.f4.accept(this, classObject);
		return _ret;
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
		Attribute _ret=null;
		String identifier = n.f1.accept(this,argu).name;
		ClassType classObject = symbolTable.getClassByName(identifier);
		n.f5.accept(this, classObject);
		n.f6.accept(this, classObject);
		return _ret;
	}

	/**
	* f0 -> Type()
	* f1 -> Identifier()
	* f2 -> ";"
	*/
	public Attribute visit(VarDeclaration n, Attribute argu) throws Exception {
		Attribute _ret=null;
		ClassType classObject;
		if((classObject = symbolTable.getClassByName(argu.name)) == null) { //Not in a class.
			String varName = n.f1.f0.toString();
			argu.tempMap.put(varName, getTempNo());
			//Initialization to 0.
			// finalCode += "\nMOVE TEMP "+argu.tempMap.get(varName)+" 0";
		}

		return _ret;
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
		ClassType ownerClass = (ClassType) argu;
		MethodType method = ownerClass.getMethodByName(identifier);
		
		identifier = ownerClass.name+"_"+identifier;
		finalCode += "\n"+identifier+" ["+(method.paramSize()+1)+"]";
		finalCode += "\nBEGIN";

		Attribute _ret=null;
		n.f7.accept(this, method);
		n.f8.accept(this, method);


		int returnTemp = n.f10.accept(this, method).tempNo;
		finalCode += "\nRETURN TEMP "+returnTemp;

		
		finalCode += "\nEND\n";
		return _ret;
	}

	/**
	* f0 -> FormalParameter()
	* f1 -> FormalParameterTail()
	*/
	public Attribute visit(FormalParameterList n, Attribute argu) throws Exception {
	  Attribute _ret=null;
	  n.f0.accept(this, argu);
	  n.f1.accept(this, argu);
	  return _ret;
	}

	/**
	* f0 -> Type()
	* f1 -> Identifier()
	*/
	public Attribute visit(FormalParameter n, Attribute argu) throws Exception {
	  Attribute _ret=null;
	  n.f0.accept(this, argu);
	  n.f1.accept(this, argu);
	  return _ret;
	}

	/**
	* f0 -> ( FormalParameterTerm() )*
	*/
	public Attribute visit(FormalParameterTail n, Attribute argu) throws Exception {
	  return n.f0.accept(this, argu);
	}

	/**
	* f0 -> ","
	* f1 -> FormalParameter()
	*/
	public Attribute visit(FormalParameterTerm n, Attribute argu) throws Exception {
	  Attribute _ret=null;
	  n.f0.accept(this, argu);
	  n.f1.accept(this, argu);
	  return _ret;
	}

	/**
	* f0 -> ArrayType()
	*       | BooleanType()
	*       | IntegerType()
	*       | Identifier()
	*/
	public Attribute visit(Type n, Attribute argu) throws Exception {
	  return n.f0.accept(this, argu);
	}

	/**
	* f0 -> "int"
	* f1 -> "["
	* f2 -> "]"
	*/
	public Attribute visit(ArrayType n, Attribute argu) throws Exception {
	  Attribute _ret=null;
	  n.f0.accept(this, argu);
	  n.f1.accept(this, argu);
	  n.f2.accept(this, argu);
	  return _ret;
	}

	/**
	* f0 -> "boolean"
	*/
	public Attribute visit(BooleanType n, Attribute argu) throws Exception {
	  return n.f0.accept(this, argu);
	}

	/**
	* f0 -> "int"
	*/
	public Attribute visit(IntegerType n, Attribute argu) throws Exception {
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
	public Attribute visit(Statement n, Attribute argu) throws Exception {
	  return n.f0.accept(this, argu);
	}

	/**
	* f0 -> "{"
	* f1 -> ( Statement() )*
	* f2 -> "}"
	*/
	public Attribute visit(Block n, Attribute argu) throws Exception {
	  Attribute _ret=null;
	  n.f0.accept(this, argu);
	  n.f1.accept(this, argu);
	  n.f2.accept(this, argu);
	  return _ret;
	}

	/**
	* f0 -> Identifier()
	* f1 -> "="
	* f2 -> Expression()
	* f3 -> ";"
	*/
	public Attribute visit(AssignmentStatement n, Attribute argu) throws Exception {
		Attribute _ret=null;
		MethodType method = (MethodType)argu;
		String varName = n.f0.accept(this, argu).name;
		int expTemp = n.f2.accept(this, argu).tempNo;
		if(method.hasVar(varName)){
			int varTemp = method.getTempOf(varName);
			finalCode += "\nMOVE TEMP "+varTemp+" TEMP "+expTemp;
		}
		else{
			ClassType classObject = method.getOwnerClass();
			int offset = classObject.getFieldOffset(varName);
			// System.out.println(varName+" = ...  offset:"+offset);
			finalCode += "\nHSTORE TEMP 0 "+offset+" TEMP "+expTemp;
		}
		return _ret;
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
		int arrayTemp = n.f0.accept(this,argu).tempNo;
		int indexTemp = n.f2.accept(this,argu).tempNo;
		int expTemp = n.f5.accept(this,argu).tempNo;
		
		String error = "ERROR";
		String noNegative = getLabel();
		String noError = getLabel();
		int negativeTemp = getTempNo();
		int outOfBoundsTemp = getTempNo();
		int sizeTemp = getTempNo();

		finalCode += "\nMOVE TEMP "+negativeTemp+" LT TEMP "+indexTemp+" 0";							//negative = (index < 0);
		finalCode += "\nCJUMP TEMP "+negativeTemp+" "+noNegative;										//if(negative)
		finalCode += "\n"+error;																		//ERROR
		finalCode += "\n"+noNegative+" NOOP";															//NONEGATIVE: (else)
		finalCode += "\nHLOAD TEMP "+sizeTemp+" TEMP "+arrayTemp+" 0";									//size = array[0];
		finalCode += "\nMOVE TEMP "+outOfBoundsTemp+" LT TEMP "+indexTemp+" TEMP "+sizeTemp;			//outOfBounds = (index < size)
		int oneTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+oneTemp+" 1";
		finalCode += "\nMOVE TEMP "+outOfBoundsTemp+" MINUS TEMP "+oneTemp+" TEMP "+outOfBoundsTemp;	//outOfBounds = not outOfBounds
		finalCode += "\nCJUMP TEMP "+outOfBoundsTemp+" "+noError;										//if(outOfBounds)
		finalCode += "\n"+error;																		//ERROR
		finalCode += "\n"+noError+" NOOP";																//NOERROR (else)
		

		//No error. Access the array and store the expression.
		int baseAddressTemp = getTempNo();
		int bytesTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+baseAddressTemp+" TEMP "+arrayTemp;									//base = array;
		finalCode += "\nMOVE TEMP "+bytesTemp+" PLUS TEMP "+indexTemp+" 1";								//bytes = index+1;
		finalCode += "\nMOVE TEMP "+bytesTemp+" TIMES TEMP "+bytesTemp+" 4";							//bytes *= 4;
		finalCode += "\nMOVE TEMP "+baseAddressTemp+" PLUS TEMP "+baseAddressTemp+" TEMP "+bytesTemp;	//base = base + bytes;
		
		finalCode += "\nHSTORE TEMP "+baseAddressTemp+" 0 TEMP "+expTemp;		
		Attribute _ret = null;
		return _ret;
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
	  Attribute _ret=null;
	  int expTemp = n.f2.accept(this, argu).tempNo;
	  String elseLabel = getLabel();
	  finalCode += "\nCJUMP TEMP "+expTemp+" "+elseLabel;
	  //Then code
	  n.f4.accept(this, argu);
	  String endLabel = getLabel();
	  finalCode += "\nJUMP "+endLabel;
	  finalCode += "\n"+elseLabel+" NOOP";
	  //Else code
	  n.f6.accept(this, argu);
	  finalCode += "\n"+endLabel+" NOOP";
	  return _ret;
	}

	/**
	* f0 -> "while"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> Statement()
	*/
	public Attribute visit(WhileStatement n, Attribute argu) throws Exception {
	  	String startLabel = getLabel();
	  	String endLabel = getLabel();
	  	int condition = getTempNo();
	  	finalCode += "\n"+startLabel+" NOOP";
	  	int expTemp = n.f2.accept(this,argu).tempNo;
	  	finalCode += "\nMOVE TEMP "+condition+" TEMP "+expTemp;
	  	finalCode += "\nCJUMP TEMP "+condition+" "+endLabel;
	  	n.f4.accept(this,argu);
	  	finalCode += "\nJUMP "+startLabel;
	  	finalCode += "\n"+endLabel+" NOOP";
		Attribute _ret=null;
		return _ret;
	}

	/**
	* f0 -> "System.out.println"
	* f1 -> "("
	* f2 -> Expression()
	* f3 -> ")"
	* f4 -> ";"
	*/
	public Attribute visit(PrintStatement n, Attribute argu) throws Exception {
		int expTemp = n.f2.accept(this,argu).tempNo;
		finalCode += "\nPRINT TEMP "+expTemp;
		Attribute _ret=null;
		return _ret;
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
	public Attribute visit(Expression n, Attribute argu) throws Exception {
	  return n.f0.accept(this, argu);
	}

	/**
	* f0 -> Clause()
	* f1 -> "&&"
	* f2 -> Clause()
	*/
	public Attribute visit(AndExpression n, Attribute argu) throws Exception {
		String endLabel = getLabel();
		int temp1 = n.f0.accept(this, argu).tempNo;
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" TEMP "+temp1;
		finalCode += "\nCJUMP TEMP "+returnTemp+" "+endLabel;
		int temp2 = n.f2.accept(this, argu).tempNo; 
		finalCode += "\nMOVE TEMP "+returnTemp+" TEMP "+temp2;
		finalCode += "\n"+endLabel+" NOOP";

		Attribute _ret= new VariableType("boolean",null);
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "<"
	* f2 -> PrimaryExpression()
	*/
	public Attribute visit(CompareExpression n, Attribute argu) throws Exception {
		int temp1 = n.f0.accept(this, argu).tempNo;
		int temp2 = n.f2.accept(this, argu).tempNo;
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" LT TEMP "+temp1+" TEMP "+temp2;

		Attribute _ret= new VariableType("boolean",null);
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "+"
	* f2 -> PrimaryExpression()
	*/
	public Attribute visit(PlusExpression n, Attribute argu) throws Exception {
		int temp1 = n.f0.accept(this, argu).tempNo;
		int temp2 = n.f2.accept(this, argu).tempNo;
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" PLUS TEMP "+temp1+" TEMP "+temp2;
		
		Attribute _ret= new VariableType("int",null);
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "-"
	* f2 -> PrimaryExpression()
	*/
	public Attribute visit(MinusExpression n, Attribute argu) throws Exception {
		int temp1 = n.f0.accept(this, argu).tempNo;
		int temp2 = n.f2.accept(this, argu).tempNo;
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" MINUS TEMP "+temp1+" TEMP "+temp2;

		Attribute _ret= new VariableType("int",null);
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "*"
	* f2 -> PrimaryExpression()
	*/
	public Attribute visit(TimesExpression n, Attribute argu) throws Exception {
		int temp1 = n.f0.accept(this, argu).tempNo;
		int temp2 = n.f2.accept(this, argu).tempNo;
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" TIMES TEMP "+temp1+" TEMP "+temp2;

		Attribute _ret= new VariableType("int",null);
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "["
	* f2 -> PrimaryExpression()
	* f3 -> "]"
	*/
	public Attribute visit(ArrayLookup n, Attribute argu) throws Exception {
		int arrayTemp = n.f0.accept(this,argu).tempNo;
		int indexTemp = n.f2.accept(this,argu).tempNo;
		int negativeTemp = getTempNo();
		int outOfBoundsTemp = getTempNo();
		int sizeTemp = getTempNo();
		String error = "ERROR";
		String noNegative = getLabel();
		String noError = getLabel();
		finalCode += "\nMOVE TEMP "+negativeTemp+" LT TEMP "+indexTemp+" 0";							//negative = (index < 0);
		finalCode += "\nCJUMP TEMP "+negativeTemp+" "+noNegative;										//if(negative)
		finalCode += "\n"+error;																		//ERROR
		finalCode += "\n"+noNegative+" NOOP";															//NONEGATIVE: (else)
		finalCode += "\nHLOAD TEMP "+sizeTemp+" TEMP "+arrayTemp+" 0";									//size = array[0];
		finalCode += "\nMOVE TEMP "+outOfBoundsTemp+" LT TEMP "+indexTemp+" TEMP "+sizeTemp;			//outOfBounds = (index < size)
		int oneTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+oneTemp+" 1";
		finalCode += "\nMOVE TEMP "+outOfBoundsTemp+" MINUS TEMP "+oneTemp+" TEMP "+outOfBoundsTemp;	//outOfBounds = not outOfBounds
		finalCode += "\nCJUMP TEMP "+outOfBoundsTemp+" "+noError;										//if(outOfBounds)
		finalCode += "\n"+error;																		//ERROR
		finalCode += "\n"+noError+" NOOP";																//NOERROR (else)
		
		//No error. Access the element and return the register.
		int baseAddressTemp = getTempNo();
		int bytesTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+baseAddressTemp+" TEMP "+arrayTemp;									//base = array;
		finalCode += "\nMOVE TEMP "+bytesTemp+" PLUS TEMP "+indexTemp+" 1";								//bytes = index+1;
		finalCode += "\nMOVE TEMP "+bytesTemp+" TIMES TEMP "+bytesTemp+" 4";							//bytes *= 4;
		finalCode += "\nMOVE TEMP "+baseAddressTemp+" PLUS TEMP "+baseAddressTemp+" TEMP "+bytesTemp;	//base = base + bytes;

		int returnTemp = getTempNo();
		finalCode += "\nHLOAD TEMP "+returnTemp+" TEMP "+baseAddressTemp+" 0";							//ret = base[0]; 

		Attribute _ret = new VariableType("int",null);
		_ret.tempNo = returnTemp;		
		return _ret;
	}

	/**
	* f0 -> PrimaryExpression()
	* f1 -> "."
	* f2 -> "length"
	*/
	public Attribute visit(ArrayLength n, Attribute argu) throws Exception {
	  Attribute _ret = new VariableType("int",null);
	  int arrayTemp = n.f0.accept(this, argu).tempNo;
	  int returnTemp = getTempNo();
	  finalCode += "\nHLOAD TEMP "+returnTemp+" TEMP "+arrayTemp+" 0";
	  _ret.tempNo = returnTemp;
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
		VariableType left = (VariableType)n.f0.accept(this,argu);
		String type = left.type;
		int objTemp = left.tempNo;
		String methodId = n.f2.f0.toString();
		ClassType classObject = symbolTable.getClassByName(type);
		int methodOffset = classObject.getMethodOffset(methodId);
		MethodType methodObject = classObject.getMethodByName(methodId);
		int objCopy = getTempNo();
		int vTableTemp = getTempNo();
		int methodTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+objCopy+" TEMP "+objTemp;
		finalCode += "\nHLOAD TEMP "+vTableTemp+" TEMP "+objCopy+" 0";						//vTableTemp = obj[0];
		finalCode += "\nHLOAD TEMP "+methodTemp+" TEMP "+vTableTemp+" "+methodOffset;		//methodTemp = vTableTemp[offset];

		MethodType dummyMethod;
		if(n.f4.present()){
			dummyMethod = (MethodType) n.f4.accept(this,argu);
		} else {
			dummyMethod = new MethodType("dummy");
		}
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" CALL TEMP "+methodTemp+"( ";
		finalCode += "TEMP "+objCopy+" ";
		for(VariableType param : dummyMethod.paramList){
			finalCode += " TEMP "+param.tempNo+" ";
		}
		finalCode += ")";
		Attribute _ret = new VariableType(methodObject.getType(), null);
		_ret.tempNo = returnTemp;
		
		return _ret;
	}

	/**
	* f0 -> Expression()
	* f1 -> ExpressionTail()
	*/
	public Attribute visit(ExpressionList n, Attribute argu) throws Exception {
		MethodType dummyMethod = new MethodType("dummy");
		VariableType parameter = (VariableType)n.f0.accept(this,argu);
		dummyMethod.addParameter(parameter);

		MethodType method2 = (MethodType)n.f1.accept(this,argu);
		dummyMethod.stealParametersFrom(method2);
		return dummyMethod;
		}

   /**
    * f0 -> ( ExpressionTerm() )*
    */
   public Attribute visit(ExpressionTail n, Attribute argu) throws Exception {
		MethodType dummyMethod = new MethodType("dummy");
		if(n.f0.present()){
			for(int i=0; i< n.f0.size(); i++){
				VariableType parameter = (VariableType)n.f0.nodes.elementAt(i).accept(this,argu);
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
	* f0 -> NotExpression()
	*       | PrimaryExpression()
	*/
	public Attribute visit(Clause n, Attribute argu) throws Exception {
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
	public Attribute visit(PrimaryExpression n, Attribute argu) throws Exception {
	  return n.f0.accept(this, argu);
	}

	/**
	* f0 -> <INTEGER_LITERAL>
	*/
	public Attribute visit(IntegerLiteral n, Attribute argu) throws Exception {
		Attribute _ret = new VariableType("int",null);
		int returnTemp = getTempNo();
		String value = n.f0.toString();
		finalCode += "\nMOVE TEMP "+returnTemp+" "+value;
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> "true"
	*/
	public Attribute visit(TrueLiteral n, Attribute argu) throws Exception {
		Attribute _ret = new VariableType("boolean",null);
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" 1";
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> "false"
	*/
	public Attribute visit(FalseLiteral n, Attribute argu) throws Exception {
		Attribute _ret = new VariableType("boolean",null);
		int returnTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+returnTemp+" 0";
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> <IDENTIFIER>
	*/
	public Attribute visit(Identifier n, Attribute argu) throws Exception {
		String name = n.f0.toString();
		ClassType classObject = symbolTable.getClassByName(name);
		if(classObject == null){ //Not a class
			if((classObject = symbolTable.getClassByName(argu.name)) != null){
				MethodType methodObject = ((ClassType)argu).getMethodByName(name);
				return methodObject;
			} else { //Not a method-declaration
				MethodType methodObject = (MethodType)argu;
				classObject = methodObject.getOwnerClass();
				methodObject = classObject.getMethodByName(name);
				if(methodObject != null){
					return methodObject;
				}
				VariableType _ret = new VariableType(null,name);
				MethodType method = (MethodType)argu;
				if(method.hasVar(name)){
					_ret.tempNo = method.getTempOf(name);
					_ret.type = method.getVarType(name);
				} else {
					classObject = method.getOwnerClass();
					int returnTemp = getTempNo();
					int offset = classObject.getFieldOffset(name);
					finalCode += "\nHLOAD TEMP "+returnTemp+" TEMP 0 "+offset;
					_ret.tempNo = returnTemp;
					_ret.type = classObject.getVarType(name);
				}	
				return _ret;
			}
		} else {
			return classObject;
		}
	}

	/**
	* f0 -> "this"
	*/
	public Attribute visit(ThisExpression n, Attribute argu) throws Exception {
		MethodType method = (MethodType) argu;
		ClassType classObject = method.getOwnerClass();
		VariableType _ret = new VariableType(classObject.name, "this");
		_ret.tempNo = 0;
		return _ret;
	}

	/**
	* f0 -> "new"
	* f1 -> "int"
	* f2 -> "["
	* f3 -> Expression()
	* f4 -> "]"
	*/
	public Attribute visit(ArrayAllocationExpression n, Attribute argu) throws Exception {
		int expTemp = n.f3.accept(this,argu).tempNo;
		String error = "ERROR";
		String noError = getLabel();
		int negativeTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+negativeTemp+" LT TEMP "+expTemp+" 0";							//negative = (expTemp < 0);
		finalCode += "\nCJUMP TEMP "+negativeTemp+" "+noError;										//if(negative)
		finalCode += "\n"+error;																	//ERROR
		finalCode += "\n"+noError+" NOOP";															//NOERROR (else)

		int fullSizeTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+fullSizeTemp+" PLUS TEMP "+expTemp+" 1";
		int addrSize = getTempNo();
		finalCode += "\nMOVE TEMP "+addrSize+" TIMES TEMP "+fullSizeTemp+" 4";
		int arrayTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+arrayTemp+" HALLOCATE TEMP "+addrSize;
		finalCode += "\nHSTORE TEMP "+arrayTemp+" 0 TEMP "+expTemp;
		
		int indexTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+indexTemp+" 4";													//index = 4;
		int conditionTemp = getTempNo();

		String startLabel = getLabel();
		finalCode += "\n"+startLabel+" NOOP";														//START:
		finalCode += "\nMOVE TEMP "+conditionTemp+" LT TEMP "+indexTemp+" TEMP "+addrSize;			//condition = (index < size);
		String outOfBoundsLabel = getLabel();
		int baseAddressTemp = getTempNo();
		
		finalCode += "\nCJUMP TEMP "+conditionTemp+" "+outOfBoundsLabel;							//if(condition)
		finalCode += "\nMOVE TEMP "+baseAddressTemp+" PLUS TEMP "+arrayTemp+" TEMP "+indexTemp;		//baseAddressTemp = array + index;
		int zeroTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+zeroTemp+" 0";
		finalCode += "\nHSTORE TEMP "+baseAddressTemp+" 0 "+"TEMP "+zeroTemp;						//baseAddressTemp[0] = 0;
		finalCode += "\nMOVE TEMP "+indexTemp+" PLUS TEMP "+indexTemp+" 4";							//index += 4;
		finalCode += "\nJUMP "+startLabel;															//goto START;
		finalCode += "\n"+outOfBoundsLabel+" NOOP";

		Attribute _ret = new VariableType("int[]",null);
		_ret.tempNo = arrayTemp;
		return _ret;
	}

	/**
	* f0 -> "new"
	* f1 -> Identifier()
	* f2 -> "("
	* f3 -> ")"
	*/
	public Attribute visit(AllocationExpression n, Attribute argu) throws Exception {
		String identifier = n.f1.accept(this,argu).name;
		ClassType classObject = symbolTable.getClassByName(identifier);
		String vTableLabel = vTableLabel(classObject);
		// System.out.println("--- Vtable: "+vTableLabel);
		int fieldCount = classObject.getFieldNumber();
		// System.out.println(classObject.name+" :: "+fieldCount);
		int bytes = (fieldCount+1)*4;
		int objTemp = getTempNo();
		int labelTemp = getTempNo();
		int vTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+objTemp+" HALLOCATE "+bytes;							//obj = malloc(bytes)
		finalCode += "\nMOVE TEMP "+labelTemp+" "+vTableLabel;								//labelTemp = VTLabel;
		finalCode += "\nHLOAD TEMP "+vTemp+" TEMP "+labelTemp+" 0";							//vTemp = labelTemp[0];
		finalCode += "\nHSTORE TEMP "+objTemp+" 0 TEMP "+vTemp;								//obj[0] = vTemp;
		int zeroTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+zeroTemp+" 0";											//zero = 0;
		int index = 4;
		while(index < bytes){
			finalCode += "\nHSTORE TEMP "+objTemp+" "+index+" TEMP "+zeroTemp;				//obj[index] = 0;
			index += 4;
		}
		Attribute _ret = new VariableType(identifier,null);
		_ret.tempNo = objTemp;
		return _ret;
	}

	/**
	* f0 -> "!"
	* f1 -> Clause()
	*/
	public Attribute visit(NotExpression n, Attribute argu) throws Exception {
		int expTemp = n.f1.accept(this,argu).tempNo;
		int returnTemp = getTempNo();
		int oneTemp = getTempNo();
		finalCode += "\nMOVE TEMP "+oneTemp+" 1";
		finalCode += "\nMOVE TEMP "+returnTemp+" MINUS TEMP "+oneTemp+" TEMP "+expTemp;
		Attribute _ret = new VariableType("boolean",null);
		_ret.tempNo = returnTemp;
		return _ret;
	}

	/**
	* f0 -> "("
	* f1 -> Expression()
	* f2 -> ")"
	*/
	public Attribute visit(BracketExpression n, Attribute argu) throws Exception {
		return n.f1.accept(this, argu);
	}

	public String getFinalCode(){
		return finalCode;
	}

	public String getLabel(){
		return "L"+labelCount++;
	}

	public int getTempNo(){
		return tempCount++;
	}

	public void buildVTables(){
		finalCode += "\n//Vtables";
		symbolTable.initOffsets();
		for(ClassType classObject : symbolTable.classList){
			if(!classObject.isMain())
				bulidVTableOf(classObject);
		}
		finalCode += "\n";
	}

	public void bulidVTableOf(ClassType classObject){
		String vTableLabel = vTableLabel(classObject);
		int labelTemp = getTempNo();
		int allocTemp = getTempNo();
		int vTableSize = classObject.getMethodCount()*4;
		finalCode += "\nMOVE TEMP "+labelTemp+" "+vTableLabel;
		finalCode += "\nMOVE TEMP "+allocTemp+" HALLOCATE "+vTableSize;
		finalCode += "\nHSTORE TEMP "+labelTemp+" 0 TEMP "+allocTemp;
		Vector<MethodType> methodList = classObject.getExtendedMethodList();
		int offset = 0;
		for(MethodType method : methodList){
			int methodTemp = getTempNo();
			String methodLabel = methodLabel(method);
			finalCode += "\nMOVE TEMP "+methodTemp+" "+methodLabel;
			finalCode += "\nHSTORE TEMP "+allocTemp+" "+offset+" TEMP "+methodTemp;
			offset += 4;
		}

	}

	public String vTableLabel(ClassType classObject){
		return classObject.name+"_vTable";
	}


	public String methodLabel(MethodType method){
		ClassType ownerClass = method.getOwnerClass();
		return ownerClass.name+"_"+method.name;
	}
}
