package dk.aau.cs.d402f13.interpreter.stdenv.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.values.AbstractTypeValue;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.StrValue;
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
  private final TypeValue player = new TypeValue("Player", false, "name");
  
  private final AbstractTypeValue action = new AbstractTypeValue("Action", false);
  private final TypeValue actionSequence = new TypeValue("ActionSequence", action,
      new ParentCallable() {
        @Override
        public Value call(Interpreter interpreter) throws StandardError {
          return action.getInstance(interpreter);
        }
      }, true, "actions");
  private final AbstractTypeValue unitAction = new AbstractTypeValue("UnitAction", action,
      new ParentCallable() {
        @Override
        public Value call(Interpreter interpreter) throws StandardError {
          return action.getInstance(interpreter);
        }
      }, false, "piece");
  private final TypeValue addAction = new TypeValue("AddAction", unitAction,
      new ParentCallable() {
        @Override
        public Value call(Interpreter interpreter) throws StandardError {
          Value v = interpreter.getSymbolTable().getVariable("piece", piece);
          return unitAction.getInstance(interpreter, v);
        }
      }, false, "piece", "to");
  private final TypeValue removeAction = new TypeValue("RemoveAction", unitAction,
      new ParentCallable() {
        @Override
        public Value call(Interpreter interpreter) throws StandardError {
          Value v = interpreter.getSymbolTable().getVariable("piece", piece);
          return unitAction.getInstance(interpreter, v);
        }
      }, false, "piece");
  private final TypeValue moveAction = new TypeValue("MoveAction", unitAction,
      new ParentCallable() {
        @Override
        public Value call(Interpreter interpreter) throws StandardError {
          Value v = interpreter.getSymbolTable().getVariable("piece", piece);
          return unitAction.getInstance(interpreter, v);
        }
      }, false, "piece", "to");
  
  private final TypeValue testCase = new AbstractTypeValue("TestCase", false);
  
  public TypeValue gameType() {
    return game;
  }
  
  public TypeValue boardType() {
    return board;
  }
  
  public TypeValue squareType() {
    return square;
  }
  
  public TypeValue gridBoardType() {
    return gridBoard;
  }
  
  public TypeValue pieceType() {
    return piece;
  }
  
  public TypeValue playerType() {
    return player;
  }
  
  public TypeValue actionType() {
    return action;
  }
  
  public TypeValue actionSequenceType() {
    return actionSequence;
  }
  
  public TypeValue unitActionType() {
    return unitAction;
  }
  
  public TypeValue addActionType() {
    return addAction;
  }
  
  public TypeValue removeActionType() {
    return removeAction;
  }
  
  public TypeValue moveActionType() {
    return moveAction;
  }
  
  public TypeValue testCaseType() {
    return testCase;
  }
  
  public GameEnvironment() {
    super();


    ////////////////////////////////////
    // type: Game
    ////////////////////////////////////
    addType(game);
    
    game.addAbstractMember("players", new AbstractMember());
    game.addAbstractMember("board", new AbstractMember());
    game.addAttribute("currentPlayer", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new IntValue(0);
      }
    }));
    game.addAttribute("currentBoard", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return object.getMember("board", board);
      }
    }));
    game.addTypeMember("currentPlayer", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        int i = ((ObjectValue)object).getAttributeInt("currentPlayer");
        Value[] players = object.getMemberList("turnOrder", player, 1);
        if (i >= players.length || i < 0) {
          throw new ArgumentError("Invalid player index:  + i");
        }
        return players[i];
      }
    }));
    game.addTypeMember("currentBoard", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("currentBoard");
      }
    }));
    game.addTypeMember("turnOrder", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return object.getMember("players");
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

    board.addAttribute("pieces", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue();
      }
    }));
    board.addTypeMember("pieces", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("pieces");
      }
    }));

    
    ////////////////////////////////////
    // type: GridBoard
    ////////////////////////////////////
    addType(gridBoard);
    
    gridBoard.addAttribute("squares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        int width = interpreter.getSymbolTable().getVariableInt("width");
        int height = interpreter.getSymbolTable().getVariableInt("height");
        Value[] types = object.getMemberList("squareTypes", square, 1);
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
    gridBoard.addTypeMember("width", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("width");
      }
    }));
    gridBoard.addTypeMember("height", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("height");
      }
    }));
    gridBoard.addTypeMember("isFull", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return BoolValue.falseValue();
      }
    }));
    gridBoard.addTypeMember("squares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("squares");
      }
    }));
    gridBoard.addTypeMember("emptySquares", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return object.getMember("squares");
      }
    }));
    gridBoard.addTypeMember("squareTypes", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue(square.getInstance(interpreter));
      }
    }));
    gridBoard.addTypeMember("addPiece", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        return null;
      }
    }));
    
    ////////////////////////////////////
    // type: Piece
    ////////////////////////////////////
    addType(piece);
    
    piece.addTypeMember("owner", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("owner");
      }
    }));
    piece.addAttribute("square", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return null;
      }
    }));
    piece.addTypeMember("square", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        Value a = ((ObjectValue)object).getAttribute("square");
        if (a == null) {
          throw new ArgumentError("Piece not on board. Invalid use of member 'square'.");
        }
        return a;
      }
    }));
    piece.addAttribute("onBoard", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return BoolValue.falseValue();
      }
    }));
    piece.addTypeMember("onBoard", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("onBoard");
      }
    }));
    piece.addTypeMember("image", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new StrValue("not-implemented.png");
      }
    }));
    piece.addTypeMember("actions", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        return new ListValue();
      }
    }));
    piece.addTypeMember("move", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, square);
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        object.beginClone();
        object.setAttribute("square", actualParameters[0]);
        object.setAttribute("onBoard", BoolValue.trueValue());
        return object.endClone();
      }
    }));
    piece.addTypeMember("remove", new Member(0, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        object.beginClone();
        object.setAttribute("onBoard", BoolValue.falseValue());
        return object.endClone();
      }
    }));
    
    ////////////////////////////////////
    // type: Player
    ////////////////////////////////////
    addType(player);
    
    player.addTypeMember("name", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("name");
      }
    }));
    
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
    
    square.addAttribute("position", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new CoordValue(1, 1);
      }
    }));
    square.addAttribute("pieces", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue();
      }
    }));
    square.addTypeMember("image", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new StrValue("not-implemented.png");
      }
    }));
    square.addTypeMember("position", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("position");
      }
    }));
    square.addTypeMember("pieces", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("pieces");
      }
    }));
    square.addTypeMember("setPosition", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, CoordValue.type());
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        object.beginClone();
        object.setAttribute("position", actualParameters[0]);
        return object.endClone();
      }
    }));
    square.addTypeMember("addPiece", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, piece);
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        ListValue list = (ListValue)object.getAttributeAs("pieces", ListValue.type());
        object.beginClone();
        object.setAttribute("pieces", list.add(actualParameters[0]));
        return object.endClone();
      }
    }));
    square.addTypeMember("removePiece", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, piece);
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        ListValue list = (ListValue)object.getAttributeAs("pieces", ListValue.type());
        object.beginClone();
        object.setAttribute("pieces", list.subtract(actualParameters[0]));
        return object.endClone();
      }
    }));
    square.addTypeMember("isOccupied", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        if (((ObjectValue)object).getAttributeList("pieces").length > 0) {
          return BoolValue.trueValue();
        }
        return BoolValue.falseValue();
      }
    }));
    square.addTypeMember("isEmpty", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        if (((ObjectValue)object).getAttributeList("pieces").length == 0) {
          return BoolValue.trueValue();
        }
        return BoolValue.falseValue();
      }
    }));
    
    ////////////////////////////////////
    // type: Action
    ////////////////////////////////////
    addType(action);
    

    ////////////////////////////////////
    // type: ActionSequence
    ////////////////////////////////////
    addType(actionSequence);
    actionSequence.addTypeMember("actions", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue(interpreter.getSymbolTable().getVariableList("actions", unitAction, 1));
      }
    }));
    actionSequence.addTypeMember("addAction", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, unitAction);
        Value[] actions = interpreter.getSymbolTable().getVariableList("actions", unitAction, 1);
        Value[] actionsAfter = new Value[actions.length + 1];
        for (int i = 0; i < actions.length; i++) {
          actionsAfter[i] = actions[i];
        }
        actionsAfter[actions.length] = actualParameters[0];
        return actionSequence.getInstance(interpreter, actionsAfter);
      }
    }));

    
    ////////////////////////////////////
    // type: UnitAction
    ////////////////////////////////////
    addType(unitAction);
    unitAction.addTypeMember("piece", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("piece", piece);
      }
    }));

    
    ////////////////////////////////////
    // type: AddAction
    ////////////////////////////////////
    addType(addAction);
    addAction.addTypeMember("to", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("to", square);
      }
    }));

    
    ////////////////////////////////////
    // type: RemoveAction
    ////////////////////////////////////
    addType(removeAction);

    
    ////////////////////////////////////
    // type: MoveAction
    ////////////////////////////////////
    addType(moveAction);
    moveAction.addTypeMember("to", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return interpreter.getSymbolTable().getVariable("to", square);
      }
    }));
    
    
    ////////////////////////////////////
    // type: TestCase
    ////////////////////////////////////
    addType(testCase);
    
  }  
  
  /**
   * Find all types extending TestCase in the symbol table
   * @return A list of testCases
   */
  public List<TypeValue> findTestCases() {
    List<TypeValue> result = new ArrayList<TypeValue>();
    for (Entry<String, TypeValue> e : types.entrySet()) {
      if (e.getValue().isSubtypeOf(testCase) && !(e.getValue() instanceof AbstractTypeValue)) {
        result.add(e.getValue());
      }
    }
    return result;
  }
  
  /**
   * Find any type extending Game in the symbol table
   * @return The Game-type if it exists or null otherwise
   * @TODO Multiple game variants?
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
