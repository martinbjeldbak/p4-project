package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternMultValue extends PatternValue {
  private static TypeValue type = new TypeValue("Pattern Multiplier", 1, false);
  private Value value;
  private int times;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  public Value getValue() {
    return value;
  }
  
  public int getTimes() {
    return this.times;
  }

  /**
   * Creates a new pattern multiplication value from the input value
   * @param value
   * @throws StandardError
   */
  public PatternMultValue(Value value) throws StandardError {
    if(isNotPatTypeCompatible(value))
      throw new TypeError("The supplied value (" + value + ") cannot be used in pattern zero-to-many");

    this.value = value;
    this.times = 0;
  }

  // If the amount of times to be repeated is fixed. I.e. /(e)2/
  public PatternMultValue(Value value, String times) throws StandardError {
    this(value);
    this.times = Integer.parseInt(times);
  }

  @Override
  public String toString() {
    if(times == 0)
      return this.value.toString() + "*";
    else
      return this.value.toString() + " " + times;
  }
}
