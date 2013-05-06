package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.Value;

public class SquareWrapper extends Wrapper implements Square {

  private int x;
  private int y;
  
  public SquareWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    CoordValue coord = getMemberCoord("position");
    x = coord.getX();
    y = coord.getY();
  }
  
  @Override
  public int getX() throws StandardError {
    return x;
  }

  @Override
  public int getY() throws StandardError {
    return y;
  }

  @Override
  public Piece[] getPieces() throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getImage() throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isEmpty() throws StandardError {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isOccupied() throws StandardError {
    // TODO Auto-generated method stub
    return false;
  }

}
