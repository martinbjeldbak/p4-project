/* This file was generated by SableCC (http://www.sablecc.org/). */

package dk.aau.cs.d402f13.ScannerParser.node;

import java.util.*;
import dk.aau.cs.d402f13.ScannerParser.analysis.*;

@SuppressWarnings("nls")
public final class APattern extends PPattern
{
    private final LinkedList<PPatternExpr> _patternExpr_ = new LinkedList<PPatternExpr>();

    public APattern()
    {
        // Constructor
    }

    public APattern(
        @SuppressWarnings("hiding") List<?> _patternExpr_)
    {
        // Constructor
        setPatternExpr(_patternExpr_);

    }

    @Override
    public Object clone()
    {
        return new APattern(
            cloneList(this._patternExpr_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAPattern(this);
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

    @Override
    public String toString()
    {
        return ""
            + toString(this._patternExpr_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._patternExpr_.remove(child))
        {
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
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

        throw new RuntimeException("Not a child.");
    }
}
