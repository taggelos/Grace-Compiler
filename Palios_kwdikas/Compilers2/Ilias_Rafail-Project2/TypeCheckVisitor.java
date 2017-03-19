import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import symbol_table.*;
import types.*;

public class TypeCheckVisitor extends GJDepthFirst<MyType, MyType> {
    public SymbolTable st;

    public TypeCheckVisitor(SymbolTable st) {       // Pairnei to SymbolTable pou dhmiourghse o MethodsVisitor
        this.st = st;   
    }

    public static VariableType getType(VariableType var, MethodType meth) throws Exception {
        if(var.type == null) {    
            String methodvar = meth.methContainsVar(var.name);
            if(methodvar == null) {             // An den brisketai se sunarthsh...
                VariableType classvar = meth.from.classContainsVar(var.name);
                if(classvar == null)            // An den brisketai oute sth klash
                    throw new Exception("Undecleared Variable " + var.name);
                methodvar = classvar.type;
            }
            return new VariableType(methodvar, var.name);
        }
        return var;
    } 

    /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public MyType visit(Goal n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f2.accept(this, argu);
        return null;
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
    public MyType visit(MainClass n, MyType argu) throws Exception {
        String id = n.f1.accept(this, argu).name;
        ClassType main = st.contains(id);
        MethodType meth = main.getMethod("main");
        n.f11.accept(this, argu);
        n.f14.accept(this, argu);
        n.f15.accept(this, meth);
        return null;
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
        String id = n.f1.accept(this, argu).name;
        ClassType classid = st.contains(id);
        n.f3.accept(this, argu);
        n.f4.accept(this, classid);
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
        String id = n.f1.accept(this, argu).name;
        ClassType classid = st.contains(id);
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        n.f3.accept(this, argu);
        n.f4.accept(this, argu);
        n.f5.accept(this, argu);
        n.f6.accept(this, classid);
        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public MyType visit(VarDeclaration n, MyType argu) throws Exception {
        String type = ((VariableType)n.f0.accept(this, argu)).type;
        String id = n.f1.accept(this, argu).name;
        return new VariableType(type, id);
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
        String id = n.f2.accept(this, argu).name;
        MethodType meth = ((ClassType) argu).getMethod(id);
        String type = ((VariableType)n.f1.accept(this, argu)).type;
        n.f8.accept(this, meth);
        VariableType retType = (VariableType) n.f10.accept(this, meth);
        retType = getType(retType, meth);

        if(!meth.type.equals(retType.type))
            throw new Exception("Wrong type returned!");
        
        return null;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public MyType visit(FormalParameterList n, MyType argu) throws Exception {
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public MyType visit(FormalParameter n, MyType argu) throws Exception {
        String type = ((VariableType)n.f0.accept(this, argu)).type;
        String id = n.f1.accept(this, argu).name;
        return new VariableType(type, id);
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
        n.f1.accept(this, argu);
        return null;
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
        return new VariableType("int[]", null);
    }

    /**
    * f0 -> "boolean"
    */
    public MyType visit(BooleanType n, MyType argu) throws Exception {
        return new VariableType("boolean", null);
    }

    /**
    * f0 -> "int"
    */
    public MyType visit(IntegerType n, MyType argu) throws Exception {
        return new VariableType("int", null);
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
        VariableType id = (VariableType) n.f0.accept(this, argu);
        VariableType expr = (VariableType) n.f2.accept(this, argu);
        id = getType(id, (MethodType) argu);
        expr = getType(expr, (MethodType) argu);

        if(id.type.equals(expr.type))         // elegxos gia methods kai vars
            return null;
        
        ClassType parent = st.contains(expr.type);        // Elegxos gia extended
        if(parent != null) {       
            while(!parent.name.equals(id.type)) {
                if(parent == null || parent.parent == null)
                    throw new Exception("Assignment between different types!");
                parent = st.contains(parent.parent);
            }
            return null;
        }

        throw new Exception("Assignment between different types!");
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
        VariableType expr1 = (VariableType) n.f2.accept(this, argu);
        VariableType expr2 = (VariableType) n.f5.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);
        expr2 = getType(expr2, (MethodType) argu);

        if(expr1.type.equals("int") && expr2.type.equals("int"))
            return null;

        throw new Exception("Assignment between different types!");
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
        VariableType expr = (VariableType) n.f2.accept(this, argu);
        n.f4.accept(this, argu);
        n.f6.accept(this, argu);

        if(expr.type == null)
            expr = getType(expr, (MethodType)argu);

        if(expr.type.equals("boolean"))
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
        VariableType expr = (VariableType) n.f2.accept(this, argu);
        n.f4.accept(this, argu);

        if(expr.type == null)
            expr = getType(expr, (MethodType)argu);

        if(expr.type.equals("boolean"))
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
    public MyType visit(PrintStatement n, MyType argu) throws Exception { 
        VariableType expr = (VariableType) n.f2.accept(this, argu);

        if(expr.type == null)
            expr = getType(expr, (MethodType)argu);

        if(expr.type.equals("int"))   //Mono integers
            return null;

        throw new Exception("Print-Statement is not Integer Expression!");
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
        VariableType clause1 = (VariableType) n.f0.accept(this, argu);
        VariableType clause2 = (VariableType) n.f2.accept(this, argu);
        clause1 = getType(clause1, (MethodType) argu);
        clause2 = getType(clause2, (MethodType) argu);

        if(clause1.type.equals("boolean") && clause2.type.equals("boolean"))
            return new VariableType("boolean", null);

        throw new Exception("Logical And between different types!");
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(CompareExpression n, MyType argu) throws Exception {
        VariableType expr1 = (VariableType) n.f0.accept(this, argu);
        VariableType expr2 = (VariableType) n.f2.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);
        expr2 = getType(expr2, (MethodType) argu);

        if(expr1.type.equals("int") && expr2.type.equals("int"))
            return new VariableType("boolean", null);

        throw new Exception("Comparison between different types!");
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(PlusExpression n, MyType argu) throws Exception {
        VariableType expr1 = (VariableType) n.f0.accept(this, argu);
        VariableType expr2 = (VariableType) n.f2.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);
        expr2 = getType(expr2, (MethodType) argu);

        if(expr1.type.equals("int") && expr2.type.equals("int"))
            return new VariableType("int", null);

        throw new Exception("Addition between different types!");
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(MinusExpression n, MyType argu) throws Exception {
        VariableType expr1 = (VariableType) n.f0.accept(this, argu);
        VariableType expr2 = (VariableType) n.f2.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);
        expr2 = getType(expr2, (MethodType) argu);

        if(expr1.type.equals("int") && expr2.type.equals("int"))
            return new VariableType("int", null);

        throw new Exception("Substraction between different types!");
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(TimesExpression n, MyType argu) throws Exception {
        VariableType expr1 = (VariableType) n.f0.accept(this, argu);
        VariableType expr2 = (VariableType) n.f2.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);
        expr2 = getType(expr2, (MethodType) argu);

        if(expr1.type.equals("int") && expr2.type.equals("int"))
            return new VariableType("int", null);

        throw new Exception("Multiplication between different types!");
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public MyType visit(ArrayLookup n, MyType argu) throws Exception {
        VariableType expr1 = (VariableType) n.f0.accept(this, argu);
        VariableType expr2 = (VariableType) n.f2.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);
        expr2 = getType(expr2, (MethodType) argu);

        if(expr1.type.equals("int[]") && expr2.type.equals("int"))
            return new VariableType("int", null);

        throw new Exception("ArrayLookup between different types!");
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public MyType visit(ArrayLength n, MyType argu) throws Exception {
        VariableType expr1 = (VariableType) n.f0.accept(this, argu);
        expr1 = getType(expr1, (MethodType) argu);

        if(expr1.type.equals("int[]"))
            return new VariableType("int", null);

        throw new Exception("Wrong Expression for ArrayLength!");
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
        VariableType expr = (VariableType) n.f0.accept(this, argu);
        expr = getType(expr, (MethodType) argu);                       
        VariableType id = (VariableType) n.f2.accept(this, argu);
        ClassType cl = st.contains(expr.type);                      
        if(!cl.classContainsMeth(id.name))                      // Den exei deilw8ei sth klash
            throw new Exception("Method " + id.name + " is not declared!");
        MethodType meth = cl.getMethod(id.name);                   // Pairnoume to onoma tou Parameter
        MethodType par = (MethodType) n.f4.accept(this, argu);
        if(n.f4.present()) { 
            if(meth.ParamList.size() != par.ParamList.size())
                throw new Exception("Wrong number of parameters!");

            for(int i = 0; i < meth.ParamList.size(); i++) {            // Elegxoume gia ka8e Parameter
                String vartype = ((VariableType) par.ParamList.get(i)).type;
                if(vartype == null) {                                   // An den yparxei to dhmiourgoume
                    VariableType temp = new VariableType(null, par.ParamList.get(i).name);
                    temp = getType(temp, (MethodType) argu);
                    vartype = temp.type;
                }
                if(vartype.equals(meth.ParamList.get(i).type)) {        // An to Method einai ths sugkekrimenhs klashs ...
                    continue;
                }
                else {
                    ClassType parent = st.contains(vartype);            // alliws, elegxoume gia extended
                    if(parent != null) {       
                        while(!parent.name.equals(meth.ParamList.get(i).type)) {
                            if(parent == null || parent.parent == null)
                                throw new Exception("Assignment between different types!");
                            parent = st.contains(parent.parent);
                        }
                        continue;
                    }
                    throw new Exception("Assignment between different types!");
                }
            }
        }
        return new VariableType(meth.type, null);
    }

    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    public MyType visit(ExpressionList n, MyType argu) throws Exception {
        VariableType expr =  (VariableType) n.f0.accept(this, argu); 
        MethodType meth = (MethodType) n.f1.accept(this, argu);
        meth.ParamList.add(expr);
        return meth;
    }

    /**
    * f0 -> ( ExpressionTerm() )*
    */
    public MyType visit(ExpressionTail n, MyType argu) throws Exception {
        MethodType meth = new MethodType(null, null);
        if(n.f0.present()) {                
            for (int i = 0 ; i < n.f0.size() ; i++)
                meth.ParamList.add((VariableType) n.f0.nodes.get(i).accept(this, argu));
        }
        return meth;
    }

    /**
    * f0 -> ","
    * f1 -> Expression()
    */
    public MyType visit(ExpressionTerm n, MyType argu) throws Exception {
        return (VariableType) n.f1.accept(this, argu);
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
        return new VariableType("int", null);
    }

    /**
    * f0 -> "true"
    */
    public MyType visit(TrueLiteral n, MyType argu) throws Exception {
        return new VariableType("boolean", null);
    }

    /**
    * f0 -> "false"
    */
    public MyType visit(FalseLiteral n, MyType argu) throws Exception {
        return new VariableType("boolean", null);
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public MyType visit(Identifier n, MyType argu) throws Exception {
        VariableType id = new VariableType(null, n.f0.toString());
        return id;
    }

    /**
    * f0 -> "this"
    */
    public MyType visit(ThisExpression n, MyType argu) throws Exception {
        VariableType var = new VariableType(((MethodType) argu).from.name, null);
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
        VariableType expr = (VariableType) n.f3.accept(this, argu);
        expr = getType(expr, (MethodType) argu);

        if(!expr.type.equals("int"))
            throw new Exception("Wrong type at ArrayAllocation!");

        return new VariableType("int[]", null);
    }

    /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public MyType visit(AllocationExpression n, MyType argu) throws Exception {
        VariableType classid = new VariableType(n.f1.accept(this, argu).name, null);

        if(st.contains(classid.type) == null)      // H klash den exei oristei
            throw new Exception("Class " + classid + " does not exist!");

        return classid;
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public MyType visit(NotExpression n, MyType argu) throws Exception {
        VariableType clause = (VariableType) n.f1.accept(this, argu);
        clause = getType(clause, (MethodType) argu);

        if(clause.type.equals("boolean"))
            return new VariableType("boolean", null);

        throw new Exception("Wrong type for Not-Clause!");
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public MyType visit(BracketExpression n, MyType argu) throws Exception {
        return n.f1.accept(this, argu);
    }

}
