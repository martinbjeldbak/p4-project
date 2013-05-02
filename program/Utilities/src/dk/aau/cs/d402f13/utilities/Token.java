package dk.aau.cs.d402f13.utilities;

public class Token {
  public enum Type {
    // Keywords
    KEY_THIS, KEY_SUPER, KEY_DEFINE, KEY_ABSTRACT, KEY_EXTENDS, KEY_TYPE,
    KEY_PATTERN, // (friend, foe, ...)
    KEY_SET, KEY_DATA,
    
    // Operators
    OP_OR, OP_AND, OP_EQUALS, OP_NOT_EQUALS, OP_NOT, OP_LESS_THAN, OP_GREATER_THAN,
    OP_LESS_OR_EQUALS, OP_GREATER_OR_EQUALS, OP_DOT, OP_DOT_DOT_DOT, OP_IS,
    OP_PLUS, OP_MINUS, OP_MULT, OP_DIV, OP_MODULO, OP_ASSIGN, OP_LAMBDA,

    // Literals
    LIT_INT, LIT_DIR, LIT_COORD, LIT_STRING,
    
    // Identifiers
    CONSTANT, TYPE, VAR,

    // Brackets
    LBRACE, RBRACE, LPAREN, RPAREN, LBRACKET, RBRACKET,
    
    // if-then-else
    IF, THEN, ELSE,
    
    // lambda
    LAMBDA_BEGIN,
    LET, IN, COMMA,
    
    // Pattern Operators
    OP_PATTERN_QUESTION, // "?"
    OP_PATTERN_NOT, // "!"
    OP_PATTERN_OR, // "|" 
    
    // EOF
    EOF,
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