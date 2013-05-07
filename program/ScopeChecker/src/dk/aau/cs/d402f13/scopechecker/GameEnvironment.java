package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class GameEnvironment {
  public static void insertInto(TypeTable tt) throws ScopeError{
    
    TypeSymbolInfo current;
    
    
    //GAME
    current = new TypeSymbolInfo(null, "Game", -1, 0);
    current.args = 1;
    current.members.add(new Member("players"));
    current.members.add(new Member("board"));
    current.members.add(new Member("currentPlayer"));
    current.members.add(new Member("turnOrder"));
    current.members.add(new Member("currentBoard"));
    current.members.add(new Member("title"));
    current.members.add(new Member("findSquares", 0, -1, 0));
    tt.addType(current);
    
    //BOARD
    current = new TypeSymbolInfo(null, "Board", -1, 0);
    current.args = 0;
    current.members.add(new Member("pieces"));
    tt.addType(current);
    
    //GRIDBOARD
    current = new TypeSymbolInfo(null, "GridBoard", -1, 0);
    current.args = 2;
    current.setParentName("Board");
    current.members.add(new Member("width"));
    current.members.add(new Member("height"));
    current.members.add(new Member("squares"));
    current.members.add(new Member("isFull"));
    current.members.add(new Member("emptySquares"));
    current.members.add(new Member("squareTypes"));
    tt.addType(current);
    
    //SQUARE
    current = new TypeSymbolInfo(null, "Square", -1, 0);
    current.args = 0;
    current.members.add(new Member("position"));
    current.members.add(new Member("pieces"));
    current.members.add(new Member("image"));
    current.members.add(new Member("isOccupied"));
    current.members.add(new Member("position"));
    current.members.add(new Member("isEmpty"));
    tt.addType(current);
    
    //PIECE
    current = new TypeSymbolInfo(null, "Piece", -1, 0);
    current.args = 1;
    current.members.add(new Member("owner"));
    current.members.add(new Member("image"));
    current.members.add(new Member("square"));
    current.members.add(new Member("onBoard"));
    current.members.add(new Member("actions", 0, -1, 0));
    tt.addType(current);
    
    //PLAYER
    current = new TypeSymbolInfo(null, "Player", -1, 0);
    current.args = 1;
    current.members.add(new Member("name"));
    current.members.add(new Member("winCondition", 0, -1, 0));
    current.members.add(new Member("tieCondition", 0, -1, 0));
    current.members.add(new Member("actions", 0, -1, 0));
    tt.addType(current);
    
    
    //ACTION
    current = new TypeSymbolInfo(null, "Action", -1, 0);
    current.args = 0;
    tt.addType(current);
   
    
    //ACTIONSEQUENCE
    current = new TypeSymbolInfo(null, "ActionSequence", -1, 0);
    current.args = -1; //skip check since it contains varargs
    current.setParentName("Action");
    current.members.add(new Member("addAction"));
    current.members.add(new Member("actions"));
    tt.addType(current);
    
    //UNITACTION
    current = new TypeSymbolInfo(null, "UnitAction", -1, 0);
    current.args = 1;
    current.setParentName("Action");
    current.members.add(new Member("piece"));
    tt.addType(current);
    
    //ADDACTION
    current = new TypeSymbolInfo(null, "AddAction", -1, 0);
    current.args = 2;
    current.parentCallArgs = 1;
    current.setParentName("UnitAction");
    current.members.add(new Member("to"));
    tt.addType(current);
  
    //REMOVEACTION
    current = new TypeSymbolInfo(null, "RemoveAction", -1, 0);
    current.args = 1;
    current.parentCallArgs = 1;
    current.setParentName("UnitAction");
    current.members.add(new Member("to"));
    
    //MOVEACTION
    current = new TypeSymbolInfo(null, "MoveAction", -1, 0);
    current.args = 2;
    current.parentCallArgs = 1;
    current.setParentName("UnitAction");
    current.members.add(new Member("to"));
    tt.addType(current);
    
    //TESTCASE
    current = new TypeSymbolInfo(null, "TestCase", -1, 0);
    current.args = 0;
    tt.addType(current);
    }
}
