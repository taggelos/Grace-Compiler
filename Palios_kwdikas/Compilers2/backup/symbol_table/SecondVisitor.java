package symbol_table;

import syntaxtree.*;
import visitor.GJNoArguDepthFirst;
import java.util.*;
import my_type.*;

/* 
    Visitor gia thn dhmiourgia tou Symbol Table 
*/

public class SecondVisitor extends GJNoArguDepthFirst<MyType> {
    private SymbolTable st;

    public SecondVisitor(LinkedList<ClassType> classes) { 
        st = new SymbolTable(classes);          // Pairnei th lista apo klaseis apo ton FirstVisitor
    }

    public SymbolTable getSymbolTable() {
        return st;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
    public MyType visit(MainClass n) throws Exception {
        n.f1.accept(this);
        n.f11.accept(this);
        n.f14.accept(this);
        n.f15.accept(this);
        VariableType id1 = new VariableType("String[]", n.f11.accept(this).getName());
        MethodType id2 = new MethodType("void", "main");
        id2.addParam(id1);
        String name = n.f1.accept(this).getName();

        if(!st.contains(name).addMethod(id2))         // Epanorismos 
            throw new Exception("Method " + name + " already exists!");

        if(n.f14.present())    
            for(int i = 0 ; i < n.f14.size() ; i++) {
                boolean flag = false;
                if(!st.contains(name).addVar((VariableType)n.f14.nodes.get(i).accept(this)))     // Epanorismos 
                    throw new Exception("Variable " + n.f14.nodes.get(i).accept(this).getName() + " already exists!");
                String vartype = ((VariableType) n.f14.nodes.get(i).accept(this)).getType();
                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { // Mh egkyros typos
                    for(int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;
                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        return null;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
    public MyType visit(ClassDeclaration n) throws Exception {
        String name = n.f1.accept(this).getName();
        n.f3.accept(this);
        n.f4.accept(this);
        if(n.f3.present()) {    
            for(int i = 0; i < n.f3.size(); i++) {   
                boolean flag = false;
                if(!st.contains(name).addVar((VariableType)n.f3.nodes.get(i).accept(this)))      // Epanorismos
                    throw new Exception("Variable " + n.f3.nodes.get(i).accept(this).getName() + " already exists!");

                String vartype = ((VariableType) n.f3.nodes.get(i).accept(this)).getType();
                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) {     // Mh egkyros typos
                    for (int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;

                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        }
        if(n.f4.present()) {    
            for (int i = 0; i < n.f4.size(); i++) {
                boolean flag = false;
                if(st.contains(name).addMethod((MethodType)n.f4.nodes.get(i).accept(this)) == false)    // Epanorismos
                    throw new Exception("Method " + n.f4.nodes.get(i).accept(this).getName() + " already exists!");

                String vartype = ((MethodType) n.f4.nodes.get(i).accept(this)).getType();
                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) {  // Mh egkyros typos
                    for (int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;
                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        }
        return null;
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
    public MyType visit(ClassExtendsDeclaration n) throws Exception {
        String id = n.f1.accept(this).getName();
        String parentid = n.f3.accept(this).getName();
        n.f5.accept(this);
        n.f6.accept(this);
        if(n.f5.present()) {     
            for(int i = 0; i < n.f5.size(); i++) {
                boolean flag = false;
                if(!st.contains(id).addVar((VariableType)n.f5.nodes.get(i).accept(this)))        // Epanorismos
                    throw new Exception("Variable " + n.f5.nodes.get(i).accept(this).getName() + " already exists!");

                String vartype = ((VariableType) n.f5.nodes.get(i).accept(this)).getType();
                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) {     // Mh egkyros typos
                    for(int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;
                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        }

        for(int i = 0 ; i < st.contains(parentid).VarList.size() ; i++)         // Pros8etoume tis metablhtes tou Parent Class
            st.contains(id).addVar((VariableType) st.contains(parentid).VarList.get(i));

        if(n.f6.present()) {
            for(int i = 0 ; i < n.f6.size() ; i++) {
                boolean flag = false;
                if(!st.contains(id).addMethod((MethodType)n.f6.nodes.get(i).accept(this)))               // Epanorismos
                    throw new Exception("Method " + n.f6.nodes.get(i).accept(this).getName() + " already exists!");
                else if(st.contains(parentid).classContainsMeth(n.f6.nodes.get(i).accept(this).getName()))  // An yparxei ston Parent Class
                    if(!st.contains(parentid).checkMethod((MethodType)n.f6.nodes.get(i).accept(this)))       
                        throw new Exception("Method " + n.f6.nodes.get(i).accept(this).getName() + " is extended but has a different prototype from the first one!");
                
                String vartype = ((MethodType) n.f6.nodes.get(i).accept(this)).getType();
                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { // Mh egkyros typos
                    for(int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;

                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        }

        for(int i = 0; i < st.contains(parentid).MethodList.size(); i++)
            st.contains(id).addMethod((MethodType) st.contains(parentid).MethodList.get(i));

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public MyType visit(VarDeclaration n) throws Exception {
        String type = n.f0.accept(this).getName();
        String id = n.f1.accept(this).getName();
        return new VariableType(type, id);
    }

    /**
    * f0 -> "public"
    * f1 -> MyType()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
    public MyType visit(MethodDeclaration n) throws Exception {
        String type = n.f1.accept(this).getName();
        String id = n.f2.accept(this).getName();
        MethodType m = (MethodType) n.f4.accept(this);
        n.f7.accept(this);
        n.f8.accept(this);
        n.f10.accept(this);
        MethodType meth = new MethodType(type, id);
        if(n.f4.present()) {   
            for(int i = 0; i < m.ParamList.size(); i++) {
                boolean flag = false;
                meth.addParam((VariableType) m.ParamList.get(i));
                String vartype = ((VariableType) m.ParamList.get(i)).getType();

                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) {     // Mh egkyros typos
                    for(int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;

                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        }
        if(n.f7.present()) {    
            for(int i = 0; i < n.f7.size(); i++) {
                if(!meth.addVar((VariableType) n.f7.nodes.get(i).accept(this)))      // Epanorismos
                    throw new Exception("Variable " + n.f7.nodes.get(i).accept(this).getName() + " already exists!");

                boolean flag = false;
                String vartype = ((VariableType) n.f7.nodes.get(i).accept(this)).getType();
                if(!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { // Mh egkyros typos
                    for(int j = 0; j < st.getST().size(); j++) {
                        if(vartype.equals(st.getST().get(j).getName()))
                            flag = true;
                    }
                } 
                else 
                    flag = true;

                if(!flag)
                    throw new Exception("Invalid type " + vartype);
            }
        }
        return meth;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public MyType visit(FormalParameterList n) throws Exception {
        VariableType par = (VariableType) n.f0.accept(this);
        MethodType meth = (MethodType) n.f1.accept(this);
        if(!meth.addParam(par))         // Epanorismos
            throw new Exception("Parameter " + par.getName() + " already exists!");
        return meth;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public MyType visit(FormalParameter n) throws Exception {
        String type = n.f0.accept(this).getName();
        String id = n.f1.accept(this).getName();
        return new VariableType(type, id);
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    public MyType visit(FormalParameterTail n) throws Exception {
        MethodType meth = new MethodType(null, null);
        if(n.f0.present()) {                
            for(int i = 0; i < n.f0.size(); i++) {
                if(!meth.addParam((VariableType) n.f0.nodes.get(i).accept(this)))        // Epanorismos
                    throw new Exception("Parameter " + n.f0.nodes.get(i).accept(this).getName() + " already exists!");
            }
        }
        return meth;
    }

        /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
    public MyType visit(FormalParameterTerm n) throws Exception {
        return (VariableType) n.f1.accept(this);
    }


        /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
    public MyType visit(Type n) throws Exception {
        return n.f0.accept(this);
    }

    /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    public MyType visit(ArrayType n) throws Exception {
        return new MyType("int[]");
    }

    /**
    * f0 -> "boolean"
    */
    public MyType visit(BooleanType n) throws Exception {
        return new MyType("boolean");
    }

    /**
    * f0 -> "int"
    */
    public MyType visit(IntegerType n) throws Exception {
        return new MyType("int");
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public MyType visit(Identifier n) throws Exception {
        return new MyType(n.f0.toString());
    }


}
