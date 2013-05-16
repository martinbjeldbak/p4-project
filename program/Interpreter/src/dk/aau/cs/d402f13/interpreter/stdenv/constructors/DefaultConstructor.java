package dk.aau.cs.d402f13.interpreter.stdenv.constructors;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class DefaultConstructor implements Callable {
  
  private TypeValue type;
  
  public DefaultConstructor(TypeValue type) {
    this.type = type;
  }

  @Override
  public Value call(Interpreter interpreter, Value... actualParameters)
      throws StandardError {
    if (!actualParameters[0].is(type)) {
      throw new ArgumentError("Invalid argument #1, expected type " + type);
    }
    return actualParameters[0].as(type);
  }

}
