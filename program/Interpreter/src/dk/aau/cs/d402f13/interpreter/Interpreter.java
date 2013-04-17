package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.interpreter.stdenv.StandardEnvironment;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
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
  protected Object visitVars(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitNegation(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitConstant(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitType(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitTypeDef(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitTypeBody(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitConstantDef(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitSuper(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitElement(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitMemberAccess(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitCallSequence(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitLoSequence(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitEqSequence(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitCmSequence(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitAsSequence(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitMdSequence(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
