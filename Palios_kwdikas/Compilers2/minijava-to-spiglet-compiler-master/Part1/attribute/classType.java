package attribute;


import syntaxtree.*;
import java.util.*;
import java.util.Vector;

public class classType extends Attribute {

	Vector<methodType> methodList;
	Vector<variableType> fieldList;
	classType extendedClass;

	public classType(String name){
		super(name);
		extendedClass = null;
		methodList = new Vector<methodType>();
		fieldList = new Vector<variableType>();
	}

	public void addMethod(methodType method){
		methodList.add(method);
		method.ownerClass = this;
	}

	public void addField(variableType variable){
		fieldList.add(variable);
	}

	public boolean methodExists(methodType method){
		for(int i=0; i<methodList.size(); i++){
			if(methodList.get(i).name.equals(method.name)){
				return true;
			}
		}
		return false;
	}

	public boolean fieldExists(variableType field){
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
			System.out.print("\t");
			fieldList.get(i).printVariable();
		}
		System.out.println("Methods: ");
		for(int i=0; i<methodList.size(); i++){
			System.out.print("\t");
			methodList.get(i).printMethod();
		}
		System.out.println();
	}

	public void inherits(classType parent){
		this.extendedClass = parent;
	}

	public classType getFather(){
		return extendedClass;
	}

	public String getVarType(String name){
		for(int i=0; i<fieldList.size(); i++){
			if(fieldList.get(i).name.equals(name))
				return fieldList.get(i).type;
		}
		return null;
	}


	public methodType getMethodByName(String methodName){
		for(int i=0; i<methodList.size(); i++){
			if(methodList.get(i).name.equals(methodName)){
				return methodList.get(i);
			}
		}
		return null;
	}

	public methodType getInheritedMethodByName(String methodName){
		classType ownerClass = this;
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
		classType ownerClass = this;
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

	public boolean isSuperClassOf(classType subclass){
		classType checkClass = subclass;
		while(checkClass != null){
			if(checkClass == this)
				return true;
			checkClass = checkClass.extendedClass;
		}
		return false;
	}
}