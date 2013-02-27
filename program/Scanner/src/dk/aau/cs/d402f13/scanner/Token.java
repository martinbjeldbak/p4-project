package dk.aau.cs.d402f13.scanner;

public class Token {
	public enum Type {
	  ASSIGN, PLUSOP, MINUSOP, PATTERNOP, MULTOP, LAMBDABEGIN,
    LAMBDAOP, NOTOP, OROP, ANDOP, QUESTOP, INT_LIT, DIR_LIT,
  	COORD_LIT, STRING_LIT, ID, FUNCTION, VAR,
  	  
  	//brackets
  	LBRACE, RBRACE, LPAREN, RPAREN, LBRACKET, RBRACKET,
  	
  	DEFINE, THIS,
  	
  	//For patterns
  	FOE, FRIEND, EMPTY,

  	IF, THEN, ELSE,
  	  
  	GAME, TITLE, PLAYERS, TURNORDER, BOARD, GRID, WIDTH,
  	HEIGHT, SETUP, PIECE, NAME, POSSIBLE_MOVES, 
  	POSSIBLE_DROPS, WALL, WIN_CONDITION, TIE_CONDITION, EOF
	}		

	public String value;
	public Type type;
	int line, offset;
	public Token(Type type, String value, int line, int offset) {
		this.type = type;
		this.value = value;
		this.line = line;
		this.offset = offset;
	}
	public Token(Type type, int line, int offset){
		this(type, "", line, offset);
	}
}	