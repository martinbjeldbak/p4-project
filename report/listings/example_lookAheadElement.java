else if (lookAheadElement()) {
  AstNode element = element();
  if (accept(Token.Type.OPERATOR)) {
    AstNode operation = astNode(Type.OPERATOR, currentToken.value);
    operation.addChild(element);
    operation.addChild(expression());
    node.addChild(operation);
  }
  else {
    node.addChild(element);
  }
}