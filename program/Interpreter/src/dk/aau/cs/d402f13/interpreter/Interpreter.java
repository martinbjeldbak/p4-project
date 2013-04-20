package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.interpreter.stdenv.StandardEnvironment;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.Visitor;
import dk.aau.cs.d402f13.utilities.errors.*;
import dk.aau.cs.d402f13.values.*;

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
  protected Value visitDirLit(AstNode node) throws StandardError {
      return new DirValue(node.value);
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
    throw new TypeError("Cannot use 'not' operator on " + v);
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
    Value v = symbolTable.getVariable(node.value);
    if (v == null) {
      throw new NameError("Undefined variable: " + node.value);
    }
    return v;
  }

  @Override
  protected Value visitVarlist(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitVars(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitNegation(AstNode node) throws StandardError {
    Value val = visit(node.getFirst());
    return val.negate();
  }

  @Override
  protected Value visitConstant(AstNode node) throws StandardError {
    Value v = symbolTable.getConstant(node.value);
    if (v == null) {
      throw new NameError("Undefined constant: " + node.value);
    }
    return v;
  }

  @Override
  protected Value visitType(AstNode node) throws StandardError {
    Value v = symbolTable.getType(node.value);
    if (v == null) {
      throw new NameError("Undefined type: " + node.value);
    }
    return v;
  }

  @Override
  protected Value visitTypeDef(AstNode node) throws StandardError {
    TypeValue type;
    String name = node.getFirst().value;
    if (node.size() > 3) {
      type = new TypeValue(name, node.get(1), (TypeValue)visit(node.get(2)), node.get(3));
    }
    else {
      type = new TypeValue(name, node.get(1));
    }
    
    if (node.getLast().type == AstNode.Type.TYPE_BODY) {
      for (AstNode defNode : node.getLast()) {
        type.addMember(defNode.getFirst().value, new Member(defNode));
      }
    }
    symbolTable.addType(name, type);
    
    return null;
  }
  
  /**
   * @TODO look at stuff
   * @param node
   * @return
   * @throws StandardError
   */
  @Override
  protected Value visitAbstractTypeDef(AstNode node) throws StandardError {
    TypeValue type;
    String name = node.getFirst().value;
    if (node.size() > 3) {
      type = new TypeValue(name, node.get(1), (TypeValue)visit(node.get(2)), node.get(3));
    }
    else {
      type = new TypeValue(name, node.get(1));
    }
    
    if (node.getLast().type == AstNode.Type.TYPE_BODY) {
      for (AstNode defNode : node.getLast()) {
        //type.addMember(defNode.getFirst().value, new Member());
      }
    }
    
    return null;
  }

  @Override
  protected Value visitTypeBody(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitAbstractDef(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitConstantDef(AstNode node) throws StandardError {
    if (node.size() < 3) {
      symbolTable.addConstant(node.getFirst().value, visit(node.getLast()));
    }
    else
      symbolTable.addConstant(node.getFirst().value, new FunValue(node.get(1), node.get(2)));
    return null;
  }

  @Override
  protected Value visitSuper(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitElement(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitMemberAccess(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Value visitCallSequence(AstNode node) throws StandardError {
    Value object = visit(node.getFirst());
    for (int i = 1; i < node.size(); i++) {
      ListValue list = (ListValue)visit(node.get(i));
      object = object.call(this, list.getValues());
    }
    return object;
  }

  @Override
  protected Value visitLoSequence(AstNode node) throws StandardError {
    Value v = visit(node.getFirst());
    
    for(int i = 1; i < node.size(); i++) {
      AstNode child = node.get(i);
      Value val = visit(child);
      
      if(v instanceof BoolValue && val instanceof BoolValue) {
        switch(child.operation) {
        case "and":
          v = ((BoolValue)v).and((BoolValue)val);
          break;
        case "or":
          v = ((BoolValue)v).or((BoolValue)val);
        default:
          v = val;
        }
      }
      else
        v = BoolValue.falseValue();
    }
    return v;
  }

  @Override
  protected Value visitEqSequence(AstNode node) throws StandardError {
    Value v = visit(node.getFirst());
    
    for(int i = 1; i < node.size(); i++) {
      AstNode child = node.get(i);
      Value val = visit(child);
      
      switch(child.operation) {
      case "==":
        v = v.equalsOp(val);
        break;
      case "!=":
        v = v.notEqual(val);
        break;
      default:
        v = val;
      }
    }
    return v;
  }

  @Override
  protected Value visitCmSequence(AstNode node) throws StandardError {
    Value v = visit(node.getFirst());
    
    for(int i = 1; i < node.size(); i++) {
      AstNode child = node.get(i);
      Value val = visit(child);
      
      switch(child.operation) {
      case "<":
        v = v.lessThan(val);
        break;
      case ">":
        v = v.greaterThan(val);
        break;
      case "<=":
        v = v.lessThanEq(val);
        break;
      case ">=":
        v = v.greaterThanEq(val);
        break;
      default:
        v = val;
      }
    }
    return v;
  }

  @Override
  protected Value visitAsSequence(AstNode node) throws StandardError {
    Value v = visit(node.getFirst());
    
    for(int i = 1; i < node.size(); i++) {
      AstNode child = node.get(i);
      Value val = visit(child);
      
      switch(child.operation) {
      case "+":
        v = v.add(val);
        break;
      case "-":
        v = v.subtract(val);
        break;
      default:
        v = val;
      }
    }
    return v;
  }

  @Override
  protected Value visitMdSequence(AstNode node) throws StandardError {
    Value v = visit(node.getFirst());
    
    for(int i = 1; i < node.size(); i++) {
      AstNode child = node.get(i);
      Value val = visit(child);
      
      switch(child.operation) {
      case "*":
        v = v.multiply(val);
        break;
      case "/":
        v = v.divide(val);
        break;
      case "%":
        v = v.mod(val);
        break;
      default:
        v = val;
      }
    }
    return v;
  }

}
