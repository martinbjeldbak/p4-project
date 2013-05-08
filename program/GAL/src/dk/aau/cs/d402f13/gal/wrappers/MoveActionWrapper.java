package dk.aau.cs.d402f13.gal.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.AddAction;
import dk.aau.cs.d402f13.utilities.gameapi.MoveAction;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.Value;

public class MoveActionWrapper extends UnitActionWrapper implements MoveAction {

  private SquareWrapper to;
  
  public MoveActionWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    to = new SquareWrapper(env, getMember("to", env.squareType()));
  }

  @Override
  public SquareWrapper getTo() throws StandardError {
    return to;
  }
 

}
