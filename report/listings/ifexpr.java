private AstNode ifExpression() throws SyntaxError {
  AstNode node = astNode(Type.IF_EXPR, "");
  expect(Token.Type.IF);
  node.addChild(expression());
  expect(Token.Type.THEN);
  node.addChild(expression());
  expect(Token.Type.ELSE);
  node.addChild(expression());

  return node;
}