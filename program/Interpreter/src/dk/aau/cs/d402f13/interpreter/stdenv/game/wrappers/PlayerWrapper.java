package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class PlayerWrapper extends Wrapper implements Player {
  
  private String name;
  
  public PlayerWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    name = getMemberString("name");
  }
  
  @Override
  public String getName() throws StandardError {
    return name;
  }

  @Override
  public boolean winCondition(Game game) throws StandardError {
    if (!(game instanceof GameWrapper)) {
      throw new InternalError("Invalid class: " + game.getClass().getSimpleName());
    }
    return callMemberBoolean("winCondition", ((GameWrapper)game).object);
  }

  @Override
  public boolean tieCondition(Game game) throws StandardError {
    if (!(game instanceof GameWrapper)) {
      throw new InternalError("Invalid class: " + game.getClass().getSimpleName());
    }
    return callMemberBoolean("tieCondition", ((GameWrapper)game).object);
  }

  @Override
  public Action[] getActions(Game game) throws StandardError {
    if (!(game instanceof GameWrapper)) {
      throw new InternalError("Invalid class: " + game.getClass().getSimpleName());
    }
    return callMemberActions("actions", ((GameWrapper)game).object);
  }

}
