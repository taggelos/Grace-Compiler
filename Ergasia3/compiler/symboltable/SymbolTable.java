package compiler.symboltable;

import java.util.*;

public class SymbolTable {
    public LinkedList<Method_t> symboltable;

    public SymbolTable(LinkedList<Method_t> method) {
    	symboltable = method;
    }

    public Method_t contains(String nam) {
        int i = 0, size = symboltable.size();
        while (i < size)
            if (symboltable.get(i++).getName().equals(nam))
                return symboltable.get(i-1);
        return null;
    }

    public void printST() {
        int i = 0;
        System.out.println("\n____Symbol Table____\n");
        while (i < symboltable.size()) {
            System.out.println("Method:");
            symboltable.get(i).printMethod();
            i++;
        }
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~\n");
    }

}