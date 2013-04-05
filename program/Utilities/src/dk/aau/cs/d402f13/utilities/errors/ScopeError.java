package dk.aau.cs.d402f13.utilities.errors;

import dk.aau.cs.d402f13.utilities.scopechecker.SymbolInfo;

@SuppressWarnings("serial")
public class ScopeError extends StandardError {

  SymbolInfo symbol;
  public ScopeError(String msg, SymbolInfo si) {
    super(msg);
    this.symbol = si;
  }
  
  public int getColumn() {
    if (this.symbol == null)
      return -1;
    return this.symbol.getOffset();
  }
  
  public int getLine() {
    if (this.symbol == null)
      return -1;
    return this.symbol.getLine();
  }
}
