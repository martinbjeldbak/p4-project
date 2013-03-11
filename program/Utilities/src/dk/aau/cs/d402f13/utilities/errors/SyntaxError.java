package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.Token;

@SuppressWarnings("serial")
public class SyntaxError extends Exception {
  public Token token;
  
  public SyntaxError(String msg, Token token){
    super(msg);
    this.token = token;
  }
  
  public Token getToken(){
    return token;
  }
  
  public int getColumn() {
    if (token == null) {
      return 1;
    }
    return token.offset;
  }
  
  public int getLine() {
    if (token == null) {
      return 1;
    }
    return token.line;
  }
}