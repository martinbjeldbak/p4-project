package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public abstract class Value {

  public abstract Type getType();
  
  public BoolValue lessThan(Value other) throws TypeError {
    throw new TypeError("This value does not the '<' operator");
  }
  
  public BoolValue lessThanEq(Value other) throws TypeError {
    throw new TypeError("This value does not the '<=' operator");
  }
  
  public BoolValue greaterThan(Value other) throws TypeError {
    throw new TypeError("This value does not support the '>' operator");
  }
  
  public BoolValue greaterThanEq(Value other) throws TypeError {
    throw new TypeError("This value does not the '>=' operator");
  }
  
  public BoolValue equalsOp(Value other) {
    return BoolValue.falseValue();
  }
  
  public final BoolValue notEqual(Value other) {
    if(this.equalsOp(other) == BoolValue.falseValue())
      return BoolValue.trueValue();
    return BoolValue.falseValue();
  }
  
  public Value add(Value other) throws TypeError {
    throw new TypeError("This value does not the '+' operator");
  }
  
  public Value subtract(Value other) throws TypeError {
    throw new TypeError("This value does not support subraction");
  }
  
  public Value multiply(Value other) throws TypeError {
    throw new TypeError("This value does not support multiplication");
  }
  
  public Value divide(Value other) throws TypeError, DivideByZeroError {
    throw new TypeError("This value does not support division");
  }
}
