package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface Board {
  public Piece[] getPieces() throws StandardError;
}
