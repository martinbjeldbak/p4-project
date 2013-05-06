package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface UnitAction extends Action {
  public Piece getPiece() throws StandardError;
}
