/* This file was generated by SableCC (http://www.sablecc.org/). */

package dk.aau.cs.d402f13.ScannerParser.node;

import java.util.*;
import dk.aau.cs.d402f13.ScannerParser.analysis.*;

@SuppressWarnings("nls")
public final class ALpatxprrintPatternVal extends PPatternVal
{
    private TLPar _lPar_;
    private final LinkedList<PPatternExpr> _patternExpr_ = new LinkedList<PPatternExpr>();
    private TRPar _rPar_;
    private TInteger _integer_;

    public ALpatxprrintPatternVal()
    {
        // Constructor
    }

    public ALpatxprrintPatternVal(
        @SuppressWarnings("hiding") TLPar _lPar_,
        @SuppressWarnings("hiding") List<?> _patternExpr_,
        @SuppressWarnings("hiding") TRPar _rPar_,
        @SuppressWarnings("hiding") TInteger _integer_)
    {
        // Constructor
        setLPar(_lPar_);

        setPatternExpr(_patternExpr_);

        setRPar(_rPar_);

        setInteger(_integer_);

    }

    @Override
    public Object clone()
    {
        return new ALpatxprrintPatternVal(
            cloneNode(this._lPar_),
            cloneList(this._patternExpr_),
            cloneNode(this._rPar_),
            cloneNode(this._integer_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALpatxprrintPatternVal(this);
    }

    public TLPar getLPar()
    {
        return this._lPar_;
    }

    public void setLPar(TLPar node)
    {
        if(this._lPar_ != null)
        {
            this._lPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lPar_ = node;
    }

    public LinkedList<PPatternExpr> getPatternExpr()
    {
        return this._patternExpr_;
    }

    public void setPatternExpr(List<?> list)
    {
        for(PPatternExpr e : this._patternExpr_)
        {
            e.parent(null);
        }
        this._patternExpr_.clear();

        for(Object obj_e : list)
        {
            PPatternExpr e = (PPatternExpr) obj_e;
            if(e.parent() != null)
            {
                e.parent().removeChild(e);
            }

            e.parent(this);
            this._patternExpr_.add(e);
        }
    }

    public TRPar getRPar()
    {
        return this._rPar_;
    }

    public void setRPar(TRPar node)
    {
        if(this._rPar_ != null)
        {
            this._rPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rPar_ = node;
    }

    public TInteger getInteger()
    {
        return this._integer_;
    }

    public void setInteger(TInteger node)
    {
        if(this._integer_ != null)
        {
            this._integer_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._integer_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lPar_)
            + toString(this._patternExpr_)
            + toString(this._rPar_)
            + toString(this._integer_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lPar_ == child)
        {
            this._lPar_ = null;
            return;
        }

        if(this._patternExpr_.remove(child))
        {
            return;
        }

        if(this._rPar_ == child)
        {
            this._rPar_ = null;
            return;
        }

        if(this._integer_ == child)
        {
            this._integer_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lPar_ == oldChild)
        {
            setLPar((TLPar) newChild);
            return;
        }

        for(ListIterator<PPatternExpr> i = this._patternExpr_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set((PPatternExpr) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

        if(this._rPar_ == oldChild)
        {
            setRPar((TRPar) newChild);
            return;
        }

        if(this._integer_ == oldChild)
        {
            setInteger((TInteger) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}