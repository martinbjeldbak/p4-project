package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternNotValue extends PatternValue {
  private static TypeValue type = new TypeValue("Pattern Not", 1, false);
  private Value value;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  /**
   * Creates a new patternNotValue with the supplied
   * value. Throws an error if that value is not allowed
   * in patterns.
   * @param val            the value to be used in the
   *                       not pattern
   * @throws StandardError if the value is not allowed
   *                       in patterns
   */
  public PatternNotValue(Value val) throws StandardError{
    if(isNotPatTypeCompatible(val))
      throw new TypeError("The supplied value (" + val + ") cannot be used in pattern not");

    this.value = val;
  }

  @Override
  public String toString() {
      return "!" + this.value.toString();
  }
}
