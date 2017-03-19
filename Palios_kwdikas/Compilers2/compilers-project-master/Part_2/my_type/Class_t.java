package my_type;

import java.util.*;

public class Class_t extends MyType {
    public LinkedList<Method_t> classMethods;
    public LinkedList<Variable_t> classVars;
    private String dad; // An yparxei

    public Class_t(String name, String dad) {
        super(name);
        classMethods = new LinkedList<Method_t>();
        classVars = new LinkedList<Variable_t>();
        this.dad = dad;
    }

    public boolean addMethod(Method_t meth) {       //Pros8etoume to Method an den uparxei hdh sth lista, h' einai h main
        for (int i = 0 ; i < classMethods.size() ; i++)
            if (classMethods.get(i).getName().equals(meth.getName()))
                return false;
            else if (meth.getName().equals("main"))
                return false;
        meth.comesFrom = this;
        classMethods.addLast(meth);
        return true;
    }

    public boolean addVar(Variable_t var) {     //Pros8etoume to Variable an den uparxei hdh sth lista
        for (int i = 0 ; i < classVars.size() ; i++)
            if (classVars.get(i).getName().equals(var.getName()))
                return false;
    	classVars.addLast(var);
        return true;
    }

    public Method_t getMethod(String methName) {
        for (int i = 0 ; i < classMethods.size() ; i++)
            if (classMethods.get(i).getName().equals(methName))
                return classMethods.get(i);
        return null;
    }

    public Variable_t classContainsVar(String varName) {
        for (int i = 0 ; i < classVars.size() ; i++)
            if (classVars.get(i).getName().equals(varName))
                return classVars.get(i);
        return null;
    }

    public boolean classContainsMeth(String methName) {
        for (int i = 0 ; i < classMethods.size() ; i++)
            if (classMethods.get(i).getName().equals(methName))
                return true;
        return false;
    }

    public boolean checkMethod(Method_t meth) {     // Elegxos an to methos einai idio me to this.method
        int i = 0;
        while (i < classMethods.size()) {
            if (classMethods.get(i).getName().equals(meth.getName()))       // Briskoume to sugkekrimeno method
                if (classMethods.get(i).getType().equals(meth.getType())) { // Elegxoume an exoun idio typo
                    LinkedList<Variable_t> parameters = classMethods.get(i).getParams();
                    if (parameters.size() != meth.getParams().size())
                        return false;
                    for (int j = 0 ; j < parameters.size() ; j++) 
                        if (parameters.get(j).getType() != meth.getParams().get(j).getType())
                            return false;
                    return true;
                }
                else
                    return false;
            i++;
        }
        return false;
    }

    public String getDad() {
        return this.dad;
    }

}