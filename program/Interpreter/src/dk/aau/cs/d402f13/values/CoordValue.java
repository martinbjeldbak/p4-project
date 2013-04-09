package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class CoordValue extends Value {
  private final int x, y;

  public CoordValue(String value) {
    int xx = 0;
    int yy = 0;
    
    for(char c : value.toCharArray()) {
      if(isLetter(c)) {
        xx = xx * 26 + c - 'A' + 1;
      } else {
        yy = yy * 10 + Integer.parseInt(Character.toString(c));
      }
    }
    
    this.x = xx;
    this.y = yy;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }

  @Override
  public Type getType() {
    return Type.COORD_LIT;
  }
  
  private boolean isLetter(char c) {
    return (c >= 'A' && c <= 'Z');
  }

}
