package dk.aau.cs.d402f13.utilities.ast;

import dk.aau.cs.d402f13.utilities.errors.StandardError;

public abstract class Visitor {

//  case ASSIGNMENT:
  protected abstract Object visitAssignment(AstNode node) throws StandardError;
//  case COORD_LIT:
  protected abstract Object visitCoordLit(AstNode node) throws StandardError;
//  case DECL:
  protected abstract Object visitDecl(AstNode node) throws StandardError;
//  case DECL_STRUCT:
  protected abstract Object visitDeclStruct(AstNode node) throws StandardError;
//  case DIR_LIT:
  protected abstract Object visitDirLit(AstNode node) throws StandardError;
//  case FUNCTION:
  protected abstract Object visitFunction(AstNode node) throws StandardError;
//  case FUNC_CALL:
  protected abstract Object visitFuncCall(AstNode node) throws StandardError;
//  case FUNC_DEF:
  protected abstract Object visitFuncDef(AstNode node) throws StandardError;
//  case GAME_DECL:
  protected abstract Object visitGameDecl(AstNode node) throws StandardError;
//  case ID:
  protected abstract Object visitId(AstNode node) throws StandardError;
//  case IF_EXPR:
  protected abstract Object visitIfExpr(AstNode node) throws StandardError;
//  case INT_LIT:
  protected abstract Object visitIntLit(AstNode node) throws StandardError;
//  case KEYWORD:
  protected abstract Object visitKeyword(AstNode node) throws StandardError;
//  case LAMBDA_EXPR:
  protected abstract Object visitLambdaExpr(AstNode node) throws StandardError;
//  case LIST:
  protected abstract Object visitList(AstNode node) throws StandardError;
//  case NOT_OPERATOR:
  protected abstract Object visitNotOperator(AstNode node) throws StandardError;
//  case OPERATOR:
  protected abstract Object visitOperator(AstNode node) throws StandardError;
//  case PATTERN:
  protected abstract Object visitPattern(AstNode node) throws StandardError;
//  case PATTERN_KEYWORD:
  protected abstract Object visitPatternKeyword(AstNode node) throws StandardError;
//  case PATTERN_MULTIPLITER:
  protected abstract Object visitPatternMultiplier(AstNode node) throws StandardError;
//  case PATTERN_NOT:
  protected abstract Object visitPatternNot(AstNode node) throws StandardError;
//  case PATTERN_OPERATOR:
  protected abstract Object visitPatternOperator(AstNode node) throws StandardError;
//  case PATTERN_OR:
  protected abstract Object visitPatternOr(AstNode node) throws StandardError;
//  case PROGRAM:
  protected abstract Object visitProgram(AstNode node) throws StandardError;
//  case STRING_LIT:
  protected abstract Object visitStringLit(AstNode node) throws StandardError;
//  case THIS:
  protected abstract Object visitThis(AstNode node) throws StandardError;
//  case VAR:
  protected abstract Object visitVar(AstNode node) throws StandardError;
//  case VARLIST:
  protected abstract Object visitVarlist(AstNode node) throws StandardError;
//  case VARS:
  protected abstract Object visitVars(AstNode node) throws StandardError;
  
  
  public Object visit(AstNode node) throws StandardError {
    try {
      switch (node.type) {
        case ASSIGNMENT:
          return visitAssignment(node);
        case COORD_LIT:
          return visitCoordLit(node);
        case DECL:
          return visitDecl(node);
        case DECL_STRUCT:
          return visitDeclStruct(node);
        case DIR_LIT:
          return visitDirLit(node);
        case FUNCTION:
          return visitFunction(node);
        case FUNC_CALL:
          return visitFuncCall(node);
        case FUNC_DEF:
          return visitFuncDef(node);
        case GAME_DECL:
          return visitGameDecl(node);
        case ID:
          return visitId(node);
        case IF_EXPR:
          return visitIfExpr(node);
        case INT_LIT:
          return visitIntLit(node);
        case KEYWORD:
          return visitKeyword(node);
        case LAMBDA_EXPR:
          return visitLambdaExpr(node);
        case LIST:
          return visitList(node);
        case NOT_OPERATOR:
          return visitNotOperator(node);
        case OPERATOR:
          return visitOperator(node);
        case PATTERN:
          return visitPattern(node);
        case PATTERN_KEYWORD:
          return visitPatternKeyword(node);
        case PATTERN_MULTIPLIER:
          return visitPatternMultiplier(node);
        case PATTERN_NOT:
          return visitPatternNot(node);
        case PATTERN_OPERATOR:
          return visitPatternOperator(node);
        case PATTERN_OR:
          return visitPatternOr(node);
        case PROGRAM:
          return visitProgram(node);
        case STRING_LIT:
          return visitStringLit(node);
        case THIS:
          return visitThis(node);
        case VAR:
          return visitVar(node);
        case VARLIST:
          return visitVarlist(node);
        case VARS:
          return visitVars(node);
        default:
          throw new StandardError("Unidentified node type: " + node.type);
      }
    }
    catch (StandardError e) {
      if (e.getNode() == null) {
        e.setNode(node);
      }
      throw e;
    }
  }

}
