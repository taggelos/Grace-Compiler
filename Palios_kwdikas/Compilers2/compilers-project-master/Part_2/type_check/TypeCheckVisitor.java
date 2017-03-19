package type_check;

import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import symbol_table.*;
import my_type.*;

public class TypeCheckVisitor extends GJDepthFirst<MyType, MyType> {
    private SymbolTable ST;

    public TypeCheckVisitor(SymbolTable ST) {
        this.ST = ST;   
    }

    public static Variable_t findType(Variable_t var, Method_t meth) throws Exception {
        if (var.getType() == null) {
            String inMethod = meth.methContains(var.getName());
            if (inMethod == null) {   // if not found in the function, we should seek in the class
                Variable_t inclassvar = meth.comesFrom.classContainsVar(var.getName());
                if (inclassvar == null)
                    throw new Exception("Undecleared Variable " + var.getName());
                inMethod = inclassvar.getType();
            }
            return new Variable_t(inMethod, var.getName());
        }
        return var;
    } 

    /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public MyType visit(Goal n, MyType argu) throws Exception {
        Variable_t _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return _ret;
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
    * f8 -> "Variable_t"
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
    public MyType visit(MainClass n, MyType argu) throws Exception {
        Variable_t _ret=null;
        n.f0.accept(this, argu);
        String id = n.f1.accept(this, argu).getName();
        Class_t mainclazz = ST.contains(id);
        Method_t meth = mainclazz.getMethod("main");
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        n.f7.accept(this, argu);
        n.f8.accept(this, argu);
        n.f9.accept(this, argu);
        n.f10.accept(this, argu);
        n.f11.accept(this, argu);
        n.f12.accept(this, argu);
        n.f13.accept(this, argu);
        n.f14.accept(this, argu);
        n.f15.accept(this, meth);
        n.f16.accept(this, argu);
        n.f17.accept(this, argu);
        return _ret;
    }

    /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
    public MyType visit(TypeDeclaration n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
    public MyType visit(ClassDeclaration n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        String s = n.f1.accept(this, argu).getName();
        Class_t class_id = ST.contains(s);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, class_id);
        n.f5.accept(this, argu);
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
    public MyType visit(ClassExtendsDeclaration n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        String s = n.f1.accept(this, argu).getName();
        Class_t class_id = ST.contains(s);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, class_id);
        n.f7.accept(this, argu);
        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public MyType visit(VarDeclaration n, MyType argu) throws Exception {
        n.f2.accept(this, argu);
        return new Variable_t(((Variable_t)n.f0.accept(this, argu)).getType(), n.f1.accept(this, argu).getName()); // isws de xreiazetai
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
    public MyType visit(MethodDeclaration n, MyType argu) throws Exception {
        String methName = n.f2.accept(this, argu).getName();
        Method_t meth = ((Class_t) argu).getMethod(methName);
        n.f0.accept(this, meth);
        String methType = ((Variable_t)n.f1.accept(this, argu)).getType();
        n.f8.accept(this, meth);
        Variable_t retType = (Variable_t) n.f10.accept(this, meth);
        retType = findType(retType, meth);
        if (!meth.getType().equals(retType.getType()))
            throw new Exception("Error at " + methName + " declaration, type is " + methType + " and return type is "+ retType.getType() + ", meth " +meth.comesFrom.getName());
        return null;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public MyType visit(FormalParameterList n, MyType argu) throws Exception {
        Variable_t _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public MyType visit(FormalParameter n, MyType argu) throws Exception {
        return new Variable_t(((Variable_t)n.f0.accept(this, argu)).getType(), n.f1.accept(this, argu).getName());
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    public MyType visit(FormalParameterTail n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
    public MyType visit(FormalParameterTerm n, MyType argu) throws Exception {
        Variable_t _ret=null;
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return _ret;
    }

    /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
    public MyType visit(Type n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    public MyType visit(ArrayType n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return new Variable_t("int[]", null);
    }

    /**
    * f0 -> "boolean"
    */
    public MyType visit(BooleanType n, MyType argu) throws Exception {
        return new Variable_t("boolean", null);
    }

    /**
    * f0 -> "int"
    */
    public MyType visit(IntegerType n, MyType argu) throws Exception {
        return new Variable_t("int", null);
    }

    /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
    public MyType visit(Statement n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
    public MyType visit(Block n, MyType argu) throws Exception {
        n.f1.accept(this, argu);
        return null;
    }

    /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    public MyType visit(AssignmentStatement n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals(t2.getType()))         // elegxos gia methods kai vars
            return null;
        
        Class_t dad = ST.contains(t2.getType());        // Elegxos gia extended
        if (dad != null) {       
            while (!dad.getName().equals(t1.getType())) {
                if (dad == null || dad.getDad() == null)
                    throw new Exception("Error assignment between different types: " + t1.getType() + " " + t2.getType());
                dad = ST.contains(dad.getDad());
            }
            return null;
        }
        throw new Exception("Error assignment between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
    public MyType visit(ArrayAssignmentStatement n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f2.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f5.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("int") && t2.getType().equals("int"))
            return null;
        throw new Exception("Error assignment between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
    public MyType visit(IfStatement n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t expr = (Variable_t) n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, argu);
        if (expr.getType() == null)
            expr = findType(expr, (Method_t)argu);
        if (expr.getType().equals("boolean"))
            return null;
        throw new Exception("If-condition is not a boolean Expression!");
    }

    /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public MyType visit(WhileStatement n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t expr = (Variable_t) n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (expr.getType() == null)
            expr = findType(expr, (Method_t)argu);
        if (expr.getType().equals("boolean"))
            return null;
        throw new Exception("while-condition is not a boolean Expression!");
    }

    /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public MyType visit(PrintStatement n, MyType argu) throws Exception { //is int
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t expr = (Variable_t) n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        if (expr.getType() == null)
            expr = findType(expr, (Method_t)argu);
        if (expr.getType().equals("boolean") || expr.getType().equals("int"))
            return null;
        throw new Exception("Print Statement not boolean or int!");
    }

    /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | Clause()
    */
    public MyType visit(Expression n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);   
    }

    /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    public MyType visit(AndExpression n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("boolean") && t2.getType().equals("boolean"))
            return new Variable_t("boolean", null);
        throw new Exception("Logical And between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(CompareExpression n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("int") && t2.getType().equals("int"))
            return new Variable_t("boolean", null);
        throw new Exception("Compare between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(PlusExpression n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("int") && t2.getType().equals("int"))
            return new Variable_t("int", null);
        throw new Exception("Addition between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(MinusExpression n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("int") && t2.getType().equals("int"))
            return new Variable_t("int", null);
        throw new Exception("Substraction between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(TimesExpression n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("int") && t2.getType().equals("int"))
            return new Variable_t("int", null);
        throw new Exception("Multiply between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public MyType visit(ArrayLookup n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        Variable_t t2 = (Variable_t) n.f2.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        t2 = findType(t2, (Method_t) argu);
        if (t1.getType().equals("int[]") && t2.getType().equals("int"))
            return new Variable_t("int", null);
        throw new Exception("ArrayLookup between different types: " + t1.getType() + " " + t2.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public MyType visit(ArrayLength n, MyType argu) throws Exception {
        Variable_t t1 = (Variable_t) n.f0.accept(this, argu);
        t1 = findType(t1, (Method_t) argu);
        if (t1.getType().equals("int[]"))
            return new Variable_t("int", null);
        throw new Exception("ArrayLength in something not int[]: " + t1.getType());
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    public MyType visit(MessageSend n, MyType argu) throws Exception {
        Variable_t clazz = (Variable_t) n.f0.accept(this, argu);
        clazz = findType(clazz, (Method_t) argu);                       // now clazz.type() is the type of PrimaryExp
        Variable_t id = (Variable_t) n.f2.accept(this, argu);
        Class_t cl = ST.contains(clazz.getType());                      
        if (!cl.classContainsMeth(id.getName()))                        // check if class primary expr type contains method identifier
            throw new Exception("Method " + id.getName() + " is not declared in class " + clazz.getType());
        Method_t existingmeth = cl.getMethod(id.getName());             // get method identifier (also parameters etc)
        Method_t keepParams = (Method_t) n.f4.accept(this, argu);
        if (n.f4.present()) {   // add parameters to method
            if (existingmeth.methodParams.size() != keepParams.methodParams.size())
                throw new Exception("Number of parameters error!");
            for (int i = 0 ; i < existingmeth.methodParams.size() ; i++) { // for each parameter
                String vartype = ((Variable_t) keepParams.methodParams.get(i)).getType();
                if (vartype == null) {
                    Variable_t tmpvar = new Variable_t(null, keepParams.methodParams.get(i).getName());
                    tmpvar = findType(tmpvar, (Method_t) argu);
                    vartype = tmpvar.getType();
                }
                if (vartype.equals(existingmeth.methodParams.get(i).getType())) {
                    continue;
                }
                else {
                    Class_t dad = ST.contains(vartype);
                    if (dad != null) {       
                        while (!dad.getName().equals(existingmeth.methodParams.get(i).getType())) {
                            if (dad == null || dad.getDad() == null)
                                throw new Exception("Error assignment between different types " + vartype);
                            dad = ST.contains(dad.getDad());
                        }
                        continue;
                    }
                    throw new Exception("Error assignment between different types " + vartype);
                }

            }
        }
        return new Variable_t(existingmeth.getType(), null);
    }

    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    public MyType visit(ExpressionList n, MyType argu) throws Exception {
        Variable_t expr =  (Variable_t) n.f0.accept(this, argu); //na tsekarw th seira 
        Method_t meth = (Method_t) n.f1.accept(this, argu);
        meth.methodParams.addLast(expr);
        return meth;
    }

    /**
    * f0 -> ( ExpressionTerm() )*
    */
    public MyType visit(ExpressionTail n, MyType argu) throws Exception {
        Method_t meth = new Method_t(null, null);
        if (n.f0.present())                 // create a linked list of variables. (parameters list)
            for (int i = 0 ; i < n.f0.size() ; i++)
                meth.methodParams.addLast( (Variable_t)n.f0.nodes.get(i).accept(this, argu) );
        return meth;
    }

    /**
    * f0 -> ","
    * f1 -> Expression()
    */
    public MyType visit(ExpressionTerm n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        return (Variable_t) n.f1.accept(this, argu);
    }

    /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
    public MyType visit(Clause n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | BracketExpression()
    */
    public MyType visit(PrimaryExpression n, MyType argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> <INTEGER_LITERAL>
    */
    public MyType visit(IntegerLiteral n, MyType argu) throws Exception {
        return new Variable_t("int", null);
    }

    /**
    * f0 -> "true"
    */
    public MyType visit(TrueLiteral n, MyType argu) throws Exception {
        return new Variable_t("boolean", null);
    }

    /**
    * f0 -> "false"
    */
    public MyType visit(FalseLiteral n, MyType argu) throws Exception {
        return new Variable_t("boolean", null);
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public MyType visit(Identifier n, MyType argu) throws Exception {
        return new Variable_t(null, n.f0.toString());
    }

    /**
    * f0 -> "this"
    */
    public MyType visit(ThisExpression n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        Variable_t var = new Variable_t(((Method_t) argu).comesFrom.getName(), null);
        return var;
    }

    /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public MyType visit(ArrayAllocationExpression n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        Variable_t t = (Variable_t) n.f3.accept(this, argu);
        t = findType(t, (Method_t) argu);
        if (!t.getType().equals("int"))
            throw new Exception("Error: new int[" + t.getType() + "], " + t.getType() + " should be int!");
        n.f4.accept(this, argu);
        return new Variable_t("int[]", null);
    }

    /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public MyType visit(AllocationExpression n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        Variable_t classname = new Variable_t(n.f1.accept(this, argu).getName(), null);
        n.f2.accept(this, argu);
        n.f3.accept(this, argu);
        if (ST.contains(classname.getType()) == null) // if class does not exist
            throw new Exception("Cannot declare " + classname + " type. This class does not exist!");
        return classname; //?????
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public MyType visit(NotExpression n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        Variable_t t = (Variable_t) n.f1.accept(this, argu);
        t = findType(t, (Method_t) argu);
        if (t.getType().equals("boolean"))
            return new Variable_t("boolean", null);
        throw new Exception("Error: Not Clause, " + t + " type given. Can apply only to boolean!");
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public MyType visit(BracketExpression n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f2.accept(this, argu);
        return n.f1.accept(this, argu);
    }

}
