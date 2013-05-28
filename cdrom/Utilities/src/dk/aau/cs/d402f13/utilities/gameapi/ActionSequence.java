package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface ActionSequence extends Action {
  public UnitAction[] getActions() throws StandardError;
  public ActionSequence addAction(UnitAction action) throws StandardError;
}
