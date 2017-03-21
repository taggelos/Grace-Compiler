/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.analysis;

import java.util.*;
import compiler.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
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
        node.getEOF().apply(this);
        node.getPExpr().apply(this);
        outStart(node);
    }

    public void inAFactorExpr(AFactorExpr node)
    {
        defaultIn(node);
    }

    public void outAFactorExpr(AFactorExpr node)
    {
        defaultOut(node);
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

    public void inAPlusExpr(APlusExpr node)
    {
        defaultIn(node);
    }

    public void outAPlusExpr(APlusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAPlusExpr(APlusExpr node)
    {
        inAPlusExpr(node);
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        if(node.getPlus() != null)
        {
            node.getPlus().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAPlusExpr(node);
    }

    public void inAMinusExpr(AMinusExpr node)
    {
        defaultIn(node);
    }

    public void outAMinusExpr(AMinusExpr node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAMinusExpr(AMinusExpr node)
    {
        inAMinusExpr(node);
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        if(node.getMinus() != null)
        {
            node.getMinus().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        outAMinusExpr(node);
    }

    public void inATermFactor(ATermFactor node)
    {
        defaultIn(node);
    }

    public void outATermFactor(ATermFactor node)
    {
        defaultOut(node);
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

    public void inAStarFactor(AStarFactor node)
    {
        defaultIn(node);
    }

    public void outAStarFactor(AStarFactor node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAStarFactor(AStarFactor node)
    {
        inAStarFactor(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getStar() != null)
        {
            node.getStar().apply(this);
        }
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outAStarFactor(node);
    }

    public void inASlashFactor(ASlashFactor node)
    {
        defaultIn(node);
    }

    public void outASlashFactor(ASlashFactor node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASlashFactor(ASlashFactor node)
    {
        inASlashFactor(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getSlash() != null)
        {
            node.getSlash().apply(this);
        }
        if(node.getFactor() != null)
        {
            node.getFactor().apply(this);
        }
        outASlashFactor(node);
    }

    public void inAIntegersTerm(AIntegersTerm node)
    {
        defaultIn(node);
    }

    public void outAIntegersTerm(AIntegersTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAIntegersTerm(AIntegersTerm node)
    {
        inAIntegersTerm(node);
        if(node.getIntegers() != null)
        {
            node.getIntegers().apply(this);
        }
        outAIntegersTerm(node);
    }

    public void inAExprTerm(AExprTerm node)
    {
        defaultIn(node);
    }

    public void outAExprTerm(AExprTerm node)
    {
        defaultOut(node);
    }

    @Override
    public void caseAExprTerm(AExprTerm node)
    {
        inAExprTerm(node);
        if(node.getRPar() != null)
        {
            node.getRPar().apply(this);
        }
        if(node.getExpr() != null)
        {
            node.getExpr().apply(this);
        }
        if(node.getLPar() != null)
        {
            node.getLPar().apply(this);
        }
        outAExprTerm(node);
    }
}
