package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class PatternOptValue extends Value {
  private static TypeValue type = new TypeValue("Pattern Optional", 1, false);
  private Value value;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  public PatternOptValue(Value pat) {
    this.value = pat;
  }

  @Override
  public String toString() {
    return this.value.toString() + "?";
  }
}
