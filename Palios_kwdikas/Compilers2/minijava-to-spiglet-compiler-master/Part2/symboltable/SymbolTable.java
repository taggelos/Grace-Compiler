package symboltable;

import syntaxtree.*;
import java.util.Vector;
import attribute.*;
import java.util.Map.*;
import java.util.HashMap;



public class SymbolTable{
	public   Vector<ClassType> classList;
	Vector<String> typeList;
	public Vector<String> primitiveTypeList ;
	public HashMap<String,Integer> classOffsets;

	public SymbolTable(){
		classList = new Vector<ClassType>();
		typeList = new Vector<String>();
		primitiveTypeList = new Vector<String>();
		primitiveTypeList.add("int");
		primitiveTypeList.add("int[]");
		primitiveTypeList.add("boolean");
		classOffsets = new HashMap<String,Integer>();
    }

    public void printClasses(){
		for(int i = 0; i< classList.size(); i++){
			classList.get(i).printClass();
		}
    }

    public ClassType getClassByName(String className){
    	for(int i = 0; i< classList.size(); i++){
        	if(classList.get(i).name.equals(className)){
        		return classList.get(i);
        	}
      	}
      	return null;
    }

    // public MethodType getMethodByName(String methodName){
	   //  for(int i = 0; i< classList.size(); i++){
	   //  	ClassType classObject = classList.get(i);
	   //  	for(int j=0; j<classObject.methodList.size(); j++){
	   //  		if(classObject.methodList.get(j).name.equals(methodName)){
	   //  			return classObject.methodList.get(j);
	   //  		}
	   //  	}
	   //  }
    // 	return null;
    // }




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

    public int getMaxTempCount(){
    	int max = 1;
		Entry<String,Integer> maxEntry = null;
    	for(int i = 0; i< classList.size(); i++){
			ClassType classObj = classList.get(i);
			for(int j=0; j<classObj.methodList.size(); j++){
				MethodType method = classObj.methodList.get(j); 
				for(Entry<String,Integer> entry : method.tempMap.entrySet()) {
				    if (maxEntry == null || entry.getValue() > max) {
				        maxEntry = entry;
				        max = entry.getValue();
				    }
				}
			}
		}
		return max;
    }

    public void initOffsets(){
    	for(int i=1; i<classList.size(); i++){
    		ClassType classObject = classList.get(i);
			if(i == 1){ //First class after mainclass
				classOffsets.put(classObject.name,0);
			} else {
				ClassType prevClass = classList.get(i-1); 
				int offset = prevClass.getMethodCount()*4;
				offset += classOffsets.get(prevClass.name);
				classOffsets.put(classObject.name,offset);
			}
		}
    }
}