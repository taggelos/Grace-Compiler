package facts_gen;

import syntaxtree.*;
import visitor.GJDepthFirst;
import java.util.*;
import java.io.*;

public class FactGeneratorVisitor extends GJDepthFirst<String, String> {
    public LinkedList<Instruction_t> instrList;
    public LinkedList<Var_t> varList;
    public LinkedList<Next_t> nextList;
    public LinkedList<VarMove_t> varMoveList;
    public LinkedList<ConstMove_t> constMoveList;
    public LinkedList<BinOpMove_t> binOpMoveList;
    public LinkedList<VarUse_t> varUseList;
    public LinkedList<VarDef_t> varDefList;
    public LinkedList<Cjump_t> cjumpList;
    public LinkedList<Jump_t> jumpList;
    public LinkedList<Args_t> argsList;
    public int ic1;
    public int ic2;

    public FactGeneratorVisitor() {
        instrList = new LinkedList<Instruction_t>();
        varList = new LinkedList<Var_t>();
        nextList = new LinkedList<Next_t>();
        varMoveList = new LinkedList<VarMove_t>();
        constMoveList = new LinkedList<ConstMove_t>();
        binOpMoveList =  new LinkedList<BinOpMove_t>();
        varUseList = new LinkedList<VarUse_t>();
        varDefList = new LinkedList<VarDef_t>();
        cjumpList = new LinkedList<Cjump_t>();
        jumpList = new LinkedList<Jump_t>();
        argsList = new LinkedList<Args_t>();
        this.ic1 = 0;
        this.ic2 = 0;
    }

    public String visit(NodeSequence n, String argu) throws Exception {
        if (n.size() == 1)
            return n.elementAt(0).accept(this,argu);
        String _ret = null;
        int _count=0;
        for ( Enumeration<Node> e = n.elements() ; e.hasMoreElements() ; ) {
            String ret = e.nextElement().accept(this,argu);
            if (ret != null) {
                if (_ret == null)
                    _ret = ret;
            }
            _count++;
        }
        return _ret;
    }

    /**
    * f0 -> "MAIN"
    * f1 -> StmtList()
    * f2 -> "END"
    * f3 -> ( Procedure() )*
    * f4 -> <EOF>
    */
    public String visit(Goal n, String argu) throws Exception {
        n.f1.accept(this, "MAIN");
        n.f3.accept(this, argu);
        return null;
    }

    /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
    public String visit(StmtList n, String argu) throws Exception {
        if (n.f0.present()) {
            for (int i = 0 ; i < n.f0.size() ; i++) {
                String str = n.f0.elementAt(i).accept(this, argu);
                if (str.matches("(.*)ERR(.*)")){
                    ic2--;
                    continue;
                }
                this.ic1++;
                instrList.addLast(new Instruction_t("\""+argu+"\"", this.ic1, "\""+str+"\""));
            }
        }
        return null;
    }

    /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> StmtExp()
    */
    public String visit(Procedure n, String argu) throws Exception {
        this.ic1 = 0;
        this.ic2 = 0;
        String id = n.f0.accept(this, argu);
        int args = Integer.parseInt(n.f2.accept(this, argu));
        for (int i = 0 ; i < args ; i++)
            argsList.addLast(new Args_t("\""+id+"\"", "\"TEMP "+ i +"\""));
        n.f4.accept(this, id);
        return null;
    }

    /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    */
    public String visit(Stmt n, String argu) throws Exception {
        this.ic2++;
        String stmt = n.f0.accept(this, argu);
        return stmt;
    }   

    /**
    * f0 -> "NOOP"
    */
    public String visit(NoOpStmt n, String argu) throws Exception {
        return "NOOP";
    }

    /**
    * f0 -> "ERROR"
    */
    public String visit(ErrorStmt n, String argu) throws Exception {
        return "ERROR";
    }

