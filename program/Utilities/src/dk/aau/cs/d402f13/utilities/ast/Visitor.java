package dk.aau.cs.d402f13.utilities.ast;

import dk.aau.cs.d402f13.utilities.errors.InternalError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public abstract class Visitor {

//  case ASSIGNMENT:
  protected abstract Object visitAssignment(AstNode node) throws StandardError;
// case CONSTANT:
  protected abstract Object visitConstant(AstNode node) throws StandardError;
// case TYPE:
  protected abstract Object visitType(AstNode node) throws StandardError;
// case TYPE_DEF:
  protected abstract Object visitTypeDef(AstNode node) throws StandardError;
// case TYPE_BODY:
  protected abstract Object visitTypeBody(AstNode node) throws StandardError;
// case ABSTRACT_DEF:
  protected abstract Object visitAbstractDef(AstNode node) throws StandardError;
//  case COORD_LIT:
  protected abstract Object visitCoordLit(AstNode node) throws StandardError;
//  case DIR_LIT:
  protected abstract Object visitDirLit(AstNode node) throws StandardError;
// case CONSTANT_DEF:
  protected abstract Object visitConstantDef(AstNode node) throws StandardError; 
//  case IF_EXPR:
  protected abstract Object visitIfExpr(AstNode node) throws StandardError;
//  case INT_LIT:
  protected abstract Object visitIntLit(AstNode node) throws StandardError;
//  case LAMBDA_EXPR:
  protected abstract Object visitLambdaExpr(AstNode node) throws StandardError;
//  case LIST:
  protected abstract Object visitList(AstNode node) throws StandardError;
//  case NOT_OPERATOR:
  protected abstract Object visitNotOperator(AstNode node) throws StandardError;
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
// case SUPER:
  protected abstract Object visitSuper(AstNode node) throws StandardError;
//  case THIS:
  protected abstract Object visitThis(AstNode node) throws StandardError;
//  case VAR:
  protected abstract Object visitVar(AstNode node) throws StandardError;
//  case VARLIST:
  protected abstract Object visitVarlist(AstNode node) throws StandardError;
//  case VARS:
  protected abstract Object visitVars(AstNode node) throws StandardError;
//  case NEGATION:
  protected abstract Object visitNegation(AstNode node) throws StandardError;
//  case ELEMENT:
  protected abstract Object visitElement(AstNode node) throws StandardError;
//  case MEMBER_ACCESS:
  protected abstract Object visitMemberAccess(AstNode node) throws StandardError;
//  case CALL_SEQUENCE:
  protected abstract Object visitCallSequence(AstNode node) throws StandardError;
//  case LO_SEQUENCE:
  protected abstract Object visitLoSequence(AstNode node) throws StandardError;
//  case EQ_SEQUENCE:
  protected abstract Object visitEqSequence(AstNode node) throws StandardError;
//  case CM_SEQUENCE:
  protected abstract Object visitCmSequence(AstNode node) throws StandardError;
//  case AS_SEQUENCE:
  protected abstract Object visitAsSequence(AstNode node) throws StandardError;
//  case MD_SEQUENCE:
  protected abstract Object visitMdSequence(AstNode node) throws StandardError;
  
  
  public Object visit(AstNode node) throws StandardError {
    try {
      // ORdered after the enum Type in AstNode
      switch (node.type) {
        case THIS:
          return visitThis(node);
        case SUPER:
          return visitSuper(node);
        case PATTERN_KEYWORD:
          return visitPatternKeyword(node);
        case PATTERN_OPERATOR:
          return visitPatternOperator(node);
        case COORD_LIT:
          return visitCoordLit(node);
        case DIR_LIT:
          return visitDirLit(node);
        case INT_LIT:
          return visitIntLit(node);
        case STRING_LIT:
          return visitStringLit(node);
        case CONSTANT:
          return visitConstant(node);
        case TYPE:
          return visitType(node);
        case VAR:
          return visitVar(node);
        case PROGRAM:
          return visitProgram(node);
        case CONSTANT_DEF:
          return visitConstantDef(node);
        case TYPE_DEF:
          return visitTypeDef(node);
        case TYPE_BODY:
          return visitTypeBody(node);
        case ABSTRACT_DEF:
          return visitAbstractDef(node);
        case VARLIST:
          return visitVarlist(node);
        case VARS:
          return visitVars(node);
        case ASSIGNMENT:
          return visitAssignment(node);
        case IF_EXPR:
          return visitIfExpr(node);
        case LAMBDA_EXPR:
          return visitLambdaExpr(node);
        case LIST:
          return visitList(node);
        case ELEMENT:
          return visitElement(node);
        case MEMBER_ACCESS:
          return visitMemberAccess(node);
        case CALL_SEQUENCE:
          return visitCallSequence(node);
        case PATTERN:
          return visitPattern(node);
        case PATTERN_OR:
          return visitPatternOr(node);
        case PATTERN_MULTIPLIER:
          return visitPatternMultiplier(node);
        case PATTERN_NOT:
          return visitPatternNot(node);
        case NEGATION:
          return visitNegation(node);
        case LO_SEQUENCE:
          return visitLoSequence(node);
        case EQ_SEQUENCE:
          return visitEqSequence(node);
        case CM_SEQUENCE:
          return visitCmSequence(node);
        case AS_SEQUENCE:
          return visitAsSequence(node);
        case MD_SEQUENCE:
          return visitMdSequence(node);
        case NOT_OPERATOR:
          return visitNotOperator(node);
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
    catch (Exception e) {
      throw new InternalError(e, node);
    }
  }

}
