package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.ast.AstNode;

public class LegalValueError extends StandardError {

  private static final long serialVersionUID = 1L;

  public LegalValueError(String arg0, AstNode node) {
    super(arg0, node);
    // TODO Auto-generated constructor stub
  }

  public LegalValueError(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

}
