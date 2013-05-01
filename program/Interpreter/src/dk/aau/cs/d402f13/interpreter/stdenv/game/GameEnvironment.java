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
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.values.AbstractTypeValue;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class GameEnvironment extends StandardEnvironment {

  private final AbstractTypeValue game = new AbstractTypeValue("Game", false, "title");
  private final TypeValue board = new TypeValue("Board", false);
  private final TypeValue square = new TypeValue("Square", false);
  private final TypeValue gridBoard = new TypeValue("GridBoard", board,
      new ParentCallable() {
        @Override
        public Value call(Interpreter interpreter) throws StandardError {
          return board.getInstance(interpreter);
        }
      }, false, "width", "height");
  private final TypeValue piece = new TypeValue("Piece", false, "owner");
  private final TypeValue player = new TypeValue("Player", false);
  
  public GameEnvironment() {
    super();


    ////////////////////////////////////
    // type: Game
    ////////////////////////////////////
    addType(game);
    
    game.addAbstractMember("players", new AbstractMember());
    game.addAbstractMember("board", new AbstractMember());
    game.addTypeMember("turnOrder", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return object.getMember("players").getValue(interpreter);
      }
    }));
    game.addTypeMember("title", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("title");
      }
    }));
    game.addTypeMember("findSquares", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        TypeValue.expect(actualParameters, 0, PatternValue.type());
        return new ListValue();
      }
    }));
    
    ////////////////////////////////////
    // type: Board
    ////////////////////////////////////
    addType(board);
    
    board.addTypeMember("squares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue();
      }
    }));
    board.addTypeMember("isFull", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return BoolValue.falseValue();
      }
    }));
    board.addTypeMember("emptySquares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return object.getMember("squares").getValue(interpreter);
      }
    }));
    
    ////////////////////////////////////
    // type: GridBoard
    ////////////////////////////////////
    addType(gridBoard);
    
    gridBoard.addAttribute("squares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        int width = ((IntValue)TypeValue.expect(
            interpreter.getSymbolTable().getVariable("width"),
            IntValue.type()
        )).getValue();
        int height = ((IntValue)TypeValue.expect(
            interpreter.getSymbolTable().getVariable("height"),
            IntValue.type()
        )).getValue(); 
        Value[] types = ((ListValue)TypeValue.expect(
            object.getMember("squareTypes").getValue(interpreter),
            ListValue.type()
        )).getValues();
        if (types.length < 1) {
          throw new TypeError("Invalid length of squareTypes-list");
        }
        TypeValue.expect(square, types); // Expect all elements of array to be of type Square
        int size = width * height;
        Value[] squares = new Value[size];
        int x = 0, y = 0, numTypes = types.length;
        for (int i = 0; i < size; i++) {
          squares[i] = types[(x + y) % numTypes];
          x++;
          if (x >= width) {
            x = 0;
            y++;
          }
        }
        return new ListValue(squares);
      }
    }));
    gridBoard.addTypeMember("squares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("squares");
      }
    }));
    gridBoard.addTypeMember("squareTypes", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue(square.getInstance(interpreter));
      }
    }));
    
    ////////////////////////////////////
    // type: Piece
    ////////////////////////////////////
    addType(piece);
    
    piece.addTypeMember("player", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("owner");
      }
    }));
    
    ////////////////////////////////////
    // type: Player
    ////////////////////////////////////
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
    
    ////////////////////////////////////
    // type: Square
    ////////////////////////////////////
    addType(square);
  }
  
  /**
   * Find any type extending Game in the symbol table
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
