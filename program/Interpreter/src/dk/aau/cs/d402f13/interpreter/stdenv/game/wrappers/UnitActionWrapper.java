package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.UnitAction;
import dk.aau.cs.d402f13.values.Value;

public abstract class UnitActionWrapper extends Wrapper implements UnitAction {

  private PieceWrapper piece;
  
  public UnitActionWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    piece = new PieceWrapper(env, getMember("piece", env.pieceType()));
  }

  @Override
  public PieceWrapper getPiece() throws StandardError {
    return piece;
  }

}
