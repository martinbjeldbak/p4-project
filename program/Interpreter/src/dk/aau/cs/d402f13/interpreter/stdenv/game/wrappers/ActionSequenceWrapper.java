package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.ActionSequence;
import dk.aau.cs.d402f13.utilities.gameapi.UnitAction;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class ActionSequenceWrapper extends Wrapper implements ActionSequence {

  
  public ActionSequenceWrapper(GameEnvironment env, Value object) {
    super(env, object);
    // TODO Auto-generated constructor stub
  }

  @Override
  public UnitAction[] getActions() throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ActionSequence addAction(UnitAction action) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
