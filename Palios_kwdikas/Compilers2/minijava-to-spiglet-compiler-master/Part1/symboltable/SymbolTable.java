package symboltable;

import syntaxtree.*;
import java.util.Vector;
import attribute.*;



public class SymbolTable{
	public   Vector<classType> classList;
	Vector<String> typeList;
	public Vector<String> primitiveTypeList ;

	public SymbolTable(){
		classList = new Vector<classType>();
		typeList = new Vector<String>();
		primitiveTypeList = new Vector<String>();
		primitiveTypeList.add("int");
		primitiveTypeList.add("int[]");
		primitiveTypeList.add("boolean");
    }

    public void printClasses(){
		for(int i = 0; i< classList.size(); i++){
			classList.get(i).printClass();
		}
    }

    public classType getClassByName(String className){
    	for(int i = 0; i< classList.size(); i++){
        	if(classList.get(i).name.equals(className)){
        		return classList.get(i);
        	}
      	}
      	return null;
    }

    public boolean typeExists(String type){  	        
	    for(int i=0; i<primitiveTypeList.size(); i++){
	    	if(primitiveTypeList.get(i).equals(type)){
	    		return true;
	    	}
	    }
        for(int i=0; i<classList.size(); i++){
	        if(classList.get(i).name.equals(type)){
	        	return true;
	        }
        }
	    return false;
    }
}