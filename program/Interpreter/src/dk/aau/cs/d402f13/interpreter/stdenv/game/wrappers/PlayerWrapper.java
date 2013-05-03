package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.values.ObjectValue;

public class PlayerWrapper implements Player {
  
  private ObjectValue object;

  public PlayerWrapper(GameEnvironment env, ObjectValue object) {
    this.object = object;
  }
  
  public ObjectValue getObject() {
    return object;
  }

}
