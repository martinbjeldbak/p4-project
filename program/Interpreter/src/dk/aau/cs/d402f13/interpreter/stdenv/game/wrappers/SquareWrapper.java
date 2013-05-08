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
  
  private String image;
  private boolean isEmpty;
  private boolean isOccupied;
  
  private PieceWrapper[] pieces;
  
  public SquareWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    CoordValue coord = getMemberCoord("position");
    x = coord.getX();
    y = coord.getY();
    image = getMemberString("image");
    isEmpty = getMemberBoolean("isEmpty");
    isOccupied = getMemberBoolean("isOccupied");
    Value[] pieces = getMemberList("pieces", env.pieceType(), 0);
    this.pieces = new PieceWrapper[pieces.length];
    for (int i = 0; i < pieces.length; i++) {
      this.pieces[i] = new PieceWrapper(env, pieces[i]);
    }
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
    return pieces;
  }
  
  public SquareWrapper addPiece(PieceWrapper piece) throws StandardError {
    return new SquareWrapper(env, callMember("addPiece", env.squareType(), piece.object));
  }

  @Override
  public String getImage() throws StandardError {
    return image;
  }

  @Override
  public boolean isEmpty() throws StandardError {
    return isEmpty;
  }

  @Override
  public boolean isOccupied() throws StandardError {
    return isOccupied;
  }

}