    /**
    * f0 -> "CJUMP"
    * f1 -> Temp()
    * f2 -> Label()
    */
    public String visit(CJumpStmt n, String argu) throws Exception {
        String tmp = n.f1.accept(this, argu);
        String label = n.f2.accept(this, argu);
        String op = "CJUMP " + tmp + " " + label;
        cjumpList.addLast(new Cjump_t("\""+argu+"\"", this.ic2, "\""+label+"\""));
        varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+tmp+"\""));
        return op;
    }

    /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
    public String visit(JumpStmt n, String argu) throws Exception {
        String label = n.f1.accept(this, argu);
        String op = "JUMP " + label;
        jumpList.addLast(new Jump_t("\""+argu+"\"", this.ic2, "\""+label+"\""));
        return op;
    }

    /**
    * f0 -> "HSTORE"
    * f1 -> Temp()
    * f2 -> IntegerLiteral()
    * f3 -> Temp()
    */
    public String visit(HStoreStmt n, String argu) throws Exception {
        String tmp1 = n.f1.accept(this, argu);
        String lit = n.f2.accept(this, argu);
        String tmp2 = n.f3.accept(this, argu);
        String op = "HSTORE " + tmp1 + " " + lit + " " + tmp2;
        varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+tmp1+"\""));
        varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+tmp2+"\""));
        return op;
    }

    /**
    * f0 -> "HLOAD"
    * f1 -> Temp()
    * f2 -> Temp()
    * f3 -> IntegerLiteral()
    */
    public String visit(HLoadStmt n, String argu) throws Exception {
        String tmp1 = n.f1.accept(this, argu);
        String tmp2 = n.f2.accept(this, argu);
        String lit = n.f3.accept(this, argu);
        String op = "HLOAD " + tmp1 + " " + tmp2 + " " + lit;
        varDefList.addLast(new VarDef_t("\""+argu+"\"", this.ic2, "\""+tmp1+"\""));
        varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+tmp2+"\""));
        return op;
    }

    /**
    * f0 -> "MOVE"
    * f1 -> Temp()
    * f2 -> Exp()
    */
    public String visit(MoveStmt n, String argu) throws Exception {
        n.f0.accept(this, argu);
        String tmp = n.f1.accept(this, argu);
        String exp = n.f2.accept(this, argu);
        String op = null;
        if (exp != null) {
            if (exp.matches("TEMP(.*)")) {
                op = "MOVE " + tmp + " " + exp;
                varMoveList.addLast(new VarMove_t("\""+argu+"\"", this.ic2, "\""+tmp+"\"", "\""+exp+"\""));
                varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+exp+"\""));
            } else if (exp.matches("[0-9]+")) {
                op = "MOVE " + tmp + " " + Integer.parseInt(exp);
                constMoveList.addLast(new ConstMove_t("\""+argu+"\"", this.ic2, "\""+tmp+"\"", Integer.parseInt(exp)));
            } else {
                op = "MOVE " + tmp + " " + exp;
                binOpMoveList.addLast(new BinOpMove_t("\""+argu+"\"", this.ic2, "\""+tmp+"\"", "\""+exp+"\""));
            }
        }
        varDefList.addLast(new VarDef_t("\""+argu+"\"", this.ic2, "\""+tmp+"\""));
        return op;
    }

    /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
    public String visit(PrintStmt n, String argu) throws Exception {
        String exp = n.f1.accept(this, argu);
        String op = "PRINT " + exp;
        if (exp != null && exp.matches("TEMP(.*)"))
            varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+exp+"\""));
        return op;
    }

    /**
    * f0 -> Call()
    *       | HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
    public String visit(Exp n, String argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> "BEGIN"
    * f1 -> StmtList()
    * f2 -> "RETURN"
    * f3 -> SimpleExp()
    * f4 -> "END"
    */
    public String visit(StmtExp n, String argu) throws Exception {
        n.f1.accept(this, argu);
        this.ic2++;
        String exp = n.f3.accept(this, argu);
        String ret = "RETURN " + exp;
        this.ic1++;
        instrList.addLast(new Instruction_t("\""+argu+"\" ", this.ic1, "\""+ret+"\""));
        if (exp != null && exp.matches("TEMP(.*)")){
            varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+exp+"\""));
        }
        return null;
    }

    /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    * f2 -> "("
    * f3 -> ( Temp() )*
    * f4 -> ")"
    */
    public String visit(Call n, String argu) throws Exception {
        String exp = n.f1.accept(this, argu);
        varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+exp+"\""));
        String tmp = "(";
        if (n.f3.present())
            for (int i = 0 ; i < n.f3.size() ; i++) {
                String t = n.f3.nodes.get(i).accept(this, argu);
                tmp += t;
                if (i < n.f3.size()-1)
                    tmp += ", ";
                varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+t+"\""));
            }
        tmp += ")";
        String op = "CALL " + exp + " " + tmp;
        return op;
    }

    /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
    public String visit(HAllocate n, String argu) throws Exception {
        String exp = n.f1.accept(this, argu);
        return "HALLOCATE " + exp;
    }

    /**
    * f0 -> Operator()
    * f1 -> Temp()
    * f2 -> SimpleExp()
    */
    public String visit(BinOp n, String argu) throws Exception {
        String op = n.f0.accept(this, argu);
        String tmp = n.f1.accept(this, argu);
        String exp = n.f2.accept(this, argu);
        varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+tmp+"\""));
        if (exp != null && exp.matches("TEMP(.*)"))
            varUseList.addLast(new VarUse_t("\""+argu+"\"", this.ic2, "\""+exp+"\""));
        return op + " " + tmp + " " + exp;
    }

    /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    */
    public String visit(Operator n, String argu) throws Exception {
        return n.f0.choice.toString();
    }

    /**
    * f0 -> Temp()
    *       | IntegerLiteral()
    *       | Label()
    */
    public String visit(SimpleExp n, String argu) throws Exception {
        return n.f0.accept(this, argu);
    }

    /**
    * f0 -> "TEMP"
    * f1 -> IntegerLiteral()
    */
    public String visit(Temp n, String argu) throws Exception {
        String t = n.f1.accept(this, argu);
        String ret = "TEMP " + t;
        Var_t v = new Var_t("\""+argu+"\"", "\""+ret+"\"");
        Var_t v2;
        for (int i = 0 ; i < varList.size() ; i++) {
            v2 = varList.get(i);
            if (v2.meth_name.equals("\"" + argu + "\"") && v2.temp.equals("\"" + ret + "\""))
                return ret;
        }
        varList.addLast(v);
        return ret;
    }

    /**
    * f0 -> <INTEGER_LITERAL>
    */
    public String visit(IntegerLiteral n, String argu) throws Exception {
        return n.f0.toString();
    }

    /**
    * f0 -> <IDENTIFIER>
    */
    public String visit(Label n, String argu) throws Exception {
        return n.f0.toString();
    }   

}
