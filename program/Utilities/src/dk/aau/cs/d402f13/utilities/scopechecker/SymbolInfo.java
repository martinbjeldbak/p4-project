package dk.aau.cs.d402f13.utilities.scopechecker;

public class SymbolInfo{
  public String name;
  public int line;
  public int offset;
  
  public SymbolInfo(String name, int line, int offset){
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
    return this.name.hashCode() ^ this.getClass().hashCode();
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
      if (other.name.equals(this.name))
        return true;
      return false;
  }
}
