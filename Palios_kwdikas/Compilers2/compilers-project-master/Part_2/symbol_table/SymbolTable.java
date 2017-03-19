package symbol_table;

import my_type.*;
import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;

public class SymbolTable {
    private LinkedList<Class_t> ST;

    public SymbolTable(LinkedList<Class_t> classes) {
        ST = classes;
    }

    public LinkedList<Class_t> getST() { return ST; }

    public Class_t contains(String nam) { // returns a class if exists in ST else null
        int i = 0, size = ST.size();
        while (i < size)
            if (ST.get(i++).getName().equals(nam))
                return ST.get(i-1);
        return null;
    }

    public void printST() {
        int i = 0, j = 0;
        System.out.println("\n____Symbol Table____\n");
        System.out.println("\n\tClasses:");
        while (i < ST.size()) {
            j = 0; 
            System.out.println("___________________________________________________");
            if (ST.get(i).getDad() == null)
                System.out.println(ST.get(i).getName() + ":\n\tVars:");
            else
                System.out.println(ST.get(i).getName() + " extends " + ST.get(i).getDad() + ":\n\tVars:");
            while (j < ST.get(i).classVars.size()) {
                System.out.print("\t\t");
                ST.get(i).classVars.get(j).printVar();
                System.out.println("");
                j++;
            }
            j = 0; 
            System.out.println("\tMethods:");
            while (j < ST.get(i).classMethods.size()) {
                System.out.print("\t\t");
                ST.get(i).classMethods.get(j).printMethod();
                j++;
            }
            i++;
        }
    }

}