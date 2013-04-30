package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class PatMultValue extends  Value {
  private static TypeValue type = new TypeValue("Pattern Multiplier", 1, false);
  private Value value;
  private final int times;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  // For the '*' operator (0-*)
  public PatMultValue(Value value) {
    this.value = value;
    this.times = 0;
  }

  // If the amount of times to be repeated is fixed. I.e. /(e)2/
  public PatMultValue(Value value, String times) {
    this.value = value;
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
