package dk.aau.cs.d402f13.values;

import java.util.Arrays;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class ListValue extends Value {

  private Value[] values = null;
  
  public ListValue(Value ... values) {
    this.values = values;
  }
  
  public Value[] getValues() {
    return values;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if (!(other instanceof ListValue)) {
      return BoolValue.falseValue();
    }
    Value[] otherValues = ((ListValue)other).getValues();
    if (otherValues.length != values.length) {
      return BoolValue.falseValue();
    }
    for (int i = 0; i < values.length; i++) {
      if (otherValues[i].equalsOp(values[i]) != BoolValue.trueValue()) {
        return BoolValue.falseValue();
      }
    }
    return BoolValue.trueValue();
  }
  
  @Override
  public String toString() {
    String s = "[";
    if (values.length > 0) {
      s += values[0].toString();
    }
    for (int i = 1; i < values.length; i++) {
      s += ", " + values[i].toString();
    }
    return s + "]";
  }
  
  // TODO: Addition and subtraction

  @Override
  public Value call(Interpreter interpreter, Value ... actualParameters) throws StandardError {
    int a;
    int b;
    
    if(actualParameters.length < 1) {
      throw new ArgumentError("Unexpected number of arguments, expected at least 1 "); // TODO
    }

    if(!(actualParameters[0] instanceof IntValue)) {
    }
    a = ((IntValue)actualParameters[0]).getValue();

    if (a < 0) {
      a = values.length + a;
    }
    if (a < 0 || a >= values.length) {
      throw new ArgumentError("Argument out of bounds");
    }
    
    if(actualParameters.length == 2) {
      if(!(actualParameters[1] instanceof IntValue)) {
      }   
      b = ((IntValue)actualParameters[1]).getValue();
      if (b < 0) {
        b = values.length + b;
      }
      if (b < 0 || b >= values.length) {
        throw new ArgumentError("Argument out of bounds");
      }
      return new ListValue(Arrays.copyOfRange(values, a, b + 1));
    }
    else if (actualParameters.length > 2) {
      throw new ArgumentError("Using too many arguments, expected max 2");
    }
    else{
      return values[a];
    }
  }
}
