package dk.aau.cs.d402f13.utilities;

public class Token {
  public enum Type {
    // Keywords
    KEYWORD, GAME, THIS,
    PATTERN_KEYWORD,  // (friend, foe, ...)
    
    // Operators
    NOT_OPERATOR,
    SHARED_OPERATOR, //those that is used both as normal_operator and pattern_operator
    PATTERN_OPERATOR, NORMAL_OPERATOR,
    
    ASSIGN,

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
    PATTERNOP, EOF;
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