@Override
protected Value visitAssignment(AstNode node) throws StandardError {
  symbolTable.openScope();
  String var = node.get(0).value;
  Value val = visit(node.get(1));
  symbolTable.addVariable(var, val);
  for (int i = 2; i < node.size() -1; i++) {
    AstNode assign = node.get(i);
    var = assign.get(0).value;
    val = visit(assign.get(1));
    symbolTable.addVariable(var, val);
  }
  Value ret = visit(node.get(node.size() - 1));
  symbolTable.closeScope();
  return ret;
}
