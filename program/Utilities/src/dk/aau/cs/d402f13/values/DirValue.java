package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.LegalValueError;

public class DirValue extends Value {
  private final int x;
  private final int y;

  public DirValue(String value) throws LegalValueError {
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
      throw new LegalValueError("Direction not of format: n, e, s, w, ne, se, sw, or nw");
    }
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  @Override
  public String toString() {
    return "(" + this.x + "," + this.y + ")";
  }

  @Override
  public Type getType() {
    return Type.DIR_LIT;
  }

}
