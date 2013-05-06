package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.AddAction;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.Value;

public class AddActionWrapper extends UnitActionWrapper implements AddAction {

  private SquareWrapper to;
  
  public AddActionWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    to = new SquareWrapper(env, getMember("to", env.squareType()));
  }

  @Override
  public SquareWrapper getTo() throws StandardError {
    return to;
  }
 

}
