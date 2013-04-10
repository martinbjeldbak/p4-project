package dk.aau.cs.d402f13.interpreter.stdenv;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.Value;

public class StandardEnvironment extends SymbolTable {

  public StandardEnvironment() {
    addFunction("size", new FunValue(
      1, false,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          if (!(actualParameters[0] instanceof ListValue)) {
            throw new ArgumentError("Invalid argument, expected a list");
          }
          int a = ((IntValue)actualParameters[0]).getValue();
          return new IntValue(((ListValue)actualParameters[0]).getValues().length);
        }
      }
    ));
  }

}
