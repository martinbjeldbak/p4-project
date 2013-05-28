package dk.aau.cs.d402f13.utilities.errors;

@SuppressWarnings("serial")
public abstract class Error extends Exception {

  
  public Error(String msg) {
    super(msg);
  }

  public abstract int getColumn();
  
  public abstract int getLine();
}
