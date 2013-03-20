package dk.aau.cs.d402f13.utilities.errors;
import dk.aau.cs.d402f13.utilities.SymbolTable.SymbolInfo;

public class ScopeError extends Exception {

  SymbolInfo symbolInfo;
  
  public ScopeError(String msg, SymbolInfo symbolInfo) {
    super(msg);
    this.symbolInfo = symbolInfo; //somone pls implement line num and offset in SymbolInfo
  }
}
