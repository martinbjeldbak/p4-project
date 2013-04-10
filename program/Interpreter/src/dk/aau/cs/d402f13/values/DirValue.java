package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class DirValue extends Value {
  private final int x;
  private final int y;

  public DirValue(String value) {
    switch(value) {
    case "n":
      this.x = 0;
      this.y = 1;
      break;
    case "e":
      this.x = 1;
      this.y = 0;
      break;
    case "s":
      this.x = 0;
      this.y = -1;
      break;
    case "w":
      this.x = -1;
      this.y = 0;
      break;
    case "ne":
      this.x = 1;
      this.y = 1;
      break;
    case "se":
      this.x = 1;
      this.y = -1;
      break;
    case "sw":
      this.x = -1;
      this.y = -1;
      break;
    case "nw":
      this.x = -1;
      this.y = 1;
      break;
    default:
      this.x = 0;
      this.y = 0;
      break;
    }
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof DirValue) {
      DirValue otherDirection = (DirValue)other;
      
      if((this.x == otherDirection.getX()) && (this.y == otherDirection.getY()))
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  @Override
  public String toString() {
    int xx = x;
    int yy = y;
    
    return null;
  }

  @Override
  public Type getType() {
    return Type.DIR_LIT;
  }

}
