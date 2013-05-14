package dk.aau.cs.d402f13.interpreter.stdenv.game;

import java.io.InputStream;
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
import dk.aau.cs.d402f13.values.FunValue;
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
  
  private ObjectValue applyUnitAction(ObjectValue gameState, ObjectValue actionObject, Interpreter interpreter) throws StandardError {
    ObjectValue pieceObject = (ObjectValue)actionObject.getMember("piece", piece);
    ObjectValue currentBoard = (ObjectValue)gameState.getMember("board", gridBoard);
    if (actionObject.is(addAction)) {
      ObjectValue toObject = (ObjectValue)actionObject.getMember("to", square);
      CoordValue position = toObject.getMemberCoord("position");
      currentBoard = (ObjectValue)currentBoard.callMember(
          "addPiece", gridBoard, interpreter, pieceObject, position);
    }
    else if (actionObject.is(removeAction)) {
      currentBoard = (ObjectValue)currentBoard.callMember(
          "removePiece", gridBoard, interpreter, pieceObject);
    }
    else if (actionObject.is(moveAction)) {
      ObjectValue toObject = (ObjectValue)actionObject.getMember("to", square);
      CoordValue position = toObject.getMemberCoord("position");
      currentBoard = (ObjectValue)currentBoard.callMember(
          "movePiece", gridBoard, interpreter, pieceObject, position);
    }
    else {
      throw new TypeError("Unknown action type: " + actionObject.getType().getName());
    }
    return (ObjectValue)gameState.callMember("setBoard", game, interpreter, currentBoard);
  }
  
  private ObjectValue undoUnitAction(ObjectValue gameState, ObjectValue actionObject, Interpreter interpreter) throws StandardError {
    ObjectValue pieceObject = (ObjectValue)actionObject.getMember("piece", piece);
    ObjectValue currentBoard = (ObjectValue)gameState.getMember("board", gridBoard);
    if (actionObject.is(addAction)) {
      ObjectValue toObject = (ObjectValue)actionObject.getMember("to", square);
      CoordValue position = toObject.getMemberCoord("position");
      pieceObject = (ObjectValue)pieceObject.callMember("move", piece, interpreter, position);
      currentBoard = (ObjectValue)currentBoard.callMember(
          "removePiece", gridBoard, interpreter, pieceObject);
    }
    else if (actionObject.is(removeAction)) {
      CoordValue position = pieceObject.getMemberCoord("position");
      currentBoard = (ObjectValue)currentBoard.callMember(
          "addPiece", gridBoard, interpreter, pieceObject, position);
    }
    else if (actionObject.is(moveAction)) {
      ObjectValue toObject = (ObjectValue)actionObject.getMember("to", square);
      CoordValue position1 = toObject.getMemberCoord("position");
      CoordValue position2 = pieceObject.getMemberCoord("position");
      pieceObject = (ObjectValue)pieceObject.callMember("move", piece, interpreter, position1);
      currentBoard = (ObjectValue)currentBoard.callMember(
          "movePiece", gridBoard, interpreter, pieceObject, position2);
    }
    else {
      throw new TypeError("Unknown action type: " + actionObject.getType().getName());
    }
    return (ObjectValue)gameState.setAttribute("board", currentBoard);
  }
  
  public GameEnvironment() {
    super();


    ////////////////////////////////////
    // type: Game
    ////////////////////////////////////
    addType(game);
    
    game.addAbstractMember("players", new AbstractMember());
    game.addAbstractMember("initialBoard", new AbstractMember());
    game.addAttribute("currentPlayer", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new IntValue(0);
      }
    }));
    game.addAttribute("board", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return object.getMember("initialBoard", board);
      }
    }));
    game.addAttribute("history", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue();
      }
    }));
    game.addSetter("currentPlayer", IntValue.type());
    game.addSetter("board", gridBoard);
    game.addSetter("history", ListValue.type());
    game.addTypeMember("history", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("history");
      }
    }));
    game.addTypeMember("currentPlayer", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        int i = ((ObjectValue)object).getAttributeInt("currentPlayer");
        Value[] players = object.getMemberList("turnOrder", player, 1);
        if (i >= players.length || i < 0) {
          throw new ArgumentError("Invalid player index: " + i);
        }
        return players[i];
      }
    }));
    game.addTypeMember("nextTurn", new Member(0, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        int i = ((ObjectValue)object).getAttributeInt("currentPlayer");
        Value[] players = object.getMemberList("turnOrder", player, 1);
        i = (i + 1) % players.length;
        return object.setAttribute("currentPlayer", new IntValue(i));
      }
    }));
    game.addTypeMember("board", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return ((ObjectValue)object).getAttribute("board");
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
    game.addTypeMember("description", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new StrValue("A board game.");
      }
    }));
    game.addTypeMember("matchSquare", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        CoordValue position = (CoordValue)TypeValue.expect(actualParameters, 0, CoordValue.type());
        PatternValue pattern = (PatternValue)TypeValue.expect(actualParameters, 1, PatternValue.type());
        /** @TODO Missing patterns!!! */
        return BoolValue.falseValue();
      }
    }));
    game.addTypeMember("matchSquares", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        Value[] positions = ((ListValue)TypeValue.expect(actualParameters, 0, ListValue.type())).getValues();
        PatternValue pattern = (PatternValue)TypeValue.expect(actualParameters, 1, PatternValue.type());
        positions = TypeValue.expect(CoordValue.type(), positions);
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        for (Value pos : positions) {
          BoolValue b = (BoolValue)object.callMemberAs(
              "matchSquare", BoolValue.type(), interpreter, pos, pattern);
          if (b == BoolValue.falseValue()) {
            return BoolValue.falseValue();
          }
        }
        return BoolValue.trueValue();
      }
    }));
    game.addTypeMember("findSquares", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        PatternValue pattern = (PatternValue)TypeValue.expect(actualParameters, 0, PatternValue.type());
        ObjectValue gameState = (ObjectValue)interpreter.getSymbolTable().getThis();
        ObjectValue boardState = (ObjectValue)gameState.getMember("board", board);
        Value[] squares = boardState.getMemberList("squares", square, 1);
        List<Value> matches = new ArrayList<Value>();
        for (Value s : squares) {
          CoordValue pos = ((ObjectValue)s).getMemberCoord("position");
          BoolValue b = (BoolValue)gameState.callMemberAs(
              "matchSquare", BoolValue.type(), interpreter, pos, pattern);
          if (b == BoolValue.trueValue()) {
            matches.add(s);
          }
        }
        return new ListValue(matches);
      }
    }));
    game.addTypeMember("findSquaresIn", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        Value[] positions = ((ListValue)TypeValue.expect(actualParameters, 0, ListValue.type())).getValues();
        PatternValue pattern = (PatternValue)TypeValue.expect(actualParameters, 1, PatternValue.type());
        ObjectValue gameState = (ObjectValue)interpreter.getSymbolTable().getThis();
        ObjectValue boardState = (ObjectValue)gameState.getMember("board", board);
        positions = TypeValue.expect(CoordValue.type(), positions);
        List<Value> matches = new ArrayList<Value>();
        for (Value pos : positions) {
          BoolValue b = (BoolValue)gameState.callMemberAs(
              "matchSquare", BoolValue.type(), interpreter, pos, pattern);
          if (b == BoolValue.trueValue()) {
            matches.add(boardState.callMember("squareAt", square, interpreter, pos));
          }
        }
        return new ListValue(matches);
      }
    }));
    game.addTypeMember("applyAction", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        TypeValue.expect(actualParameters, 0, action);
        ObjectValue actionObject = (ObjectValue)actualParameters[0];
        ListValue history = (ListValue)object.getAttributeAs("history", ListValue.type());
        history = (ListValue)history.add(actionObject); 
        object = (ObjectValue)object.setAttribute("history", history);
        object = (ObjectValue)object.callMember("nextTurn", game, interpreter);
        if (actionObject.is(actionSequence)) {
          Value[] sequence = object.getMemberList("actions", action, 1);
          for (Value a : sequence) {
            if (a.is(unitAction)) {
              object = applyUnitAction(object, (ObjectValue)a, interpreter);
            }
            else {
              throw new TypeError("Invalid action encountered");
            }
          }
          return object;
        }
        else {
          return applyUnitAction(object, actionObject, interpreter);
        }
      }
    }));
    game.addTypeMember("undoAction", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        TypeValue.expect(actualParameters, 0, action);
        ObjectValue actionObject = (ObjectValue)actualParameters[0];
        if (actionObject.is(actionSequence)) {
          Value[] sequence = object.getMemberList("actions", action, 1);
          for (int i = sequence.length - 1; i >= 0; i--) {
            Value a = sequence[i];
            object = undoUnitAction(object, (ObjectValue)a, interpreter);
          }
          return object;
        }
        else {
          return undoUnitAction(object, actionObject, interpreter);
        }
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
    board.addSetter("pieces", ListValue.type());
//    board.addTypeMember("setPieces", new Member(1, false, new Callable() {
//      @Override
//      public Value call(Interpreter interpreter, Value... actualParameters)
//          throws StandardError {
//        Value[] pieces = ((ListValue)TypeValue.expect(actualParameters, 0, ListValue.type())).getValues();
//        TypeValue.expect(piece, pieces);
//        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
//        return object.setAttribute("pieces", actualParameters[0]);
//      }
//    }));

    
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
          squares[i] = squares[i].callMember("setPosition", square, interpreter, new CoordValue(x + 1, y + 1));
          x++;
          if (x >= width) {
            x = 0;
            y++;
          }
        }
        return new ListValue(squares);
      }
    }));
    gridBoard.addSetter("squares", ListValue.type());
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
        if (object.getMemberList("emptySquares", square).length > 0) {
          return BoolValue.falseValue();
        }
        return BoolValue.trueValue();
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
        Value[] squares = object.getMemberList("squares", square, 1);
        ArrayList<Value> emptySquares = new ArrayList<Value>();
        for (Value s : squares) {
          if (s.getMemberBoolean("isEmpty")) {
            emptySquares.add(s);
          }
        }
        return new ListValue(emptySquares);
      }
    }));
    gridBoard.addTypeMember("squareTypes", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return new ListValue(square.getInstance(interpreter));
      }
    }));
    gridBoard.addTypeMember("squareAt", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        CoordValue pos = (CoordValue)TypeValue.expect(actualParameters, 0, CoordValue.type());
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        int x = pos.getX();
        int y = pos.getY();
        int width = object.getMemberInt("width");
        int height = object.getMemberInt("height");
        if (x < 1 || x > width || y < 1 || y > height) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        int size = width * height;
        Value[] squares = object.getMemberList("squares", square, size);
        int i = (y - 1) * width + (x - 1);
        if (i < 0 || i >= size) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        return squares[(y - 1) * width + (x - 1)];
      }
    }));
    gridBoard.addTypeMember("setSquaresAt", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        TypeValue.expect(actualParameters, 0, square);
        ObjectValue sq = (ObjectValue)actualParameters[0];
        Value[] positions = ((ListValue)TypeValue.expect(actualParameters, 1, ListValue.type())).getValues();
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        int width = object.getMemberInt("width");
        int height = object.getMemberInt("height");
        int size = width * height;
        Value[] squares = object.getMemberList("squares", square, size);
        for (Value coord : positions) {
          if (!coord.is(CoordValue.type())) {
            throw new TypeError("Invalid element type in list for 'setSquares', expected Coordinate");
          }
          CoordValue pos = (CoordValue)coord.as(CoordValue.type()); 
          int x = pos.getX();
          int y = pos.getY();
          int i = (y - 1) * width + (x - 1);
          sq = (ObjectValue)sq.callMember("setPosition", square, interpreter, coord);
          squares[i] = sq;
        }
        object = (ObjectValue)object.setAttribute("squares", new ListValue(squares));
        return object;
      }
    }));
    gridBoard.addTypeMember("addPiece", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, piece);
        ObjectValue p = (ObjectValue)actualParameters[0]; 
        CoordValue pos = (CoordValue)TypeValue.expect(actualParameters, 1, CoordValue.type());
        p = (ObjectValue)p.callMember("move", piece, interpreter, pos);
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        int x = pos.getX();
        int y = pos.getY();
        int width = object.getMemberInt("width");
        int height = object.getMemberInt("height");
        if (x < 1 || x > width || y < 1 || y > height) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        int size = width * height;
        Value[] squares = object.getMemberList("squares", square, size);
        Value[] newList = new Value[size];
        int target = (y - 1) * width + (x - 1);
        if (target < 0 || target >= size) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        for (int i = 0; i < newList.length; i++) {
          if (i == target) {
            newList[i] = ((ObjectValue)squares[i])
                .callMember("addPiece", square, interpreter, p);
          }
          else {
            newList[i] = squares[i];
          }
        }
        object = (ObjectValue)object.setAttribute("squares", new ListValue(newList));
        Value[] pieces = object.getMemberList("pieces", piece);
        newList = new Value[pieces.length + 1];
        for  (int i = 0; i < pieces.length; i++) {
          newList[i] = pieces[i];
        }
        newList[newList.length - 1] = p;
        object = (ObjectValue)object.callMember("setPieces", gridBoard, interpreter, new ListValue(newList));
        return object;
      }
    }));
    gridBoard.addTypeMember("addPieces", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, piece);
        ObjectValue p = (ObjectValue)actualParameters[0];
        Value[] positions = ((ListValue)TypeValue.expect(actualParameters, 1, ListValue.type())).getValues();
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        for (Value coord : positions) {
          if (!coord.is(CoordValue.type())) {
            throw new TypeError("Invalid element type in list for 'addPieces', expected Coordinate");
          }
          object = (ObjectValue)object.callMember("addPiece", gridBoard, interpreter, p, coord);
        }
        return object;
      }
    }));
    gridBoard.addTypeMember("removePiece", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, piece);
        ObjectValue p = (ObjectValue)actualParameters[0]; 
        CoordValue pos = p.getMemberCoord("position");
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        int x = pos.getX();
        int y = pos.getY();
        int width = object.getMemberInt("width");
        int height = object.getMemberInt("height");
        if (x < 1 || x > width || y < 1 || y > height) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        int size = width * height;
        Value[] squares = object.getMemberList("squares", square, size);
        Value[] newList = new Value[size];
        int target = (y - 1) * width + (x - 1);
        if (target < 0 || target >= size) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        for (int i = 0; i < newList.length; i++) {
          if (i == target) {
            newList[i] = ((ObjectValue)squares[i])
                .callMember("removePiece", square, interpreter, p);
          }
          else {
            newList[i] = squares[i];
          }
        }
        object = (ObjectValue)object.setAttribute("squares", new ListValue(newList));
        Value[] pieces = object.getMemberList("pieces", piece);
        newList = new Value[pieces.length - 1];
        for  (int i = 0; i < pieces.length - 1; i++) {
          if (pieces[i].equals(p)) {
            newList[i] = p.callMember("remove", piece, interpreter);
          }
          else {
            newList[i] = pieces[i];
          }
        }
        object = (ObjectValue)object.callMember("setPieces", gridBoard, interpreter, new ListValue(newList));
        return object;
      }
    }));
    gridBoard.addTypeMember("movePiece", new Member(2, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters) throws StandardError {
        TypeValue.expect(actualParameters, 0, piece);
        ObjectValue p = (ObjectValue)actualParameters[0];
        CoordValue pos1 = p.getMemberCoord("position");
        CoordValue pos2 = (CoordValue)TypeValue.expect(actualParameters, 1, CoordValue.type());
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        ObjectValue p2 = (ObjectValue)p.callMember("move", piece, interpreter, pos2);
        int x1 = pos1.getX();
        int y1 = pos1.getY();
        int x2 = pos2.getX();
        int y2 = pos2.getY();
        int width = object.getMemberInt("width");
        int height = object.getMemberInt("height");
        if (x1 < 1 || x1 > width || y1 < 1 || y1 > height) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        if (x2 < 1 || x2 > width || y2 < 1 || y2 > height) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        int size = width * height;
        Value[] squares = object.getMemberList("squares", square, size);
        Value[] newList = new Value[size];
        int target1 = (y1 - 1) * width + (x1 - 1);
        int target2 = (y2 - 1) * width + (x2 - 1);
        if (target1 < 0 || target1 >= size
            || target2 < 0 || target2 >= size) {
          throw new ArgumentError("Coordinate out of bounds");
        }
        for (int i = 0; i < newList.length; i++) {
          if (i == target1) {
            newList[i] = ((ObjectValue)squares[i])
                .callMember("removePiece", square, interpreter, p);
          }
          else if (i == target2) {
            newList[i] = ((ObjectValue)squares[i])
                .callMember("addPiece", square, interpreter, p2);
          }
          else {
            newList[i] = squares[i];
          }
        }
        object = (ObjectValue)object.setAttribute("squares", new ListValue(newList));
        Value[] pieces = object.getMemberList("pieces", piece);
        newList = new Value[pieces.length - 1];
        for  (int i = 0; i < pieces.length; i++) {
          if (pieces[i].equals(p)) {
            newList[i] = p2;
          }
          else {
            newList[i] = pieces[i];
          }
        }
        object = (ObjectValue)object.callMember("setPieces", gridBoard, interpreter, new ListValue(newList));
        return object;
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
    piece.addAttribute("position", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        return null;
      }
    }));
    piece.addTypeMember("position", new Member(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        Value a = ((ObjectValue)object).getAttribute("position");
        if (a == null) {
          throw new ArgumentError("Piece not on board. Invalid use of member 'position'.");
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
        ObjectValue owner = (ObjectValue)object.getMember("owner", player);
        String ownerName = owner.getMemberString("name");
        String pieceName = ((ObjectValue)object).getSubType().getName();
        return new StrValue(ownerName + "_" + pieceName + ".png");
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
        TypeValue.expect(actualParameters, 0, CoordValue.type());
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        object.beginClone();
        object.setAttribute("position", actualParameters[0]);
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
        String squareName = ((ObjectValue)object).getSubType().getName();
        return new StrValue(squareName + ".png");
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
    square.addTypeMember("setPieces", new Member(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        Value[] pieces = ((ListValue)TypeValue.expect(actualParameters, 0, ListValue.type())).getValues();
        TypeValue.expect(piece, pieces);
        ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
        return object.setAttribute("pieces", actualParameters[0]);
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
    
    ////////////////////////////////////
    // Global functions
    ////////////////////////////////////
    
    addConstant("addActions", new FunValue(
      2, false,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          TypeValue.expect(actualParameters, 0, piece);
          ObjectValue p = (ObjectValue)actualParameters[0];
          Value[] positions = ((ListValue)TypeValue.expect(actualParameters, 1, ListValue.type())).getValues();
          Value[] actions = new Value[positions.length];
          for (int i = 0; i < positions.length; i++) {
            Value position = positions[i];
            if (position.is(square)) {
              actions[i] = addAction.getInstance(interpreter, p, position);
            }
            else {
              throw new TypeError("Invalid element type in list for 'addActions', expected Square");
            }
          }
          return new ListValue(actions); 
        }
      }
    ));
    addConstant("moveActions", new FunValue(
      2, false,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          TypeValue.expect(actualParameters, 0, piece);
          ObjectValue p = (ObjectValue)actualParameters[0];
          Value[] positions = ((ListValue)TypeValue.expect(actualParameters, 1, ListValue.type())).getValues();
          Value[] actions = new Value[positions.length];
          for (int i = 0; i < positions.length; i++) {
            Value position = positions[i];
            if (position.is(square)) {
              actions[i] = moveAction.getInstance(interpreter, p, position);
            }
            else {
              throw new TypeError("Invalid element type in list for 'moveActions', expected Square");
            }
          }
          return new ListValue(actions); 
        }
      }
    ));
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
  
  public List<TypeValue> findGameTypes() {
    ArrayList<TypeValue> games = new ArrayList<TypeValue>();
    for (Entry<String, TypeValue> e : types.entrySet()) {
      if (e.getValue().isSubtypeOf(game) && !(e.getValue() instanceof AbstractTypeValue)) {
        games.add(e.getValue());
      }
    }
    return games;
  }

}
