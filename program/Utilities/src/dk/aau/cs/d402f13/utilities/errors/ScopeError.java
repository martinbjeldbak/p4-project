package dk.aau.cs.d402f13.utilities.errors;

@SuppressWarnings("serial")
public class ScopeError extends StandardError {

  int line, offset;
  public ScopeError(String msg, int line, int offset) {
    super(msg);   
    this.line = line;
    this.offset = offset;
  }
  
  
  public int getColumn() {
  return offset;
  }
  
  public int getLine() {
    return line;
  }
}
