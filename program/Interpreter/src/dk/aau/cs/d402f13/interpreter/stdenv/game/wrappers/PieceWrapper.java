package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class PieceWrapper extends Wrapper implements Piece {

  private String image;
  private boolean onBoard;
  private PlayerWrapper owner;
  private SquareWrapper square;
  
  
  public PieceWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    image = getMemberString("image");
    onBoard = getMemberBoolean("onBoard");
    owner = new PlayerWrapper(env, getMember("owner", env.playerType()));
    if (onBoard) {
      square = new SquareWrapper(env, getMember("square", env.squareType()));
    }
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
