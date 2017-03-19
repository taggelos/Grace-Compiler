import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import symbol_table.*;
import types.*;

public class SpigletVisitor extends GJDepthFirst<MyType, MyType> {
    public SymbolTable st;
    public Labels l;
    public String output;

    public SpigletVisitor(SymbolTable st) {       // Pairnei to SymbolTable pou dhmiourghse o MethodsVisitor
        this.st = st;   
        this.l = new Labels();
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
        MethodType meth1 = main.getMethod("main");
        this.output = new String("MAIN\n");
        
        //Dimiourgia tou vTable:
        for(int j = 0; j < st.symboltable.size(); j++) {
            if(st.symboltable.get(j).isMain == 0) {
                String label = l.create_class_label(st.symboltable.get(j).name);
                String vtable = new String("TEMP " + (++st.TEMP_count));
                String temp1 = new String("TEMP " + (++st.TEMP_count));
                int offset = st.symboltable.get(j).meth_count;
                this.output += "MOVE " + vtable + " " + label + "\n";               //Pairnoume th dieu8unsh tou vTable
                this.output += "MOVE " + temp1 + " HALLOCATE " + (offset*4) + "\n"; //Bazoume enan deikth apo th dieu8unsh tou vTable...
                this.output += "HSTORE " + vtable + " 0 " + temp1 + "\n";           

                for(int i = 0; i < offset; i++) {                                  //... na deixnei stis sunarthseis
                    String temp2 = new String("TEMP " + (++st.TEMP_count));
                    MethodType meth2 = st.symboltable.get(j).MethodList.get(i);
                    this.output += "MOVE " + temp2 + " " + meth2.from.name + "_" + meth2.name + "\n";
                    this.output += "HSTORE " + temp1 + " " + (i*4) + " " + temp2 + "\n";
                }
            }
        }

        n.f14.accept(this, meth1);
        n.f15.accept(this, meth1);
        this.output += new String("END\n");
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
        MethodType meth = new MethodType(null, classid.name);
        n.f3.accept(this, meth);
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
        n.f3.accept(this, argu);
        MethodType meth = new MethodType(null, classid.name);
        n.f5.accept(this, meth);
        n.f6.accept(this, classid);
        return null;
    }

    /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
    public MyType visit(VarDeclaration n, MyType argu) throws Exception {    
        MethodType meth = (MethodType) argu;
        String var = n.f1.f0.toString();
        if(meth.from != null) {         //An einai metablhth sunarthshs (kai oxi klashs)
            String temp = new String("TEMP " + (++st.TEMP_count));
            if((meth = meth.from.getMethod(meth.name)) != null) {
                meth.addVarTEMP(var, temp);
                this.output += new String("MOVE " + temp + " " + "0\n");
            }
        }

        return null;
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
        ClassType c = (ClassType) argu;
        String name = n.f2.accept(this, c).name;
        MethodType meth = c.getMethod(name);
        this.output += "\n" + c.name + "_" + name + "[" + (meth.par_count+1) + "]\n";
        this.output += "BEGIN\n";
        n.f7.accept(this, meth);
        n.f8.accept(this, meth);
        VariableType r = (VariableType) n.f10.accept(this, meth);
        this.output += "RETURN " + r.type + "\n";
        this.output += "END\n";

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
        n.f0.accept(this, argu);
        n.f1.accept(this, argu);
        return null;
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
        return null;
    }

    /**
    * f0 -> "boolean"
    */
    public MyType visit(BooleanType n, MyType argu) throws Exception {  
        return null;
    }

