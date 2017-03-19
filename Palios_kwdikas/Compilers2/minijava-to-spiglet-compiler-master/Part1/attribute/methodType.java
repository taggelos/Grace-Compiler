package attribute;

import symboltable.*;
import syntaxtree.*;
import java.util.*;
import java.util.Vector;

public class methodType extends Attribute {

	Vector<variableType> paramList;
	Vector<variableType> localVarList;
	public String returnType;
	classType ownerClass;


	public methodType(String name){
		super(name);
		paramList = new Vector<variableType>();
		localVarList = new Vector<variableType>();
		ownerClass = null;
		returnType ="";
	}

	public void addLocalVariable(variableType var){
		localVarList.add(var);
	}

	public void addParameter(variableType var){
		paramList.add(var);
	}

	public void setType(String type){
		returnType = type;
	}

	public String getType(){
		return returnType;
	}

	public void setName(String name){
		this.name = name;
	}

	public void stealParametersFrom(methodType method) throws Exception{
		for(int i=0; i<method.paramList.size(); i++){
			variableType parameter = method.paramList.get(i);
	   		if(!this.variableExists(parameter))
		 		this.addParameter(parameter);
		 	else
		 		throw new Exception("Error: Double declaration of parameter "+parameter.type+" "+parameter.name);
		}
	}

	public boolean variableExists(variableType var){
		for(int i=0; i<localVarList.size(); i++){
			if((localVarList.get(i).name!=null) && (localVarList.get(i).name.equals(var.name)))
				return true;
		}
		for(int i=0; i<paramList.size(); i++){
			if((paramList.get(i).name!=null) && (paramList.get(i).name.equals(var.name)))
				return true;
		}
		return false;
	}

	public void printMethod(){
		System.out.println("Method name: "+name+" Return type: "+returnType);
		System.out.println("\t\tParameters: ");
		for(int i=0; i<paramList.size(); i++){
			System.out.print("\t\t\t");
			paramList.get(i).printVariable();
		}
		System.out.println("\t\tLocal Variables: ");
		for(int i=0; i<localVarList.size(); i++){
			System.out.print("\t\t\t");
			localVarList.get(i).printVariable();
		}
		System.out.println();
	}

	public classType getOwnerClass(){
		return ownerClass;
	}

	public String getVarType(String name){
		for(int i=0; i<paramList.size(); i++){
			if(paramList.get(i).name.equals(name))
				return paramList.get(i).type;
		}
		for(int i=0; i<localVarList.size(); i++){
			if(localVarList.get(i).name.equals(name))
				return localVarList.get(i).type;
		}
		return null;
	}

	public boolean matchParameters(methodType other, methodType currenMethod, SymbolTable symbolTable) throws Exception{
		for(int i=0; i<this.paramList.size(); i++){
			if(i >= other.paramList.size())
				return false;
			variableType var1 = this.paramList.get(i);
			variableType var2 = other.paramList.get(i);
			String type1 = var1.getType(symbolTable, currenMethod); 
			String type2 = var2.getType(symbolTable, currenMethod);
			if(!variableType.matchType(symbolTable, type1, type2))
				return false;
		}
		if(this.paramList.size() == other.paramList.size())
			return true;
		else
			return false;
	}

	public boolean isOverridableBy(methodType method, SymbolTable symbolTable) throws Exception{
		if(!(this.returnType.equals(method.returnType)) || !(this.name.equals(method.name)))
			return false;
		if(!this.matchParameters(method, null, symbolTable))
			return false;
		return true;
	}
}