package dk.aau.cs.d402f13.scanner;

public class Token {
  public enum Type {
    // Keywords
    KEYWORD, GAME, PIECE, THIS,
    WIDTH, HEIGHT, TITLE, PLAYERS,
    TURNORDER, BOARD, GRID, SETUP,
    WALL, NAME, POSSIBLE_DROPS,
    POSSIBLE_MOVES, WIN_CONDITION,
    TIE_CONDITION,
    // Operators
    OPERATOR, NOT_OPERATOR,
    // Pattern keywords
    PATTERN_KEYWORD, FRIEND, FOE,
    EMPTY,
    // Pattern operators
    PATTERN_OPERATOR, MULTOP,
    QUESTOP, PLUSOP, NOTOP,
    // Literals
    INT_LIT, DIR_LIT, COORD_LIT,
    STRING_LIT,
    // Identifiers
    FUNCTION, ID, VAR,
    // Program structure
    DEFINE,
    // Brackets
    LBRACE, RBRACE, LPAREN, RPAREN,
    LBRACKET, RBRACKET,
    // if-then-else
    IF, THEN, ELSE,
    // lambda
    LAMBDABEGIN, LAMBDAOP,
    // Pattern begin
    PATTERNOP, EOF,
  }

  public String value;
  public Type type;
  public int line, offset;

  public Token(Type type, String value, int line, int offset) {
    this.type = type;
    this.value = value;
    this.line = line;
    this.offset = offset;
  }

  public Token(Type type, int line, int offset) {
    this(type, "", line, offset);
  }
}