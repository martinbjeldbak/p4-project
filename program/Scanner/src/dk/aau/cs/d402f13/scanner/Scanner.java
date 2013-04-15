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
  public static final String operators = "!&*+-=><?(){}#[]/|,.";
  
  private int line = 1;
  private int offset = -1;
  private int previousLineLength = 0;

  private InputStreamReader input;
  private int currentChar = -1;
  private int nextChar = -1;

  public Scanner(InputStream input) throws UnsupportedEncodingException {
    this.input = new InputStreamReader(input, "UTF-8");
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
    String str = "";
    while(isAnycase()) {
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
        
      // Pattern keywords
      case "foe":
      case "friend":
      case "empty":
        return token(Type.KEY_PATTERN, str);
        
      // Operators
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

  public Token scanUppercase() {
    // Can be Type or Coordinate, e.g. Int or A3
    Token t = token(Type.TYPE);
    while (isAnycase()) {
      t.value += current();
      pop();
    }
    // if digit comes after the alphacharacters, it must be a
    // coordinate, else an identifier
    if (isDigit()) {
      t.type = Type.LIT_COORD;
      while (isDigit()){
        t.value += current();
        pop();
      }
    }
    return t;

  }

  public Token scanVar() throws ScannerError {
    // called when token starts with $
    Token t = token(Type.VAR);
    pop(); // remove initial $
    while (isAnycase()) {
      t.value += current();
      pop();
    }
    if (t.value.length() < 0) {
      throw new ScannerError("Invalid variable: " + t.value, token(Type.EOF));
    }
    return t;
  }

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

  public Token scanNumeric() {
    Token t = token(Type.LIT_INT);
    while (isDigit()) {
      t.value += current();
      pop();
    }
    return t;
  }

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