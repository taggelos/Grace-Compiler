package types;

import java.util.*;

public class ClassType extends MyType {
    public LinkedList<MethodType> MethodList;       // Lista me ta Methods tou Class
    public LinkedList<VariableType> VarList;        // Lista me ta Vars tou Class
    public String parent;                           // Gonio klash an yparxei

    public ClassType(String name, String parent) {
        super(name);
        MethodList = new LinkedList<MethodType>();
        VarList = new LinkedList<VariableType>();
        this.parent = parent;
    }

    public boolean addMethod(MethodType meth) {       // Pros8etoume to Method an den uparxei hdh sth lista, h' einai h main
        for(int i = 0 ; i < MethodList.size() ; i++) {
            if(MethodList.get(i).name.equals(meth.name))
                return false;
            else if(meth.name.equals("main"))
                return false;
        }
        meth.from = this;
        MethodList.add(meth);
        return true;
    }

    public MethodType getMethod(String methName) {
        for(int i = 0 ; i < MethodList.size() ; i++) {
            if(MethodList.get(i).name.equals(methName))
                return MethodList.get(i);
        }
        return null;
    }

    public boolean checkMethod(MethodType meth) {     // Elegxos an to methos einai idio me to this.method       
        for(int i = 0; i < MethodList.size(); i++) {
            if(MethodList.get(i).name.equals(meth.name)) {        // Briskoume an exei oristei tetoia Method
                if(MethodList.get(i).type.equals(meth.type)) {    // Elegxoume an exoun idio typo
                    LinkedList<VariableType> parameters = MethodList.get(i).ParamList;
                    if(parameters.size() != meth.ParamList.size())        // Elegxos twn Parameters tou Method
                        return false;
                    for(int j = 0; j < parameters.size(); j++) {
                        if(parameters.get(j).type != meth.ParamList.get(j).type) {
                            return false;
                        }
                    }
                    return true;
                }
                else
                    return false;
            }
        }
        return false;
    }    

    public boolean classContainsMeth(String methName) {         // Elegxoume an h sunarthsh yparxei sth lista twn Methods
        for(int i = 0 ; i < MethodList.size() ; i++) {
            if(MethodList.get(i).name.equals(methName))
                return true;
        }
        return false;
    }

    public boolean addVar(VariableType var) {                   // Pros8etoume to Variable an den uparxei hdh sth lista
        for(int i = 0 ; i < VarList.size() ; i++) {
            if(VarList.get(i).name.equals(var.name))
                return false;
        }
    	VarList.add(var);
        return true;
    }

    public VariableType classContainsVar(String varName) {      // An yparxei h metablhth sth lista twn Vars thn epistrefoume
        for(int i = 0 ; i < VarList.size() ; i++) {
            if(VarList.get(i).name.equals(varName))
                return VarList.get(i);
        }
        return null;
    }
}