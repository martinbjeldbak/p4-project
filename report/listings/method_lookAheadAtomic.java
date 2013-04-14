private boolean lookAheadAtomic() {
  return lookAhead(Token.Type.LPAREN) 
      || lookAhead(Token.Type.VAR)
      || lookAhead(Token.Type.LBRACKET)
      || lookAhead(Token.Type.PATTERN_BEGIN) 
      || lookAhead(Token.Type.THIS)
      || lookAhead(Token.Type.SUPER) 
      || lookAheadLiteral()
      || lookAhead(Token.Type.TYPE) 
      || lookAhead(Token.Type.CONSTANT);
}