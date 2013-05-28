package dk.aau.cs.d402f13.interpreter;

import java.util.Comparator;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class SortComparator implements Comparator<Value> {
  
  private Interpreter interpreter;
  private FunValue function;
  
  private StandardError error;

  public SortComparator(Interpreter interpreter, FunValue function) {
    this.interpreter = interpreter;
    this.function = function;
  }
  
  public StandardError getError() {
    return error;
  }
  
  @Override
  public int compare(Value a, Value b) {
    try {
      Value r = function.call(interpreter, a, b);
      IntValue ir = (IntValue)TypeValue.expect(r, IntValue.type());
      return ir.getValue();
    }
    catch (StandardError e) {
      error = e;
    }
    return 0;
  }

}
