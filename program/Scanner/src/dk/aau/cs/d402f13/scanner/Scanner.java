package dk.aau.cs.d402f13.scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import dk.aau.cs.d402f13.utilities.errors.ScannerError;
import dk.aau.cs.d402f13.utilities.errors.SyntaxError;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.Token.Type;

@SuppressWarnings("unused")
public class Scanner {
  public static final String whitespace = " \t\r\n";
  public static final String operators = "!&*+-=><?()%{}#[]/|.,";
  
  private int line = 1;
  private int offset = -1;
  private int previousLineLength = 0;

  private InputStreamReader input;
  private int currentChar = -1;
  private int nextChar = -1;

  public Scanner(InputStream input) throws UnsupportedEncodingException {
    this.input = new InputStreamReader(input, "UTF-8");
    pop();
    pop(); // Double-pop! (To get info into currentChar and nextChar
  }

  public char current() {
    return (char) currentChar;
  }
  
  public char peek() {
    return (char) nextChar;
  }

  public char pop() {
    currentChar = nextChar;
    try {
      if (input.ready()) {
        nextChar = input.read();
      }
      else {
        nextChar = -1;
      }
    }
    catch (IOException excetption) {
      nextChar = -1;
    }
    if (currentChar == '\n') {
      line++;
      previousLineLength = offset + 1;
      offset = 0;
    }
    else {
      offset++;
    }
    return (char)currentChar;
  }
  
  public Token token(Token.Type type, String value) {
    return new Token(type, value, line, offset);
  }
  
  public Token token(Token.Type type) {
    return token(type, "");
  }

  public boolean isEol() {
    return current() == '\n';
  }

  public boolean isEof() {
    return currentChar < 0;
  }

  public boolean isWhitespace() {
    return whitespace.indexOf(current()) != -1;
  }

  public boolean isDigit() {
    char c = current();
    return c >= '0' && c <= '9';
  }

  public boolean isOperator() {
    return operators.indexOf(current()) != -1;
  }

  public boolean isUppercase() {
    char c = current();
    return c >= 'A' && c <= 'Z';
  }

  public boolean isLowercase() {
    char c = current();
    return c >= 'a' && c <= 'z';
  }

  public boolean isAnycase() {
    char c = current();
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
  }

  public Token scanKeyword() throws ScannerError {
    Token t = token(Type.FUNCTION);
    while (isAnycase()) {
      t.value += current();
      pop();
    }
    switch (t.value) {
      // Keywords
      case "define":
        t.type = Type.DEFINE;
        break;
      case "if":
        t.type = Type.IF;
        break;
      case "then":
        t.type = Type.THEN;
        break;
      case "else":
        t.type = Type.ELSE;
        break;
      case "game":
        t.type = Type.GAME;
        break;
      case "this":
        t.type = Type.THIS;
        break;
      case "width":
      case "height":
      case "title":
      case "players":
      case "turnOrder":
      case "board":
      case "grid":
      case "setup":
      case "piece":
      case "wall":
      case "name":
      case "possibleDrops":
      case "possibleMoves":
      case "winCondition":
      case "tieCondition":
        t.type = Token.Type.KEYWORD;
        break;
      // Operators
      case "and":
      case "or":
        t.type = Token.Type.NORMAL_OPERATOR;
        break;
      case "let":
        t.type = Token.Type.LET;
        break;
      case "in":
        t.type = Token.Type.IN;
        break;
      case "not":
        t.type = Token.Type.NOT_OPERATOR;
        break;
      // Pattern keywords
      case "foe":
      case "friend":
      case "empty":
      case "any":
        t.type = Token.Type.PATTERN_KEYWORD;
        break;
      // Direction literal
      case "ne":
      case "nw":
      case "se":
      case "sw":
      case "n":
      case "s":
      case "e":
      case "w":
        t.type = Token.Type.DIR_LIT;
        break;
      default:
        if (t.value.length() < 2) {
          throw new ScannerError("Invalid function or keyword: " + t.value, token(Type.EOF));
        }
    }
    return t;
  }

  public Token scanUppercase() throws ScannerError {
    // Can be ID or Coordinate, e.g. Noughts or A3
    Boolean canBeID = true;
	Boolean canBeCoord = true;
	Boolean seenDigit = false;
	Token t = token(Type.COORD_LIT);
    while (isAnycase()) {
      if (isLowercase()){
        canBeCoord = false; //coordinates can't contain lowercase letters
      }
      t.value += current();
      pop();
    }
	while (isDigit()) {
	  seenDigit = true;
	  canBeID = false; //identifiers can't contain digits
      t.value += current();
      pop();
    }
	
	canBeCoord = seenDigit ? canBeCoord : false; //if not containing a digit, it can't be coordinate
	
	if (canBeID)
	{
	  t.type = Token.Type.ID;
	  if (canBeCoord)
	    throw new ScannerError("ambiguity between coordinate or identifier: " + t.value, token(Type.EOF));
	}
	else{ //token is predeclared as COORD_LIT
	  if (!canBeCoord) //token can neither be ID nor COORD_LIT
	    throw new ScannerError("Invalid coordinate or identifier: " + t.value, token(Type.EOF));
	}
    return t;
  }

