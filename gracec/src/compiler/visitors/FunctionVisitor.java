/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.visitors;

import java.util.*;

import compiler.analysis.DepthFirstAdapter;
import compiler.node.*;

public class FunctionVisitor extends DepthFirstAdapter
{
	public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    public void defaultOut(@SuppressWarnings("unused") Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getPProgram().apply(this);
        node.getEOF().apply(this);
        outStart(node);
    }

    public void inAProgram(AProgram node)
    {
        defaultIn(node);
    }

    public void outAProgram(AProgram node)
    {
        defaultOut(node);
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

    public void inAFunDef(AFunDef node)
    {
        defaultIn(node);
    }

    public void outAFunDef(AFunDef node)
    {
        defaultOut(node);
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
            List<PLocalDef> copy = new ArrayList<PLocalDef>(node.getLocal());
            for(PLocalDef e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getBlock());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAFunDef(node);
    }

    public void inAHeader(AHeader node)
    {
        defaultIn(node);
    }

    public void outAHeader(AHeader node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAHeader(AHeader node)
    {
        inAHeader(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getPars() != null)
        {
            node.getPars().apply(this);
        }
        if(node.getReturnT() != null)
        {
            node.getReturnT().apply(this);
        }
        outAHeader(node);
    }

    public void inASimpleParFparDef(ASimpleParFparDef node)
    {
        defaultIn(node);
    }

    public void outASimpleParFparDef(ASimpleParFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASimpleParFparDef(ASimpleParFparDef node)
    {
        inASimpleParFparDef(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        outASimpleParFparDef(node);
    }

    public void inAMultParFparDef(AMultParFparDef node)
    {
        defaultIn(node);
    }

    public void outAMultParFparDef(AMultParFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMultParFparDef(AMultParFparDef node)
    {
        inAMultParFparDef(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outAMultParFparDef(node);
    }

    public void inAMultTypesFparDef(AMultTypesFparDef node)
    {
        defaultIn(node);
    }

    public void outAMultTypesFparDef(AMultTypesFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMultTypesFparDef(AMultTypesFparDef node)
    {
        inAMultTypesFparDef(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outAMultTypesFparDef(node);
    }

    public void inASemiParFparDef(ASemiParFparDef node)
    {
        defaultIn(node);
    }

    public void outASemiParFparDef(ASemiParFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASemiParFparDef(ASemiParFparDef node)
    {
        inASemiParFparDef(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        if(node.getFparDef() != null)
        {
            node.getFparDef().apply(this);
        }
        outASemiParFparDef(node);
    }

    public void inANoneFparDef(ANoneFparDef node)
    {
        defaultIn(node);
    }

    public void outANoneFparDef(ANoneFparDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANoneFparDef(ANoneFparDef node)
    {
        inANoneFparDef(node);
        outANoneFparDef(node);
    }

    public void inAFunLocalDef(AFunLocalDef node)
    {
        defaultIn(node);
    }

    public void outAFunLocalDef(AFunLocalDef node)
    {
        defaultOut(node);
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

    public void inADecLocalDef(ADecLocalDef node)
    {
        defaultIn(node);
    }

    public void outADecLocalDef(ADecLocalDef node)
    {
        defaultOut(node);
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

    public void inAVarLocalDef(AVarLocalDef node)
    {
        defaultIn(node);
    }

    public void outAVarLocalDef(AVarLocalDef node)
    {
        defaultOut(node);
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

    public void inAFunDec(AFunDec node)
    {
        defaultIn(node);
    }

    public void outAFunDec(AFunDec node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunDec(AFunDec node)
    {
        inAFunDec(node);
        if(node.getHeader() != null)
        {
            node.getHeader().apply(this);
        }
        outAFunDec(node);
    }

    public void inAIntDataTypes(AIntDataTypes node)
    {
        defaultIn(node);
    }

    public void outAIntDataTypes(AIntDataTypes node)
    {
        defaultOut(node);
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

    public void inACharDataTypes(ACharDataTypes node)
    {
        defaultIn(node);
    }

    public void outACharDataTypes(ACharDataTypes node)
    {
        defaultOut(node);
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

    public void inASimpleTypes(ASimpleTypes node)
    {
        defaultIn(node);
    }

    public void outASimpleTypes(ASimpleTypes node)
    {
        defaultOut(node);
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

    public void inAArrayTypes(AArrayTypes node)
    {
        defaultIn(node);
    }

    public void outAArrayTypes(AArrayTypes node)
    {
        defaultOut(node);
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
            List<TIntegers> copy = new ArrayList<TIntegers>(node.getIntegers());
            for(TIntegers e : copy)
            {
                e.apply(this);
            }
        }
        outAArrayTypes(node);
    }

    public void inASimpleReturnType(ASimpleReturnType node)
    {
        defaultIn(node);
    }

    public void outASimpleReturnType(ASimpleReturnType node)
    {
        defaultOut(node);
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

    public void inANoneReturnType(ANoneReturnType node)
    {
        defaultIn(node);
    }

    public void outANoneReturnType(ANoneReturnType node)
    {
        defaultOut(node);
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

    public void inAVarDef(AVarDef node)
    {
        defaultIn(node);
    }

    public void outAVarDef(AVarDef node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAVarDef(AVarDef node)
    {
        inAVarDef(node);
        {
            List<TIdentifier> copy = new ArrayList<TIdentifier>(node.getName());
            for(TIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getType() != null)
        {
            node.getType().apply(this);
        }
        outAVarDef(node);
    }

    public void inAFunCal(AFunCal node)
    {
        defaultIn(node);
    }

    public void outAFunCal(AFunCal node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunCal(AFunCal node)
    {
        inAFunCal(node);
        if(node.getName() != null)
        {
            node.getName().apply(this);
        }
        {
            List<PExpr> copy = new ArrayList<PExpr>(node.getExprs());
            for(PExpr e : copy)
            {
                e.apply(this);
            }
        }
        outAFunCal(node);
    }

    public void inAIdLVal(AIdLVal node)
    {
        defaultIn(node);
    }

    public void outAIdLVal(AIdLVal node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIdLVal(AIdLVal node)
    {
        inAIdLVal(node);
        if(node.getIdentifier() != null)
        {
            node.getIdentifier().apply(this);
        }
        outAIdLVal(node);
    }

    public void inAStringLVal(AStringLVal node)
    {
        defaultIn(node);
    }

    public void outAStringLVal(AStringLVal node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAStringLVal(AStringLVal node)
    {
        inAStringLVal(node);
        if(node.getStringLiteral() != null)
        {
            node.getStringLiteral().apply(this);
        }
        outAStringLVal(node);
    }

    public void inAIdBracketsLVal(AIdBracketsLVal node)
    {
        defaultIn(node);
    }

    public void outAIdBracketsLVal(AIdBracketsLVal node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIdBracketsLVal(AIdBracketsLVal node)
    {
        inAIdBracketsLVal(node);
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAIdBracketsLVal(node);
    }

    public void inAIfHeader(AIfHeader node)
    {
        defaultIn(node);
    }

    public void outAIfHeader(AIfHeader node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfHeader(AIfHeader node)
    {
        inAIfHeader(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        outAIfHeader(node);
    }

    public void inANoElseIfTrail(ANoElseIfTrail node)
    {
        defaultIn(node);
    }

    public void outANoElseIfTrail(ANoElseIfTrail node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANoElseIfTrail(ANoElseIfTrail node)
    {
        inANoElseIfTrail(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outANoElseIfTrail(node);
    }

    public void inAWithElseIfTrail(AWithElseIfTrail node)
    {
        defaultIn(node);
    }

    public void outAWithElseIfTrail(AWithElseIfTrail node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAWithElseIfTrail(AWithElseIfTrail node)
    {
        inAWithElseIfTrail(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseSt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAWithElseIfTrail(node);
    }

    public void inAAndExprExpr(AAndExprExpr node)
    {
        defaultIn(node);
    }

    public void outAAndExprExpr(AAndExprExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAndExprExpr(AAndExprExpr node)
    {
        inAAndExprExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAAndExprExpr(node);
    }

    public void inAOrExprExpr(AOrExprExpr node)
    {
        defaultIn(node);
    }

    public void outAOrExprExpr(AOrExprExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAOrExprExpr(AOrExprExpr node)
    {
        inAOrExprExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAOrExprExpr(node);
    }

    public void inANotExprExpr(ANotExprExpr node)
    {
        defaultIn(node);
    }

    public void outANotExprExpr(ANotExprExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANotExprExpr(ANotExprExpr node)
    {
        inANotExprExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outANotExprExpr(node);
    }

    public void inALessThanExpr(ALessThanExpr node)
    {
        defaultIn(node);
    }

    public void outALessThanExpr(ALessThanExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALessThanExpr(ALessThanExpr node)
    {
        inALessThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outALessThanExpr(node);
    }

    public void inAGreaterThanExpr(AGreaterThanExpr node)
    {
        defaultIn(node);
    }

    public void outAGreaterThanExpr(AGreaterThanExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGreaterThanExpr(AGreaterThanExpr node)
    {
        inAGreaterThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAGreaterThanExpr(node);
    }

    public void inAGreaterEqualThanExpr(AGreaterEqualThanExpr node)
    {
        defaultIn(node);
    }

    public void outAGreaterEqualThanExpr(AGreaterEqualThanExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAGreaterEqualThanExpr(AGreaterEqualThanExpr node)
    {
        inAGreaterEqualThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAGreaterEqualThanExpr(node);
    }

    public void inALessEqualThanExpr(ALessEqualThanExpr node)
    {
        defaultIn(node);
    }

    public void outALessEqualThanExpr(ALessEqualThanExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALessEqualThanExpr(ALessEqualThanExpr node)
    {
        inALessEqualThanExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outALessEqualThanExpr(node);
    }

    public void inAEqualExpr(AEqualExpr node)
    {
        defaultIn(node);
    }

    public void outAEqualExpr(AEqualExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAEqualExpr(AEqualExpr node)
    {
        inAEqualExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAEqualExpr(node);
    }

    public void inANotEqualExpr(ANotEqualExpr node)
    {
        defaultIn(node);
    }

    public void outANotEqualExpr(ANotEqualExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseANotEqualExpr(ANotEqualExpr node)
    {
        inANotEqualExpr(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outANotEqualExpr(node);
    }

    public void inAAddExpr(AAddExpr node)
    {
        defaultIn(node);
    }

    public void outAAddExpr(AAddExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAddExpr(AAddExpr node)
    {
        inAAddExpr(node);
        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
        }
        outAAddExpr(node);
    }

    public void inASubExpr(ASubExpr node)
    {
        defaultIn(node);
    }

    public void outASubExpr(ASubExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASubExpr(ASubExpr node)
    {
        inASubExpr(node);
        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
        }
        outASubExpr(node);
    }

    public void inAMultExpr(AMultExpr node)
    {
        defaultIn(node);
    }

    public void outAMultExpr(AMultExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMultExpr(AMultExpr node)
    {
        inAMultExpr(node);
        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
        }
        outAMultExpr(node);
    }

    public void inAModExpr(AModExpr node)
    {
        defaultIn(node);
    }

    public void outAModExpr(AModExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAModExpr(AModExpr node)
    {
        inAModExpr(node);
        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
        }
        outAModExpr(node);
    }

    public void inADivExpr(ADivExpr node)
    {
        defaultIn(node);
    }

    public void outADivExpr(ADivExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseADivExpr(ADivExpr node)
    {
        inADivExpr(node);
        if(node.getExpr1() != null)
        {
            node.getExpr1().apply(this);
        }
        if(node.getExpr2() != null)
        {
            node.getExpr2().apply(this);
        }
        outADivExpr(node);
    }

    public void inAPlusOrMinusExpr(APlusOrMinusExpr node)
    {
        defaultIn(node);
    }

    public void outAPlusOrMinusExpr(APlusOrMinusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlusOrMinusExpr(APlusOrMinusExpr node)
    {
        inAPlusOrMinusExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAPlusOrMinusExpr(node);
    }

    public void inAIntExpr(AIntExpr node)
    {
        defaultIn(node);
    }

    public void outAIntExpr(AIntExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntExpr(AIntExpr node)
    {
        inAIntExpr(node);
        if(node.getIntegers() != null)
        {
            node.getIntegers().apply(this);
        }
        outAIntExpr(node);
    }

    public void inACharExpr(ACharExpr node)
    {
        defaultIn(node);
    }

    public void outACharExpr(ACharExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseACharExpr(ACharExpr node)
    {
        inACharExpr(node);
        if(node.getCharConst() != null)
        {
            node.getCharConst().apply(this);
        }
        outACharExpr(node);
    }

    public void inALValExpr(ALValExpr node)
    {
        defaultIn(node);
    }

    public void outALValExpr(ALValExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseALValExpr(ALValExpr node)
    {
        inALValExpr(node);
        if(node.getLVal() != null)
        {
            node.getLVal().apply(this);
        }
        outALValExpr(node);
    }

    public void inAParExpr(AParExpr node)
    {
        defaultIn(node);
    }

    public void outAParExpr(AParExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAParExpr(AParExpr node)
    {
        inAParExpr(node);
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAParExpr(node);
    }

    public void inAFunCalExpr(AFunCalExpr node)
    {
        defaultIn(node);
    }

    public void outAFunCalExpr(AFunCalExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunCalExpr(AFunCalExpr node)
    {
        inAFunCalExpr(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        outAFunCalExpr(node);
    }

    public void inAReturnstmtExpr(AReturnstmtExpr node)
    {
        defaultIn(node);
    }

    public void outAReturnstmtExpr(AReturnstmtExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAReturnstmtExpr(AReturnstmtExpr node)
    {
        inAReturnstmtExpr(node);
        if(node.getReturnexpr() != null)
        {
            node.getReturnexpr().apply(this);
        }
        outAReturnstmtExpr(node);
    }

    public void inASemiStmt(ASemiStmt node)
    {
        defaultIn(node);
    }

    public void outASemiStmt(ASemiStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASemiStmt(ASemiStmt node)
    {
        inASemiStmt(node);
        outASemiStmt(node);
    }

    public void inAAssignmentStmt(AAssignmentStmt node)
    {
        defaultIn(node);
    }

    public void outAAssignmentStmt(AAssignmentStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAAssignmentStmt(AAssignmentStmt node)
    {
        inAAssignmentStmt(node);
        if(node.getLeft() != null)
        {
            node.getLeft().apply(this);
        }
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        outAAssignmentStmt(node);
    }

    public void inAIfstmtStmt(AIfstmtStmt node)
    {
        defaultIn(node);
    }

    public void outAIfstmtStmt(AIfstmtStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfstmtStmt(AIfstmtStmt node)
    {
        inAIfstmtStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        if(node.getStmt() != null)
        {
            node.getStmt().apply(this);
        }
        outAIfstmtStmt(node);
    }

    public void inAIfElseStmt(AIfElseStmt node)
    {
        defaultIn(node);
    }

    public void outAIfElseStmt(AIfElseStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIfElseStmt(AIfElseStmt node)
    {
        inAIfElseStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getThen());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getElseSt());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAIfElseStmt(node);
    }

    public void inAWhilestmtStmt(AWhilestmtStmt node)
    {
        defaultIn(node);
    }

    public void outAWhilestmtStmt(AWhilestmtStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAWhilestmtStmt(AWhilestmtStmt node)
    {
        inAWhilestmtStmt(node);
        if(node.getCond() != null)
        {
            node.getCond().apply(this);
        }
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getBody());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outAWhilestmtStmt(node);
    }

    public void inAFunCalStmt(AFunCalStmt node)
    {
        defaultIn(node);
    }

    public void outAFunCalStmt(AFunCalStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAFunCalStmt(AFunCalStmt node)
    {
        inAFunCalStmt(node);
        if(node.getFunCal() != null)
        {
            node.getFunCal().apply(this);
        }
        outAFunCalStmt(node);
    }

    public void inAReturnstmtStmt(AReturnstmtStmt node)
    {
        defaultIn(node);
    }

    public void outAReturnstmtStmt(AReturnstmtStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAReturnstmtStmt(AReturnstmtStmt node)
    {
        inAReturnstmtStmt(node);
        if(node.getReturnexpr() != null)
        {
            node.getReturnexpr().apply(this);
        }
        outAReturnstmtStmt(node);
    }

    public void inABlockStmt(ABlockStmt node)
    {
        defaultIn(node);
    }

    public void outABlockStmt(ABlockStmt node)
    {
        defaultOut(node);
    }

    @Override
    public void caseABlockStmt(ABlockStmt node)
    {
        inABlockStmt(node);
        {
            List<PStmt> copy = new ArrayList<PStmt>(node.getBlock());
            for(PStmt e : copy)
            {
                e.apply(this);
            }
        }
        outABlockStmt(node);
    }
}
