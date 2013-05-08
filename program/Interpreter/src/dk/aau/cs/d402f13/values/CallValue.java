package dk.aau.cs.d402f13.values;

import java.util.HashMap;

import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.Member;
import dk.aau.cs.d402f13.interpreter.Scope;
import dk.aau.cs.d402f13.utilities.errors.DivideByZeroError;
import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;

public class CallValue extends Value implements Cloneable {

  private Interpreter interpreter;
  private FunValue function;
  private Value[] parameters;
  
  private Value value = null;
  
  public CallValue(Interpreter interpreter, FunValue function, Value... parameters) {
    this.interpreter = interpreter;
    this.function = function;
    this.parameters = parameters; 
  }
  
  public void evaluate() throws StandardError {
    if (value == null) {
      value = function.call(interpreter, parameters);
    }
  }
  
  public FunValue getFunction() {
    return function;
  }
  
  public Value[] getParameters() {
    return parameters;
  }
  
  public Value getValue() throws StandardError {
    evaluate();
    return value;
  }
  
  @Override
  public Value getMember(String member) throws StandardError {
    evaluate();
    return value.getMember(member);
  }

  /** {@inheritDoc}  */
  @Override
  public TypeValue getType() throws StandardError {
    evaluate();
    if (value == this) {
      throw new InternalError("Recursion error in CallValue");
    }
    return value.getType();
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value as(TypeValue type) throws StandardError {
    evaluate();
    return value.as(type);
  }

  /** {@inheritDoc}  */
  @Override
  public Value call(Interpreter interpreter, Value ... actualParameters) throws StandardError {
    evaluate();
    return value.call(interpreter, actualParameters);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThan(Value other) throws StandardError {
    evaluate();
    return value.lessThan(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue lessThanEq(Value other) throws StandardError {
    evaluate();
    return value.lessThanEq(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThan(Value other) throws StandardError {
    evaluate();
    return value.greaterThan(other);
  }

  /** {@inheritDoc}  */
  @Override
  public BoolValue greaterThanEq(Value other) throws StandardError {
    evaluate();
    return value.greaterThanEq(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value add(Value other) throws StandardError {
    evaluate();
    return value.add(other);
  }

  /** {@inheritDoc}  */
  @Override
  public Value subtract(Value other) throws StandardError {
    evaluate();
    return value.subtract(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value multiply(Value other) throws StandardError {
    evaluate();
    return value.multiply(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value divide(Value other) throws StandardError {
    evaluate();
    return value.divide(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value mod(Value other) throws StandardError {
    evaluate();
    return value.mod(other);
  }
  
  /** {@inheritDoc}  */
  @Override
  public Value negate() throws StandardError {
    evaluate();
    return value.negate();
  }
}
