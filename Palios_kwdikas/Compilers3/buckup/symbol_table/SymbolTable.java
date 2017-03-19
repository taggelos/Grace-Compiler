package symbol_table;

import types.*;
import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;

public class SymbolTable {
    public LinkedList<ClassType> symboltable;
    public int TEMP_count;

    public SymbolTable(LinkedList<ClassType> classes) {
        symboltable = classes;
        TEMP_count = 0;
    }

    public ClassType contains(String nam) {       // Epistrefei to onoma ths klashs an yparxei sto Symbol Table 
        for(int i = 0; i < symboltable.size(); i++) {
            if(symboltable.get(i).name.equals(nam))
                return symboltable.get(i);
        }
        return null;
    }
}