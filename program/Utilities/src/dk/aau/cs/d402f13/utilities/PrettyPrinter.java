package dk.aau.cs.d402f13.utilities;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.Visitor;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class PrettyPrinter extends Visitor {
  
  private String indentationString = "  ";
  private int indentationLevel = 0;
  private String indentation = "";
  
  public PrettyPrinter(String indentationString) {
    this.indentationString = indentationString;
  }
  
  public PrettyPrinter() {
    this("  ");
  }
  
  private String ind() {
    return indentation;
  }
  
  private void incr() {
    indentation += "  ";
    indentationLevel += 1;
  }
  
  private void decr() {
    indentationLevel -= 1;
    indentation = "";
    for (int i = 0; i < indentationLevel; i++) {
      indentation += indentationString;
    }
  }
  
  private boolean isElement(AstNode node) {
    return node.type == Type.VAR ||
        node.type == Type.LIST ||
        node.type == Type.PATTERN ||
        node.type == Type.KEYWORD ||
        node.type == Type.DIR_LIT ||
        node.type == Type.COORD_LIT ||
        node.type == Type.INT_LIT ||
        node.type == Type.STRING_LIT ||
        node.type == Type.ID ||
        node.type == Type.FUNCTION;
  }

  @Override
  public String visit(AstNode node) throws StandardError {
    return (String)super.visit(node);
  }

  @Override
  protected String visitAssignment(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitCoordLit(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitDecl(AstNode node) throws StandardError {
    String code = visit(node.get(0));
    code += " ";
    code += visit(node.get(1));
    return code + "\n";
  }

  @Override
  protected String visitDeclStruct(AstNode node) throws StandardError {
    String code = "{\n";
    incr();
    for (AstNode child : node) {
      code += indentation + visit(child);
    }
    decr();
    return code + indentation + "}";
  }

  @Override
  protected String visitDirLit(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitFunction(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitFuncCall(AstNode node) throws StandardError {
    String code = "";
    if (isElement(node.get(0))) {
      code += visit(node.get(0));
    }
    else {
      code += "(" + visit(node.get(0)) + ")";
    }
    code += visit(node.get(1));
    return code;
  }

  @Override
  protected String visitFuncDef(AstNode node) throws StandardError {
    String code = "define ";
    // TODO Auto-generated method stub
    return code + "\n";
  }

  @Override
  protected String visitGameDecl(AstNode node) throws StandardError {
    String code = "game ";
    code += visit(node.get(0));
    return code;
  }

  @Override
  protected String visitId(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitIfExpr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitIntLit(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitKeyword(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitLambdaExpr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitList(AstNode node) throws StandardError {
    String code = "[";
    int l = node.size() - 1;
    for (int i = 0; i < l; i++) {
      if (isElement(node.get(i))) {
        code += visit(node.get(i)) + " ";
      }
      else {
        code += "(" + visit(node.get(i)) + ") ";
      }
    }
    if (isElement(node.get(l))) {
      code += visit(node.get(l));
    }
    else {
      code += "(" + visit(node.get(l)) + ")";
    }
    return code + "]";
  }

  @Override
  protected String visitNotOperator(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitOperator(AstNode node) throws StandardError {
    String code = "";
    if (isElement(node.get(0))) {
      code += visit(node.get(0));
    }
    else {
      code += "(" + visit(node.get(0)) + ")";
    }
    code += " " + node.value + " ";
    code += visit(node.get(1));
    return code;
  }

  @Override
  protected String visitPattern(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitPatternKeyword(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitPatternMultiplier(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitPatternNot(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitPatternOperator(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitPatternOr(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String visitProgram(AstNode node) throws StandardError {
    String code = "";
    for (AstNode child : node) {
      code += visit(child);
    }
    return code;
  }

  @Override
  protected String visitStringLit(AstNode node) throws StandardError {
    return "\"" + node.value + "\"";
  }

  @Override
  protected String visitThis(AstNode node) throws StandardError {
    return "this";
  }

  @Override
  protected String visitVar(AstNode node) throws StandardError {
    return "$" + node.value;
  }

  @Override
  protected String visitVarlist(AstNode node) throws StandardError {
    // TODO Auto-generated method stub
    return null;
  }

}
