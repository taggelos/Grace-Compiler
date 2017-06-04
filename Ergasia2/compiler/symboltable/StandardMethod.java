package compiler.symboltable;

import java.util.*;

public class StandardMethod{
    private String name;
    private String return_type;
    private String params;
   

    public StandardMethod(String name, String return_type, String params) {
        this.name = name;
        this.return_type = return_type;
        this.params = params;
    }

    public String getReturnType() {
        return this.return_type;
    }

    public String getParams() {
        return this.params;
    }
    
    public String getName() {
        return this.name;
    }
    
}