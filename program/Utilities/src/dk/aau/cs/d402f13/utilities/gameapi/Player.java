package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.types.Action;

public interface Player {
  public boolean winCondition(Game game) throws StandardError;
  public boolean tieCondition(Game game) throws StandardError;
  public Action[] getActions() throws StandardError;
}
