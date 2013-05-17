package dk.aau.cs.d402f13.gal.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Board;
import dk.aau.cs.d402f13.values.Value;

public class BoardWrapper extends Wrapper implements Board {
  
  private PieceWrapper[] pieces;
  
  public BoardWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    
    Value[] pieces = getMemberList("pieces", env.pieceType(), 0);
    this.pieces = new PieceWrapper[pieces.length];
    for (int i = 0; i < pieces.length; i++) {
      this.pieces[i] = new PieceWrapper(env, pieces[i]);
    }
  }

  @Override
  public PieceWrapper[] getPieces() throws StandardError {
    return pieces;
  }
}
