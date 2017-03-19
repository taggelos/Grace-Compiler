package symbol_table;

import syntaxtree.*;
import visitor.GJNoArguDepthFirst;
import java.util.*;
import types.*;

/* 
    Visitor gia thn dhmiourgia tou Symbol Table 
*/

public class MethodsVisitor extends GJNoArguDepthFirst<MyType> {
    public SymbolTable st;

    public MethodsVisitor(LinkedList<ClassType> classes) { 
        st = new SymbolTable(classes);          // Pairnei th lista apo klaseis apo ton ClassesVisitor
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
        VariableType id1 = new VariableType("String[]", n.f11.accept(this).name);
        MethodType id2 = new MethodType("void", "main");
        id2.addParam(id1);
        String class_name = n.f1.accept(this).name;

        if(n.f14.present()) {   
            for(int i = 0; i < n.f14.size(); i++) {       
                id2.addVar((VariableType) n.f14.nodes.get(i).accept(this));
            }
        }
        ClassType main = st.contains(class_name);
        main.addMethod(id2);
        main.isMain = 1;

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
        String name = n.f1.accept(this).name;
        n.f3.accept(this);
        n.f4.accept(this);
        ClassType c = st.contains(name);
        if(n.f3.present()) {    
            for(int i = 0; i < n.f3.size(); i++) { 
                VariableType var = (VariableType) n.f3.nodes.get(i).accept(this);
                c.addVar(var);
            }
        }
        if(n.f4.present()) {    
            for(int i = 0; i < n.f4.size(); i++) {     
                MethodType meth = (MethodType) n.f4.nodes.get(i).accept(this);
                c.addMethod(meth);
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
        String id = n.f1.accept(this).name;
        String parentid = n.f3.accept(this).name;
        n.f5.accept(this);
        n.f6.accept(this);

        ClassType class_child = st.contains(id);
        ClassType class_parent = st.contains(parentid);

        for(int i = 0; i < class_parent.VarList.size(); i++) {
            class_child.addExtendedVar((VariableType) class_parent.VarList.get(i));  //Pros8etoume tis metablites tou gonea sto paidi
        }
        if(n.f5.present()) {   
            for(int i = 0; i < n.f5.size(); i++) {
                class_child.addVar((VariableType) n.f5.nodes.get(i).accept(this));   //Pros8etoume kai tis metablites tou paidiou
            }
        }
        for(int i = 0; i < class_parent.MethodList.size(); i++) {                     //Pros8etoume tis sunarthseis tou gonea sto paidi 
            MethodType meth_parent = (MethodType) class_parent.MethodList.get(i);
            if(!meth_parent.name.equals("main")) {
                class_child.addExtendedMethod(meth_parent);
                class_child.meth_count = class_parent.meth_count;
            }
        }
        if(n.f6.present()) {     // Pros8etoume tis sunarthseis tou paidiou
            for(int i = 0; i < n.f6.size(); i++) {
                MethodType meth = (MethodType) n.f6.nodes.get(i).accept(this);
                if(!class_parent.classContainsMeth(meth.name)) {    // An o goneas den exei tetoia sunarthsh, apla thn pros8etoume
                    class_child.addMethod(meth);
                }
                else {                                              // Alliws, thn antika8istoume
                    for(int j = 0; j < class_child.MethodList.size(); j++) {
                        if(class_child.MethodList.get(j).name.equals(meth.name)) {
                            meth.from = class_child;
                            meth.id = j+1;
                            class_child.MethodList.set(j, meth);
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public MyType visit(VarDeclaration n) throws Exception {
        String type = n.f0.accept(this).name;
        String id = n.f1.accept(this).name;
        VariableType var = new VariableType(type, id);
        return var;
    }

    /**
    * f0 -> "public"
    * f1 -> Type()
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
        String type = n.f1.accept(this).name;
        String id = n.f2.accept(this).name;
        MethodType m = (MethodType) n.f4.accept(this);
        n.f7.accept(this);
        n.f8.accept(this);
        n.f10.accept(this);
        MethodType meth = new MethodType(type, id);
        if(n.f4.present()) {   
            for(int i = 0; i < m.ParamList.size(); i++) {           
                meth.addParam((VariableType) m.ParamList.get(i));
            }
        }
        if(n.f7.present()) {    
            for(int i = 0; i < n.f7.size(); i++) {                 
                meth.addVar((VariableType) n.f7.nodes.get(i).accept(this));      
            }
        }

        if (st.TEMP_count < meth.par_count)
            st.TEMP_count = meth.par_count;

        return meth;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public MyType visit(FormalParameterList n) throws Exception {
        VariableType par = (VariableType) n.f0.accept(this);
        MethodType meth = (MethodType) n.f1.accept(this);
        meth.addParam(par);        
        
        return meth;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public MyType visit(FormalParameter n) throws Exception {
        String type = n.f0.accept(this).name;
        String id = n.f1.accept(this).name;
        VariableType var = new VariableType(type, id);
        return var;
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    public MyType visit(FormalParameterTail n) throws Exception {
        MethodType meth = new MethodType(null, null);
        if(n.f0.present()) {                
            for(int i = 0; i < n.f0.size(); i++) {   
                VariableType par = (VariableType) n.f0.nodes.get(i).accept(this);
                meth.addParam(par);       
                    
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
        MyType id = new MyType(n.f0.toString());
        return id;
    }


}
