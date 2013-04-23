package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class DirValue extends Value {
  private final int x;
  private final int y;
  
  private static TypeValue type = new TypeValue("Direction", 1, false);

  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }
  
  public DirValue(int x, int y) {
    this.x = x;
    this.y = y;
  }

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
    }
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue equalsOp(Value other) {
    if(other.is(DirValue.type())) {
      DirValue oDir = null;
      try {
        oDir = (DirValue)other.as(DirValue.type());
      } catch (TypeError typeError) {
        return BoolValue.falseValue();
      }

      if((this.x == oDir.getX()) & (this.y == oDir.getY()))
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }

  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws TypeError {
    if(other.is(DirValue.type())) {
      DirValue oDir = (DirValue)other.as(DirValue.type());
      return new DirValue((x + oDir.getX()), y + (oDir.getY()));
    }
    else if(other.is(CoordValue.type())) {
      CoordValue oCoord = (CoordValue)other.as(CoordValue.type());
      return new CoordValue(x + oCoord.getX(), y + oCoord.getY());
    }
    else if(other.is(StrValue.type())) {
      StrValue oStr = (StrValue)other.as(StrValue.type());
      return new StrValue(this.toString() + oStr.getValue());
    }
    throw new TypeError("Addition cannot be done on direction with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws TypeError {
    if(other.is(DirValue.type())) {
      DirValue oDir = (DirValue)other.as(DirValue.type());
      return new DirValue(x - oDir.getX(), y - oDir.getY());
    }
    throw new TypeError("Cannot subtract a " + other + " from a direction");
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value negate() throws TypeError {
    return new DirValue(-x, -y);
  }
  
  @Override
  public String toString() {
    return toDir(x, y);
  }
  
  private String toDir(int x, int y) {
    if((x < 2 && x > -2) && (y < 2 && y > -2)) {
      if(x == 0 && y == 1)
        return "n";
      else if(x == 1 && y == 0)
        return "e";
      else if(x == 0 && y == -1)
        return "s";
      else if(x == -1 && y == 0)
        return "w";
      else if(x == 1 && y == 1)
        return "ne";
      else if(x == 1 && y == -1)
        return "se";
      else if(x == -1 && y == -1)
        return "sw";
      else if(x == -1 && y == 1)
        return "nw";
    }
    return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")";
  }
}
