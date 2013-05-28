package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.ast.AstNode;

@SuppressWarnings("serial")
public class TypeError extends StandardError {

  public TypeError(String arg0, AstNode node) {
    super(arg0, node);
    // TODO Auto-generated constructor stub
  }

  public TypeError(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

}
