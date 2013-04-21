package dk.aau.cs.d402f13.interpreter.stdenv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.DirValue;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.StrValue;
import dk.aau.cs.d402f13.values.TypeValue;
import dk.aau.cs.d402f13.values.Value;

public class StandardEnvironment extends SymbolTable {

  public StandardEnvironment() {
    
    addType("Boolean", BoolValue.type());
    addType("Coordinate", CoordValue.type());
    addType("Direction", DirValue.type());
    addType("Function", FunValue.type());
    addType("Integer", IntValue.type());
    addType("List", ListValue.type());
    addType("Pattern", PatternValue.type());
    addType("String", StrValue.type());
    addType("Type", TypeValue.type());
    
    ////////////////////////////////////
    // Type functions
    ////////////////////////////////////
    
    addConstant("typeOf", new FunValue(
      1, false,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          return actualParameters[0].getType();
        }
      }
    ));
    
    ////////////////////////////////////
    // List functions
    ////////////////////////////////////
    addConstant("size", new FunValue(
      1, false,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          ListValue a = (ListValue)TypeValue.expect(actualParameters, 0, ListValue.type());
          return new IntValue(a.getValues().length);
        }
      }
    ));
    addConstant("union", new FunValue(
      1, true,
      new Callable() {
        @Override
        public Value call(Interpreter interpreter, Value... actualParameters)
            throws StandardError {
          ListValue a = (ListValue)TypeValue.expect(actualParameters, 0, ListValue.type());
          List<Value> result = new ArrayList<Value>(Arrays.asList(a.getValues()));
          for (int i = 1; i < actualParameters.length; i++) {
            ListValue b = (ListValue)TypeValue.expect(actualParameters, i, ListValue.type());
            Value[] values = b.getValues();
            for (int j = 0; j < values.length; j++) {
              if (!result.contains(values[j])) {
                result.add(values[j]);
              }
            }
          }
          Value[] resultArray = new Value[result.size()];
          return new ListValue(result.toArray(resultArray));
        }
      }
    ));
  }

}
