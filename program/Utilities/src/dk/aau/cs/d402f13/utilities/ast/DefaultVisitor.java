package dk.aau.cs.d402f13.utilities.ast;

import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public abstract class DefaultVisitor extends Visitor {

  private void visitChildren(AstNode node) throws StandardError{
    for (AstNode next : node){
      visit(next);
    }
  }
  
  @Override
  protected Object visitAssignment(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitCoordLit(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitDecl(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitDeclStruct(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitDirLit(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitFunction(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitFuncCall(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitFuncDef(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitGameDecl(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitId(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitIfExpr(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitIntLit(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitKeyword(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitLambdaExpr(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitList(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitNotOperator(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitOperator(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitPattern(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitPatternKeyword(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitPatternMultiplier(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitPatternNot(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitPatternOperator(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitPatternOr(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitProgram(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitStringLit(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitThis(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitVar(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitVarlist(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

  @Override
  protected Object visitVars(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }

}
