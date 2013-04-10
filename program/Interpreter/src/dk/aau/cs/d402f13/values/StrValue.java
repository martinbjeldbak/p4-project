package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class StrValue extends Value {
  private final String value;

  public StrValue(String value) {
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other instanceof StrValue) {
      if(this.value.equals(((StrValue) other).value))
        return BoolValue.trueValue();
    }
    return BoolValue.falseValue();
  }
  
  @Override
  public Value add(Value other) throws TypeError {
    
    if(other instanceof StrValue) 
      return new StrValue(this.value + ((StrValue)other).getValue());
    else if(other instanceof IntValue)
      return new StrValue(this.value + ((IntValue)other).toString());
    else if(other instanceof BoolValue)
      return new StrValue(this.value + ((BoolValue)other).toString());
    else if(other instanceof CoordValue)
      return new StrValue(this.value + ((CoordValue)other).toString());
    else if(other instanceof DirValue)
      return new StrValue(this.value + ((DirValue)other).toString());
    
    throw new TypeError("Cannot add a " + other.getType() + " to a string");
  }
  
  @Override
  public String toString() {
    return "" + this.value;
  }

  @Override
  public Type getType() {
    return Type.STRING_LIT;
  }

}
