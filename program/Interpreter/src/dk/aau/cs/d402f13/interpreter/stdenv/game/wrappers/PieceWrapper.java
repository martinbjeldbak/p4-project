package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.values.Value;

public class PieceWrapper extends Wrapper implements Piece {

  private String image;
  private PlayerWrapper owner;
  private SquareWrapper square;
  
  private boolean onBoard;
  
  public PieceWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    image = getMemberString("image");
  }
  
  @Override
  public String getImage() throws StandardError {
    return image;
  }

  @Override
  public PlayerWrapper getOwner() throws StandardError {
    return owner;
  }

  @Override
  public SquareWrapper getSquare() throws StandardError {
    return square;
  }

  @Override
  public boolean isOnBoard() throws StandardError {
    return onBoard;
  }

  @Override
  public Action[] getActions() throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
