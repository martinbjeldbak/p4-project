package dk.aau.cs.d402f13.utilities.ast;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public abstract class DefaultVisitor extends Visitor {

  protected void visitChildren(AstNode node) throws StandardError{
    for (AstNode child : node){
      visit(child);
    }
  }
  
  @Override
  protected Object visitAssignment(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitConstant(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitType(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitTypeDef(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object vistAbstractTypeDef(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitTypeBody(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitCoordLit(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitDirLit(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitConstantDef(AstNode node) throws StandardError {
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
  protected Object visitSuper(AstNode node) throws StandardError {
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
  
  @Override
  protected Object visitNegation(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitElement(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitMemberAccess(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitCallSequence(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitLoSequence(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitEqSequence(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitCmSequence(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitAsSequence(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
  
  @Override
  protected Object visitMdSequence(AstNode node) throws StandardError {
    visitChildren(node);
    return null;
  }
}
