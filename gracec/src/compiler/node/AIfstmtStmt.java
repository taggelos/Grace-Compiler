/* This file was generated by SableCC (http://www.sablecc.org/). */

package compiler.node;

import java.util.*;
import compiler.analysis.*;

@SuppressWarnings("nls")
public final class AIfstmtStmt extends PStmt
{
    private final LinkedList<PStmt> _then_ = new LinkedList<PStmt>();
    private final LinkedList<PStmt> _elsest_ = new LinkedList<PStmt>();

    public AIfstmtStmt()
    {
        // Constructor
    }

    public AIfstmtStmt(
        @SuppressWarnings("hiding") List<?> _then_,
        @SuppressWarnings("hiding") List<?> _elsest_)
    {
        // Constructor
        setThen(_then_);

        setElsest(_elsest_);

    }

    @Override
    public Object clone()
    {
        return new AIfstmtStmt(
            cloneList(this._then_),
            cloneList(this._elsest_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIfstmtStmt(this);
    }

    public LinkedList<PStmt> getThen()
    {
        return this._then_;
    }

    public void setThen(List<?> list)
    {
        for(PStmt e : this._then_)
        {
            e.parent(null);
        }
        this._then_.clear();

        for(Object obj_e : list)
        {
            PStmt e = (PStmt) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._then_.add(e);
        }
    }

    public LinkedList<PStmt> getElsest()
    {
        return this._elsest_;
    }

    public void setElsest(List<?> list)
    {
        for(PStmt e : this._elsest_)
        {
            e.parent(null);
        }
        this._elsest_.clear();

        for(Object obj_e : list)
        {
            PStmt e = (PStmt) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._elsest_.add(e);
        }
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._then_)
            + toString(this._elsest_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._then_.remove(child))
        {
            return;
        }

        if(this._elsest_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        for(ListIterator<PStmt> i = this._then_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PStmt) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        for(ListIterator<PStmt> i = this._elsest_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PStmt) newChild);
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
