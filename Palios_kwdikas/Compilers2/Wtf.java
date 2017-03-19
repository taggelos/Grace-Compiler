import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import java.io.*;

public class Wtf extends GJDepthFirst<String, String> {
//class Wtf implements GJVisitor<R,A> {

    Set<String> setint = new HashSet<String>();
    Set<String> setintar = new HashSet<String>();
    Set<String> setbool = new HashSet<String>();


    Map<String,String> dec = new HashMap<String,String>(); 
    //dec.put("a", "10");

    /*public String check(String a) {
        System.out.println("Size = " + setint.size());
        if(setint.isEmpty())
            System.out.println("isEmpty");
        if(setint.contains(a))
            System.out.println("Contains "+ a);
        return null;
    } */

    /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
    public String visit(Goal n, String argu) throws Exception {
        System.out.println("Goal :");
        
        String typedec = n.f1.accept(this, null);
        String main = n.f0.accept(this, null);
        //String a = check("aaa");
        //setint.add("aaa");
        //a = check("aaa");
        System.out.println("MAINA :" + main);
        //return main;
        return "";
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
    public String visit(MainClass n, String argu) throws Exception {
        System.out.println("MainClass :");
        String id1 = n.f1.accept(this, "class");
        //String id1 = n.f1.accept(this, null);
        System.out.println("WTF");
        String id2 = n.f11.accept(this, null);
        String vardec = n.f14.accept(this, null);
        String stat = n.f15.accept(this, null);

        //System.out.println("WTF");
        System.out.println("STAT :" + stat);
        return stat;
    }

    /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
    public String visit(TypeDeclaration n, String argu) throws Exception {
        System.out.println("TypeDeclaration :");
        return n.f0.accept(this, null);
    }

    /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
    public String visit(ClassDeclaration n, String argu) throws Exception {
        System.out.println("ClassDeclaration :");
        String id = n.f1.accept(this, null);
        String vardec = n.f3.accept(this, null);
        String methdec = n.f4.accept(this, null);

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
    public String visit(ClassExtendsDeclaration n, String argu) throws Exception {
        System.out.println("ClassExtendsDeclaration :");
        String id1 = n.f1.accept(this, null);
        String id2 = n.f3.accept(this, null);
        String vardec = n.f5.accept(this, null);
        String methdec = n.f6.accept(this, null);

        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public String visit(VarDeclaration n, String argu) throws Exception {
        System.out.println("VarDeclaration :");
        
        //System.out.println("ID = " + id);
        String type = n.f0.accept(this, null);
        String id = n.f1.accept(this, type);

        System.out.println("id = " + id);
        System.out.println("SETINT = " + setint);

        //return null;
        return "";
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
    public String visit(MethodDeclaration n, String argu) throws Exception {
        System.out.println("MethodDeclaration :");
        String type = n.f1.accept(this, null);
        String id = n.f2.accept(this, type);
        String parlist = null;
        if(n.f4.present())
            parlist = n.f4.accept(this, null);

        String vardec = n.f7.accept(this, null);
        String stat = n.f8.accept(this, null);
        String expr = n.f10.accept(this, null);
        System.out.println("VAREXPR === " + expr);

        dec.put(id, expr);
        System.out.println("VARDEC = " + dec);

        return expr;
    }

    /**
    * f0 -> FormalParameter()
    * f1 -> FormalParameterTail()
    */
    public String visit(FormalParameterList n, String argu) throws Exception {
        System.out.println("FormalParameterList :");
        String par = n.f0.accept(this, null);
        String partail = n.f1.accept(this, null);

        return par;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
    public String visit(FormalParameter n, String argu) throws Exception {
        System.out.println("FormalParameter :");
        
        //System.out.println("ID = " + id);
        String type = n.f0.accept(this, null);
        String id = n.f1.accept(this, type);

        System.out.println("id = " + id);
        System.out.println("ARGS = " + setint);

        return null;
    }

    /**
    * f0 -> ( FormalParameterTerm() )*
    */
    public String visit(FormalParameterTail n, String argu) throws Exception {
        System.out.println("FormalParameterTail :");
        return n.f0.accept(this, null);
    }

    /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
    public String visit(FormalParameterTerm n, String argu) throws Exception {
        System.out.println("FormalParameterTerm :");
        return n.f1.accept(this, null);
    }

    /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | StringType()
    *       | Identifier()
    */
    public String visit(Type n, String argu) throws Exception {
        System.out.println("Type :");
        return n.f0.accept(this, null);
    }

    /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
    public String visit(ArrayType n, String argu) throws Exception {
        System.out.println("ArrayType :");
        //setintar.add(argu);
        return "int[]";
        //return null;
    }

    /**
    * f0 -> "boolean"
    */
    public String visit(BooleanType n, String argu) throws Exception {
        System.out.println("BooleanType :");
        //setbool.add(argu);
        return "bollean";
        //return null;
    }

    /**
    * f0 -> "int"
    */
    public String visit(IntegerType n, String argu) throws Exception {
        System.out.println("IntegerType :");
        //setint.add(argu);
        return "int";
        //return null;
    }

    /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
    public String visit(Statement n, String argu) throws Exception {
        System.out.println("Statement :");
        String a = n.f0.accept(this, null);
        System.out.println("AAAAAAAAAAAAAA : " + a);

        return a;
    }

    /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
    public String visit(Block n, String argu) throws Exception {
        System.out.println("Block :");
        return n.f1.accept(this, null);
    }

    /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
    public String visit(AssignmentStatement n, String argu) throws Exception {
        System.out.println("AssignmentStatement :");
        String id = n.f0.accept(this, null);
        String expr = n.f2.accept(this, null);

        if(dec.containsKey(expr))
            dec.put(id, dec.get(expr));
        else
            dec.put(id, expr);
        System.out.println("DEC = " + dec);

        return null; 
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
    public String visit(ArrayAssignmentStatement n, String argu) throws Exception {
        System.out.println("ArrayAssignmentStatement :");
        String id = n.f0.accept(this, null);
        String expr1 = n.f2.accept(this, null);
        String expr2 = n.f5.accept(this, null);

        return expr2;
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
    public String visit(IfStatement n, String argu) throws Exception {
        System.out.println("IfStatement :");
        String expr = n.f2.accept(this, null);
        //String stat1 = n.f4.accept(this, null);
        //String stat2 = n.f6.accept(this, null);

        if(expr == 1+"") {
            return n.f4.accept(this, null);
        }
        else {
            return n.f6.accept(this, null);
        }

    }

    /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public String visit(WhileStatement n, String argu) throws Exception {
        System.out.println("WhileStatement :");
        //String expr = n.f2.accept(this, null);
        //String stat = n.f4.accept(this, null);
        String stat;

        while(n.f2.accept(this, null) == 1+"") {
            stat = n.f4.accept(this, null);
        }
        //else {
        return null;
        //}

    }

    /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public String visit(PrintStatement n, String argu) throws Exception {
        System.out.println("PrintStatement :");
        String expr = n.f2.accept(this, "call");
        System.out.println(expr);
        return expr;
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
    public String visit(Expression n, String argu) throws Exception {
        System.out.println("Expression :");
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> Clause()
    * f1 -> "&&"
    * f2 -> Clause()
    */
    public String visit(AndExpression n, String argu) throws Exception {
        System.out.println("AndExpression :");
        String clause1 = n.f0.accept(this, null);
        String clause2 = n.f2.accept(this, null);

        if(clause1 == "1" && clause2 == "1")
            return "1";
        else
            return "0";
        
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public String visit(CompareExpression n, String argu) throws Exception {
        System.out.println("CompareExpression :");
        String expr1 = n.f0.accept(this, "call");
        String expr2 = n.f2.accept(this, "call");
        Integer result = Integer.parseInt(expr1) - Integer.parseInt(expr2);
        
        if(result < 0)
            return 1+"";
        else if(result > 0)
            return 0+"";

        return null;
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public String visit(PlusExpression n, String argu) throws Exception {
        System.out.println("PlusExpression :");
        String expr1 = n.f0.accept(this, "call");
        String expr2 = n.f2.accept(this, "call");

        System.out.println(">>>>  " + expr1 + " + " + expr2 + "  <<<<<<<<<<");
        Integer result = Integer.parseInt(expr1) + Integer.parseInt(expr2);
        
        return result + "";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public String visit(MinusExpression n, String argu) throws Exception {
        System.out.println("MinusExpression :");
        String expr1 = n.f0.accept(this, "call");
        String expr2 = n.f2.accept(this, "call");

        System.out.println(">>>>  " + expr1 + " - " + expr2 + "  <<<<<<<<<<");
        Integer result = Integer.parseInt(expr1) - Integer.parseInt(expr2);
        
        return result + "";    
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public String visit(TimesExpression n, String argu) throws Exception {
        System.out.println("TimesExpression :");
        String expr1 = n.f0.accept(this, "call");
        String expr2 = n.f2.accept(this, "call");

        System.out.println(">>>>  " + expr1 + " * " + expr2 + "  <<<<<<<<<<");
        Integer result = Integer.parseInt(expr1) * Integer.parseInt(expr2);
        
        return result + "";
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public String visit(ArrayLookup n, String argu) throws Exception {
        System.out.println("ArrayLookup :");
        String expr1 = n.f0.accept(this, null);
        String expr2 = n.f2.accept(this, null);

        return expr1;
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public String visit(ArrayLength n, String argu) throws Exception {
        System.out.println("ArrayLength :");
        String expr = n.f0.accept(this, null);

        return expr;
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
    public String visit(MessageSend n, String argu) throws Exception {
        System.out.println("MessageSend :");
        String expr = n.f0.accept(this, null);
        String id = n.f2.accept(this, null);

        String exprlist = null;
        if(n.f4.present())
            exprlist = n.f4.accept(this, null);

        return id;    //??
    }

    /**
    * f0 -> Expression()
    * f1 -> ExpressionTail()
    */
    public String visit(ExpressionList n, String argu) throws Exception {
        System.out.println("ExpressionList :");
        String expr = n.f0.accept(this, null);
        String exprtail = n.f1.accept(this, null);

        return expr;
    }

    /**
    * f0 -> ( ExpressionTerm() )*
    */
    public String visit(ExpressionTail n, String argu) throws Exception {
        System.out.println("ExpressionTail :");
        return n.f0.accept(this, null);
    }

    /**
    * f0 -> ","
    * f1 -> Expression()
    */
    public String visit(ExpressionTerm n, String argu) throws Exception {
        System.out.println("ExpressionTerm :");
        return n.f1.accept(this, null);
    }

    /**
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
    public String visit(Clause n, String argu) throws Exception {
        System.out.println("Clause :");
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
    public String visit(PrimaryExpression n, String argu) throws Exception {
        System.out.println("PrimaryExpression :");
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> <Integer_LITERAL>
    */
    public String visit(IntegerLiteral n, String argu) throws Exception {
        System.out.println("StringLiteral :");
        //return Character.getNumericValue(f0);
        return n.f0.toString();
    }

    /**
    * f0 -> "true"
    */
    public String visit(TrueLiteral n, String argu) throws Exception {
        return 1+"";
    }

    /**
    * f0 -> "false"
    */
    public String visit(FalseLiteral n, String argu) throws Exception {
        return 0+"";
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public String visit(Identifier n, String type) throws Exception {
        System.out.println("Identifier :");
        System.out.println("aaaaaaaa = " + n.f0.toString());

        //return String.parseInt(n.f0.toString());
        System.out.println("Type = " + type);

        if(type == "int[]") {
            System.out.println("int[]");
            if(setintar.contains(n.f0.toString())) {
                return dec.get(n.f0);
            }
            else {
                setintar.add(n.f0.toString());
                return n.f0.toString();
            }
                
        }
        else if(type == "boolean") {
            System.out.println("boolean");
            if(setbool.contains(n.f0.toString())) {
                return dec.get(n.f0);
            }
            else {
                setbool.add(n.f0.toString());
                return n.f0.toString();
            }
        }
        else if(type == "int") {
            System.out.println("int");
            if(setint.contains(n.f0.toString())) {
                return dec.get(n.f0);
                
            }
            else {
                setint.add(n.f0.toString());
                return n.f0.toString();
            }
        }
        else if(type == null){
            System.out.println("dec : " + dec);
            System.out.println("agou : " + dec.get(n.f0.toString()));
            //return dec.get(n.f0);
            return n.f0.toString();
            //System.out.println("class");
            //return String.parseInt("class");
            //System.out.println("Size = " + setint.size());
        }
        else if(type == "call") {
            System.out.println("dec : " + dec);
            System.out.println("agou : " + dec.get(n.f0.toString()));
            System.out.println("callmemaybe");
            return dec.get(n.f0.toString());
        }

        //System.out.println("aaaaaaaa = " + n.f0.toString());

   
        //return dec.get(n.f0.toString());  //??
        return null;
    }

    /**
    * f0 -> "this"
    */
    public String visit(ThisExpression n, String argu) throws Exception {
        return null;
    }

    /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
    public String visit(ArrayAllocationExpression n, String argu) throws Exception {
        System.out.println("ArrayAllocationExpression :");
        return n.f3.accept(this, null);
    }

    /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public String visit(AllocationExpression n, String argu) throws Exception {
        System.out.println("AllocationExpression :");
        return n.f1.accept(this, null);
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n, String argu) throws Exception {
        System.out.println("NotExpression :");
        String clause = n.f1.accept(this, null);

            if(clause == "1")
                return "0";
            else
                return "1"; 
    }

    /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
    public String visit(BracketExpression n, String argu) throws Exception {
        System.out.println("BracketExpression :");
        return n.f1.accept(this, null);
    }

}
