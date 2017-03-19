import syntaxtree.*;
import visitor.GJNoArguDepthFirst;
import java.util.*;
import java.util.Vector;
import attribute.*;
import symboltable.*;


public class SymbolTableBuilder extends GJNoArguDepthFirst<Attribute> {

  	SymbolTable symbolTable;


    
	public SymbolTableBuilder(SymbolTable symbolTable){
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

   
	public Attribute visit(MainClass n) throws Exception {
		String identifier = n.f1.accept(this).name;
		ClassType classObject = symbolTable.getClassByName(identifier); 

		MethodType mainMethod = new MethodType("main");
		mainMethod.setType("void");
		classObject.addMethod(mainMethod);

		VariableType argv = new VariableType("String[]",n.f11.accept(this).name);
		mainMethod.addParameter(argv);

		if(n.f14.present()){
			for(int i=0; i< n.f14.size(); i++){
				VariableType local_var = (VariableType)n.f14.nodes.elementAt(i).accept(this);
				if(!mainMethod.variableExists(local_var)){
					if(symbolTable.typeExists(local_var.type)){
						mainMethod.addLocalVariable(local_var);
					} else {
						throw new Exception("Error: Invalid type: "+local_var.type+" "+local_var.name);
					}
				}
				else{
					throw new Exception("Error: Double declaration of local variable "+local_var.type+" "+local_var.name);
				}
			}
		}
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
	public Attribute visit(ClassDeclaration n) throws Exception {
		String identifier = n.f1.accept(this).name;
		ClassType classObject = symbolTable.getClassByName(identifier);
		if(n.f3.present()){
			for(int i=0; i< n.f3.size(); i++){
				VariableType field = (VariableType)n.f3.nodes.elementAt(i).accept(this);
				if(!classObject.fieldExists(field)){
					if(symbolTable.typeExists(field.type)){
						classObject.addField(field);
					} else {
						throw new Exception("Error: Invalid type: "+field.type+" "+field.name);
					}
				}
				else{
					throw new Exception("Error: Double declaration of field "+field.type+" "+field.name);
				}
			}
		}
		if(n.f4.present()){
			for(int i=0; i< n.f4.size(); i++){
				MethodType method = (MethodType)n.f4.nodes.elementAt(i).accept(this);
				if(!classObject.methodExists(method)){
					if(symbolTable.typeExists(method.returnType)){
						classObject.addMethod(method);
					} else {
						throw new Exception("Error: Invalid type: "+method.returnType+" "+method.name+"(..)");
					}
				}
				else{
					throw new Exception("Error: Double definition of method "+method.name);
				}
			}
		}
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
	public Attribute visit(ClassExtendsDeclaration n) throws Exception {
		String identifier = n.f1.accept(this).name;
		ClassType classObject = symbolTable.getClassByName(identifier);

		String parentIdentifier = n.f3.accept(this).name;
		ClassType parentClassObject = symbolTable.getClassByName(parentIdentifier);
		classObject.inherits(parentClassObject);

		if(n.f5.present()){
			for(int i=0; i< n.f5.size(); i++){
				VariableType field = (VariableType)n.f5.nodes.elementAt(i).accept(this);
				if(!classObject.fieldExists(field)){
					if(symbolTable.typeExists(field.type)){
						classObject.addField(field);
					} else {
						throw new Exception("Error: Invalid type: "+field.type+" "+field.name);
					}
				}
				else{
					throw new Exception("Error: Double declaration of field "+field.type+" "+field.name);
				}
			}
		}
		if(n.f6.present()){
			for(int i=0; i< n.f6.size(); i++){
				MethodType method = (MethodType)n.f6.nodes.elementAt(i).accept(this);
				if(!classObject.methodExists(method)){
					if(symbolTable.typeExists(method.returnType)){
						MethodType inheritedMethod = classObject.getInheritedMethodByName(method.name);
						if((inheritedMethod == null) || inheritedMethod.isOverridableBy(method, symbolTable)){
							classObject.addMethod(method);
						}
						else{
							throw new Exception("Error: method "+inheritedMethod.name+" cannot be overriden with current declaration");
						}
					} else {
						throw new Exception("Error: Invalid type: "+method.returnType+" "+method.name+"(..)");
					}
				}
				else{
					throw new Exception("Error: Double definition of method "+method.name);
				}
			}
		}
		return null;
	}

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
	public Attribute visit(VarDeclaration n) throws Exception {
		String type = n.f0.accept(this).name; 
		String identifier = n.f1.accept(this).name; 
		return new VariableType(type, identifier);
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
	public Attribute visit(MethodDeclaration n) throws Exception {
   		String identifier = n.f2.accept(this).name;
		MethodType method;
      	if(n.f4.present()){
		    method = (MethodType)n.f4.accept(this);
	      	method.setName(identifier);
      	} else {
		    method = new MethodType(identifier);
      	}
      	String returnType = n.f1.accept(this).name;
      	method.setType(returnType);

		if(n.f7.present()){
			for(int i=0; i< n.f7.size(); i++){
				VariableType local_var = (VariableType)n.f7.nodes.elementAt(i).accept(this);
				if(!method.variableExists(local_var)){
					if(symbolTable.typeExists(local_var.type)){
						method.addLocalVariable(local_var);
					} else {
						throw new Exception("Error: Invalid type: "+local_var.type+" "+local_var.name);
					}
				}
				else{
					throw new Exception("Error: Double declaration of local variable "+local_var.type+" "+local_var.name);
				}
			}
		}
		return method;
	}

   /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
	public Attribute visit(FormalParameterList n) throws Exception {
		MethodType method = new MethodType("dummy");
   		VariableType parameter = (VariableType)n.f0.accept(this);
   		if(!method.variableExists(parameter)){
	 		if(symbolTable.typeExists(parameter.type)){
				method.addParameter(parameter);
			} else {
				throw new Exception("Error: Invalid type: "+parameter.type+" "+parameter.name);
			}
   		}
	 	else{
	 		throw new Exception("Error: Double declaration of parameter "+parameter.type+" "+parameter.name);
	 	}
		MethodType method2 = (MethodType)n.f1.accept(this);
		method.stealParametersFrom(method2);
		return method;
	}

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
	public Attribute visit(FormalParameter n) throws Exception {
		String type = n.f0.accept(this).name; 
		String identifier = n.f1.accept(this).name; 
 		return new VariableType(type, identifier);
	}

   /**
    * f0 -> ( FormalParameterTerm() )*
    */
   public Attribute visit(FormalParameterTail n) throws Exception {
		MethodType method = new MethodType("dummy");
		if(n.f0.present()){
			for(int i=0; i< n.f0.size(); i++){
				VariableType parameter = (VariableType)n.f0.nodes.elementAt(i).accept(this);
				if(!method.variableExists(parameter)){
			 		if(symbolTable.typeExists(parameter.type)){
						method.addParameter(parameter);
					} else {
						throw new Exception("Error: Invalid type: "+parameter.type+" "+parameter.name);
					}				}
			 	else{
			 		throw new Exception("Error: Double declaration of parameter "+parameter.type+" "+parameter.name);
			 	}
			}
		}
		return method;
	}

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public Attribute visit(FormalParameterTerm n) throws Exception {
   		VariableType var = (VariableType)n.f1.accept(this);
		return var;
   }

  

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public Attribute visit(ArrayType n) throws Exception {
      return new Attribute("int[]");
    }

   /**
    * f0 -> "boolean"
    */
   public Attribute visit(BooleanType n) throws Exception {
      return new Attribute("boolean");
   }

   /**
    * f0 -> "int"
    */
   public Attribute visit(IntegerType n) throws Exception {
      return new Attribute("int");
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public Attribute visit(Identifier n) throws Exception {
      return new Attribute(n.f0.toString());
   }
   

}
