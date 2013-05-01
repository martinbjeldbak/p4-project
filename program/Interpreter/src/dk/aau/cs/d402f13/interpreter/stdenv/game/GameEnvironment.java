package dk.aau.cs.d402f13.interpreter.stdenv.game;

import java.util.Map.Entry;

import dk.aau.cs.d402f13.interpreter.AbstractMember;
import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.ConstantCallable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.ParentCallable;
import dk.aau.cs.d402f13.interpreter.stdenv.StandardEnvironment;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.AbstractTypeValue;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class GameEnvironment extends StandardEnvironment {

  private final AbstractTypeValue game = new AbstractTypeValue("Game", false, "title");
  
  public GameEnvironment() {
    super();
    
    addType(game);
    
    game.addAbstractMember("players", new AbstractMember());
    game.addAbstractMember("board", new AbstractMember());
    game.addTypeMember("title", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("title");
      }
    }));
    
    
    final TypeValue board = new TypeValue("Board", false);
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
    
    piece.addTypeMember("player", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("owner");
      }
    }));
    
    
    final TypeValue player = new TypeValue("Player", false);
    addType(player);
    
    player.addTypeMember("winCondition", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        return BoolValue.falseValue();
      }
    }));
    
    player.addTypeMember("tieCondition", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        return BoolValue.falseValue();
      }
    }));
    
    player.addTypeMember("actions", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        return new ListValue();
      }
    }));
    
    
    final TypeValue square = new TypeValue("Square", false);
    addType(square);
  }
  
  /**
   * Find the a type extending Game in the symboltable
   * @return The Game-type if it exists or null otherwise
   */
  public TypeValue findGameType() {
    for (Entry<String, TypeValue> e : types.entrySet()) {
      if (e.getValue().isSubtypeOf(game) && !(e.getValue() instanceof AbstractTypeValue)) {
        return e.getValue();
      }
    }
    return null;
  }

}
