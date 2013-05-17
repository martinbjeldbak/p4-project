package dk.aau.cs.d402f13.evaluation;

import dk.aau.cs.d402f13.utilities.SimpleDir;

public class PatternCheckVal {
  String keyVal;
  SimpleDir dir;
  public PatternCheckVal(SimpleDir dir){
    this.dir = new SimpleDir(dir.x, dir.y);
  }
  public PatternCheckVal(SimpleDir dir, String keyVal){
    this(dir);
    this.keyVal = keyVal;
  }
  public void setDir(SimpleDir dir){
    this.dir = new SimpleDir(dir.x, dir.y);
  }
  public PatternCheckVal clone(){
    return new PatternCheckVal(new SimpleDir(this.dir.x, this.dir.y), keyVal);
  }
  public void print(){
    System.out.print("("+this.dir.x+","+this.dir.y+","+this.keyVal+")");
  }
  
  @Override
  public int hashCode() {
    return (this.dir.x * 117 + this.dir.y) ^ this.keyVal.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
      if (obj == null)
          return false;
      if (obj == this)
          return true;
      if (obj.getClass() != getClass())
          return false;
  
      PatternCheckVal other = (PatternCheckVal) obj;
      if (!this.keyVal.equals(other.keyVal))
        return false;
      if (this.dir.x != other.dir.x || this.dir.y != other.dir.y)
        return false;
      return true;
  }
  
}
