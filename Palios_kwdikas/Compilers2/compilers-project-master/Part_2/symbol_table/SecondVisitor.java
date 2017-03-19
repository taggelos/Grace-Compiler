package symbol_table;

import syntaxtree.*;
import visitor.GJNoArguDepthFirst;
import java.util.*;
import my_type.*;

/* Second Visitor Pattern creates the Symbol Table */
public class SecondVisitor extends GJNoArguDepthFirst<MyType> {
    private SymbolTable st;

    public SecondVisitor(LinkedList<Class_t> classes) { 
        st = new SymbolTable(classes);
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
        n.f0.accept(this);
        n.f1.accept(this);
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        n.f6.accept(this);
        n.f7.accept(this);
        n.f8.accept(this);
        n.f9.accept(this);
        n.f10.accept(this);
        n.f11.accept(this);
        n.f12.accept(this);
        n.f13.accept(this);
        n.f14.accept(this);
        n.f15.accept(this);
        n.f16.accept(this);
        n.f17.accept(this);
        Variable_t v = new Variable_t("String[]", n.f11.accept(this).getName());
        Method_t m = new Method_t("void", "main");
        m.addParam(v);
        String name = n.f1.accept(this).getName();
        if (!st.contains(name).addMethod(m)) // not unique method
            throw new Exception("Method " + name + " already exists!");
        if (n.f14.present())    // add MainClass Variables
            for (int i = 0 ; i < n.f14.size() ; i++) {
                boolean found = false;
                if (!st.contains(name).addVar((Variable_t)n.f14.nodes.get(i).accept(this))) // not unique variables
                    throw new Exception("Class " + name + ": Variable " + n.f14.nodes.get(i).accept(this).getName() + " already exists!");
                String vartype = ((Variable_t) n.f14.nodes.get(i).accept(this)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(name + ": Cannot declare " + vartype + " does not exist!");
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
        n.f0.accept(this);
        String name = n.f1.accept(this).getName();
        n.f2.accept(this);
        n.f3.accept(this);
        n.f4.accept(this);
        n.f5.accept(this);
        if (n.f3.present())     // add Class Variables
            for (int i = 0 ; i < n.f3.size() ; i++) {   // for every variable
                boolean found = false;
                if (!st.contains(name).addVar((Variable_t)n.f3.nodes.get(i).accept(this))) // if variable isnt unique
                    throw new Exception("Class " + name + ": Variable " + n.f3.nodes.get(i).accept(this).getName() + " already exists!");
                String vartype = ((Variable_t) n.f3.nodes.get(i).accept(this)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(name + ": Cannot declare " + vartype + " does not exist!");
            }
        if (n.f4.present())     // add Class Methods
            for (int i = 0 ; i < n.f4.size() ; i++) {
                boolean found = false;
                if (st.contains(name).addMethod((Method_t)n.f4.nodes.get(i).accept(this)) == false) // if method isnt unique
                    throw new Exception("Class " + name + ": Method " + n.f4.nodes.get(i).accept(this).getName() + " already exists!");
                String vartype = ((Method_t) n.f4.nodes.get(i).accept(this)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(name + ": Cannot declare " + vartype + " does not exist!");
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
        n.f0.accept(this);
        String id = n.f1.accept(this).getName();
        n.f2.accept(this);
        String did = n.f3.accept(this).getName();
        n.f4.accept(this);
        n.f5.accept(this);
        n.f6.accept(this);
        n.f7.accept(this);
        if (n.f5.present())     // add Class Variables
            for (int i = 0 ; i < n.f5.size() ; i++) {
                boolean found = false;
                if (!st.contains(id).addVar((Variable_t)n.f5.nodes.get(i).accept(this))) // if variable isnt unique
                    throw new Exception("Class " + id + ": Variable " + n.f5.nodes.get(i).accept(this).getName() + " already exists!");
                String vartype = ((Variable_t) n.f5.nodes.get(i).accept(this)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(id + ": Cannot declare " + vartype + " does not exist!");
            }
        for (int i = 0 ; i < st.contains(did).classVars.size() ; i++) //add variables from father-class
            st.contains(id).addVar((Variable_t) st.contains(did).classVars.get(i));
        if (n.f6.present())     // add Class Methods
            for (int i = 0 ; i < n.f6.size() ; i++) {
                boolean found = false;
                if (!st.contains(id).addMethod((Method_t)n.f6.nodes.get(i).accept(this)))               // if method isnt unique
                    throw new Exception("Class " + id + ": Method " + n.f6.nodes.get(i).accept(this).getName() + " already exists!");
                else if (st.contains(did).classContainsMeth(n.f6.nodes.get(i).accept(this).getName()))  // if the father-class has a method with this name
                    if (!st.contains(did).checkMethod((Method_t)n.f6.nodes.get(i).accept(this)))        // and if it isnt exactly the same error 
                        throw new Exception("Class " + id + ": Method " + n.f6.nodes.get(i).accept(this).getName() + " is extended but has a different prototype from the first one!");
                String vartype = ((Method_t) n.f6.nodes.get(i).accept(this)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(id + ": Cannot declare " + vartype + " does not exist!");
            }
        for (int i = 0 ; i < st.contains(did).classMethods.size() ; i++)
            st.contains(id).addMethod((Method_t) st.contains(did).classMethods.get(i));
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
        n.f2.accept(this);
        return new Variable_t(type, id);
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
        n.f0.accept(this);
        String type = n.f1.accept(this).getName();
        String id = n.f2.accept(this).getName();
        n.f3.accept(this);
        Method_t m = (Method_t) n.f4.accept(this);
        n.f5.accept(this);
        n.f6.accept(this);
        n.f7.accept(this);
        n.f8.accept(this);
        n.f9.accept(this);
        n.f10.accept(this);
        n.f11.accept(this);
        n.f12.accept(this);
        Method_t meth = new Method_t(type, id);
        if (n.f4.present())    // add parameters to method
            for (int i = 0 ; i < m.methodParams.size() ; i++) {
                boolean found = false;
                meth.addParam((Variable_t) m.methodParams.get(i));
                String vartype = ((Variable_t) m.methodParams.get(i)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(id + ": Cannot declare " + vartype + " does not exist!");
            }
        if (n.f7.present())     // add method Variables
            for (int i = 0 ; i < n.f7.size() ; i++) {
                if (!meth.addVar((Variable_t) n.f7.nodes.get(i).accept(this)))
                    throw new Exception("Method " + id + ": Variable " + n.f7.nodes.get(i).accept(this).getName() + " already exists!");
                boolean found = false;
                String vartype = ((Variable_t) n.f7.nodes.get(i).accept(this)).getType();
                if (!vartype.equals("int") && !vartype.equals("boolean") && !vartype.equals("int[]")) { //if not classic types
                    for (int j = 0 ; j < st.getST().size() ; j++)
                        if (vartype.equals(st.getST().get(j).getName()))
                            found = true;
                } else 
                    found = true;
                if (!found)
                    throw new Exception(id + ": Cannot declare " + vartype + " does not exist!");
            }
        return meth;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public MyType visit(FormalParameterList n) throws Exception {
        Variable_t fp = (Variable_t) n.f0.accept(this);
        Method_t meth = (Method_t) n.f1.accept(this);
        if (!meth.addParam(fp))
            throw new Exception("Parameter " + fp.getName() + " already exists!");
        return meth;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public MyType visit(FormalParameter n) throws Exception {
        String type = n.f0.accept(this).getName();
        String id = n.f1.accept(this).getName();
        return new Variable_t(type, id);
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    public MyType visit(FormalParameterTail n) throws Exception {
        Method_t meth = new Method_t(null, null);
        if (n.f0.present())                 // create a linked list of variables. (parameters list)
            for (int i = 0 ; i < n.f0.size() ; i++)
                if (!meth.addParam((Variable_t) n.f0.nodes.get(i).accept(this)))
                    throw new Exception("Parameter " + n.f0.nodes.get(i).accept(this).getName() + " already exists!");
        return meth;
    }

        /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
    public MyType visit(FormalParameterTerm n) throws Exception {
        n.f0.accept(this);
        return (Variable_t) n.f1.accept(this);
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