    /**
    * f0 -> "int"
    */
    public MyType visit(IntegerType n, MyType argu) throws Exception {  
        return null;
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
        String id = n.f0.accept(this, argu).name;
        MethodType meth = (MethodType) argu;
        VariableType v = (VariableType) n.f2.accept(this, argu);
        String expr = v.type;
        if(meth != null) {
            VariableType var = meth.methContainsVar(id);
            if(var == null) {
                ClassType c = st.contains(meth.from.name);
                if(c != null) {
                    var = c.classContainsVar(id);
                    if(var != null) {
                        this.output += "HSTORE " + "TEMP 0 " + (var.id*4) + " " + expr + "\n";
                        return null;
                    } 
                }
            }
            this.output += "MOVE " + var.var_TEMP + " " + expr + "\n";
        }
        else {
            ClassType c = st.contains(meth.from.name);
            if(c != null) {
                VariableType var = var = c.classContainsVar(id);
                if(var != null) {
                    this.output += "HSTORE " + "TEMP 0 " + (var.id*4) + " " + expr + "\n";
                    return null;
                }
            }
        }

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
    public MyType visit(ArrayAssignmentStatement n, MyType argu) throws Exception {     
        String error = l.create_label();
        String noerror = l.create_label();
        VariableType v1 = (VariableType) n.f0.accept(this, argu);
        String id = v1.type;
        String length = new String("TEMP " + (++st.TEMP_count));
        String length_check = new String("TEMP " + (++st.TEMP_count));
        this.output += "HLOAD " + length + " " + id + " 0" + "\n";      //Megethos tou pinaka
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String ar_pos = v2.type;
        this.output += "MOVE " + length_check + " LT " + ar_pos + " " + length + "\n";  //Elegxos an ar_pos < length
        this.output += "CJUMP " + length_check + " " + error + "\n";
        this.output += "MOVE " + length_check + " LT " + ar_pos + " 0" + "\n";          //Elegxos an ar_pos < 0
        String one = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + one + " 1" + "\n";
        this.output += "MOVE " + length_check + " MINUS " + one + " " + length_check + "\n";
        this.output += "CJUMP " + length_check + " " + error + "\n";
        this.output += "JUMP " + noerror + "\n";
        this.output += error + " NOOP\n";
        this.output += "ERROR\n";
        this.output += noerror + " NOOP\n";
        String array = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + array + " " + id + "\n";           //Pairnoume th dieu8unsh tou pinaka
        this.output += "MOVE " + array + " PLUS " + array + " 4\n";
        String temp = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + temp + " TIMES " + ar_pos + " 4\n";    //Epomenh 8esh tou pinaka
        this.output += "MOVE " + array + " PLUS " + array + " " + temp + "\n";
        VariableType v3 = (VariableType) n.f5.accept(this, argu);
        String expr = v3.type;
        this.output += "HSTORE " + array + " 0 " + expr + "\n";         //Arxikopoihsh me 0

        return new VariableType(array, null);
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
        String l1 = l.create_label();
        String l2 = l.create_label();
        VariableType v = (VariableType) n.f2.accept(this, argu);
        String expr = v.type;
        this.output += "CJUMP " + expr + " " + l1 + "\n";
        n.f4.accept(this, argu);
        this.output += "JUMP " + l2 + "\n";
        this.output += l1 + " NOOP\n";
        n.f6.accept(this, argu);
        this.output += l2 + " NOOP\n";

        return null;
    }

    /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
    public MyType visit(WhileStatement n, MyType argu) throws Exception {   
        String l1 = l.create_label();
        String l2 = l.create_label();
        this.output += l1 + " NOOP\n";
        VariableType v = (VariableType) n.f2.accept(this, argu);
        String expr = v.type;
        String condition = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + condition + " " + expr + "\n";
        this.output += "CJUMP " + condition + " " + l2 + "\n";
        n.f4.accept(this, argu);
        this.output += "JUMP " + l1 + "\n";
        this.output += l2 + " NOOP\n";

        return null;
    }

    /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
    public MyType visit(PrintStatement n, MyType argu) throws Exception {   
        VariableType v = (VariableType) n.f2.accept(this, argu);
        String expr = v.type;
        this.output += "PRINT " + expr + "\n";
        return null;
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
        VariableType v1 = (VariableType) n.f0.accept(this, argu);
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String clause1 = v1.type;
        String clause2 = v2.type;
        String temp = new String("TEMP " + (++st.TEMP_count));
        String label = l.create_label();
        this.output += new String("MOVE " + temp + " " + clause1 + "\n");
        this.output += new String("CJUMP " + clause1 + " " + label + "\n");
        this.output += new String("MOVE " + temp + " " + clause2 + "\n");
        this.output += new String(label + " NOOP" + "\n");

        return new VariableType(temp, null);
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(CompareExpression n, MyType argu) throws Exception {    
        String temp = new String("TEMP " + (++st.TEMP_count));
        VariableType v1 = (VariableType) n.f0.accept(this, argu);
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String expr1 = v1.type;
        String expr2 = v2.type;
        this.output += new String("MOVE " + temp + " LT " + expr1 + " " + expr2 + "\n");
        
        return new VariableType(temp, null);
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(PlusExpression n, MyType argu) throws Exception {   
        VariableType v1 = (VariableType) n.f0.accept(this, argu);
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String expr1 = v1.type;
        String expr2 = v2.type;
        String temp = new String("TEMP " + (++st.TEMP_count));
        this.output += new String("MOVE " + temp + " PLUS " + expr1 + " " + expr2 + "\n");
        
        return new VariableType(temp, null);
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(MinusExpression n, MyType argu) throws Exception {  
        VariableType v1 = (VariableType) n.f0.accept(this, argu);
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String expr1 = v1.type;
        String expr2 = v2.type;
        String temp = new String("TEMP " + (++st.TEMP_count));
        this.output += new String("MOVE " + temp + " MINUS " + expr1 + " " + expr2 + "\n");
        
        return new VariableType(temp, null);
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
    public MyType visit(TimesExpression n, MyType argu) throws Exception {  
        VariableType v1 = (VariableType) n.f0.accept(this, argu); 
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String expr1 = v1.type;
        String expr2 = v2.type;
        String temp = new String("TEMP " + (++st.TEMP_count));
        this.output += new String("MOVE " + temp + " TIMES " + expr1 + " " + expr2 + "\n");
        
        return new VariableType(temp, null);
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
    public MyType visit(ArrayLookup n, MyType argu) throws Exception {  
        String error = l.create_label();
        String noerror = l.create_label();
        VariableType v1 = (VariableType) n.f0.accept(this, argu);
        String id = v1.type;
        String length = new String("TEMP " + (++st.TEMP_count));
        String length_check = new String("TEMP " + (++st.TEMP_count));
        this.output += "HLOAD " + length + " " + id + " 0" + "\n";      //Megethos tou pinaka
        VariableType v2 = (VariableType) n.f2.accept(this, argu);
        String ar_pos = v2.type;
        this.output += "MOVE " + length_check + " LT " + ar_pos + " " + length + "\n";  //Elegxos an ar_pos < length
        this.output += "CJUMP " + length_check + " " + error + "\n";
        this.output += "MOVE " + length_check + " LT " + ar_pos + " 0" + "\n";          //Elegxos an ar_pos < 0
        String one = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + one + " 1" + "\n";
        this.output += "MOVE " + length_check + " MINUS " + one + " " + length_check + "\n";
        this.output += "CJUMP " + length_check + " " + error + "\n";
        this.output += "JUMP " + noerror + "\n";
        this.output += error + " NOOP\n";
        this.output += "ERROR\n";
        this.output += noerror + " NOOP\n";
        String array = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + array + " " + id + "\n";           //Pairnoume th dieu8unsh tou pinaka
        this.output += "MOVE " + array + " PLUS " + array + " 4\n";
        String temp = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + temp + " TIMES " + ar_pos + " 4\n";    //Epomenh 8esh tou pinaka
        this.output += "MOVE " + array + " PLUS " + array + " " + temp + "\n";
        String r = new String("TEMP " + + (++st.TEMP_count));
        this.output += "HLOAD " + r + " " + array + " 0" + "\n";

        return new VariableType(r, null);
    }

    /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
    public MyType visit(ArrayLength n, MyType argu) throws Exception {  
        VariableType v = (VariableType) n.f0.accept(this, argu);
        String expr = v.type;
        String length = new String("TEMP " + (++st.TEMP_count));
        this.output += new String("HLOAD " + length + " " + expr + " " + "0\n");
        
        return new VariableType(length, null);
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
        VariableType fun = (VariableType) n.f2.accept(this, argu);
        String className = expr.var_TEMP;
        String exprTemp = expr.type;
        ClassType c = st.contains(className);
        MethodType meth = c.getMethod(fun.name);
        int offset = meth.id - 1;
        String vtable_address = new String("TEMP " + (++st.TEMP_count));
        String thisTemp = new String("TEMP " + (++st.TEMP_count));
        String methTemp = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + thisTemp + " " + exprTemp + "\n";              
        this.output += "HLOAD " + vtable_address + " " + thisTemp + " 0\n";     //dieu8unsh tou vTable
        this.output += "HLOAD " + methTemp + " " + vtable_address + " " + (offset*4) + "\n";  

        MethodType params = (MethodType) n.f4.accept(this, argu);
        String allparams = new String(" ");
        if (n.f4.present()) {                                                   //Pros8etoume ta orismata
            for(int i = 0; i < params.ParamList.size(); i++) {               
                VariableType var = ((VariableType) params.ParamList.get(i));
                String par = new String("TEMP " + (++st.TEMP_count));    
                this.output += "MOVE " + par + " " + var.type + "\n";
                allparams += par + " ";
            }
        }
        String result = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + result + " CALL " + methTemp + "( " + thisTemp + allparams + ")\n";
        VariableType v = new VariableType(result, null);
        v.var_TEMP = meth.type;
        return v; 
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
            for(int i = 0; i < n.f0.size(); i++)
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
        String temp = "TEMP " + (++st.TEMP_count);
        this.output += "MOVE " + temp + " " + n.f0.toString() + "\n";
        return new VariableType(temp, null);
    }

    /**
    * f0 -> "true"
    */
    public MyType visit(TrueLiteral n, MyType argu) throws Exception {   
        String temp = "TEMP " + (++st.TEMP_count);
        this.output += "MOVE " + temp + " " + "1" + "\n";
        return new VariableType(temp, null);
    }

    /**
    * f0 -> "false"
    */
    public MyType visit(FalseLiteral n, MyType argu) throws Exception {   
        String temp = "TEMP " + (++st.TEMP_count);
        this.output += "MOVE " + temp + " " + "0" + "\n";
        return new VariableType(temp, null);
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public MyType visit(Identifier n, MyType argu) throws Exception {   
        String id = n.f0.toString();
        if(argu == null) 
            return new VariableType(null, id);
        ClassType c = st.contains(argu.name);
        VariableType var1;
        if(c != null) {                                   //An einai onoma klashs ...
            var1 = c.classContainsVar(id);
            if(var1 != null) {                                 //kai o identifier einai pedio klashs
                VariableType var2 = new VariableType(var1.var_TEMP, id);
                var2.var_TEMP = c.name;
                return var2;
            } 
            else {                                              //alliws, an einai sunarthsh
                MethodType meth = c.getMethod(id);
                if(meth != null)
                    return new VariableType(null, id);
            }
        } 
        else {                                            //An einai onoma sunarthshs ...
            MethodType meth = (MethodType) argu;
            var1 = meth.methContainsVar(id);             
            if(var1 != null) {                                 //kai o identifier einai metablhth
                VariableType var2 = new VariableType(var1.var_TEMP, id);
                var2.var_TEMP = var1.type;
                return var2;
            } 
            else {                                              //alliws, an einai pedio klashs
                c = st.contains(meth.from.name);
                if(c != null) {
                    var1 = c.classContainsVar(id);
                    if (var1 == null) {  
                        return new VariableType(null, id);  
                    }
                    String temp = "TEMP " + (++st.TEMP_count);
                    this.output += "HLOAD " + temp + " TEMP 0 " + (var1.id*4) + "\n";
                    VariableType var2 = new VariableType(temp, id);
                    var2.var_TEMP = var1.type;
                    return var2;
                }
            }
        }
        return null; 
    }

    /**
    * f0 -> "this"
    */
    public MyType visit(ThisExpression n, MyType argu) throws Exception {   
        VariableType var = new VariableType("TEMP 0", "this");
        MethodType meth = (MethodType) argu;
        ClassType c = meth.from;
        var.var_TEMP = c.name;

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
        String l1 = l.create_label();
        String l2 = l.create_label();
        String noerror = l.create_label();
        VariableType v1 = (VariableType) n.f3.accept(this, argu);
        String length = v1.type;
        String length_check = new String("TEMP " + (++st.TEMP_count));
        String size = new String("TEMP " + (++st.TEMP_count));
        String alloc_size = new String("TEMP " + (++st.TEMP_count));
        String counter = new String("TEMP " + (++st.TEMP_count));
        String array = new String("TEMP " + (++st.TEMP_count));
        String ar_address = new String("TEMP " + (++st.TEMP_count));
        String zero = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + length_check + " LT " + length + " 0" + "\n";      //Elegxos an length < 0
        this.output += "CJUMP " + length_check + " " + noerror + "\n";
        this.output += "ERROR\n";
        this.output += noerror + " NOOP\n";
        this.output += "MOVE " + size + " PLUS " + length + " 1" + "\n";            //Desmeboume xwro gia ton pinaka
        this.output += "MOVE " + alloc_size + " TIMES " + size + " 4" + "\n";     
        this.output += "MOVE " + array + " HALLOCATE " + alloc_size + "\n";
        this.output += "HSTORE " + array + " 0 " + length + "\n";                   //Apo8ukeuoume sthn prwth 8esh to length
        this.output += "MOVE " + ar_address + " " + array + "\n";                   //Kratame thn dieu8unsh tou pinaka
        this.output += "MOVE " + counter + " 4" + "\n";                             //Arxikopoioume ton counter me 4
        this.output += l1 + " NOOP\n";
        this.output += "MOVE " + length_check + " LT " + counter + " " + alloc_size + "\n";
        this.output += "CJUMP " + length_check + " " + l2 + "\n";
        this.output += "MOVE " + array + " PLUS " + array + " 4" + "\n";            //Pame sthn epomenh 8esh tou pinaka
        this.output += "MOVE " + zero + " 0" + "\n";
        this.output += "HSTORE " + array + " 0" + zero + "\n";
        this.output += "MOVE " + counter + " PLUS " + counter + " 4" + "\n";        //Au3anoume ton counter kata 4
        this.output += "JUMP " + l1 + "\n";
        this.output += l2 + " NOOP\n"; 

        return new VariableType(ar_address, null);
    }

    /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
    public MyType visit(AllocationExpression n, MyType argu) throws Exception {     
        String id = n.f1.accept(this, argu).name;
        ClassType c = st.contains(id);
        String temp = new String("TEMP " + (++st.TEMP_count));
        String vtable = new String("TEMP " + (++st.TEMP_count));
        String vtable_address = new String("TEMP " + (++st.TEMP_count));
        String label = l.create_class_label(c.name);
        this.output += "MOVE " + temp + " HALLOCATE " + (c.var_count+1)*4 + "\n";
        this.output += "MOVE " + vtable_address + " " + label + "\n";
        this.output += "HLOAD " + vtable + " " + vtable_address + " 0\n";
        this.output += "HSTORE " + temp + " 0 " + vtable + "\n";
        String zero = new String("TEMP " + (++st.TEMP_count));
        this.output += "MOVE " + zero + " " + "0\n";
        for (int i = 1; i <= c.var_count; i++)
            this.output += "HSTORE " + temp + " " + (i*4) + " " + zero + "\n";
        VariableType var = new VariableType(temp, id);
        var.var_TEMP = id;

        return var;
    }

    /**
    * f0 -> "!"
    * f1 -> Clause()
    */
    public MyType visit(NotExpression n, MyType argu) throws Exception {    
        String temp1 = new String("TEMP " + (++st.TEMP_count));
        String temp2 = new String("TEMP " + (++st.TEMP_count));
        VariableType v = (VariableType) n.f1.accept(this, argu);
        String clause = v.type;
        this.output += "MOVE " + temp2 + " " + "1\n";
        this.output += "MOVE " + temp1 + " MINUS " + temp2 + " " + clause + "\n";

        return new VariableType(temp1, null);
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
