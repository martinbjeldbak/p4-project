package dk.aau.cs.d402f13.utilities.errors;

@SuppressWarnings("serial")
public abstract class StandardError extends Exception {

  
  public StandardError(String msg) {
    super(msg);
  }

  public abstract int getColumn();
  
  public abstract int getLine();
}