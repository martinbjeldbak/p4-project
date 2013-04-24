package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.Value;

public interface ParentCallable {
  Value call(Interpreter interpreter) throws StandardError;
}
