/* This file was generated by SableCC (http://www.sablecc.org/). */

package dk.aau.cs.d402f13.ScannerParser.node;

import java.util.*;
import dk.aau.cs.d402f13.ScannerParser.analysis.*;

@SuppressWarnings("nls")
public final class AAssignment extends PAssignment
{
    private TLet _let_;
    private TVariable _variable_;
    private TEql _eql_;
    private PExpression _left_;
    private final LinkedList<PVarExpr> _varExpr_ = new LinkedList<PVarExpr>();
    private TIn _in_;
    private PExpression _right_;

    public AAssignment()
    {
        // Constructor
    }

    public AAssignment(
        @SuppressWarnings("hiding") TLet _let_,
        @SuppressWarnings("hiding") TVariable _variable_,
        @SuppressWarnings("hiding") TEql _eql_,
        @SuppressWarnings("hiding") PExpression _left_,
        @SuppressWarnings("hiding") List<?> _varExpr_,
        @SuppressWarnings("hiding") TIn _in_,
        @SuppressWarnings("hiding") PExpression _right_)
    {
        // Constructor
        setLet(_let_);

        setVariable(_variable_);

        setEql(_eql_);

        setLeft(_left_);

        setVarExpr(_varExpr_);

        setIn(_in_);

        setRight(_right_);

    }

    @Override
    public Object clone()
    {
        return new AAssignment(
            cloneNode(this._let_),
            cloneNode(this._variable_),
            cloneNode(this._eql_),
            cloneNode(this._left_),
            cloneList(this._varExpr_),
            cloneNode(this._in_),
            cloneNode(this._right_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAssignment(this);
    }

    public TLet getLet()
    {
        return this._let_;
    }

    public void setLet(TLet node)
    {
        if(this._let_ != null)
        {
            this._let_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._let_ = node;
    }

    public TVariable getVariable()
    {
        return this._variable_;
    }

    public void setVariable(TVariable node)
    {
        if(this._variable_ != null)
        {
            this._variable_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._variable_ = node;
    }

    public TEql getEql()
    {
        return this._eql_;
    }

    public void setEql(TEql node)
    {
        if(this._eql_ != null)
        {
            this._eql_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._eql_ = node;
    }

    public PExpression getLeft()
    {
        return this._left_;
    }

    public void setLeft(PExpression node)
    {
        if(this._left_ != null)
        {
            this._left_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._left_ = node;
    }

    public LinkedList<PVarExpr> getVarExpr()
    {
        return this._varExpr_;
    }

    public void setVarExpr(List<?> list)
    {
        for(PVarExpr e : this._varExpr_)
        {
            e.parent(null);
        }
        this._varExpr_.clear();

        for(Object obj_e : list)
        {
            PVarExpr e = (PVarExpr) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._varExpr_.add(e);
        }
    }

    public TIn getIn()
    {
        return this._in_;
    }

    public void setIn(TIn node)
    {
        if(this._in_ != null)
        {
            this._in_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._in_ = node;
    }

    public PExpression getRight()
    {
        return this._right_;
    }

    public void setRight(PExpression node)
    {
        if(this._right_ != null)
        {
            this._right_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._right_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._let_)
            + toString(this._variable_)
            + toString(this._eql_)
            + toString(this._left_)
            + toString(this._varExpr_)
            + toString(this._in_)
            + toString(this._right_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._let_ == child)
        {
            this._let_ = null;
            return;
        }

        if(this._variable_ == child)
        {
            this._variable_ = null;
            return;
        }

        if(this._eql_ == child)
        {
            this._eql_ = null;
            return;
        }

        if(this._left_ == child)
        {
            this._left_ = null;
            return;
        }

        if(this._varExpr_.remove(child))
        {
            return;
        }

        if(this._in_ == child)
        {
            this._in_ = null;
            return;
        }

        if(this._right_ == child)
        {
            this._right_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._let_ == oldChild)
        {
            setLet((TLet) newChild);
            return;
        }

        if(this._variable_ == oldChild)
        {
            setVariable((TVariable) newChild);
            return;
        }

        if(this._eql_ == oldChild)
        {
            setEql((TEql) newChild);
            return;
        }

        if(this._left_ == oldChild)
        {
            setLeft((PExpression) newChild);
            return;
        }

        for(ListIterator<PVarExpr> i = this._varExpr_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PVarExpr) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._in_ == oldChild)
        {
            setIn((TIn) newChild);
            return;
        }

        if(this._right_ == oldChild)
        {
            setRight((PExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}