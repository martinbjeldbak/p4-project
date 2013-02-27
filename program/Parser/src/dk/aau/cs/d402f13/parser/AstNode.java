package dk.aau.cs.d402f13.parser;
import java.util.ArrayList;
import java.util.Iterator;

public class AstNode implements Iterable {
  public enum Type{
    //Keywords
    GAME, PIECE, THIS,  WIDTH, HEIGHT, TITLE, PLAYERS, TURNORDER, 
    BOARD, GRID, SETUP, WALL, NAME, POSSIBLE_DROPS, POSSIBLE_MOVES, 
    WIN_CONDITION, TIE_CONDITION, AND, OR, FRIEND, FOE, EMPTY,
    MULT_OP, QUEST_OP, PLUS_OP, NOT_OP,
    //Literals
    INT_LIT, DIR_LIT, COORD_LIT, STRING_LIT,
    //Identifiers
    FUNCTION, ID, VAR,
    //Program structure
    PROGRAM, FUNC_DEF, GAME_DECL, DECL_STRUCT, DECL, STRUCT, 
    KEYWORD, //Skal keyword ikke også være her?
    //Expressions
    EXPR, ELEM, FUNC_CALL, IF_EXPR, LAMBDA_EXPR, LIST, 
    //Patterns
    PATTERN, PATTERN_EXPR, PATTERN_VAL, PATTERN_CHECK
  }
  
  public Type type;
  public String value;
  private ArrayList<AstNode> children;
  
  public void addChild(AstNode child){
    if(child != null){
      this.children.add(child);
    }
  }
  
  public AstNode(Type type, String value){
    this.type = type;
    this.value = value;
  }
  

  @Override
  public Iterator iterator() {
    return children.iterator();
  }
  

}
