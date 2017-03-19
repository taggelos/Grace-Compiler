package attribute;


import syntaxtree.*;
import symboltable.*;
import java.util.*;
import java.util.Vector;

public class ClassType extends Attribute {

	public Vector<MethodType> methodList;
	Vector<VariableType> fieldList;
	ClassType extendedClass;
	boolean isMainClass;

	public ClassType(String name){
		super(name);
		extendedClass = null;
		methodList = new Vector<MethodType>();
		fieldList = new Vector<VariableType>();
		tempMap = new HashMap<String,Integer>();
		isMainClass = false;
	}

	public void addMethod(MethodType method){
		methodList.add(method);
		method.ownerClass = this;
		if(method.name.equals("main"))
			isMainClass = true;
	}

	public void addField(VariableType variable){
		fieldList.add(variable);
	}

	public boolean methodExists(MethodType method){
		for(int i=0; i<methodList.size(); i++){
			if(methodList.get(i).name.equals(method.name)){
				return true;
			}
		}
		return false;
	}

	public boolean fieldExists(VariableType field){
		for(int i=0; i<fieldList.size(); i++){
			if(fieldList.get(i).name.equals(field.name)){
				return true;
			}
		}
		return false;
	}

	public void printClass(){
		System.out.print("Class name: "+name);
		if(extendedClass != null)
			System.out.println(" extends: "+extendedClass.name);
		else
			System.out.println();
		System.out.println("Fields: ");
		for(int i=0; i<fieldList.size(); i++){
			System.out.print("\t"+"TEMP "+tempMap.get(fieldList.get(i).name)+") ");
			System.out.print("\t"+"OFFSET: "+getFieldOffset(fieldList.get(i).name)+" ");
			// System.out.print("\t");
			fieldList.get(i).printVariable();
		}
		System.out.println("Methods: ");
		for(int i=0; i<methodList.size(); i++){
			System.out.print("\t");
			methodList.get(i).printMethod();
		}
		System.out.println();
	}

	public void inherits(ClassType parent){
		this.extendedClass = parent;
	}

	public ClassType getFather(){
		return extendedClass;
	}

	public String getVarType(String name){
		for(int i=0; i<fieldList.size(); i++){
			if(fieldList.get(i).name.equals(name))
				return fieldList.get(i).type;
		}
		if(extendedClass != null)
			return extendedClass.getVarType(name);
		return null;
	}


	public MethodType getMethodByName(String methodName){
		for(int i=0; i<methodList.size(); i++){
			if(methodList.get(i).name.equals(methodName)){
				return methodList.get(i);
			}
		}
		if(extendedClass != null)
			return extendedClass.getMethodByName(methodName);
		return null;
	}

	public MethodType getInheritedMethodByName(String methodName){
		ClassType ownerClass = this;
		while(ownerClass != null){	
			for(int i=0; i<ownerClass.methodList.size(); i++){
				if(ownerClass.methodList.get(i).name.equals(methodName)){
					return ownerClass.methodList.get(i);
				}
			}
			ownerClass = ownerClass.extendedClass;
		}
		return null;
	}

	public boolean hasMethod(String methodName){
		ClassType ownerClass = this;
		while(ownerClass != null){
			for(int i=0; i<ownerClass.methodList.size(); i++){
				if(ownerClass.methodList.get(i).name.equals(methodName)){
					return true;
				}
			}
			ownerClass = ownerClass.extendedClass;
		}
		return false;
	}

	public boolean isSuperClassOf(ClassType subclass){
		ClassType checkClass = subclass;
		while(checkClass != null){
			if(checkClass == this)
				return true;
			checkClass = checkClass.extendedClass;
		}
		return false;
	}

	public int getFieldOffset(String varName){
		int initialNumber = this.getFieldNumber();
		initialNumber -= fieldList.size();
		initialNumber *= 4;
		for(int i=0; i<fieldList.size(); i++){
			if(fieldList.get(i).name.equals(varName))
				return initialNumber+((i+1)*4);
		}
		//DEBUG
		if(extendedClass != null)
			return extendedClass.getFieldOffset(varName);
		return -1;
	}

	public int getFieldNumber(){
		if(extendedClass == null)
			return fieldList.size();
		return extendedClass.getFieldNumber() + fieldList.size();
	}

	public int getMethodOffset(String methodName){
		Vector<MethodType> extendedMethodList = this.getExtendedMethodList();
		for(int i = 0; i< extendedMethodList.size(); i++){
			MethodType method = extendedMethodList.get(i);
			if(method.name.equals(methodName)){
				return (i*4);
			}
		}
		return -1;
	}



	public int getMethodCount(){
		return this.getExtendedMethodList().size();
	}

	public boolean isMain(){
		return this.isMainClass;
	}

	public Vector<MethodType> getExtendedMethodList(){
		if(extendedClass == null){
			if(isMainClass)
				return new Vector<MethodType>();
			else
				return this.methodList;
		}
		Vector<MethodType> fatherMethods = extendedClass.getExtendedMethodList();
		Vector<MethodType> returnList = new Vector(fatherMethods);
		for(MethodType method : this.methodList){
			boolean ovverrides = false;
			int index = -1;
			for(int i = 0; i < fatherMethods.size(); i++){
				MethodType fatherMethod = fatherMethods.get(i);
				if(method.name.equals(fatherMethod.name)){
				// if(method.isOverridableBy(fatherMethod,symbolTable)){
					ovverrides = true;
					index = i;
					break;
				}
			}
			if(ovverrides){
				returnList.set(index, method);
			} else {
				returnList.add(method);
			}
		}
		return returnList;
	}

}