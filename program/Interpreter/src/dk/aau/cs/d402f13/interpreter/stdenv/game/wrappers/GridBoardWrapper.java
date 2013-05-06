package dk.aau.cs.d402f13.interpreter.stdenv.game.wrappers;

import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Board;
import dk.aau.cs.d402f13.utilities.gameapi.GridBoard;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.Value;

public class GridBoardWrapper extends BoardWrapper implements GridBoard {
  
  private Square[] squares;
  private Square[] emptySquares;
  private Square[] squareTypes;
  
  private boolean isFull;

  private int width;
  private int height;
  
  public GridBoardWrapper(GameEnvironment env, Value object) throws StandardError {
    super(env, object);
    
    width = getMemberInt("width");
    height = getMemberInt("height");
    isFull = getMemberBoolean("isFull");
  }

  @Override
  public Square[] getSquares() throws StandardError {
    return squares;
  }

  @Override
  public Square[] getEmptySquares() throws StandardError {
    return emptySquares;
  }

  @Override
  public Square getSquareAt(int x, int y) throws StandardError {
    // faster: i = y * width + x
    for (Square s : squares) {
      if (s.getX() == x && s.getY() == y) {
        return s;
      }
    }
    throw new ArgumentError("Coordinates out of bounds");
  }

  @Override
  public Square[] getSquareTypes() throws StandardError {
    return squareTypes;
  }

  @Override
  public boolean isFull() throws StandardError {
    return isFull;
  }

  @Override
  public int getWidth() throws StandardError {
    return width;
  }

  @Override
  public int getHeight() throws StandardError {
    return height;
  }
}
