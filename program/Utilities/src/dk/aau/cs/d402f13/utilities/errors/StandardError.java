package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.ast.AstNode;

@SuppressWarnings("serial")
public class StandardError extends Error {

  private AstNode node;
  
  public StandardError(String arg0, AstNode node) {
    super(arg0);
    this.node = node;
  }
  
  public StandardError(String arg0) {
    this(arg0, null);
  }

  public AstNode getNode() {
    return node;
  }
  
  public void setNode(AstNode node) {
    this.node = node;
  }
  
  @Override
  public int getColumn() {
    if (node == null) {
      return 1;
    }
    return node.offset;
  }
  
  @Override
  public int getLine() {
    if (node == null) {
      return 1;
    }
    return node.line;
  }
}