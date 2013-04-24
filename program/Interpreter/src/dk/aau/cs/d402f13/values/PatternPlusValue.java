package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class PatternPlusValue extends Value {
  private static TypeValue type = new TypeValue("Pattern Optional", 1, false);
  private Value value;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public PatternPlusValue(Value pattern) {
    this.value = pattern;
  }

  @Override
  public String toString() {
    return this.value.toString() + "+";
  }
}
