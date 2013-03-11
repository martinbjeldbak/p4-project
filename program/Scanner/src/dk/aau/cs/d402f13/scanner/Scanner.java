package dk.aau.cs.d402f13.scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import dk.aau.cs.d402f13.scanner.Token.Type;

@SuppressWarnings("unused")
public class Scanner {
  public static final String whitespace = " \t\r\n";
  public static final String operators = "!&*+-=>?(){}#[]/|";
  public int offset, line;

  private InputStreamReader input;
  private int nextChar = -1;

  public Scanner(InputStream input) throws UnsupportedEncodingException {
    this.line = 1; // keeps track of line number and offset so when meeting an
                   // error, users can see on which line and offset they messed
                   // up
    this.offset = 1;
    this.input = new InputStreamReader(input, "UTF-8");
    pop();
  }

  public char peek() {
    return (char) nextChar;
  }

  public char pop() {
    int value = nextChar;
    this.offset++;
    if (isEol()) {
      this.line++;
      this.offset = 1;
    }

    try {
      nextChar = input.read();
    }
    catch (IOException exception) {}
    return (char) value;
  }

  public boolean isEol() {
    return peek() == '\n';
  }

  public boolean isEof() {
    return (int) peek() == 65535;
  }

  public boolean isWhitespace() {
    return whitespace.indexOf(peek()) != -1;
  }

  public boolean isDigit() {
    char c = peek();
    return c >= '0' && c <= '9';
  }

  public boolean isOperator() {
    return operators.indexOf(peek()) != -1;
  }

  public boolean isUppercase() {
    char c = peek();
    return c >= 'A' && c <= 'Z';
  }

  public boolean isLowercase() {
    char c = peek();
    return c >= 'a' && c <= 'z';
  }

  public boolean isAnycase() {
    char c = peek();
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
  }

  public Token scanKeyword() throws Exception {
    String value = "";
    while (isAnycase()) {
      value += pop();
    }
    switch (value) {
    case "if":
      return new Token(Type.IF, line, offset);
    case "then":
      return new Token(Type.THEN, line, offset);
    case "else":
      return new Token(Type.ELSE, line, offset);
      // KEYWORD
    case "game":
      return new Token(Type.GAME, line, offset);
    case "piece":
      return new Token(Type.KEYWORD, "piece", line, offset);
    case "this":
      return new Token(Type.THIS, "this", line, offset);
    case "width":
      return new Token(Type.KEYWORD, "width", line, offset);
    case "height":
      return new Token(Type.KEYWORD, "height", line, offset);
    case "title":
      return new Token(Type.KEYWORD, "title", line, offset);
    case "players":
      return new Token(Type.KEYWORD, "players", line, offset);
    case "turnOrder":
      return new Token(Type.KEYWORD, "turnOrder", line, offset);
    case "board":
      return new Token(Type.KEYWORD, "board", line, offset);
    case "grid":
      return new Token(Type.KEYWORD, "grid", line, offset);
    case "setup":
      return new Token(Type.KEYWORD, "setup", line, offset);
    case "wall":
      return new Token(Type.KEYWORD, "wall", line, offset);
    case "name":
      return new Token(Type.KEYWORD, "name", line, offset);
    case "possibleDrops":
      return new Token(Type.KEYWORD, "possibleDrops", line, offset);
    case "possibleMoves":
      return new Token(Type.KEYWORD, "possibleMoves", line, offset);
    case "winCondition":
      return new Token(Type.KEYWORD, "winCondition", line, offset);
    case "tieCondition":
      return new Token(Type.KEYWORD, "tieCondition", line, offset);
      // Operators
    case "and":
      return new Token(Type.OPERATOR, "and", line, offset);
    case "or":
      return new Token(Type.OPERATOR, "or", line, offset);
    case "not":
      return new Token(Type.NOT_OPERATOR, "not", line, offset);
      // Pattern keywords
    case "foe":
      return new Token(Type.PATTERN_KEYWORD, "foe", line, offset);
    case "friend":
      return new Token(Type.PATTERN_KEYWORD, "friend", line, offset);
    case "empty":
      return new Token(Type.PATTERN_KEYWORD, "empty", line, offset);
      // Direction
    case "ne":
      return new Token(Type.DIR_LIT, "ne", line, offset);
    case "nw":
      return new Token(Type.DIR_LIT, "nw", line, offset);
    case "se":
      return new Token(Type.DIR_LIT, "se", line, offset);
    case "sw":
      return new Token(Type.DIR_LIT, "sw", line, offset);
    case "n":
      return new Token(Type.DIR_LIT, "n", line, offset);
    case "s":
      return new Token(Type.DIR_LIT, "s", line, offset);
    case "e":
      return new Token(Type.DIR_LIT, "e", line, offset);
    case "w":
      return new Token(Type.DIR_LIT, "w", line, offset);

    case "define":
      return new Token(Type.DEFINE, line, offset);
    }
    if (value.length() >= 2) //functions must be: lowercase anycase {anycase}
      return new Token(Type.FUNCTION, value, line, offset);
    else throw new Exception("Undefined token " + value);      
  }

