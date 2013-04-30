package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternValue extends Value {
  private Value[] values = null;
  private static TypeValue type = new TypeValue("Pattern", 1, false);

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  /**
   * Creates a new pattern from the given parameters. The method
   * checks the values to see if they're allowed in patterns, throws
   * an error if they're not allowed.
   * @param patterns       the desired values to be added to the pattern
   * @throws StandardError if one of the values isn't compatible in
   *                       patterns
   */
  public PatternValue(Value ... patterns) throws StandardError {
    for(Value val : patterns) {
      if(isNotPatTypeCompatible(val))
        throw new TypeError("The supplied value (" + val + ") cannot be used in patterns");
    }

    this.values = patterns;
  }

  @Override
  /** {@inheritDoc} */
  public Value add(Value other) throws StandardError {
    if(other.is(ListValue.type()))
      return ListValue.prepend(this, other);
    throw new TypeError("Cannot add " + other + " to a pattern");
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("/");

    for(int i = 0; i < values.length - 1; i++) {
      sb.append(values[i] + " ");
    }
    sb.append(values[values.length-1] + "/");

    return sb.toString();
  }

  /**
   * Checks the parameter to see if it's allowed in patterns.
   * @param v              the value to be checked
   * @return               true if the value can exist in patterns
   * @throws StandardError when trying to add an invalid value to
   *                       a pattern
   */
  protected boolean isPatTypeCompatible(Value v) throws StandardError {
    if(v.is(IntValue.type()))
      return true;
    else if(v.is(StrValue.type()))
      return true;
    else if(v.is(DirValue.type()))
      return true;
    else if(v.is(CoordValue.type()))
      return true;
    else if(v.is(FunValue.type()))
      return true;
    else if(v.is(ListValue.type()))
      return true;
    else if(v.is(ActionValue.type()))
      return true;
    else if(v.is(PatternValue.type()))
      return true;
    else if(v.is(PatternMultValue.type()))
      return true;
    else if(v.is(PatternOptValue.type()))
      return true;
    else if(v.is(PatternPlusValue.type()))
      return true;
    else if(v.is(PatternNotValue.type()))
      return true;
    else if(v.is(PatternKeyValue.type()))
      return true;
    else if(v.is(PatternOrValue.type()))
      return true;
    return false;
  }

  /** @link isPatTypeCompatible */
  protected boolean isNotPatTypeCompatible(Value v) throws StandardError {
    return !isPatTypeCompatible(v);
  }
}
