package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternOptValue extends PatternValue {
  private static TypeValue type = new TypeValue("Pattern Optional", 1, false);
  private Value value;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  public PatternOptValue(Value value) throws StandardError {
    if(isNotPatTypeCompatible(value))
      throw new TypeError("The supplied value (" + value + ") cannot be used in pattern optional");

    this.value = value;
  }

  @Override
  public String toString() {
    return this.value.toString() + "?";
  }
}
