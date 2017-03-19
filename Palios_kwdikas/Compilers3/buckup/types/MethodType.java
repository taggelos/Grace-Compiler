package types;

import java.util.*;

public class MethodType extends MyType {
    public String type;
    public LinkedList<VariableType> ParamList;      // Lista me ta Params tou Method
    public LinkedList<VariableType> VarList;        // Lista me ta Vars tou Method
    public ClassType from;                          // H klash pou anhkei to Method
    public int id;
    public int par_count;
    public int var_count;

    public MethodType(String type, String name) {
        super(name);
        this.type = type;
        this.ParamList = new LinkedList<VariableType>();
        this.VarList = new LinkedList<VariableType>();
        this.from = null;
    }

    public boolean addParam(VariableType param) {           // Pros8etoume to Parameter an den uparxei hdh sth lista
        for(int i = 0; i < ParamList.size(); i++) {
            if(ParamList.get(i).name.equals(param.name))
                return false;
        }

        this.par_count += 1;     
        param.id = this.par_count;
        param.var_TEMP = new String("TEMP " + param.id);
        ParamList.add(param);
        return true;
    }

    public boolean addVar(VariableType var) {           // Pros8etoume th metablhth an den yparxei hdh sth lista twn Vars h' twn Parameters
        for(int i = 0; i < VarList.size(); i++) {
            if(VarList.get(i).name.equals(var.name))
                return false;
        }

        for(int j = 0; j < ParamList.size(); j++) {
            if(ParamList.get(j).name.equals(var.name))
                return false;
        }

        VarList.add(var);
        return true;
    }    

    public VariableType methContainsVar(String varName) {
        for(int i = 0; i < VarList.size(); i++) {         // Elexgoume an to onoma ths metablhths uparxei sth lista VarList
            if(VarList.get(i).name.equals(varName))
                return VarList.get(i);
        }
        for(int j = 0; j < ParamList.size(); j++) {       // Elexgoume an to onoma ths metablhths uparxei sth lista ParamList
            if(ParamList.get(j).name.equals(varName))
                return ParamList.get(j);
        }
        return null;
    }

    public void addVarTEMP(String var, String temp) {
        for(int i = 0; i < VarList.size(); i++) {
            if(VarList.get(i).name.equals(var)) {
                VarList.get(i).var_TEMP = temp;
            }
        }
    }
}