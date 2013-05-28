package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.ObjectValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class SetterCallable implements Callable {

  private TypeValue type;
  private String attribute;
  
  public SetterCallable(String attribute, TypeValue type) {
    this.attribute = attribute;
    this.type = type;
  }

  @Override
  public Value call(Interpreter interpreter, Value... actualParameters)
      throws StandardError {
    TypeValue.expect(actualParameters, 0, type);
    ObjectValue object = (ObjectValue)interpreter.getSymbolTable().getThis();
    return object.setAttribute(attribute, actualParameters[0]);
  }

}
