package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.ast.AstNode;

@SuppressWarnings("serial")
public class InternalError extends StandardError {
  
  private Exception e;
  
  public InternalError(String s) {
    super(s);
    e = this;
  }
  
  public InternalError(String s, AstNode node) {
    super(s, node);
    e = this;
  }

  public InternalError(Exception e) {
    super(e.getClass().getName() + ": " + e.getMessage());
    this.e = e;
  }
  
  public InternalError(Exception e, AstNode node) {
    super(e.getClass().getName() + ": " + e.getMessage(), node);
    this.e = e;
  }
  
  @Override
  public void printStackTrace() {
    e.printStackTrace();
  }

}