  public Token scanId() {
    // Like Noughts or Crosses
    String value = "";
    while (isAnycase()) {
      value += pop();
    }
    return new Token(Type.ID, value, line, offset);
  }

  public Token scanUppercase() {
    // Can be ID or Coordinate, e.g. Noughts or A3
    String value = "";
    while (isAnycase()) {
      value += pop();
    }
    if (isDigit()) // if digit comes after the alphacharacters, it must be a
                   // coordinate, else an identifier
    {
      while (isDigit()){
        value += pop();
      }
      return new Token(Type.COORD_LIT, value, line, offset);
    }
    else return new Token(Type.ID, value, line, offset);

  }

  public Token scanVar() throws Exception {
    // called when token starts with $
    String value = "";
    pop(); // remove initial $
    while (isAnycase()) {
      value += pop();
    }
    if (value.length() > 0)
      return new Token(Type.VAR, value, line, offset);
    else
      throw new Exception("Undefined token " + value);
  }

  public Token scanOperator() throws Exception {
    char c = pop();
    switch (c) { // these operators are unambiguous
      case '[':
        return new Token(Type.LBRACKET, line, offset);
      case ']':
        return new Token(Type.RBRACKET, line, offset);
      case '{':
        return new Token(Type.LBRACE, line, offset);
      case '}':
        return new Token(Type.RBRACE, line, offset);
      case '(':
        return new Token(Type.LPAREN, line, offset);
      case ')':
        return new Token(Type.RPAREN, line, offset);
      case '!':
        return new Token(Type.NOTOP, line, offset);
      case '+':
        return new Token(Type.PATTERN_OPERATOR, "plus_op", line, offset);
      case '-':
        return new Token(Type.PATTERN_OPERATOR, "minus_op", line, offset);
      case '*':
        return new Token(Type.PATTERN_OPERATOR, "mult_op", line, offset);
      case '?':
        return new Token(Type.PATTERN_OPERATOR, "quest_op", line, offset);
      case '/':
        if (peek() == '/') {
          while (!isEol()) {
            pop();
          }
          return scan();
        }
        return new Token(Type.PATTERNOP, line, offset);
      case '|':
        return new Token(Type.PATTERN_OPERATOR, "or_op", line, offset);
      case '#':
        return new Token(Type.LAMBDABEGIN, line, offset);
      case '=':
        if (peek() == '>') {
          pop();
          return new Token(Type.LAMBDAOP, line, offset);
        }
      default:
        throw new Exception("Undefined token " + c);
    }
  }

  public Token scanNumeric() {
    String value = "";
    while (isDigit()) {
      value += pop();
    }
    return new Token(Type.INT_LIT, value, line, offset);
  }

  public Token scanString() {
    String value = "";
    pop();
    char c;
    while ((c = pop()) != '"' && !isEof()) {
      value += c;
      if (c == '\\') {
        value += pop();
      }
    }
    return new Token(Type.STRING_LIT, value, line, offset);
  }

  public Token scan() throws Exception {
    while (isWhitespace()) {
      pop();
    }
    if (isEof()) { return new Token(Type.EOF, line, offset); }
    if (isDigit()) { return scanNumeric(); }
    if (isUppercase()) { return scanUppercase(); }
    if (isOperator()) { return scanOperator(); }
    if (isLowercase()) { return scanKeyword(); }
    if (peek() == '"') { return scanString(); }
    if (peek() == '$') { return scanVar(); }

    throw new Exception("Could not find a valid token starting with char "
        + peek());
  }

  public static void main(String[] args) throws Exception {
    // FileInputStream f = new FileInputStream("programExample1.txt");
    Scanner s = new Scanner(System.in);
    Token t;
    while ((t = s.scan()).type != Type.EOF) {
      System.out.println("Read token: " + t.type + " (" + t.value
          + ") on line: " + t.line + ", offset: " + t.offset);
    }
  }
}