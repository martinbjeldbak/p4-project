package dk.aau.cs.d402f13.utilities.gameapi;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public interface GridBoard extends Board {
  public Square[] getSquares() throws StandardError;
  public Square[] getEmptySquares() throws StandardError;
  public Square getSquareAt(int x, int y) throws StandardError;
  public Square[] getSquareTypes() throws StandardError;
  public boolean isFull() throws StandardError;
  public int getWidth() throws StandardError;
  public int getHeight() throws StandardError;
}
