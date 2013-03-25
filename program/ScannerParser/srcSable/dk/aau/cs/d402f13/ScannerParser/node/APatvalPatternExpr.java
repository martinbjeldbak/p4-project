/* This file was generated by SableCC (http://www.sablecc.org/). */

package dk.aau.cs.d402f13.ScannerParser.node;

import dk.aau.cs.d402f13.ScannerParser.analysis.*;

@SuppressWarnings("nls")
public final class APatvalPatternExpr extends PPatternExpr
{
    private PPatternVal _patternVal_;
    private PStarQmarkPlus _starQmarkPlus_;

    public APatvalPatternExpr()
    {
        // Constructor
    }

    public APatvalPatternExpr(
        @SuppressWarnings("hiding") PPatternVal _patternVal_,
        @SuppressWarnings("hiding") PStarQmarkPlus _starQmarkPlus_)
    {
        // Constructor
        setPatternVal(_patternVal_);

        setStarQmarkPlus(_starQmarkPlus_);

    }

    @Override
    public Object clone()
    {
        return new APatvalPatternExpr(
            cloneNode(this._patternVal_),
            cloneNode(this._starQmarkPlus_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPatvalPatternExpr(this);
    }

    public PPatternVal getPatternVal()
    {
        return this._patternVal_;
    }

    public void setPatternVal(PPatternVal node)
    {
        if(this._patternVal_ != null)
        {
            this._patternVal_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._patternVal_ = node;
    }

    public PStarQmarkPlus getStarQmarkPlus()
    {
        return this._starQmarkPlus_;
    }

    public void setStarQmarkPlus(PStarQmarkPlus node)
    {
        if(this._starQmarkPlus_ != null)
        {
            this._starQmarkPlus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._starQmarkPlus_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._patternVal_)
            + toString(this._starQmarkPlus_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._patternVal_ == child)
        {
            this._patternVal_ = null;
            return;
        }

        if(this._starQmarkPlus_ == child)
        {
            this._starQmarkPlus_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._patternVal_ == oldChild)
        {
            setPatternVal((PPatternVal) newChild);
            return;
        }

        if(this._starQmarkPlus_ == oldChild)
        {
            setStarQmarkPlus((PStarQmarkPlus) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
