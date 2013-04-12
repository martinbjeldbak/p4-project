package dk.aau.cs.d402f13.utilities.scopechecker;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable.SymbolType;

public class SymbolInfo{
  SymbolType type;
  String name;
  int line;
  int offset;
  
  public SymbolInfo(SymbolType type, String name, int line, int offset){
    this.type = type;
    this.name = name;
    this.line = line;
    this.offset = offset;
  }
  
  public int getLine(){
    return this.line;
  }
  public int getOffset(){
    return this.offset;
  }
  
  //override equals and hashcode so symbols can be put in a hashmap
  @Override
  public int hashCode() {
    return this.name.hashCode() ^ this.type.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null)
          return false;
      if (obj == this)
          return true;
      if (obj.getClass() != getClass())
          return false;
  
      SymbolInfo other = (SymbolInfo) obj;
      if (other.name.equals(this.name) && other.type.equals(this.type))
        return true;
      return false;
  }
}
