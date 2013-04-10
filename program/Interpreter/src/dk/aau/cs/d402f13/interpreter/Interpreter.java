package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.interpreter.stdenv.StandardEnvironment;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.Visitor;
import dk.aau.cs.d402f13.utilities.errors.ArgumentError;
import dk.aau.cs.d402f13.utilities.errors.NameError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.errors.TypeError;
import dk.aau.cs.d402f13.values.BoolValue;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.DirValue;
import dk.aau.cs.d402f13.values.FunValue;
import dk.aau.cs.d402f13.values.IntValue;
import dk.aau.cs.d402f13.values.ListValue;
import dk.aau.cs.d402f13.values.PatternValue;
import dk.aau.cs.d402f13.values.StrValue;
import dk.aau.cs.d402f13.values.Value;

public class Interpreter extends Visitor {
  private SymbolTable symbolTable = new StandardEnvironment();

  public Interpreter() throws StandardError {
  }
  
  public SymbolTable getSymbolTable() {
    return symbolTable;
  }
  
  @Override
  public Value visit(AstNode node) throws StandardError {
    return (Value)super.visit(node);
  }

  @Override
  protected Value visitAssignment(AstNode node) throws StandardError {
    symbolTable.openScope();
    String var = node.get(0).value;
    Value val = visit(node.get(1));
    symbolTable.addVariable(var, val);
    for (int i = 2; i < node.size() -1; i++) {
      AstNode assign = node.get(i);
      var = assign.get(0).value;
      val = visit(assign.get(1));
      symbolTable.addVariable(var, val);
    }
    Value ret = visit(node.get(node.size() - 1));
    symbolTable.closeScope();
    return ret;
  }

  @Override
  protected CoordValue visitCoordLit(AstNode node) throws StandardError {
      return new CoordValue(node.value);
  }

  @Override
  protected Value visitDecl(AstNode node) throws StandardError {
    
    return null;
  }

  @Override
  protected Value visitDeclStruct(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitDirLit(AstNode node) throws StandardError {
      return new DirValue(node.value);
  }

  @Override
  protected Value visitFunction(AstNode node) throws StandardError {
    FunValue fun = symbolTable.getFunction(node.value);
    if (fun == null)
      throw new NameError("Undefined function: " + node.value);
    return fun;
  }

  @Override
  protected Value visitFuncCall(AstNode node) throws StandardError {
    Value v = visit(node.get(0));
    
    if(v instanceof FunValue) {
      ListValue list = (ListValue)visit(node.get(1));
      
      return ((FunValue)v).call(this, list.getValues());
    }
    else if(v instanceof ListValue) {
      
    }
    else
      throw new TypeError("Must be function or list value", node.get(0));
    
    return null;
  }

  @Override
  protected Value visitFuncDef(AstNode node) throws StandardError {
    symbolTable.addFunction(node.get(0).value, new FunValue(node.get(1), node.get(2)));
    
    return null;
  }

  @Override
  protected Value visitGameDecl(AstNode node) throws StandardError {
    visit(node.getFirst());
    
    return null;
  }

  @Override
  protected Value visitId(AstNode node) throws StandardError {
    
    return new StrValue(node.value);
  }

  @Override
  protected Value visitIfExpr(AstNode node) throws StandardError {
    Value b = visit(node.get(0));
    
    if (!(b instanceof BoolValue)) {
     throw new TypeError("Not a boolean", node.get(0)); 
    }
    
    if(b == BoolValue.trueValue())
      return visit(node.get(1));

    return visit(node.get(2));    
  }

  @Override
  protected IntValue visitIntLit(AstNode node) throws StandardError {
      return new IntValue(node.value);
  }

  @Override
  protected Value visitKeyword(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitLambdaExpr(AstNode node) throws StandardError {
    return new FunValue(node.getFirst(), node.getLast(), symbolTable.currentScope());
  }

  @Override
  protected Value visitList(AstNode node) throws StandardError {
    Value[] values = new Value[node.size()];
    
    for(int i = 0; i < node.size(); i++) {
      values[i] = visit(node.get(i));
    }
    
    return new ListValue(values);
  }

  @Override
  protected Value visitNotOperator(AstNode node) throws StandardError {
    Value v = visit(node.getFirst());
    
    if(v instanceof BoolValue) {
      return ((BoolValue)v).not();
    }
    throw new TypeError("Cannot use 'not' operator on type " + v.getType());
  }

  @Override
  protected Value visitOperator(AstNode node) throws StandardError {
    Value a = visit(node.getFirst());
    Value b = visit(node.getLast());
    switch(node.value) {
      case "and":
        if((a instanceof BoolValue) && (b instanceof BoolValue))
          return ((BoolValue)a).and((BoolValue)b);
        throw new TypeError("Both 'and' operands need to be of type boolean", node);
      case "or":
        if((a instanceof BoolValue) && (b instanceof BoolValue))
          return ((BoolValue)a).or((BoolValue)b);
        throw new TypeError("Both 'or' operands need to be of type boolean", node);
      case "<":
        return a.lessThan(b);
      case ">":
        return a.greaterThan(b);
      case "<=":
        return a.lessThanEq(b);
      case ">=":
        return a.greaterThanEq(b);
      case "==":
        return a.equalsOp(b);
      case "!=":
        return a.notEqual(b);
      case "+":
        return a.add(b);
      case "-":
        return a.subtract(b);
      case "*":
        return a.multiply(b);
      case "/":
        return a.divide(b);
    }
    
    return null;
  }

  @Override
  protected Value visitPattern(AstNode node) throws StandardError {
      for(AstNode child : node) {
        visit(child);
    }
    
    return null;
  }

  @Override
  protected Value visitPatternKeyword(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitPatternMultiplier(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitPatternNot(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitPatternOperator(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitPatternOr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitProgram(AstNode node) throws StandardError {
    for(AstNode child : node) {
      visit(child);
    }
    return null;
  }

  @Override
  protected StrValue visitStringLit(AstNode node) throws StandardError {
      return new StrValue(node.value);
  }

  @Override
  protected Value visitThis(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitVar(AstNode node) throws StandardError {
    return symbolTable.getVariable(node.value);
  }

  @Override
  protected Value visitVarlist(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitVars(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitNegation(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
