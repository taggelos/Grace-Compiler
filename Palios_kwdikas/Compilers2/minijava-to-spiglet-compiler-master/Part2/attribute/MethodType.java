package attribute;

import symboltable.*;
import syntaxtree.*;
import java.util.*;
import java.util.Vector;

public class MethodType extends Attribute {

	public Vector<VariableType> paramList;
	Vector<VariableType> localVarList;
	public String returnType;
	ClassType ownerClass;
	int offset;
	int temps;


	public MethodType(String name){
		super(name);
		paramList = new Vector<VariableType>();
		localVarList = new Vector<VariableType>();
		ownerClass = null;
		returnType ="";
		temps = -1;
		tempMap = new HashMap<String,Integer>();
	}

	public void addLocalVariable(VariableType var){
		// tempMap.put(var.name, getNextTemp());
		localVarList.add(var);
	}

	public void addParameter(VariableType var){
		tempMap.put(var.name, getNextTemp());
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

	public void stealParametersFrom(MethodType method) throws Exception{
		for(int i=0; i<method.paramList.size(); i++){
			VariableType parameter = method.paramList.get(i);
	   		if(!this.variableExists(parameter))
		 		this.addParameter(parameter);
		 	else
		 		throw new Exception("Error: Double declaration of parameter "+parameter.type+" "+parameter.name);
		}
	}

	public boolean variableExists(VariableType var){
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
		System.out.println("Method name: ("+ownerClass.name+"_)"+name+" Return type: "+returnType);
		System.out.println("\t\tParameters: ");
		for(int i=0; i<paramList.size(); i++){
			VariableType var = paramList.get(i);
			System.out.print("\t\t\t"+"TEMP "+tempMap.get(var.name)+") ");
			var.printVariable();
		}
		System.out.println("\t\tLocal Variables: ");
		for(int i=0; i<localVarList.size(); i++){
			VariableType var = localVarList.get(i);
			System.out.print("\t\t\t"+"TEMP "+tempMap.get(var.name)+") ");
			// System.out.print("\t\t\t");
			var.printVariable();
		}
		System.out.println();
	}

	public ClassType getOwnerClass(){
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

	public boolean matchParameters(MethodType other, MethodType currenMethod, SymbolTable symbolTable) throws Exception{
		for(int i=0; i<this.paramList.size(); i++){
			if(i >= other.paramList.size())
				return false;
			VariableType var1 = this.paramList.get(i);
			VariableType var2 = other.paramList.get(i);
			String type1 = var1.getType(symbolTable, currenMethod); 
			String type2 = var2.getType(symbolTable, currenMethod);
			if(!VariableType.matchType(symbolTable, type1, type2))
				return false;
		}
		if(this.paramList.size() == other.paramList.size())
			return true;
		else
			return false;
	}

	public boolean isOverridableBy(MethodType method, SymbolTable symbolTable) throws Exception{
		if(!(this.returnType.equals(method.returnType)) || !(this.name.equals(method.name)))
			return false;
		if(!this.matchParameters(method, null, symbolTable))
			return false;
		return true;
	}

	public int getNextTemp(){
		if(temps == -1){
			temps = paramList.size()+1;
		}
		return temps++;
	}

	public int paramSize(){
		return paramList.size();
	}

	public int getTempOf(String varName){
		if (this.tempMap.containsKey(varName))
			return this.tempMap.get(varName);
		ClassType owner = this.ownerClass;
		while(owner != null){
			if(owner.tempMap.containsKey(varName))
				return owner.tempMap.get(varName);
			owner = owner.getFather();
		}
		return -1;
	}

	public boolean hasVar(String varName){
		// printMap(this.tempMap);
		return this.tempMap.containsKey(varName);
	}


}