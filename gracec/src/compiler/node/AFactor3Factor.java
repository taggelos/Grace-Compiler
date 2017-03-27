/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AFactor3Factor extends PFactor
{
    private PFactor _factor_;
    private TSlash _slash_;
    private PTerm _term_;

    public AFactor3Factor()
    {
        // Constructor
    }

    public AFactor3Factor(
        @SuppressWarnings("hiding") PFactor _factor_,
        @SuppressWarnings("hiding") TSlash _slash_,
        @SuppressWarnings("hiding") PTerm _term_)
    {
        // Constructor
        setFactor(_factor_);

        setSlash(_slash_);

        setTerm(_term_);

    }

    @Override
    public Object clone()
    {
        return new AFactor3Factor(
            cloneNode(this._factor_),
            cloneNode(this._slash_),
            cloneNode(this._term_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFactor3Factor(this);
    }

    public PFactor getFactor()
    {
        return this._factor_;
    }

    public void setFactor(PFactor node)
    {
        if(this._factor_ != null)
        {
            this._factor_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._factor_ = node;
    }

    public TSlash getSlash()
    {
        return this._slash_;
    }

    public void setSlash(TSlash node)
    {
        if(this._slash_ != null)
        {
            this._slash_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._slash_ = node;
    }

    public PTerm getTerm()
    {
        return this._term_;
    }

    public void setTerm(PTerm node)
    {
        if(this._term_ != null)
        {
            this._term_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._term_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._factor_)
            + toString(this._slash_)
            + toString(this._term_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._factor_ == child)
        {
            this._factor_ = null;
            return;
        }

        if(this._slash_ == child)
        {
            this._slash_ = null;
            return;
        }

        if(this._term_ == child)
        {
            this._term_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._factor_ == oldChild)
        {
            setFactor((PFactor) newChild);
            return;
        }

        if(this._slash_ == oldChild)
        {
            setSlash((TSlash) newChild);
            return;
        }

        if(this._term_ == oldChild)
        {
            setTerm((PTerm) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}