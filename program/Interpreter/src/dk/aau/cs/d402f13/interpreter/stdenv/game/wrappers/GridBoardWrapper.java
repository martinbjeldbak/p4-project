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
    
    Value[] squares = getMemberList("squares", env.pieceType(), 1);
    this.squares = new SquareWrapper[squares.length];
    for (int i = 0; i < squares.length; i++) {
      this.squares[i] = new SquareWrapper(env, squares[i]);
    }
    
    Value[] squareTypes = getMemberList("squareTypes", env.pieceType(), 1);
    this.squareTypes = new SquareWrapper[squareTypes.length];
    for (int i = 0; i < squareTypes.length; i++) {
      this.squareTypes[i] = new SquareWrapper(env, squareTypes[i]);
    }
    
    Value[] emptySquares = getMemberList("emptySquares", env.pieceType(), 1);
    this.emptySquares = new SquareWrapper[emptySquares.length];
    for (int i = 0; i < emptySquares.length; i++) {
      this.emptySquares[i] = new SquareWrapper(env, emptySquares[i]);
    }
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
    int i = y * width + x;
    if (i < 0 || i >= squares.length) {
      throw new ArgumentError("Coordinates out of bounds");
    }
    return squares[i];
//    for (Square s : squares) {
//      if (s.getX() == x && s.getY() == y) {
//        return s;
//      }
//    }
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
