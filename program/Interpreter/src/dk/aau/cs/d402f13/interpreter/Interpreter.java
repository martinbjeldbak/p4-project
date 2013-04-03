package dk.aau.cs.d402f13.interpreter;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.Visitor;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class Interpreter extends Visitor {

  public Interpreter() {
    // TODO Auto-generated constructor stub
  }

  @Override
  protected Object visitAssignment(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitCoordLit(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitDecl(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitDeclStruct(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitDirLit(AstNode node) throws StandardError {
    if(node.value != null && node.type == Type.DIR_LIT) {
      //return
    }
  }

  @Override
  protected Object visitFunction(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitFuncCall(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitFuncDef(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitGameDecl(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitId(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitIfExpr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Integer visitIntLit(AstNode node) throws StandardError {
    if(node.value != null && node.type == Type.INT_LIT) {
      return Integer.parseInt(node.value);
    }
    else {
      throw new StandardError("Integer value must not be null and of type int", node);
    }
  }

  @Override
  protected Object visitKeyword(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitLambdaExpr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitList(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitNotOperator(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitOperator(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    if(node.value != null && node.type == Type.OPERATOR) {
      
    }
    
    return null;
  }

  @Override
  protected Object visitPattern(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitPatternKeyword(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitPatternMultiplier(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitPatternNot(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitPatternOperator(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitPatternOr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitProgram(AstNode node) throws StandardError {
    if(node.value != null && node.type == Type.PROGRAM) {
      for(AstNode child : node){
        if(child.type == Type.FUNC_DEF) {
          visitFuncDef(child);
        }
        else if(child.type == Type.GAME_DECL) {
          visitGameDecl(child);
          break;
        }
        else {
          throw new StandardError("Node is not of type function def or game decl", node);
        }
      }
    }
    return null;
  }

  @Override
  protected String visitStringLit(AstNode node) throws StandardError {
    if(node.value != null && node.type == Type.STRING_LIT) {
      return node.value;
    }
    else {
      throw new StandardError("String literals cannot be null and have to be of type string", node);
    }
  }

  @Override
  protected Object visitThis(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitVar(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Object visitVarlist(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
