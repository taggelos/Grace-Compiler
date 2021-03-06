/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AIdBracketsLVal extends PLVal
{
    private PLVal _lVal_;
    private TLBr _lBr_;
    private PExpr _expr_;
    private TRBr _rBr_;

    public AIdBracketsLVal()
    {
        // Constructor
    }

    public AIdBracketsLVal(
        @SuppressWarnings("hiding") PLVal _lVal_,
        @SuppressWarnings("hiding") TLBr _lBr_,
        @SuppressWarnings("hiding") PExpr _expr_,
        @SuppressWarnings("hiding") TRBr _rBr_)
    {
        // Constructor
        setLVal(_lVal_);

        setLBr(_lBr_);

        setExpr(_expr_);

        setRBr(_rBr_);

    }

    @Override
    public Object clone()
    {
        return new AIdBracketsLVal(
            cloneNode(this._lVal_),
            cloneNode(this._lBr_),
            cloneNode(this._expr_),
            cloneNode(this._rBr_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIdBracketsLVal(this);
    }

    public PLVal getLVal()
    {
        return this._lVal_;
    }

    public void setLVal(PLVal node)
    {
        if(this._lVal_ != null)
        {
            this._lVal_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lVal_ = node;
    }

    public TLBr getLBr()
    {
        return this._lBr_;
    }

    public void setLBr(TLBr node)
    {
        if(this._lBr_ != null)
        {
            this._lBr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lBr_ = node;
    }

    public PExpr getExpr()
    {
        return this._expr_;
    }

    public void setExpr(PExpr node)
    {
        if(this._expr_ != null)
        {
            this._expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expr_ = node;
    }

    public TRBr getRBr()
    {
        return this._rBr_;
    }

    public void setRBr(TRBr node)
    {
        if(this._rBr_ != null)
        {
            this._rBr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rBr_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lVal_)
            + toString(this._lBr_)
            + toString(this._expr_)
            + toString(this._rBr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lVal_ == child)
        {
            this._lVal_ = null;
            return;
        }

        if(this._lBr_ == child)
        {
            this._lBr_ = null;
            return;
        }

        if(this._expr_ == child)
        {
            this._expr_ = null;
            return;
        }

        if(this._rBr_ == child)
        {
            this._rBr_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lVal_ == oldChild)
        {
            setLVal((PLVal) newChild);
            return;
        }

        if(this._lBr_ == oldChild)
        {
            setLBr((TLBr) newChild);
            return;
        }

        if(this._expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        if(this._rBr_ == oldChild)
        {
            setRBr((TRBr) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
