package dk.aau.cs.d402f13.scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import dk.aau.cs.d402f13.scanner.Token.Type;


public class Scanner {
  public static final String whitespace = " \t\r\n";
  public static final String operators = "!&*+-=>?(){}[]/|";
  public int offset, line;


  private InputStream input;
  private int nextChar = -1;
  
  public Scanner(InputStream input) {
    this.line = 0;  //keeps track of line number and offset so when meeting an error, users can see on which line and offset they messed up
    this.offset = 0;
    this.input = input;
    pop();
  }
 
  
  public char peek() {
    return (char)nextChar;
  }
  
  public char pop() {
    int value = nextChar;
    this.offset++; 
    if (isEol())
    {
     this.line++;
     this.offset = 0;
    }
      
    try {
      nextChar = input.read();
    }
    catch (IOException exception) {
      
    }
    return (char)value;
  }

  public boolean isEol() {
    return peek() == '\n';
  }
  
  public boolean isEof() {
    return (int)peek() == 65535;
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
  public boolean isAlphacharacter() {
    char c = peek();
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
  }
  
  public Token scanKeyword() throws Exception {
    String value = "";
    while (isAlphacharacter()) {
      value += pop();
    }
    switch (value)
    {
    case "if": 
      return new Token(Type.IF, line, offset);
    case "then":
      return new Token(Type.THEN, line, offset);
    case "else":
      return new Token(Type.ELSE, line, offset);
    //KEYWORD...?  
    case "game":
      return new Token(Type.KEYWORD, "game", line, offset);
    case "piece":
      return new Token(Type.KEYWORD, "piece", line, offset);
    case "this":
      return new Token(Type.KEYWORD, "this", line, offset);
    case "width":
      return new Token(Type.KEYWORD, "width", line, offset);
    case "height":
      return new Token(Type.KEYWORD, "height", line, offset);
    case "title":
      return new Token(Type.KEYWORD, "title", line, offset);
    case "players":
      return new Token(Type.KEYWORD, "players", line, offset);
    case "turnorder":
      return new Token(Type.KEYWORD, "turnorder", line, offset);
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
    case "possible_drops":
      return new Token(Type.KEYWORD, "possible_drops", line, offset);
    case "possible_moves":
      return new Token(Type.KEYWORD, "possible_moves", line, offset);
    case "win_condition":
      return new Token(Type.KEYWORD, "win_condition", line, offset);
    case "tie_condition":
      return new Token(Type.KEYWORD, "tie_condition", line, offset);
      
    /*  
    case "game":
      return new Token(Type.GAME, line, offset);
    case "piece":
      return new Token(Type.PIECE, line, offset);
    case "this":
      return new Token(Type.THIS, line, offset);
    case "width":
      return new Token(Type.WIDTH, line, offset);
    case "height":
      return new Token(Type.HEIGHT, line, offset);
    case "title":
      return new Token(Type.TITLE, line, offset);
    case "players":
      return new Token(Type.PLAYERS, line, offset);
    case "turnorder":
      return new Token(Type.TURNORDER, line, offset);
    case "board":
      return new Token(Type.BOARD, line, offset);
    case "grid":
      return new Token(Type.GRID, line, offset);
    case "setup":
      return new Token(Type.SETUP, line, offset);
    case "wall":
      return new Token(Type.WALL, line, offset);
    case "name":
      return new Token(Type.NAME, line, offset);
    case "possible_drops":
      return new Token(Type.POSSIBLE_DROPS, line, offset);
    case "possible_moves":
      return new Token(Type.POSSIBLE_MOVES, line, offset);
    case "win_condition":
      return new Token(Type.WIN_CONDITION, line, offset);
    case "tie_condition":
      return new Token(Type.TIE_CONDITION, line, offset);  
     */
      
    //Operators?
    case "and":
      return new Token(Type.OPERATOR, "and", line, offset);
    case "or":
      return new Token(Type.OPERATOR, "or", line, offset);
    //Pattern keywords
    case "foe":
      return new Token(Type.PATTERN_KEYWORD, "foe", line, offset);
    case "friend":
      return new Token(Type.PATTERN_KEYWORD, "friend", line, offset);
    case "empty":
      return new Token(Type.PATTERN_KEYWORD, "empty", line, offset);
    //Direction
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
    return new Token(Type.FUNCTION, value, line, offset);
  }
  public Token scanId() {
    //Like Noughts or Crosses
     String value = "";
     while (isAlphacharacter()) {
       value += pop();
     }
     return new Token(Type.ID, value, line, offset);
   }
  public Token scanUppercase() {
    //Can be ID or Coordinate, e.g. Noughts or A3
     String value = "";
     while (isAlphacharacter()) {
       value += pop();
     }
     if (isDigit())  //if digit comes after the alphacharacters, it must be a coordinate, else an identifier
     {
       while (isDigit())
         value += pop();
       return new Token(Type.COORD_LIT, value, line, offset);
     }
     else
       return new Token(Type.ID, value, line, offset);
       
   }
 public Token scanVar() {
   //called when token starts with $
    String value = "";
    pop(); //remove initial $
    while (isAlphacharacter()) {
      value += pop();
    }
    return new Token(Type.VAR, value, line, offset);
  }
  
  public Token scanOperator() throws Exception {
    String value = "";
      while (isOperator()) {
        value += pop();
        if (value.length() == 2 && value.equals("//")) {
          while (!isEol()) {
            pop();
          }
          return scan();
        }
        switch (value) {    //these operators are unambiguous
        case "[":
          return new Token(Type.LBRACKET, line, offset);
        case "]":
          return new Token(Type.RBRACKET, line, offset);
        case "{":
          return new Token(Type.LBRACE, line, offset);
        case "}":
          return new Token(Type.RBRACE, line, offset);
        case "(":
          return new Token(Type.LPAREN, line, offset);
        case ")":
          return new Token(Type.RPAREN, line, offset);
        case "!":
          return new Token(Type.RPAREN, line, offset);
        case "+":
          return new Token(Type.PATTERN_OPERATOR, "plus_op", line, offset);
        case "-":
          return new Token(Type.PATTERN_OPERATOR, "minus_op", line, offset);
        case "*":
          return new Token(Type.PATTERN_OPERATOR, "mult_op", line, offset);
        case "?":
          return new Token(Type.PATTERN_OPERATOR, "quest_op", line, offset);
        /*
        case "+":
          return new Token(Type.PLUSOP, line, offset);
        case "-":
          return new Token(Type.MINUSOP, line, offset);
        case "*":
          return new Token(Type.MULTOP, line, offset);
        case "?":
          return new Token(Type.QUESTOP, line, offset);
        */
        case "/":
          return new Token(Type.PATTERNOP, line, offset);
        case "|":
          return new Token(Type.OROP, line, offset);
        case "#":
          return new Token(Type.LAMBDABEGIN, line, offset);
      }
        switch (value) { //these operators are ambiguous so they 
        //are first evaluated when nextChar is not an operator
        case "=>":
          return new Token(Type.LAMBDAOP, line, offset);
      //  case "=":
      //    return new Token(Type.ASSIGN, line, offset);
      }
     
      }
      throw new Exception("Undefined token " + value);
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
    if (isEof()) {
      return new Token(Type.EOF, line, offset);
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
    if (isLowercase()){
      return scanKeyword();
    }
    if (peek() == '"'){
      return scanString();
    }
    if (peek() == '$'){
      return scanVar();
    }
    
    throw new Exception("Could not find a valid token starting with char " + peek());
  }
  
  public static void main(String[] args) throws Exception {
    FileInputStream f = new FileInputStream("programExample1.txt");
    Scanner s = new Scanner(f);
    Token t;
    while ((t = s.scan()).type != Type.EOF) {
      System.out.println("Read token: " + t.type + " (" + t.value + ") on line: " + t.line + ", offset: " + t.offset);
    }
  }

}
