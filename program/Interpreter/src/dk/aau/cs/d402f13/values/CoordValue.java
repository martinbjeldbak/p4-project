package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class CoordValue extends Value {
  private final int x, y;
  
  private static TypeValue type = new TypeValue("Coordinate", 1, false);

  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

  public CoordValue(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public CoordValue(String value) throws StandardError  {
    int xx = 0;
    int yy = 0;
    
    for(char c : value.toCharArray()) {
      if(isLetter(c)) {
        xx = xx * 26 + (c - 'A') + 1;
      } else {
        yy = yy * 10 + Integer.parseInt(Character.toString(c));
      }
    }
    if (yy < 1) {
      throw new TypeError("Coordinate y-value must be at least 1");
    }
    
    this.x = xx;
    this.y = yy;
  }
  
  /**
   * Returns the X coordinate of the current coordinate object.
   * @return the X coordinate
   */
  public int getX() {
    return this.x;
  }
  
  /**
   * Returns the Y coordinate of the current coordinate object.
   * @return the Y coordinate
   */
  public int getY() {
    return this.y;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof CoordValue) {
      CoordValue otherCoord = (CoordValue) other;
      
      if((this.x == otherCoord.getX()) && (this.y == otherCoord.getY()))
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws StandardError {
    if(other.is(StrValue.type())) {
      StrValue oStr = (StrValue)other.as(StrValue.type());
      return new StrValue(this.toString() + oStr.getValue());
    }
    else if(other.is(DirValue.type())) {
      DirValue oDir = (DirValue)other.as(DirValue.type());

      int xx = x + oDir.getX();
      int yy = y + oDir.getY();

      if(xx < 1 || yy < 1)
        throw new TypeError("The addition yields an invalid coordinate");

      return new CoordValue(x + oDir.getX(), y + oDir.getY());
    }
    else if(other.is(ListValue.type())) {
      return ListValue.prepend(this, other);
    }
    throw new ArgumentError("Addition cannot be done on coordinates with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws StandardError {
    if(other.is(CoordValue.type())) {
      CoordValue oCoord = (CoordValue)other.as(CoordValue.type());
      return new DirValue(x - oCoord.getX(), y - oCoord.getY());
    }
    else if(other.is(DirValue.type())) {
      DirValue oDir = (DirValue)other;
      return new CoordValue(x - oDir.getX(), y - oDir.getY());
    }
    throw new TypeError("Cannot subtract a " + other + " from a coordinate");
  }
  
  @Override
  public String toString() {
    return toColumn(x) + y + " (" + x + ", " + y + ")";
  }
  
  private String toColumn(int xValue) {
    StringBuilder sb = new StringBuilder();
    int remain = xValue;
    
    do {
      sb.append(intCharToLetter(remain % 26));
      remain = (remain - 1) / 26;
    } while (remain > 0);

   return sb.reverse().toString();
  }
  
  private Character intCharToLetter(int value) {
    if(value == 0)
      return 'Z';
    else if (value > 0 && value <= 27) {
      return Character.valueOf((char)(value + 'A' - 1));
    }
    else
      return null;
  }
  
  private boolean isLetter(char c) {
    return (c >= 'A' && c <= 'Z');
  }
}
