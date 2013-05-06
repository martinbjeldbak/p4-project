package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.utilities.gameapi.ActionSequence;
import dk.aau.cs.d402f13.utilities.gameapi.UnitAction;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class ActionSequenceWrapper extends Wrapper implements ActionSequence {

  private UnitActionWrapper[] actions;
  
  public ActionSequenceWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    Value[] actions = getMemberList("actions", env.unitActionType(), 1);
    this.actions = new UnitActionWrapper[actions.length];
    for (int i = 0; i < actions.length; i++) {
      Value action = actions[i];
      if (action.is(env.addActionType())) {
        this.actions[i] = new AddActionWrapper(env, action);
      }
      else if (action.is(env.removeActionType())) {
        this.actions[i] = new RemoveActionWrapper(env, action);
      }
      else if (action.is(env.moveActionType())) {
        this.actions[i] = new MoveActionWrapper(env, action);
      }
      else {
        throw new TypeError("Invalid action type: " + action.getType().getName());
      }
    }
  }

  @Override
  public UnitActionWrapper[] getActions() throws StandardError {
    return actions;
  }

  @Override
  public ActionSequenceWrapper addAction(UnitAction action) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
