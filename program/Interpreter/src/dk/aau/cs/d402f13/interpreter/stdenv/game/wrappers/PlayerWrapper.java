package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class PlayerWrapper extends Wrapper implements Player {
  
  public PlayerWrapper(GameEnvironment env, Value object) {
    super(env, object);
  }

  @Override
  public boolean winCondition(Game game) throws StandardError {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean tieCondition(Game game) throws StandardError {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Action[] getActions() throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
