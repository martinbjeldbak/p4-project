package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class PieceWrapper extends Wrapper implements Piece {

  private String image;
  private boolean onBoard;
  private PlayerWrapper owner;
  private int x;
  private int y;
  
  
  public PieceWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    image = getMemberString("image");
    onBoard = getMemberBoolean("onBoard");
    owner = new PlayerWrapper(env, getMember("owner", env.playerType()));
    if (onBoard) {
      CoordValue coord = getMemberCoord("position");
      x = coord.getX();
      y = coord.getY();
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
  public int getX() throws StandardError {
    return x;
  }
  
  @Override
  public int gety() throws StandardError {
    return y;
  }
  
  public PieceWrapper setPosition(int x, int y) throws StandardError {
    return new PieceWrapper(env, callMember("setPosition", env.pieceType(), new CoordValue(x, y)));
  }

  @Override
  public boolean isOnBoard() throws StandardError {
    return onBoard;
  }

  @Override
  public Action[] getActions(Game game) throws StandardError {
    if (!(game instanceof GameWrapper)) {
      throw new InternalError("Invalid class: " + game.getClass().getSimpleName());
    }
    return callMemberActions("actions", ((GameWrapper)game).object);
  }

}
