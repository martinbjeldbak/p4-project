package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class CoordValue extends Value {
  private final int x, y;
  
  public CoordValue(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public CoordValue(String value) {
    int xx = 0;
    int yy = 0;
    
    for(char c : value.toCharArray()) {
      if(isLetter(c)) {
        xx = xx * 26 + (c - 'A') + 1;
      } else {
        yy = yy * 10 + Integer.parseInt(Character.toString(c));
      }
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
  public Value add(Value other) throws TypeError {
    if(other instanceof StrValue)
      return new StrValue(this.toString() + ((StrValue)other).getValue());
    else if(other instanceof DirValue) {
      DirValue oDir = (DirValue)other;
      return new CoordValue(x + oDir.getX(), y + oDir.getY());
    }
    throw new TypeError("Addition cannot be done on coordinates with " + other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws TypeError {
    if(other instanceof CoordValue) {
      CoordValue oCoord = (CoordValue)other;
      return new CoordValue(x - oCoord.getX(), y - oCoord.getY());
    }
    else if(other instanceof DirValue) {
      DirValue oDir = (DirValue)other;
      return new CoordValue(x - oDir.getX(), y - oDir.getY());
    }
    throw new TypeError("Cannot subract a " + other + " from a coordinate");
  }
  
  @Override
  public String toString() {
    return toColumn(x) + y;
  }
  
  private String toColumn(int xValue) {
    StringBuilder sb = new StringBuilder();
    int remain = xValue;
    
    // FIXME: DOESNT WORK WITH 'Z'
    do {
      if(remain % 26 == 0)
        sb.append(intCharToLetter(26));
      else
        sb.append(intCharToLetter(remain % 26));
      remain = remain / 26;
    } while (remain > 0);

   return sb.reverse().toString();
  }
  
  private Character intCharToLetter(int value) {
    if (value >= 0 && value < 27) {
      return Character.valueOf((char)(value + 'A' - 1));
    }
    else if(value == 0)
      return 'Z';
    else
      return null;
  }
  
  private boolean isLetter(char c) {
    return (c >= 'A' && c <= 'Z');
  }
}
