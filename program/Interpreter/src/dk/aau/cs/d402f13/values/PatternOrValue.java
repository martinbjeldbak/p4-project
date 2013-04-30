package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternOrValue extends PatternValue {
  private static TypeValue type = new TypeValue("Pattern Or", 1, false);
  private Value left, right;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  /**
   * Creates a new pattern or with left and right nodes. Can be
   * seen as 'left' | 'right'
   * @param left           the left pattern value to be added
   * @param right          the right pattern value to be added
   * @throws StandardError if either values are not allowed in
   *                       patterns
   */
  public PatternOrValue(Value left, Value right) throws StandardError {
    if (isNotPatTypeCompatible(left))
      throw new TypeError("The left value " + left + " is not allowed in patterns");
    else if (isNotPatTypeCompatible(right))
      throw new TypeError("The right value " + right + "is not allowed in patterns");

    this.left = left;
    this.right = right;
  }

  @Override
  public String toString() {
    return this.left + " | " + this.right;
  }
}
