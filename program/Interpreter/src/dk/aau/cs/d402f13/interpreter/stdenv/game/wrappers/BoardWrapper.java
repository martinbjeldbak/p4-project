package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.types.Board;
import dk.aau.cs.d402f13.values.ObjectValue;

public class BoardWrapper extends Board {

  private ObjectValue object;
  
  public ObjectValue getObject() {
    return object;
  }
  
  public BoardWrapper(GameEnvironment env, ObjectValue object) {
    this.object = object;
  }
}
