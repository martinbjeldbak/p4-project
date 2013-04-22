package dk.aau.cs.d402f13.interpreter.stdenv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dk.aau.cs.d402f13.interpreter.Callable;
import dk.aau.cs.d402f13.interpreter.ConstantCallable;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.SymbolTable;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.ConstMemberValue;
import dk.aau.cs.d402f13.values.ConstValue;
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
    
    ////////////////////////////////////
    // type: Boolean
    ////////////////////////////////////
    addType("Boolean", BoolValue.type());
    
    ////////////////////////////////////
    // type: Coordinate
    ////////////////////////////////////
    addType("Coordinate", CoordValue.type());
    
    ////////////////////////////////////
    // type: Direction
    ////////////////////////////////////
    addType("Direction", DirValue.type());
    
    ////////////////////////////////////
    // type: Function
    ////////////////////////////////////
    addType("Function", FunValue.type());
    
    ////////////////////////////////////
    // type: Integer
    ////////////////////////////////////
    addType("Integer", IntValue.type());
    
    ////////////////////////////////////
    // type: List
    ////////////////////////////////////
    addType("List", ListValue.type());
    
    ListValue.type().addStaticMember("size", new ConstMemberValue(new ConstantCallable() {
      @Override
      public Value call(Interpreter interpreter, Value object) throws StandardError {
        ListValue a = (ListValue)object;
        return new IntValue(a.getValues().length);
      }
    }));
    
    ListValue.type().addStaticMember("sort", new ConstMemberValue(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        return interpreter.getSymbolTable().getThis();
      }
    }));
    
    ////////////////////////////////////
    // type: Pattern
    ////////////////////////////////////
    addType("Pattern", PatternValue.type());
    
    ////////////////////////////////////
    // type: String
    ////////////////////////////////////
    addType("String", StrValue.type());
    
    ////////////////////////////////////
    // type: Type
    ////////////////////////////////////
    addType("Type", TypeValue.type());
    
    TypeValue.type().addStaticMember("isSubtypeOf", new ConstMemberValue(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        TypeValue a = (TypeValue)interpreter.getSymbolTable().getThis();
        TypeValue b = (TypeValue)TypeValue.expect(actualParameters, 0, TypeValue.type());
        a.ensureSuperType(interpreter);
        return a.isSubtypeOf(b) ? BoolValue.trueValue() : BoolValue.falseValue();
      }
    }));
    
    TypeValue.type().addStaticMember("isSupertypeOf", new ConstMemberValue(1, false, new Callable() {
      @Override
      public Value call(Interpreter interpreter, Value... actualParameters)
          throws StandardError {
        TypeValue a = (TypeValue)interpreter.getSymbolTable().getThis();
        TypeValue b = (TypeValue)TypeValue.expect(actualParameters, 0, TypeValue.type());
        b.ensureSuperType(interpreter);
        return a.isSupertypeOf(b) ? BoolValue.trueValue() : BoolValue.falseValue();
      }
    }));
    
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
