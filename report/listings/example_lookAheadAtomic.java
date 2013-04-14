private AstNode expression() throws SyntaxError {
  if (lookAhead(Token.Type.LET)) {
    return assignment();
  }
  else if (lookAhead(Token.Type.IF)) {
    return ifExpression();
  }
  else if (lookAhead(Token.Type.LAMBDA_BEGIN)) {
    return lambdaExpression();
  }
  {...}
  else if (lookAheadAtomic() || lookAhead(Token.Type.NOT_OPERATOR)) {
    return operation();
  }
  {...}
}