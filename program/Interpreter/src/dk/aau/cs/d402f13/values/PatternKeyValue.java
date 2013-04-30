package dk.aau.cs.d402f13.values;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class PatternKeyValue extends PatternValue{
  private static TypeValue type = new TypeValue("Pattern Keyword", 1, false);
  private String keyword;

  @Override
  public TypeValue getType() throws StandardError {
    return type;
  }

  public static TypeValue type() {
    return type;
  }

  /**
   * Creates a new pattern value representing a keyword
   * @param keyword        the keyword to be saved
   * @throws StandardError if the supplied parameter isn't
   *                       a keyword
   */
  public PatternKeyValue(String keyword) throws StandardError {
    switch (keyword.toLowerCase()) {
      case "friend":
        this.keyword = "friend";
        break;
      case "foe":
        this.keyword = "foe";
        break;
      case "empty":
        this.keyword = "empty";
        break;
      default:
        throw new TypeError("The string " + keyword + " is not a pattern keyword");
    }
  }

  @Override
  public String toString() {
    /* Capitalize the string (not needed... I think)
    StringBuilder sb = new StringBuilder(keyword);
    sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
    return sb.toString();
    */
    return keyword;
  }
}
