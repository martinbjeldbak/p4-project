package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface MoveAction extends UnitAction {
  public Square getTo() throws StandardError;
}
