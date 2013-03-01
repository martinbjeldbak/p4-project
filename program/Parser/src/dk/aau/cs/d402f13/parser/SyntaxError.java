package dk.aau.cs.d402f13.parser;

import dk.aau.cs.d402f13.scanner.Token;

public class SyntaxError extends Exception {
  public Token token;
  
  public SyntaxError(String msg, Token token){
    super(msg);
    this.token = token;
  }
  
  public Token getToken(){
    return token;
  }
  
}