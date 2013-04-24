package dk.aau.cs.d402f13.interpreter.stdenv.game;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.ParentCallable;
import dk.aau.cs.d402f13.interpreter.stdenv.StandardEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.AbstractTypeValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class GameEnvironment extends StandardEnvironment {

  public GameEnvironment() {
    super();
    
    final TypeValue game = new AbstractTypeValue("Game", false, "title");
    addType(game);
    
    
    final TypeValue board = new AbstractTypeValue("Board", false);
    addType(board);
    
    
    final TypeValue gridBoard = new TypeValue("GridBoard", board,
        new ParentCallable() {
          @Override
          public Value call(Interpreter interpreter) throws StandardError {
            return board.getInstance(interpreter);
          }
        }, false, "width", "height");
    addType(gridBoard);
    
    
    final TypeValue piece = new TypeValue("Piece", false, "owner");
    addType(piece);
    
    
    final TypeValue player = new TypeValue("Player", false);
    addType(player);
    
    
    final TypeValue square = new TypeValue("Square", false);
    addType(square);
  }

}
