package dk.aau.cs.d402f13.utilities;

import dk.aau.cs.d402f13.utilities.scopechecker.SymbolInfo;

public class SimpleDir {
  public int x, y;
  public SimpleDir(int x, int y){
    this.x = x;
    this.y = y;
  }
  public SimpleDir add(SimpleDir val){
    return new SimpleDir(this.x + val.x, this.y + val.y);
  }
  public SimpleDir clone(){
    return new SimpleDir(this.x, this.y);
  }
  @Override
  public int hashCode() {
    return this.x + this.y * 119;
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null)
          return false;
      if (obj == this)
          return true;
      if (obj.getClass() != getClass())
          return false;
  
      SimpleDir other = (SimpleDir) obj;
      if (other.x == this.x && other.y == this.y)
        return true;
      return false;
  }
  
}
