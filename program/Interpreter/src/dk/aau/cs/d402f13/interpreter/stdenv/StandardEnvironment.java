package dk.aau.cs.d402f13.interpreter.stdenv;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.Value;

public class StandardEnvironment extends SymbolTable {

  public StandardEnvironment() {
    addFunction("add", new FunValue(
      2, false,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          if (!(actualParameters[0] instanceof IntValue
              && actualParameters[1] instanceof IntValue)) {
            throw new ArgumentError("Invalid arguments, expected two integers");
          }
          int a = ((IntValue)actualParameters[0]).getValue();
          int b = ((IntValue)actualParameters[1]).getValue();
          return new IntValue(a + b);
        }
      }
    ));
  }

}
