package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.ast.AstNode;


public class InternalError extends StandardError {
  
  private Exception e;

  public InternalError(Exception e, AstNode node) {
    super(e.getClass().getName() + ": " + e.getMessage(), node);
    this.e = e;
  }
  
  @Override
  public void printStackTrace() {
    e.printStackTrace();
  }

}