  public Token scanVar() throws ScannerError {
    // called when token starts with $
    Token t = token(Token.Type.VAR);
    pop(); // remove initial $
    while (isAnycase()) {
      t.value += current();
      pop();
    }
    if (t.value.length() <= 0) {
      throw new ScannerError("Invalid unnamed variable", token(Type.EOF));
    }
    return t;
  }

  public Token scanOperator() throws ScannerError {
    Token t = token(Type.NORMAL_OPERATOR);
    char c = current();
    t.value += c;
    pop();
    switch (c) {
      case '[':
        t.type = Type.LBRACKET;
        break;
      case ']':
        t.type = Type.RBRACKET;
        break;
      case '{':
        t.type = Type.LBRACE;
        break;
      case '}':
        t.type = Type.RBRACE;
        break;
      case '(':
        t.type = Type.LPAREN;
        break;
      case ')':
        t.type = Type.RPAREN;
        break;
      case ',':
        t.type = Type.COMMA;
        break;
      case '|':
        t.type = Type.PATTERN_OR;
        break;
      case '+':
      case '*':
        t.type = Type.SHARED_OPERATOR; //can be both normal operator and pattern operator
        break;
      case '!':
        if (current() == '=') {
          t.value += '=';
          pop();
          t.type = Type.NORMAL_OPERATOR; // !=
          break;
        }
        else{
          t.type = Type.PATTERN_NOT; // !
          break;
        }
          
      case '?':
        t.type = Type.PATTERN_OPERATOR;
        break;
      case '%':
      case '-':
        t.type = Type.NORMAL_OPERATOR;
        break;
      case '=':
        if (current() == '>') {
          pop();
          t.type = Type.LAMBDAOP; // =>
          break;
        }
        else if (current() == '=') {
          t.value += '=';
          pop();
          t.type = Type.NORMAL_OPERATOR; // ==
          break;
        }
        else{
          t.type = Type.ASSIGN; // =
          break;
        }
      case '>':
        if (current() == '=') {  // >=
          t.value += '=';
          pop();
          t.type = Type.NORMAL_OPERATOR;
          break;
        }
        else {
          t.type = Type.NORMAL_OPERATOR; // >
          break;
        }
      case '<':
        if (current() == '=') { // <=
          t.value += '=';
          pop();
          t.type = Type.NORMAL_OPERATOR;
          break;
        }
        else {
          t.type = Type.NORMAL_OPERATOR; // <
          break;
        }
      case '/':
        if (current() == '/') {
          while (!isEol()) {
            pop();
          }
          return scan();
        }
        t.type = Type.PATTERNOP;
        break;
      case '#':
        t.type = Type.LAMBDABEGIN;
        break;
      case '.':
        if (current() == '.') {
          t.value += '.';
          pop();
          if (current() == '.') {
            t.value += '.';
            pop();
            t.type = Type.TRIPLEDOTS;
            return t;
          }
        }
      default:
        throw new ScannerError("Undefined operator: " + t.value, token(Type.EOF));
    }
    return t;
  }

  public Token scanNumeric() {
    Token t = token(Type.INT_LIT);
    while (isDigit()) {
      t.value += current();
      pop();
    }
    return t;
  }

  public Token scanString() throws ScannerError {
    Token t = token(Type.STRING_LIT);
    pop();
    char c;
    while ((c = current()) != '"') {
      if (isEof()) {
        throw new ScannerError("Missing end of string literal", token(Type.EOF, ""));
      }
      t.value += c;
      pop();
      if (c == '\\') {
        t.value += current();
        pop();
      }
    }
    pop();
    return t;
  }

  public Token scan() throws ScannerError {
    while (isWhitespace()) {
      pop();
    }
    if (isEof()) {
      return token(Type.EOF);
    }
    if (isDigit()) {
      return scanNumeric();
    }
    if (isUppercase()) {
      return scanUppercase();
    }
    if (isOperator()) {
      return scanOperator();
    }
    if (isLowercase()) {
      return scanKeyword();
    }
    if (current() == '"') {
      return scanString();
    }
    if (current() == '$') {
      return scanVar();
    }
    throw new ScannerError("Unidentified character: " + current(), token(Type.EOF));
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