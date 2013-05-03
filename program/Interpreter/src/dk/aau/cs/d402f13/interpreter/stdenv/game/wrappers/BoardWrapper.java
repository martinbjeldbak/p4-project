package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Board;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class BoardWrapper extends Wrapper implements Board {
  
  public BoardWrapper(GameEnvironment env, Value object) {
    super(env, object);
  }

  @Override
  public Piece[] getPieces() throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }
}
