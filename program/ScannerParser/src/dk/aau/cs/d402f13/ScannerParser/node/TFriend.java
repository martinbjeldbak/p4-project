/* This file was generated by SableCC (http://www.sablecc.org/). */

package dk.aau.cs.d402f13.ScannerParser.node;

import dk.aau.cs.d402f13.ScannerParser.analysis.*;

@SuppressWarnings("nls")
public final class TFriend extends Token
{
    public TFriend()
    {
        super.setText("friend");
    }

    public TFriend(int line, int pos)
    {
        super.setText("friend");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TFriend(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFriend(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TFriend text.");
    }
}