package attribute;
import symboltable.*;;
import syntaxtree.*;
import java.util.*;
import java.util.Vector;

public class VariableType extends Attribute {

	public String type;


	public VariableType(String type, String identifier){
		super(identifier);
		this.type = type;
	}

	public void printVariable(){
		System.out.println(type+" "+name);
	}

	public String getType(){
		return type;
	}

	public String getType(SymbolTable symbolTable, MethodType method) throws Exception{
		if(this.type != null)
			return this.type;

		String result = method.getVarType(this.name);
		
		if(result != null){
			return result;
		}

		ClassType owner = method.getOwnerClass();
		while(owner != null){
			result = owner.getVarType(this.name);
			if(result != null){
				this.type = result;
				return result;
			}
			owner = owner.getFather();
		}
		throw new Exception("Error: Variable "+this.name+" undecleared, first use in this function.");
	}

	public static boolean matchType(SymbolTable symbolTable, String type1, String type2){
		if(type1.equals(type2))
			return true;
		for(int i=0; i<symbolTable.primitiveTypeList.size(); i++ ){
			if(type1.equals(symbolTable.primitiveTypeList.get(i)) || type1.equals(symbolTable.primitiveTypeList.get(i)))
				return false;
		}

		ClassType class1 = symbolTable.getClassByName(type1);	
		ClassType class2 = symbolTable.getClassByName(type2);

		return class1.isSuperClassOf(class2);
	}
	
}