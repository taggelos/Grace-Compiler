/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import java.util.*;
import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AFunCal extends PFunCal
{
    private TIdentifier _name_;
    private final LinkedList<PExpr> _exprs_ = new LinkedList<PExpr>();

    public AFunCal()
    {
        // Constructor
    }

    public AFunCal(
        @SuppressWarnings("hiding") TIdentifier _name_,
        @SuppressWarnings("hiding") List<?> _exprs_)
    {
        // Constructor
        setName(_name_);

        setExprs(_exprs_);

    }

    @Override
    public Object clone()
    {
        return new AFunCal(
            cloneNode(this._name_),
            cloneList(this._exprs_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFunCal(this);
    }

    public TIdentifier getName()
    {
        return this._name_;
    }

    public void setName(TIdentifier node)
    {
        if(this._name_ != null)
        {
            this._name_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._name_ = node;
    }

    public LinkedList<PExpr> getExprs()
    {
        return this._exprs_;
    }

    public void setExprs(List<?> list)
    {
        for(PExpr e : this._exprs_)
        {
            e.parent(null);
        }
        this._exprs_.clear();

        for(Object obj_e : list)
        {
            PExpr e = (PExpr) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._exprs_.add(e);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._name_)
            + toString(this._exprs_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._name_ == child)
        {
            this._name_ = null;
            return;
        }

        if(this._exprs_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._name_ == oldChild)
        {
            setName((TIdentifier) newChild);
            return;
        }

        for(ListIterator<PExpr> i = this._exprs_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PExpr) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        throw new RuntimeException("Not a child.");
    }
}
