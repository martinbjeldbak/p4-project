package dk.aau.cs.d402f13.parser;
import java.util.LinkedList;
import ludus.Token;


public class Parser {

	/**
	 * @param args
	 */
	
	private LinkedList<Token> tokens;
	private Token currentToken;
	private Token nextToken;
	
	private Token peek(){
		return nextToken;
	}
	
	private Token pop(){
		Token next = nextToken;
		tokens.poll();
		nextToken = tokens.peek();
		return next;
	}
	
	private boolean lookAhead(Token.Type type){
		return nextToken != null && nextToken.type == type;
	}
	
	private boolean lookAheadLiteral(){
		return lookAhead(Token.Type.INT_LIT)
		    || lookAhead(Token.Type.DIR_LIT)
		    || lookAhead(Token.Type.COORD_LIT)
		    || lookAhead(Token.Type.STRING_LIT);
	}
	
	private boolean lookAheadKeyword(){
	  return lookAhead(Token.Type.GAME)
        || lookAhead(Token.Type.PIECE)
        || lookAhead(Token.Type.THIS)
        || lookAhead(Token.Type.WIDTH)
        || lookAhead(Token.Type.HEIGHT)
        || lookAhead(Token.Type.TITLE)
        || lookAhead(Token.Type.PLAYERS)
        || lookAhead(Token.Type.TURNORDER)
        || lookAhead(Token.Type.BOARD)
        || lookAhead(Token.Type.GRID)
        || lookAhead(Token.Type.SETUP)
        || lookAhead(Token.Type.WALL)
        || lookAhead(Token.Type.NAME)
        || lookAhead(Token.Type.POSSIBLE_DROPS)
        || lookAhead(Token.Type.POSSIBLE_MOVES)
        || lookAhead(Token.Type.WIN_CONDITION)
        || lookAhead(Token.Type.TIE_CONDITION);
	}
	
	private boolean lookAheadElement(){
		return lookAheadLiteral()
			  || lookAhead(Token.Type.LPAREN)
			  || lookAhead(Token.Type.VAR)
			  || lookAhead(Token.Type.LBRACKET)
			  || lookAhead(Token.Type.PATTERNOP)
		    || lookAheadKeyword();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
