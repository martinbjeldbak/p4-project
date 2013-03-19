private boolean lookAheadElement() {
  return lookAheadLiteral()
    || lookAhead(Token.Type.LPAREN)
    || lookAhead(Token.Type.VAR)
    || lookAhead(Token.Type.LBRACKET)
    || lookAhead(Token.Type.PATTERNOP)
    || lookAhead(Token.Type.KEYWORD)
    || lookAhead(Token.Type.THIS)
    || lookAhead(Token.Type.ID);
}