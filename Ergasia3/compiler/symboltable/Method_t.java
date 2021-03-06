package compiler.symboltable;

import java.util.*;

public class Method_t extends MyType {
    private String return_type;
    public LinkedList<Variable_t> methodParams;
    public LinkedList<Variable_t> methodVars;
    public LinkedList<Method_t> methodList;
    public Method_t from;
    //public boolean isDeclaration;

    public Method_t(String return_type, String name) {
        super(name);
        this.return_type = return_type;
        this.methodParams = new LinkedList<Variable_t>();
        this.methodVars = new LinkedList<Variable_t>();
        this.methodList = new LinkedList<Method_t>();
        this.from = null;
       // this.isDeclaration=false;
    }

    public String get_return_type() {
        return this.return_type;
    }

    public LinkedList<Variable_t> getParams() {
        return this.methodParams;
    }

    public void addFrom(Method_t from) {
        this.from = from;
    }

    public boolean addMethod(Method_t meth) {       // Pros8etoume to Method an den uparxei hdh sth lista, h' einai h main
        for(int i = 0 ; i < methodList.size() ; i++) {
            if(methodList.get(i).getName().equals(meth.getName()))
                return false;
        }
        //meth.from = this;
        methodList.add(meth);
        return true;
    }

    public Method_t getMethod(String methName) {
        for(int i = 0 ; i < methodList.size() ; i++) {
            if(methodList.get(i).getName().equals(methName))
                return methodList.get(i);
        }
        return null;
    }

    public String methContains(String varName) {
        int i = 0;
        while (i < methodVars.size()) {         // Elexgoume an to onoma ths metablhths uparxei sth lista methodVars
            //System.out.println(varName + " <> "+ methodVars.get(i).getName());
            if (methodVars.get(i).getName().equals(varName))
                return methodVars.get(i).getType();
            i++;
        }
        i = 0;
        while (i < methodParams.size()) {       // Elexgoume an to onoma ths metablhths uparxei sth lista methodParams
            //System.out.println(varName + " <> "+ methodParams.get(i).getName());
            if (methodParams.get(i).getName().equals(varName))
                return methodParams.get(i).getType();
            i++;
        }
        return null;
    }

    public boolean addParam(Variable_t param) {
        int i = 0;
        while (i < methodParams.size())
            if (methodParams.get(i++).getName().equals(param.getName()))
                return false;
        methodParams.addLast(param);
        return true;
    }

    public boolean addVar(Variable_t var) {
        int i = 0;
        while (i < methodVars.size())
            if (methodVars.get(i++).getName().equals(var.getName()))
                return false;
        for (int j = 0 ; j < methodParams.size() ; j++)
            if (methodParams.get(j).getName().equals(var.getName()))
                return false;
        methodVars.addLast(var);
        return true;
    }

    public void printMethod() {
    	String f;
        if(this.from != null)
        f = this.from.getName();
        else f = "null";
        System.out.print(return_type + " " + this.getName() + ", FROM: " + f + "(");
        int i = 0;
        while (i < methodParams.size()) {
            methodParams.get(i++).printVar();
            if (i != methodParams.size())
                System.out.print(", ");
        }
        System.out.println(")\n\tMethod Variables:");
        i = 0;
        while (i < methodVars.size()) {
            System.out.print("\t\t");
            methodVars.get(i++).printVar();
            System.out.println("");
        }
        i = 0;
        /*while (i < methodList.size()) {
        	System.out.println("--->"+methodList.get(i++).getName());
            if (i != methodList.size())
                System.out.print(", ");
        } */
    }
}