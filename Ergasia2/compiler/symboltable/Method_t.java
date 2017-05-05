package symboltable;

import java.util.*;

public class Method_t extends MyType {
    private String return_type;
    public LinkedList<Variable_t> methodParams;
    public LinkedList<Variable_t> methodVars;

    public Method_t(String return_type, String name) {
        super(name);
        this.return_type = return_type;
        this.methodParams = new LinkedList<Variable_t>();
        this.methodVars = new LinkedList<Variable_t>();
    }

    public String get_return_type() {
        return this.return_type;
    }

    public LinkedList<Variable_t> getParams() {
        return this.methodParams;
    }

    public String methContains(String varName) {
        int i = 0;
        while (i < methodVars.size()) {         // Elexgoume an to onoma ths metablhths uparxei sth lista methodVars
            if (methodVars.get(i).getName().equals(varName))
                return methodVars.get(i).get_return_type();
            i++;
        }
        i = 0;
        while (i < methodParams.size()) {       // Elexgoume an to onoma ths metablhths uparxei sth lista methodParams
            if (methodParams.get(i).getName().equals(varName))
                return methodParams.get(i).get_return_type();
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
        System.out.print(return_type + " " + this.getName() + "(");
        int i = 0;
        while (i < methodParams.size()) {
            methodParams.get(i++).printVar();
            if (i != methodParams.size())
                System.out.print(", ");
        }
        System.out.println(")\n\t\t\tMethod Variables:");
        i = 0;
        while (i < methodVars.size()) {
            System.out.print("\t\t\t\t");
            methodVars.get(i++).printVar();
            System.out.println("");
        }
    }
}