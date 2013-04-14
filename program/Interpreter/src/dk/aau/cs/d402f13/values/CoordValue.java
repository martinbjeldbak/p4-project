package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class CoordValue extends Value {
  private final int x, y;

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
  
  @Override
  public String toString() {
    return toColumn(x) + y;
  }
  
  private String toColumn(int xValue) {
    int xx = xValue;
    
    
    
    return "";
  }
  
  private boolean isLetter(char c) {
    return (c >= 'A' && c <= 'Z');
  }
  


}
