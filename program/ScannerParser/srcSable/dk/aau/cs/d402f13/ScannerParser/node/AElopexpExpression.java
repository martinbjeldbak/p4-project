/* This file was generated by SableCC (http://www.sablecc.org/). */

package dk.aau.cs.d402f13.ScannerParser.node;

import dk.aau.cs.d402f13.ScannerParser.analysis.*;

@SuppressWarnings("nls")
public final class AElopexpExpression extends PExpression
{
    private PElement _element_;
    private POperator _operator_;
    private PExpression _expression_;

    public AElopexpExpression()
    {
        // Constructor
    }

    public AElopexpExpression(
        @SuppressWarnings("hiding") PElement _element_,
        @SuppressWarnings("hiding") POperator _operator_,
        @SuppressWarnings("hiding") PExpression _expression_)
    {
        // Constructor
        setElement(_element_);

        setOperator(_operator_);

        setExpression(_expression_);

    }

    @Override
    public Object clone()
    {
        return new AElopexpExpression(
            cloneNode(this._element_),
            cloneNode(this._operator_),
            cloneNode(this._expression_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAElopexpExpression(this);
    }

    public PElement getElement()
    {
        return this._element_;
    }

    public void setElement(PElement node)
    {
        if(this._element_ != null)
        {
            this._element_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._element_ = node;
    }

    public POperator getOperator()
    {
        return this._operator_;
    }

    public void setOperator(POperator node)
    {
        if(this._operator_ != null)
        {
            this._operator_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._operator_ = node;
    }

    public PExpression getExpression()
    {
        return this._expression_;
    }

    public void setExpression(PExpression node)
    {
        if(this._expression_ != null)
        {
            this._expression_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expression_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._element_)
            + toString(this._operator_)
            + toString(this._expression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._element_ == child)
        {
            this._element_ = null;
            return;
        }

        if(this._operator_ == child)
        {
            this._operator_ = null;
            return;
        }

        if(this._expression_ == child)
        {
            this._expression_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._element_ == oldChild)
        {
            setElement((PElement) newChild);
            return;
        }

        if(this._operator_ == oldChild)
        {
            setOperator((POperator) newChild);
            return;
        }

        if(this._expression_ == oldChild)
        {
            setExpression((PExpression) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
