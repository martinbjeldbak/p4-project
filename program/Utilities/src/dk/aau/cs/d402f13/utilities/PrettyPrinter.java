package dk.aau.cs.d402f13.utilities;

import java.lang.reflect.Array;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.Visitor;
import dk.aau.cs.d402f13.utilities.errors.StandardError;

public class PrettyPrinter extends Visitor {
  
  private String indentationString = "  ";
  private int indentationLevel = 0;
  private String indentation = "";
  
  private int maxWidth = 50;
  
  private boolean inPattern = false;
  
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
        node.type == Type.TYPE ||
        node.type == Type.DIR_LIT ||
        node.type == Type.COORD_LIT ||
        node.type == Type.INT_LIT ||
        node.type == Type.STRING_LIT ||
        node.type == Type.THIS ||
        node.type == Type.SUPER ||
        node.type == Type.CONSTANT;
  }
  
  //////////////////////////////////////
  //// Visitor implementation below ////
  //////////////////////////////////////

  @Override
  public String visit(AstNode node) throws StandardError {
    return (String)super.visit(node);
  }

  @Override
  protected String visitAssignment(AstNode node) throws StandardError {
    String code = "let ";
    code += visit(node.get(0)) + " = ";
    code += visit(node.get(1));
    int l = node.size() - 1;
    incr();
    for (int i = 2; i < l; i++) {
      code += ",\n";
      code += indentation + visit(node.get(i).get(0)) + " = ";
      code += visit(node.get(i).get(1));
    }
    code += "\n";
    code += indentation + "in " + visit(node.get(l));
    decr();
    return code;
  }

  @Override
  protected String visitCoordLit(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitDirLit(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitConstant(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitConstantDef(AstNode node) throws StandardError {
    String code = "define ";
    code += visit(node.get(0)) + " ";
    code += visit(node.get(1)) + "\n";
    incr();
    code += indentation + visit(node.get(2));
    decr();
    return code + "\n\n";
  }

  @Override
  protected String visitIfExpr(AstNode node) throws StandardError {
    String code = "if ";
    incr();
    code += visit(node.get(0)) + "\n" + indentation + "then ";
    code += visit(node.get(1)) + "\n" + indentation + "else ";
    code += visit(node.get(2));
    decr();
    return code;
  }

  @Override
  protected String visitIntLit(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitLambdaExpr(AstNode node) throws StandardError {
    String code = "#";
    code += visit(node.get(0)) + " => ";
    code += visit(node.get(1));
    return code;
  }

  @Override
  protected String visitList(AstNode node) throws StandardError {
    String code = "[";
    incr();
    int l = node.size();
    if (l > 0) {
      if (l > 1) {
        code += "\n" + indentation;
      }
      code += visit(node.get(0));
      if (l > 1) {
        for (int i = 1; i < l; i++) {
          code += ",\n" + indentation;
          code += visit(node.get(i));
        }
        code += "\n";
      }
    }
    decr();
    if (l > 1) {
      code += indentation;
    }
    return code + "]";
  }

  @Override
  protected String visitNotOperator(AstNode node) throws StandardError {
    return "not " + visit(node.get(0));
  }

  @Override
  protected String visitPattern(AstNode node) throws StandardError {
    String code = "";
    boolean patternStarter = false;
    if (!inPattern) {
      code += "/";
      inPattern = true;
      patternStarter = true;
    }
    else {
      code += "(";
    }
    code += visit(node.get(0));
    for (int i = 1; i < node.size(); i++) {
      code += " " + visit(node.get(i));
    }
    if (patternStarter) {
      inPattern = false;
      code += "/";
    }
    else {
      code += ")";
    }
    return code;
  }

  @Override
  protected String visitPatternKeyword(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected String visitPatternMultiplier(AstNode node) throws StandardError {
    return "(" + visit(node.get(0)) + ")" + node.value;
  }

  @Override
  protected String visitPatternNot(AstNode node) throws StandardError {
    return "!" + visit(node.get(0));
  }

  @Override
  protected String visitPatternOperator(AstNode node) throws StandardError {
    return visit(node.get(0)) + node.value;
  }

  @Override
  protected String visitPatternOr(AstNode node) throws StandardError {
    return visit(node.get(0)) + "|" + visit(node.get(1));
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
    String code = "[";
    int l = node.size();
    if (l > 0) {
      code += visit(node.get(0));
      for (int i = 1; i < l; i++) {
        code += ", " + visit(node.get(i));
      }
    }
    return code + "]";
  }

  @Override
  protected Object visitVars(AstNode node) throws StandardError {
    return "... $" + node.value;
  }
  
  @Override
  protected Object visitNegation(AstNode node) throws StandardError {
    String code = "-";
    if (isElement(node.get(0))) {
      code += visit(node.get(0));
    }
    else {
      code += "(" + visit(node.get(0)) + ")";
    }
    return code;
  }

  @Override
  protected Object visitType(AstNode node) throws StandardError {
    return node.value;
  }

  @Override
  protected Object visitTypeDef(AstNode node) throws StandardError {
    String code = "type " + visit(node.getFirst()) + visit(node.get(1));
    if (node.get(2).type == Type.TYPE) {
      incr();
      code += " " + visit(node.get(2)) + visit(node.get(3));
      decr();
    }
    if (node.getLast().type == Type.TYPE_BODY) {
      code += " " + visit(node.getLast());
    }
    return code + "\n\n";
  }

  @Override
  protected Object visitTypeBody(AstNode node) throws StandardError {
    String code = "{\n";
    incr();
    for (AstNode child : node) {
      code += indentation + visit(child);
    }
    decr();
    return code + "}\n";
  }

  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError {
    String code = "define abstract " + visit(node.getFirst());
    if (node.getLast().type == Type.VARLIST) {
      code += visit(node.getLast());
    }
    return code + "\n\n";
  }

  @Override
  protected Object visitSuper(AstNode node) throws StandardError {
    return "super";
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
