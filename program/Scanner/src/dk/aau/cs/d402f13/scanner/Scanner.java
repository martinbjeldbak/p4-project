package dk.aau.cs.d402f13.scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.ScannerError;
import dk.aau.cs.d402f13.utilities.errors.SyntaxError;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.Token.Type;

@SuppressWarnings("unused")
public class Scanner {
  public static final String whitespace = " \t\r\n";
  public static final String operators = "!&*+-=><?%(){}#[]/|,.";
  
  private int line = 1;
  private int offset = -1;
  private int previousLineLength = 0;

  private InputStreamReader input;
  private int currentChar = -1;
  private int nextChar = -1;

  public Scanner(InputStream input) throws InternalError {
    try {
    this.input = new InputStreamReader(input, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      throw new InternalError(e);
    }
    pop();
    pop(); // Double-pop! (To get info into currentChar and nextChar)
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
  
  public Token token(Type type, String value) {
    return new Token(type, value, line, offset);
  }
  
  public Token token(Type type) {
    return token(type, "");
  }

  /**
   * Is used to check if current() is a linebreak. The scan()-method
   * uses the method to check if it should return a eol-token 
   * @return true if linebreak, else false
   */
  public boolean isEol() {
    return current() == '\n';
  }

  /**
   * Checks if currentChar is EOF
   * @return true if currenChar is EOF. Otherwise false
   */
  public boolean isEof() {
    return currentChar < 0;
  }
 
  /**
   * Checks for whitespace. 
   * @return true if it is whitespace. Otherwise false
   */
  public boolean isWhitespace() {
    return whitespace.indexOf(current()) != -1;
  }

  
  /**
   * Checks if current() is a digit
   * @returns true if current is a digit. Otherwise false
   */
  public boolean isDigit() {
    char c = current();
    return c >= '0' && c <= '9';
  }

  /**
   * Checks if current() is an operator "!&*+-=><?%(){}#[]/|,."
   * @returns true if it is an operator. Otherwise false
   */
  public boolean isOperator() {
    return operators.indexOf(current()) != -1;
  }
  
  /**
   * Checks if current() is an uppercase letter
   * @returns true if it is. Otherwise false
   */
  public boolean isUppercase() {
    char c = current();
    return c >= 'A' && c <= 'Z';
  }
  
  /**
   * Checks if current() is a lowercase letter
   * @returns true if it is. Otherwise false
   */
  public boolean isLowercase() {
    char c = current();
    return c >= 'a' && c <= 'z';
  }
  
  /**
   * Checks if current() is a letter
   * @returns true if it is. Otherwise false
   */
  public boolean isAnycase() {
    char c = current();
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
  }

  
  /**
   * Is called from the Scan()-method. Tries to match the current str to a keyword
   * @returns the token that match the current string str.
   * @throws ScannerError the length of the string is less than 1.
   */
  public Token scanKeyword() throws ScannerError {
    String str = "";
    while(isAnycase() || isDigit()) {
      str += current();
      pop();
    }
    switch (str) {
      // Keywords
      case "this":
        return token(Type.KEY_THIS, str);
      case "super":
        return token(Type.KEY_SUPER, str);     
      case "define":
        return token(Type.KEY_DEFINE, str);    
      case "abstract":
        return token(Type.KEY_ABSTRACT, str);    
      case "extends":
        return token(Type.KEY_EXTENDS, str);    
      case "type":
        return token(Type.KEY_TYPE, str);
      case "set":
        return token(Type.KEY_SET, str);
      case "data":
        return token(Type.KEY_DATA, str);
        
      // Pattern keywords
      case "foe":
      case "friend":
      case "empty":
        return token(Type.KEY_PATTERN, str);
        
      // Operators
      case "is":
        return token(Type.OP_IS, str);
      case "or":
        return token(Type.OP_OR, str);
      case "and":
        return token(Type.OP_AND, str);
      case "not":
        return token(Type.OP_NOT, str);  
      case "let":
        return token(Type.LET, str);
      case "in":
        return token(Type.IN, str);
      case "if":
        return token(Type.IF, str);
      case "then":
        return token(Type.THEN, str);
      case "else":
        return token(Type.ELSE, str);
           
      // Direction literal
      case "ne":
      case "nw":
      case "se":
      case "sw":
      case "n":
      case "s":
      case "e":
      case "w":
        return token(Type.LIT_DIR, str);
      default:
        if (str.length() < 1) {
          throw new ScannerError("Invalid function or keyword: " + str, token(Type.EOF));
        }
        return token(Type.CONSTANT, str);
    }
  }

  /**
   * Is called from the Scan()-method. Tries to assign the token t to a type-token or a coordinat-token.
   * @returns a type-token if t consists of letters or a coordinat-token if t consists of letters
   * followed by digits.
   * @throws ScannerError if t consists of lowercase letters followed by digits.
   */
  public Token scanUppercase() throws ScannerError {
    // Can be Type or Coordinate, e.g. Int or A3
    Token t = token(Type.TYPE);

    while (isUppercase()) {
      t.value += current();
      pop();
    }
    while (isDigit()) {
      t.type = Type.LIT_COORD;
      t.value += current();
      pop();
    }

    while(isUppercase() || isLowercase() || isDigit()){
      t.type = Type.TYPE;
      t.value += current();
      pop();
    }
    return t;
  }
  
  /**
   * Is called from the scan()-method. Tries to assign the token t to a variable-token
   * @return a variable-token if t consists of a $ followed by a number of anycase letters. 
   * @throws ScannerError if the length of t is less than 0.
   */
  public Token scanVar() throws ScannerError {
    // called when token starts with $
    Token t = token(Type.VAR);
    pop(); // remove initial $
    
    while (isAnycase() || isDigit()) {
      t.value += current();
      pop();
    }
    
    if (t.value.length() < 0) {
      throw new ScannerError("Invalid variable: " + t.value, token(Type.EOF));
    }
    return t;
  }
  
  
  /**
   * Is called from the scan()-method. Tries to match the String str to a operator token.
   * @returns the operator token that matches str
   * @throws ScannerError if the str doesn't match a valid operator
   */
  public Token scanOperator() throws ScannerError {
    String str = "";
    char c = current();
    str += c;
    pop();
    switch (c) {

      case '+':
        return token(Type.OP_PLUS, str);
      case '-':
        return token(Type.OP_MINUS, str);        
      case '*':
        return token(Type.OP_MULT, str);
      case '/':
        if (current() == '/') {
          while (!isEol()) {
            pop();
          }
          return scan();
        }
        return token(Type.OP_DIV, str);
      case '%':
        return token(Type.OP_MODULO, str);
      
      case '=':
        if(current() == '=' ){
          str += "=";
          pop();
          return token(Type.OP_EQUALS, str);
        }
        else if (current() == '>') {
          str += ">";
          pop();
          return token(Type.OP_LAMBDA, str);
        }
        else
          return token(Type.OP_ASSIGN, str);
      
      case '>':
        if (current() == '=') {  // >=
          str += "=";
          pop();
          return token(Type.OP_GREATER_OR_EQUALS, str);
        }
        else {
          return token(Type.OP_GREATER_THAN, str);
        }
        
      case '<':
        if (current() == '=') { // <=
          str += "=";
          pop();
          return token(Type.OP_GREATER_OR_EQUALS, str);
        }
        else {
          return token(Type.OP_LESS_THAN, str);
        }
      
      case '#':
        return token(Type.LAMBDA_BEGIN, str);
        
      case '!':
        if (current() == '=') {
          str += "=";
          pop();
          return token(Type.OP_NOT_EQUALS, str);
        }
        else{
          return token(Type.OP_PATTERN_NOT, str);
        }
        
      case '|':
        return token(Type.OP_PATTERN_OR, str); 
      case '?':
        return token(Type.OP_PATTERN_QUESTION, str);  
        
      case '.':
        if (current() == '.') {
          str += ".";
          pop();
          if (current() == '.') {
            str += ".";
            pop();
            return token(Type.OP_DOT_DOT_DOT, str);
          }
          else throw new ScannerError("Undefined operator: " + str, token(Type.EOF));
        }
        else {
          return token(Type.OP_DOT, ".");
        }
        
      case ',':
        return token(Type.COMMA, str);   
      case '[':
        return token(Type.LBRACKET, str);    
      case ']':
        return token(Type.RBRACKET, str);
      case '{':
        return token(Type.LBRACE, str);       
      case '}':
        return token(Type.RBRACE, str);        
      case '(':
        return token(Type.LPAREN, str);        
      case ')':
        return token(Type.RPAREN, str);          
      default:
        throw new ScannerError("Undefined operator: " + str, token(Type.EOF));
    }
  }

  /**
   * Is called from the scan()-method. Tries to assign the token t to an Int literal token
   * @returns an int literal token if t consists of only digits.
   */
  public Token scanNumeric() {
    Token t = token(Type.LIT_INT);
    while (isDigit()) {
      t.value += current();
      pop();
    }
    return t;
  }
  
  
  /**
   * Is called from the scan()-method. Tries to assign the token t to a String literal token
   * @returns a String literal token if t consists of a '"'  followed by anything, and ending with a '"' 
   * @throws ScannerError if t doesn't end with a '"'
   */
  public Token scanString() throws ScannerError {
    Token t = token(Type.LIT_STRING);
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

  /**
   * The main scan()-method, which picks the appropriate sub scan-method, depending on which
   * symbol is currently looked at. Is called in the main()-method.
   * @returns the appropriate sub scan-method 
   * @throws ScannerError if the current symbol isn't a valid one
   */
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
    //FileInputStream f = new FileInputStream("kent-game-2.0.junta");
    Scanner s = new Scanner(System.in);
    Token t;
    while ((t = s.scan()).type != Type.EOF) {
      System.out.println("Read token: " + t.type + " (" + t.value
          + ") on line: " + t.line + ", offset: " + t.offset);
    }
  }
}