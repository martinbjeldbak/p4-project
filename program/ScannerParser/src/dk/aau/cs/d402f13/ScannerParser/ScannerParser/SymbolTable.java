package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import java.util.HashMap;
import java.util.Map;

import dk.aau.cs.d402f13.ScannerParser.Values.Value;

public class SymbolTable {
  private SymbolTable parent;
  
  private Map<String, Value> variableValues = new HashMap<String, Value>();
  private Map<String, Type> variableTypes = new HashMap<String, Type>();

  public SymbolTable() {
    this.parent = null;
  }
  
  public SymbolTable(SymbolTable parent) {
    this.parent = parent;
  }
  
  public SymbolTable getParent() {
    return this.parent;
  }

}
