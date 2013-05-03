package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface Square {
  public int getX() throws StandardError;
  public int getY() throws StandardError;
  public Piece[] getPieces() throws StandardError;
  public String getImage() throws StandardError;
}
