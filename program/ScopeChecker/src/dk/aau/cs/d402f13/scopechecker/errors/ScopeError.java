package dk.aau.cs.d402f13.scopechecker.errors;
import dk.aau.cs.d402f13.scopechecker.utilities.SymbolTable.SymbolInfo;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class ScopeError extends StandardError {

  SymbolInfo symbolInfo;
  
  public ScopeError(String msg, SymbolInfo symbolInfo) {
    super(msg);
    this.symbolInfo = symbolInfo;
  }
  
  public int getColumn() {
    if (symbolInfo == null) {
      return 1;
    }
    return symbolInfo.getOffset();
  }
  
  public int getLine() {
    if (symbolInfo == null) {
      return 1;
    }
    return symbolInfo.getLine();
  }
}
