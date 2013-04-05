package dk.aau.cs.d402f13.utilities.scopechecker;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable.SymbolType;

public class SymbolInfo{
  SymbolType type;
  Boolean declaration;
  String name;
  int line;
  int offset;
  
  SymbolInfo(SymbolType type, Boolean declared, String name, int line, int offset){
    this.type = type;
    this.declaration = declared;
    this.name = name;
    this.line = line;
    this.offset = offset;
  }
  public void print(){
    String dec = this.declaration ? "DECLARED" : "UNDECLARED";
    System.out.println(this.type + " : " + this.name + "[" + dec + "]");
  }
  public int getLine(){
    return this.line;
  }
  public int getOffset(){
    return this.offset;
  }
}
