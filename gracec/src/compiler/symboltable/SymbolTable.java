package compiler.symboltable;

import java.util.*;

public class SymbolTable {
    private LinkedList<Method_t> ST;

    public SymbolTable(LinkedList<Method_t> method) {
        ST = method;
    }

    public LinkedList<Method_t> getST() { return ST; }

    public Method_t contains(String nam) { // returns a class if exists in ST else null
        int i = 0, size = ST.size();
        while (i < size)
            if (ST.get(i++).getName().equals(nam))
                return ST.get(i-1);
        return null;
    }

    public void printST() {
        int i = 0;
        System.out.println("\n____Symbol Table____\n");
        System.out.println("\n\tmethod:");
        while (i < ST.size()) {
            System.out.println("\tMethods:");
            ST.get(i).printMethod();
            i++;
        }
    }

}