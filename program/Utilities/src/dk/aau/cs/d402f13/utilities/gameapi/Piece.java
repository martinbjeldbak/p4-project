package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface Piece {
  public String getImage() throws StandardError;
  public Player getOwner() throws StandardError;
  public int getX() throws StandardError;
  public int getY() throws StandardError;
  public boolean isOnBoard() throws StandardError;
  public Action[] getActions(Game game) throws StandardError;
}
