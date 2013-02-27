package dk.aau.cs.d402f13.parser;
import java.util.LinkedList;

import dk.aau.cs.d402f13.scanner.Token;


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
		    || lookAheadKeyword()
		    || lookAhead(Token.Type.ID);
	}
	
	private boolean accept(Token.Type type){
	  if(lookAhead(type)){
	    currentToken = pop();
	    return true;
	  }
	  return false;
	}
	
	private Token expect(Token.Type type) throws SyntaxError {
	  if(accept(type)){
	    return currentToken;
	  }
	  if(nextToken == null){
	    throw new SyntaxError("Empty token stream, expected " + type, null);
	  }
	  throw new SyntaxError("Unexpected token " + nextToken.type + ", expected " + type, nextToken);
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	  /*
	  try {
      AstNode ast = p.parse(tokens);
      ast.print();
      OutputStreamWriter f = new OutputStreamWriter(
        new FileOutputStream(new File("ast.dot"), false)
      );
      ast.export(f);
      f.close();
    }
    catch (SyntaxError e) {
      System.out.flush();
      if (e.getToken() == null) {
        System.err.println("Syntax error: " + e.getMessage());
      }
      else {
        System.err.println("Syntax error: " + e.getMessage()
            + " on input line " + e.getToken().line
            + " offset " + e.getToken().offset + ":");
        System.err.println(input.split("\n")[e.getToken().line - 1]);
        for (int i = 1; i < e.getToken().offset; i++) {
          System.err.print("-");
        }
        System.err.println("^");
      }
    }
    */

	}

}
