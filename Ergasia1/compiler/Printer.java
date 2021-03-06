package compiler;

import java.util.*;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class Printer extends DepthFirstAdapter
{
    StringBuffer output = new StringBuffer();
    String tabs = "";
    boolean flag = false;
    boolean flagbr = false;
    boolean flagif = false;
    boolean flagass = false;
    boolean flagfun = false;

    public void addtab()
    {
        tabs+='\t';
    }
    public void subtab()
    {
        tabs = tabs.substring(1);
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }


    @Override
    public void caseAProgram(AProgram node)
    {
        inAProgram(node);
        if(node.getFunDef() != null)
        {
            node.getFunDef().apply(this);
        }
        outAProgram(node);
    }
    
    public StringBuffer getoutput(){
    	return output;
    }
    
    @Override
    public void caseAFunDef(AFunDef node)
    {
        inAFunDef(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        {
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocalDef());
            for(PLocalDef e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outAFunDef(node);
    }

    @Override
    public void caseAHeader(AHeader node)
    {
        inAHeader(node);
        if(node.getFun() != null)
        {
            output.append(tabs+"Function Definition: \n");
            addtab();
            node.getFun().apply(this);            
        }
        if(node.getIdentifier() != null)
        {
            output.append(tabs+"Name: " + node.getIdentifier() +'\n');
            node.getIdentifier().apply(this);            
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getFparDef() != null)
        {
            output.append(tabs+"Parameters: \n");
            addtab();
            node.getFparDef().apply(this); 
            subtab();
        }
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getReturnType() != null)
        {
            output.append(tabs+"Return Type: ");
            node.getReturnType().apply(this);
            output.append(node.getReturnType());
            output.append('\n');
        }
        outAHeader(node);

    }

    @Override
    public void caseASimpleParFparDef(ASimpleParFparDef node)
    {	
    	inASimpleParFparDef(node);
        if(node.getRef() != null)
        {
            node.getRef().apply(this);
        }
        if(node.getIdentifier() != null)
        {
        	output.append(tabs+"Parameter Name: " + node.getIdentifier() + '\n');
            node.getIdentifier().apply(this);
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getTypes() != null)
        {
        	output.append(tabs+"Parameter Type: " + node.getTypes() + "\n\n");
            node.getTypes().apply(this);
        }
        outASimpleParFparDef(node);
    }

    @Override
    public void caseAMultParFparDef(AMultParFparDef node)
    {
        inAMultParFparDef(node);
        if(node.getRef() != null)
        {
            node.getRef().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            output.append(tabs+"Parameter Name: " + node.getIdentifier() + "\n");
            node.getIdentifier().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outAMultParFparDef(node);
    }

    @Override
    public void caseAMultTypesFparDef(AMultTypesFparDef node)
    {
        inAMultTypesFparDef(node);
        if(node.getRef() != null)
        {
            node.getRef().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            output.append(tabs+"Parameter Name: " + node.getIdentifier() + "\n");
            node.getIdentifier().apply(this);
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getTypes() != null)
        {
            output.append(tabs+"Parameter Type: " + node.getTypes() + "\n\n");
            node.getTypes().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outAMultTypesFparDef(node);
    }

    @Override
    public void caseASemiParFparDef(ASemiParFparDef node)
    {
        inASemiParFparDef(node);
        if(node.getRef() != null)
        {
            node.getRef().apply(this);
        }
        if(node.getIdentifier() != null)
        {
            output.append(tabs+"Parameter Name: " + node.getIdentifier() + "\n");
            node.getIdentifier().apply(this);
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getTypes() != null)
        {
            output.append(tabs+"Parameter Type: " + node.getTypes() + "\n\n");
            node.getTypes().apply(this);
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outASemiParFparDef(node);
    }

    @Override
    public void caseANoneFparDef(ANoneFparDef node)
    {
        inANoneFparDef(node);
        outANoneFparDef(node);
    }

    @Override
    public void caseAIntDataTypes(AIntDataTypes node)
    {
        inAIntDataTypes(node);
        if(node.getInt() != null)
        {
            node.getInt().apply(this);
        }
        outAIntDataTypes(node);
    }

    @Override
    public void caseACharDataTypes(ACharDataTypes node)
    {
        inACharDataTypes(node);
        if(node.getChar() != null)
        {
            node.getChar().apply(this);
        }
        outACharDataTypes(node);
    }

    @Override
    public void caseABracketsArrayTypes(ABracketsArrayTypes node)
    {
        inABracketsArrayTypes(node);
        if(node.getLBr() != null)
        {
            node.getLBr().apply(this);
        }
        if(node.getIntegers() != null)
        {
            node.getIntegers().apply(this);
        }
        if(node.getRBr() != null)
        {
            node.getRBr().apply(this);
        }
        outABracketsArrayTypes(node);
    }

    @Override
    public void caseASimpleTypes(ASimpleTypes node)
    {
        inASimpleTypes(node);
        if(node.getDataTypes() != null)
        {
            node.getDataTypes().apply(this);
        }
        outASimpleTypes(node);
    }

    @Override
    public void caseAArrayTypes(AArrayTypes node)
    {
        inAArrayTypes(node);
        if(node.getDataTypes() != null)
        {
            node.getDataTypes().apply(this);
        }
        {
            List<PArrayTypes> copy = new ArrayList<PArrayTypes>(node.getArrayTypes());
            for(PArrayTypes e : copy)
            {
                e.apply(this);
            }
        }
        outAArrayTypes(node);
    }

    @Override
    public void caseASimpleReturnType(ASimpleReturnType node)
    {
        inASimpleReturnType(node);
        if(node.getDataTypes() != null)
        {
            node.getDataTypes().apply(this);
        }
        outASimpleReturnType(node);
    }

    @Override
    public void caseANoneReturnType(ANoneReturnType node)
    {
        inANoneReturnType(node);
        if(node.getNothing() != null)
        {
            node.getNothing().apply(this);
        }
        outANoneReturnType(node);
    }

    @Override
    public void caseAFunLocalDef(AFunLocalDef node)
    {
        inAFunLocalDef(node);
        if(node.getFunDef() != null)
        {
            node.getFunDef().apply(this);
        }
        outAFunLocalDef(node);
    }

    @Override
    public void caseADecLocalDef(ADecLocalDef node)
    {
        inADecLocalDef(node);
        if(node.getFunDec() != null)
        {
            node.getFunDec().apply(this);
        }
        outADecLocalDef(node);
    }

    @Override
    public void caseAVarLocalDef(AVarLocalDef node)
    {
        inAVarLocalDef(node);
        if(node.getVarDef() != null)
        {
            node.getVarDef().apply(this);
        }
        outAVarLocalDef(node);
    }

    @Override
    public void caseAFunDec(AFunDec node)
    {
        inAFunDec(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outAFunDec(node);
    }

    @Override
    public void caseABlock(ABlock node)
    {
        output.append(tabs+"Body { \n");
        inABlock(node);
        if(node.getLAg() != null)
        {
            node.getLAg().apply(this);
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getStmt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getRAg() != null)
        {
            node.getRAg().apply(this);
            output.append("\n"+tabs+"} \n");
        }
        outABlock(node);
    }

    @Override
    public void caseASemiStmt(ASemiStmt node)
    {
        inASemiStmt(node);
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outASemiStmt(node);
    }

    @Override
    public void caseAAssignmentStmt(AAssignmentStmt node)
    {
        inAAssignmentStmt(node);
        if(node.getAssignment() != null)
        {
            node.getAssignment().apply(this);
        }
        outAAssignmentStmt(node);
    }

    @Override
    public void caseABlockStmt(ABlockStmt node)
    {
        inABlockStmt(node);
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outABlockStmt(node);
    }

    @Override
    public void caseAFunCalStmt(AFunCalStmt node)
    {
        inAFunCalStmt(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outAFunCalStmt(node);
    }

    @Override
    public void caseAIfstmtStmt(AIfstmtStmt node)
    {
        inAIfstmtStmt(node);
        if(node.getIfstmt() != null)
        {
            node.getIfstmt().apply(this);
        }
        outAIfstmtStmt(node);
    }

    @Override
    public void caseAWhilestmtStmt(AWhilestmtStmt node)
    {
        inAWhilestmtStmt(node);
        flagif = true;
        if(node.getWhilestmt() != null)
        {
            addtab();
            output.append(tabs+"While statement: \n" );
            node.getWhilestmt().apply(this);
            subtab();
        }
        flagif = false;
        outAWhilestmtStmt(node);
    }

    @Override
    public void caseAReturnstmtStmt(AReturnstmtStmt node)
    {
        inAReturnstmtStmt(node);
        if(node.getReturnstmt() != null)
        {
            node.getReturnstmt().apply(this);
        }
        outAReturnstmtStmt(node);
    }

    @Override
    public void caseAAssignment(AAssignment node)
    {
        inAAssignment(node);
        addtab();
        output.append(tabs+"\n"+tabs+"Assignment: "+ node + "\n");
        flagass = true;
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
            output.append("\n");
        }
        if(node.getArrow() != null)
        {
            node.getArrow().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
            flagass = false;
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        
        subtab();
        outAAssignment(node);
        output.append("\n");

    }

    @Override
    public void caseAIdLVal(AIdLVal node)
    {
        inAIdLVal(node);
        if(node.getIdentifier() != null)
        {
            addtab();
        	if(node.parent().toString().equals(node.getIdentifier().toString()) && !flag && !flagif && !flagbr)
        		output.append("\n"+tabs+"LVal Name: " + node.getIdentifier() + "\n");
            else if((flagass && !flagbr )|| (flagfun && !flagbr) )
            {
                output.append("\n"+tabs+"LVal Name: " + node.getIdentifier() + " ");
            }
            else if(flagif && !flagfun && !flagbr) output.append("\n"+tabs+"LVal Name: " + node.getIdentifier() + " ");
            subtab();
            node.getIdentifier().apply(this);
            
            
        }
        outAIdLVal(node);
    }

    @Override
    public void caseAStringLVal(AStringLVal node)
    {
        inAStringLVal(node);
        if(node.getStringLiteral() != null)
        {
            addtab();
            if(!flag) 
            {
                output.append(tabs+"Type: char[]" + '\n');
                output.append(tabs+"Value: " + node.getStringLiteral() + "\n");
            }

            node.getStringLiteral().apply(this);
            subtab();
        }
        outAStringLVal(node);
    }

    @Override
    public void caseAIdBracketsLVal(AIdBracketsLVal node)
    {
        inAIdBracketsLVal(node);
        if(node.getLVal() != null)
        {
            
            flagbr = true;
            addtab();
            if(!flag) 
                output.append("\n"+tabs+"LVal Name: " + node);
            else
            {
                output.append(node);
            }
            flagass = false;
            subtab();
            node.getLVal().apply(this);

        }
        if(node.getLBr() != null)
        {
            node.getLBr().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getRBr() != null)
        {
            node.getRBr().apply(this);
        }
        flagbr = false;
        flagass = true;
        outAIdBracketsLVal(node);
    }

    @Override
    public void caseAFunCal(AFunCal node)
    {
        addtab();
        flagfun = true;
        output.append("\n"+tabs+"Function Call: \n");

        inAFunCal(node);
        if(node.getIdentifier() != null)
        {
            addtab();
            if(!flag) output.append(tabs+"Name: " + node.getIdentifier() + '\n');
            subtab();
            node.getIdentifier().apply(this);
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getExprList() != null)
        {
            addtab();
            if(!flag) output.append(tabs+"Arguments: \n");
            node.getExprList().apply(this);
            subtab();
        }
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        subtab();
        flagfun = false;
        outAFunCal(node);
    }

    @Override
    public void caseASimpleExprList(ASimpleExprList node)
    {
        inASimpleExprList(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outASimpleExprList(node);
    }

    @Override
    public void caseAListExprList(AListExprList node)
    {
        inAListExprList(node);
        if(node.getExprList() != null)
        {
            node.getExprList().apply(this);
            output.append("\n");
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
            output.append("\n");
        }
        outAListExprList(node);
    }

    @Override
    public void caseANoneExprList(ANoneExprList node)
    {
        inANoneExprList(node);
        outANoneExprList(node);
    }

    @Override
    public void caseAVarDef(AVarDef node)
    {
        inAVarDef(node);
        addtab();
        output.append("\n"+tabs+"Variable Definition: \n");

        if(node.getVar() != null)
        {
            node.getVar().apply(this);
        }
        if(node.getVarIds() != null)
        {
            addtab();
            output.append(tabs+"Name: ");
            output.append(node.getVarIds() + "\n");
            node.getVarIds().apply(this);
            subtab();
        }
        if(node.getColon() != null)
        {
            node.getColon().apply(this);
        }
        if(node.getTypes() != null)
        {
            addtab();
            output.append(tabs+"Type: ");
            output.append(node.getTypes() + "\n\n");
            node.getTypes().apply(this);
            subtab();
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outAVarDef(node);
    }

    @Override
    public void caseAIdVarIds(AIdVarIds node)
    {
        inAIdVarIds(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        outAIdVarIds(node);
    }

    @Override
    public void caseAListVarIds(AListVarIds node)
    {
        inAListVarIds(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        if(node.getComma() != null)
        {
            node.getComma().apply(this);
        }
        if(node.getVarIds() != null)
        {
            node.getVarIds().apply(this);
        }
        outAListVarIds(node);
    }

    @Override
    public void caseAWhilestmt(AWhilestmt node)
    {
        inAWhilestmt(node);
        if(node.getWhile() != null)
        {
            node.getWhile().apply(this);
        }
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        if(node.getDo() != null)
        {
            node.getDo().apply(this);
        }
        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }
        outAWhilestmt(node);
    }

    @Override
    public void caseAWhileWithElse(AWhileWithElse node)
    {
        inAWhileWithElse(node);
        if(node.getWhile() != null)
        {
            output.append(tabs+"While else statement: \n" );
            node.getWhile().apply(this);
        }
        if(node.getCond() != null)
        {
            output.append(tabs+"cond (else) statement: \n" );
            node.getCond().apply(this);
        }
        if(node.getDo() != null)
        {
            output.append(tabs+"do (else) statement: \n" );
            node.getDo().apply(this);
        }
        if(node.getStmtWithElse() != null)
        {
            node.getStmtWithElse().apply(this);
        }
        outAWhileWithElse(node);
    }

    @Override
    public void caseAIfstmt(AIfstmt node)
    {
        inAIfstmt(node);
        flagif = true;
        if(node.getIfHeader() != null)
        {
            addtab();
            output.append(tabs+"If statement: \n" );
            addtab();
            node.getIfHeader().apply(this);
            subtab();
        }
        flagif = false;
        if(node.getIfTrail() != null)
        {
            output.append(tabs+"Then"+"\n");
            addtab();
            node.getIfTrail().apply(this);
            subtab();
        }
        if(tabs.length() > 4) subtab();
        outAIfstmt(node);
    }

    @Override
    public void caseAIfHeader(AIfHeader node)
    {
        inAIfHeader(node);
        if(node.getIf() != null)
        {
            node.getIf().apply(this);
        }
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        if(node.getThen() != null)
        {
            node.getThen().apply(this);
        }
        outAIfHeader(node);
    }

    @Override
    public void caseANoElseIfTrail(ANoElseIfTrail node)
    {
        inANoElseIfTrail(node);
        if(node.getThen() != null)
        {
            node.getThen().apply(this);
        }
        outANoElseIfTrail(node);
    }

    @Override
    public void caseAWithElseIfTrail(AWithElseIfTrail node)
    {
        inAWithElseIfTrail(node);
        if(node.getThen() != null)
        {            
            node.getThen().apply(this);
        }
        if(node.getElse() != null)
        {
            subtab();
            output.append("\n"+tabs+"Else"+"\n");
            node.getElse().apply(this);
        }
        if(node.getElseSt() != null)
        {
            node.getElseSt().apply(this);
        }
        outAWithElseIfTrail(node);
    }

    @Override
    public void caseASemiStmtWithElse(ASemiStmtWithElse node)
    {
        inASemiStmtWithElse(node);
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outASemiStmtWithElse(node);
    }

    @Override
    public void caseAAssignmentStmtWithElse(AAssignmentStmtWithElse node)
    {
        inAAssignmentStmtWithElse(node);
        if(node.getAssignment() != null)
        {
            node.getAssignment().apply(this);
        }
        outAAssignmentStmtWithElse(node);
    }

    @Override
    public void caseAStmtReturnstmtStmtWithElse(AStmtReturnstmtStmtWithElse node)
    {
        inAStmtReturnstmtStmtWithElse(node);
        if(node.getReturnstmt() != null)
        {
            node.getReturnstmt().apply(this);
        }
        outAStmtReturnstmtStmtWithElse(node);
    }

    @Override
    public void caseAStmtFuncalStmtWithElse(AStmtFuncalStmtWithElse node)
    {
        inAStmtFuncalStmtWithElse(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outAStmtFuncalStmtWithElse(node);
    }

    @Override
    public void caseABlockStmtWithElse(ABlockStmtWithElse node)
    {
        inABlockStmtWithElse(node);
        if(node.getBlock() != null)
        {
            node.getBlock().apply(this);
        }
        outABlockStmtWithElse(node);
    }

    @Override
    public void caseAIfStmtWithElse(AIfStmtWithElse node)
    {
        inAIfStmtWithElse(node);
        if(node.getIfElse() != null)
        {
            node.getIfElse().apply(this);
        }
        outAIfStmtWithElse(node);
    }

    @Override
    public void caseAWhileStmtWithElse(AWhileStmtWithElse node)
    {
        inAWhileStmtWithElse(node);
        if(node.getWhileWithElse() != null)
        {
            node.getWhileWithElse().apply(this);
        }
        outAWhileStmtWithElse(node);
    }

    @Override
    public void caseAIfElse(AIfElse node)
    {
        inAIfElse(node);
        flagif = true;
        if(node.getIfHeader() != null)
        {            
            output.append(tabs+"if else"+"\n");
            node.getIfHeader().apply(this);
        }
        flagif = false;
        if(node.getThen() != null)
        {
            output.append(tabs+"then (ifelse)"+"\n");
            node.getThen().apply(this);
        }
        if(node.getElse() != null)
        {
            output.append(tabs+"else (ifelse)"+"\n");
            node.getElse().apply(this);
        }
        if(node.getElseSt() != null)
        {
            node.getElseSt().apply(this);
        }
        outAIfElse(node);
    }

    @Override
    public void caseACondAndCond(ACondAndCond node)
    {
        inACondAndCond(node);
        if(node.getConditionalAndExpression() != null)
        {
            node.getConditionalAndExpression().apply(this);
        }
        outACondAndCond(node);
    }

    @Override
    public void caseAOrExprCond(AOrExprCond node)
    {
        inAOrExprCond(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getOr() != null)
        {
            output.append(tabs+"OR ["+"\n");
            addtab();
            node.getOr().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            subtab();
            output.append(tabs+"]\n");
        }
        outAOrExprCond(node);
    }

    @Override
    public void caseACondNotConditionalAndExpression(ACondNotConditionalAndExpression node)
    {
        inACondNotConditionalAndExpression(node);
        if(node.getConditionalNotExpression() != null)
        {
            node.getConditionalNotExpression().apply(this);
        }
        outACondNotConditionalAndExpression(node);
    }

    @Override
    public void caseAAndExprConditionalAndExpression(AAndExprConditionalAndExpression node)
    {
        inAAndExprConditionalAndExpression(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getAnd() != null)
        {
            output.append(tabs+"AND ["+"\n");
            addtab();
            node.getAnd().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
            subtab();
            output.append(tabs+"]\n");
        }
        outAAndExprConditionalAndExpression(node);
    }

    @Override
    public void caseANotExprConditionalNotExpression(ANotExprConditionalNotExpression node)
    {
        inANotExprConditionalNotExpression(node);
        if(node.getNot() != null)
        {
            output.append(tabs+"NOT ["+"\n");
            addtab();
            node.getNot().apply(this);
        }
        if(node.getConditionalNotExpression() != null)
        {
            node.getConditionalNotExpression().apply(this);
            subtab();
            output.append(tabs+"]\n");
        }
        outANotExprConditionalNotExpression(node);
    }

    @Override
    public void caseAComparativeConditionalNotExpression(AComparativeConditionalNotExpression node)
    {
        inAComparativeConditionalNotExpression(node);
        if(node.getComparativeExpression() != null)
        {
            node.getComparativeExpression().apply(this);
        }
        outAComparativeConditionalNotExpression(node);
    }

    @Override
    public void caseARelationalComparativeExpression(ARelationalComparativeExpression node)
    {
        inARelationalComparativeExpression(node);
        if(node.getRelationalExpression() != null)
        {
            node.getRelationalExpression().apply(this);
        }
        outARelationalComparativeExpression(node);
    }

    @Override
    public void caseAEqualComparativeExpression(AEqualComparativeExpression node)
    {
        inAEqualComparativeExpression(node);
        if(node.getLeft() != null)
        {
            output.append(tabs + "Left (" );
            node.getLeft().apply(this);
            output.append(")" + "\n");
        }
        if(node.getEq() != null)
        {
            output.append(tabs + "equals"  + "\n");
            node.getEq().apply(this);
        }
        if(node.getRight() != null)
        {
            output.append(tabs + "Right (" );
            node.getRight().apply(this);
            output.append(")" + "\n");
        }
        outAEqualComparativeExpression(node);
    }

    @Override
    public void caseANotEqualComparativeExpression(ANotEqualComparativeExpression node)
    {
        inANotEqualComparativeExpression(node);
        if(node.getLeft() != null)
        {
            output.append(tabs + "Left (" );
            node.getLeft().apply(this);
            output.append(")" + "\n");
        }
        if(node.getNeq() != null)
        {
            output.append(tabs + "not equals"  + "\n");
            node.getNeq().apply(this);
        }
        if(node.getRight() != null)
        {
            output.append(tabs + "Right (" );
            node.getRight().apply(this);
            output.append(")" + "\n");
        }
        outANotEqualComparativeExpression(node);
    }

    @Override
    public void caseAAdditiveRelationalExpression(AAdditiveRelationalExpression node)
    {
        inAAdditiveRelationalExpression(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAAdditiveRelationalExpression(node);
    }

    @Override
    public void caseALessThanRelationalExpression(ALessThanRelationalExpression node)
    {
        inALessThanRelationalExpression(node);
        if(node.getLeft() != null)
        {
            output.append(tabs + "Left (" );
            node.getLeft().apply(this);
            output.append(")" + "\n");
        }
        if(node.getLt() != null)
        {
            output.append(tabs + "less than"  + "\n");
            node.getLt().apply(this);
        }
        if(node.getRight() != null)
        {
            output.append(tabs + "Right (" );
            node.getRight().apply(this);
            output.append(")" + "\n");
        }
        outALessThanRelationalExpression(node);
    }

    @Override
    public void caseAGreaterThanRelationalExpression(AGreaterThanRelationalExpression node)
    {
        inAGreaterThanRelationalExpression(node);
        if(node.getLeft() != null)
        {
            output.append(tabs + "Left (" );
            node.getLeft().apply(this);
            output.append(")" + "\n");
        }
        if(node.getGt() != null)
        {
            output.append(tabs + "greater than"  + "\n");
            node.getGt().apply(this);
        }
        if(node.getRight() != null)
        {
            output.append(tabs + "Right (" );
            node.getRight().apply(this);            
            output.append(")" + "\n");
        }
        outAGreaterThanRelationalExpression(node);
    }

    @Override
    public void caseAGreaterEqualThanRelationalExpression(AGreaterEqualThanRelationalExpression node)
    {
        inAGreaterEqualThanRelationalExpression(node);
        if(node.getLeft() != null)
        {            
            output.append(tabs + "Left (" );
            node.getLeft().apply(this);
            output.append(")" + "\n");
        }
        if(node.getGteq() != null)
        {
            output.append(tabs + "greater-equal than"  + "\n");
            node.getGteq().apply(this);
        }
        if(node.getRight() != null)
        {
            output.append(tabs + "Right (" );
            node.getRight().apply(this);      
            output.append(")" + "\n");
        }
        outAGreaterEqualThanRelationalExpression(node);
    }

    @Override
    public void caseALessEqualThanRelationalExpression(ALessEqualThanRelationalExpression node)
    {
        inALessEqualThanRelationalExpression(node);
        if(node.getLeft() != null)
        {
            output.append(tabs + "Left (" );
            node.getLeft().apply(this);            
            output.append(")" + "\n");
        }
        if(node.getLteq() != null)
        {
            output.append(tabs + "less-equal than"  + "\n");
            node.getLteq().apply(this);
        }
        if(node.getRight() != null)
        {
            output.append(tabs + "Right (" );
            node.getRight().apply(this);            
            output.append(")" + "\n");
        }
        outALessEqualThanRelationalExpression(node);
    }

    @Override
    public void caseASemiReturnstmt(ASemiReturnstmt node)
    {
        inASemiReturnstmt(node);
        if(node.getReturn() != null)
        {
            node.getReturn().apply(this);
        }
        if(node.getExpr() != null)
        {
            addtab();
            output.append(tabs+"Return Expression: ");
            node.getExpr().apply(this);
            subtab();
        }
        if(node.getSemi() != null)
        {
            node.getSemi().apply(this);
        }
        outASemiReturnstmt(node);
    }

    @Override
    public void caseAFactorExpr(AFactorExpr node)
    {
        inAFactorExpr(node);
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outAFactorExpr(node);
    }

    @Override
    public void caseAAddExpr(AAddExpr node)
    {
        inAAddExpr(node);
        addtab();
        if(!flag && flagass) output.append("\n"+tabs+"Expr: ");
        if(flagfun) output.append(tabs);
        flag = true;
        subtab();
        if(node.getExpr() != null)
        {
            if(!flagbr) output.append(" ( ");
            node.getExpr().apply(this);
        }
        if(node.getPlus() != null)
        {
            if(!flagbr) output.append(" + ");
            node.getPlus().apply(this);
        }
        if(node.getFactor() != null)
        {
            
            node.getFactor().apply(this);
            if(!flagbr) output.append(" ) ");
        }
        flag = false;
        outAAddExpr(node);
    }

    @Override
    public void caseASubExpr(ASubExpr node)
    {
        inASubExpr(node);
        addtab();
        if(!flag && flagass) output.append("\n"+tabs+"Expr: ");
        if(flagfun) output.append(tabs);
        flag = true;
        subtab();
        if(node.getExpr() != null)
        {
            if(!flagbr) output.append(" ( ");
            node.getExpr().apply(this);
        }
        if(node.getMinus() != null)
        {
            if(!flagbr) output.append(" - ");
            node.getMinus().apply(this);
        }
        if(node.getFactor() != null)
        {
            
            node.getFactor().apply(this);
            if(!flagbr) output.append(" ) ");
        }
        
        flag = false;
        outASubExpr(node);
    }

    @Override
    public void caseATermFactor(ATermFactor node)
    {
        inATermFactor(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        outATermFactor(node);
    }

    @Override
    public void caseAMultFactor(AMultFactor node)
    {
        inAMultFactor(node);
        addtab();
        if(!flag && flagass) output.append("\n"+tabs+"Expr: ");
        if(flagfun) output.append(tabs);
        flag = true;
        subtab();
        if(node.getFactor() != null)
        {
            if(!flagbr) output.append(" ( ");
            node.getFactor().apply(this);
        }
        if(node.getStar() != null)
        {
            if(!flagbr) output.append(" * ");
            node.getStar().apply(this);
        }
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
            if(!flagbr) output.append(" ) ");
        }
        flag = false;
        outAMultFactor(node);
    }

    @Override
    public void caseAModFactor(AModFactor node)
    {
        inAModFactor(node);
        addtab();
        if(!flag && flagass) output.append("\n"+tabs+"Expr: ");
        if(flagfun) output.append(tabs);
        flag = true;
        subtab();
        if(node.getFactor() != null)
        {
            if(!flagbr) output.append(" ( ");
            node.getFactor().apply(this);
        }
        if(node.getMod() != null)
        {
            if(!flagbr) output.append(" mod ");
            node.getMod().apply(this);
        }
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
            if(!flagbr) output.append(" ) ");
        }
        flag = false;
        outAModFactor(node);
    }

    @Override
    public void caseADivFactor(ADivFactor node)
    {
        inADivFactor(node);
        addtab();
        if(!flag && flagass) output.append("\n"+tabs+"Expr: ");
        if(flagfun) output.append(tabs);
        flag = true;
        subtab();
        if(node.getFactor() != null)
        {
            if(!flagbr) output.append(" ( ");
            node.getFactor().apply(this);
        }
        if(node.getDiv() != null)
        {
            if(!flagbr) output.append(" / ");
            node.getDiv().apply(this);
        }
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
            if(!flagbr) output.append(" ) ");
        }
        flag = false;
        outADivFactor(node);
    }
    
    @Override
    public void caseAPlusOrMinusTerm(APlusOrMinusTerm node)
    {
        inAPlusOrMinusTerm(node);
        if(node.getPlusOrMinus() != null)
        {
            node.getPlusOrMinus().apply(this);
        }
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        outAPlusOrMinusTerm(node);
    }

    @Override
    public void caseAIntTerm(AIntTerm node)
    {
        inAIntTerm(node);
        if(!flagbr) output.append("\n"+tabs+"\tInteger: " );

        if(node.getIntegers() != null)
        {
            if(!flagbr) output.append(node.getIntegers() );
            node.getIntegers().apply(this);
        }
        outAIntTerm(node);
    }

    @Override
    public void caseACharTerm(ACharTerm node)
    {
        inACharTerm(node);
        if(node.getCharConst() != null)
        {
            output.append(tabs+"\tCharTerm: "+ node.getCharConst()+ "\n"  );
            node.getCharConst().apply(this);
        }
        outACharTerm(node);
    }

    @Override
    public void caseALValTerm(ALValTerm node)
    {
        inALValTerm(node);
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
        }
        outALValTerm(node);
    }

    @Override
    public void caseAFunCalTerm(AFunCalTerm node)
    {
        inAFunCalTerm(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        outAFunCalTerm(node);
    }

    @Override
    public void caseAParTerm(AParTerm node)
    {
        inAParTerm(node);
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        if(node.getExpr() != null)
        {
            output.append(" ( ");
            node.getExpr().apply(this);
            output.append(" ) ");
        }
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        outAParTerm(node);
    }

    @Override
    public void caseAPlusPlusOrMinus(APlusPlusOrMinus node)
    {
        inAPlusPlusOrMinus(node);
        if(node.getPlus() != null)
        {
            if(!flag && !flagass) output.append(tabs+"\t");
            output.append("+");
            node.getPlus().apply(this);
        }
        outAPlusPlusOrMinus(node);
    }

    @Override
    public void caseAMinusPlusOrMinus(AMinusPlusOrMinus node)
    {
        inAMinusPlusOrMinus(node);
        if(node.getMinus() != null)
        {
            if(!flag && !flagass) output.append(tabs+"\t");
            output.append("-");
            node.getMinus().apply(this);
        }
        outAMinusPlusOrMinus(node);
    }
}
