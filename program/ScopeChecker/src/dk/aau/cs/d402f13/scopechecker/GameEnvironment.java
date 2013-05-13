package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.ConstantMember;
import dk.aau.cs.d402f13.utilities.scopechecker.FunctionMember;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class GameEnvironment {
  public static void insertInto(TypeTable tt) throws ScopeError{
    
    TypeSymbolInfo current;
    
    
    //GAME
    current = new TypeSymbolInfo(null, "Game", -1, 0);
    current.args = 1;
    current.members.add(new FunctionMember("findSquares", 1));
    current.members.add(new FunctionMember("applyAction", 1));
    current.members.add(new FunctionMember("undoAction", 1));
    current.members.add(new FunctionMember("matchSquare", 2));
    current.members.add(new FunctionMember("matchSquares", 2));
    current.members.add(new FunctionMember("findSquaresIn", 2));
    current.members.add(new FunctionMember("setHistory", 1));
    current.members.add(new FunctionMember("setBoard", 1));
    current.members.add(new FunctionMember("setCurrentPlayer", 1));
    current.members.add(new FunctionMember("nextTurn", 0));
    current.members.add(new ConstantMember("players"));
    current.members.add(new ConstantMember("initialBoard", true, -1, 0));
    current.members.add(new ConstantMember("currentPlayer"));
    current.members.add(new ConstantMember("turnOrder"));
    current.members.add(new ConstantMember("board"));
    current.members.add(new ConstantMember("title"));
    current.members.add(new ConstantMember("description"));
    current.members.add(new ConstantMember("history"));
    tt.addType(current);
    
    //BOARD
    current = new TypeSymbolInfo(null, "Board", -1, 0);
    current.args = 0;
    current.members.add(new FunctionMember("addPiece", 2));
    current.members.add(new FunctionMember("addPieces", 2));
    current.members.add(new FunctionMember("setPieces", 1));
    current.members.add(new ConstantMember("pieces"));
    tt.addType(current);
    
    //GRIDBOARD
    current = new TypeSymbolInfo(null, "GridBoard", -1, 0);
    current.args = 2;
    current.setParentName("Board");
    current.members.add(new FunctionMember("squareAt", 1));
    current.members.add(new FunctionMember("removePiece", 1));
    current.members.add(new FunctionMember("movePiece", 2));
    current.members.add(new ConstantMember("width"));
    current.members.add(new ConstantMember("height"));
    current.members.add(new ConstantMember("squares"));
    current.members.add(new ConstantMember("isFull"));
    current.members.add(new ConstantMember("emptySquares"));
    current.members.add(new ConstantMember("squareTypes"));
    tt.addType(current);
    
    //SQUARE
    current = new TypeSymbolInfo(null, "Square", -1, 0);
    current.args = 0;
    current.members.add(new FunctionMember("setPieces", 1));
    current.members.add(new FunctionMember("addPiece", 1));
    current.members.add(new FunctionMember("removePiece", 1));
    current.members.add(new FunctionMember("setPosition", 1));
    current.members.add(new ConstantMember("position"));
    current.members.add(new ConstantMember("pieces"));
    current.members.add(new ConstantMember("image"));
    current.members.add(new ConstantMember("isOccupied"));
    current.members.add(new ConstantMember("position"));
    current.members.add(new ConstantMember("isEmpty"));
    tt.addType(current);
    
    //PIECE
    current = new TypeSymbolInfo(null, "Piece", -1, 0);
    current.args = 1;
    current.members.add(new FunctionMember("move", 1));
    current.members.add(new FunctionMember("remove", 0));
    current.members.add(new FunctionMember("actions", 1));
    current.members.add(new ConstantMember("owner"));
    current.members.add(new ConstantMember("image"));
    current.members.add(new ConstantMember("position"));
    current.members.add(new ConstantMember("onBoard"));
    tt.addType(current);
    
    //PLAYER
    current = new TypeSymbolInfo(null, "Player", -1, 0);
    current.args = 1;
    current.members.add(new FunctionMember("winCondition", 1));
    current.members.add(new FunctionMember("tieCondition", 1));
    current.members.add(new FunctionMember("actions", 1));
    current.members.add(new ConstantMember("name"));
    tt.addType(current);
    
    //ACTION
    current = new TypeSymbolInfo(null, "Action", -1, 0);
    current.args = 0;
    tt.addType(current);
    
    //ACTIONSEQUENCE
    current = new TypeSymbolInfo(null, "ActionSequence", -1, 0);
    current.args = 0; //0 args additional to varargs
    current.varArgs = true;
    current.setParentName("Action");
    current.members.add(new FunctionMember("addAction", 1));
    current.members.add(new ConstantMember("actions"));
    tt.addType(current);
    
    //UNITACTION
    current = new TypeSymbolInfo(null, "UnitAction", -1, 0);
    current.args = 1;
    current.setParentName("Action");
    current.members.add(new ConstantMember("piece"));
    tt.addType(current);
    
    //ADDACTION
    current = new TypeSymbolInfo(null, "AddAction", -1, 0);
    current.args = 2;
    current.parentCallArgs = 1;
    current.setParentName("UnitAction");
    current.members.add(new ConstantMember("to"));
    tt.addType(current);
  
    //REMOVEACTION
    current = new TypeSymbolInfo(null, "RemoveAction", -1, 0);
    current.args = 1;
    current.parentCallArgs = 1;
    current.setParentName("UnitAction");
    current.members.add(new ConstantMember("to"));
    tt.addType(current);
    
    //MOVEACTION
    current = new TypeSymbolInfo(null, "MoveAction", -1, 0);
    current.args = 2;
    current.parentCallArgs = 1;
    current.setParentName("UnitAction");
    current.members.add(new ConstantMember("to"));
    tt.addType(current);
    
    //TESTCASE
    current = new TypeSymbolInfo(null, "TestCase", -1, 0);
    current.args = 0;
    tt.addType(current);
    
    //GLOBAL
    current = tt.getGlobal();
    current.members.add(new FunctionMember("addActions", 2));
    current.members.add(new FunctionMember("moveActions", 2));
    tt.addType(current);
    }
}
