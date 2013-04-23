package dk.aau.cs.d402f13.values;

public class ActionValue extends Value {

  private static TypeValue type = new TypeValue("Action", 1, false);
  
  @Override
  public TypeValue getType() {
    return type;
  }
  
  public static TypeValue type() {
    return type;
  }

}
