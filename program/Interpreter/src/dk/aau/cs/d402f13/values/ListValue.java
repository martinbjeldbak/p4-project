package dk.aau.cs.d402f13.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class ListValue extends Value {

  private Value[] values = null;
  
  private static TypeValue type = new TypeValue("List", 1, false);

  public static ListValue prepend(Value val, Value list) throws TypeError {
    if(list.isNot(ListValue.type()))
      throw new TypeError("The supplied value needs to be a list");

    Value[] oValues = ((ListValue)list.as(ListValue.type())).getValues();
    Value[] ret = new Value[oValues.length + 1];
    ret[0] = val;
    System.arraycopy(oValues, 0, ret, 1, oValues.length);
    return new ListValue(ret);
  }

  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }
  
  public ListValue(Value ... values) {
    this.values = values;
  }
  
  public Value[] getValues() {
    return values;
  }

  public int getLength() {
    return values.length;
  }
  
  @Override
  public BoolValue equalsOp(Value other) {
    if(other.isNot(ListValue.type())) {
      return BoolValue.falseValue();
    }

    Value[] otherValues = new Value[0];
    try {
      otherValues = ((ListValue)other.as(ListValue.type())).getValues();
    } catch (TypeError typeError) {
      return BoolValue.falseValue();
    }

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

  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws TypeError {
    if(other.is(ListValue.type())) {
      Value[] oValues = ((ListValue)other.as(ListValue.type())).getValues();
      Value[] ret = new Value[values.length + oValues.length];

      System.arraycopy(values, 0, ret, 0, values.length);
      System.arraycopy(oValues, 0, ret, values.length, oValues.length);

      return new ListValue(ret);
    }
    // Else add the single element to the list
    Value[] ret = new Value[values.length + 1];
    System.arraycopy(values, 0, ret, 0, values.length);
    ret[values.length] = other;

    return new ListValue(ret);
  }

  /**
   * Removes all occurences of the supplied parameter in the current list
   * @param other the RHS of the subtraction operator
   * @return      a new list with all occurrences of the parameter removed
   * @throws TypeError
   */
  @Override
  public Value subtract(Value other) throws TypeError {
    if(other.is(ListValue.type())) {
      Value[] oValues = ((ListValue)other.as(ListValue.type())).getValues();
      List<Value> oValueList = Arrays.asList(oValues);
      List<Value> valueList = Arrays.asList(values);

      try {
      valueList.removeAll(oValueList);
      }
      catch(UnsupportedOperationException uoe) {
        valueList = new ArrayList<Value>(valueList);
        valueList.removeAll(oValueList);
      }

      Value[] resultArray = new Value[valueList.size()];
      return new ListValue(valueList.toArray(resultArray));
    }
    // Else remove the single element
    List<Value> valueList = Arrays.asList(values);

    for(Value val : valueList) {
      if(val.equals(other)) {
        try {
        valueList.remove(other);
        }
        catch(UnsupportedOperationException uoe) {
          valueList = new ArrayList<Value>(valueList);
          valueList.remove(other);
        }
      }
    }

    Value[] resultArray = new Value[valueList.size()];
    return new ListValue(valueList.toArray(resultArray));
  }

  @Override
  public Value call(Interpreter interpreter, Value ... actualParameters) throws StandardError {
    int a;
    int b;
    
    if(actualParameters.length < 1)
      throw new ArgumentError("Unexpected number of arguments, expected at least 1");

    if(actualParameters[0].isNot(IntValue.type()))
      throw new ArgumentError("First argument needs to be of type int");
    a = ((IntValue)actualParameters[0]).getValue();

    if (a < 0)
      a = values.length + a;
    if (a < 0 || a >= values.length)
      throw new ArgumentError("Argument out of bounds");
    
    if(actualParameters.length == 2) {
      if(actualParameters[1].isNot(IntValue.type()))
        throw new ArgumentError("Second argument also needs to be of type int");
      b = ((IntValue)actualParameters[1]).getValue();
      
      if (b < 0)
        b = values.length + b;
      
      if (b < 0 || b >= values.length) {
        throw new ArgumentError("Argument out of bounds");
      }
      return new ListValue(Arrays.copyOfRange(values, a, b + 1));
    }
    else if (actualParameters.length > 2)
      throw new ArgumentError("Using too many arguments, expected max 2");
    else
      return values[a];
  }
}