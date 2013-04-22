package dk.aau.cs.d402f13.scopechecker;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.ConstSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable;
import dk.aau.cs.d402f13.utilities.scopechecker.VarSymbolInfo;


public class FuncVarScopeVisitor extends DefaultVisitor {
  private SymbolTable currentST; //used for VARs - can be nested
  private boolean varDeclaringMode; //if true, next VAR seen is a declaration, otherwise a use

  public FuncVarScopeVisitor(){
  
  }
  
  public void openScope(){
    currentST = new SymbolTable(currentST); //make new symbolTable point to old symbolTable to preserve scope hierarchy
  }
  
  private void closeScope() throws ScopeError {
   if (this.currentST.getParent() == null)    //if exiting global scope,
      currentST.checkConstErrors();        //check that no function references exist
    currentST = currentST.getParent();
  }

  @Override
  protected Object visitProgram(AstNode node) throws StandardError {
    varDeclaringMode = false;
    openScope();
    insertDefaultFunctions(); //findSquares, union, ...
    visitChildren(node);
    closeScope();
    return null;
  }
  
 void insertDefaultFunctions() throws ScopeError{ //the functions that exists in our language
   currentST.foundDeclConst(new ConstSymbolInfo("andSquares", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("findSquares", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("union", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("forall", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("isEmpty", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("move", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("moveAndCapture", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("isCurrentPlayer", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("isFirstMove", true, true, 0, -1, 0));
   currentST.foundDeclConst(new ConstSymbolInfo("size", true, true, 0, -1, 0));
 }

@Override
protected Object visitAssignment(AstNode node) throws StandardError {
  //VAR EXPR [ASSIGNMENT]+ EXPR       case 1
  //VAR EXPR                          case 2
  
  Iterator<AstNode> it = node.iterator();
  
  //if ASSIGNMENT contains two EXPR, open a new scope (case 1)
  //else, the assignments is just in the same scope (case 2)
  boolean simpleAssign = node.size() == 2;
 
  if (simpleAssign){
    varDeclaringMode = true;
    visit(it.next()); //visit VAR
    varDeclaringMode = false; //exit varDeclaringMode since Expr may use vars
    visit(it.next()); //visit EXPR
    return null;
  }
  
  openScope();
  
  varDeclaringMode = true;
  visit(it.next()); //visit VAR
  varDeclaringMode = false;
  while (it.hasNext()){
    visit(it.next());
  }
  closeScope();
  return null;
}


@Override
protected Object visitLambdaExpr(AstNode node) throws StandardError {
  //VARLIST - EXPR
  Iterator<AstNode> it = node.iterator();
  openScope();
  varDeclaringMode = true;
  visit(it.next()); //visit VARLIST
  varDeclaringMode = false;
  visit(it.next()); //visit EXPR
  closeScope();
  return null; 
}

  @Override
  protected Object visitConstantDef(AstNode node) throws StandardError {
    //functions can only be declared in the global function scope, which functionST represents 
    //Save the name of the function defined
    Iterator<AstNode> it = node.iterator();
    //CONSTANT_DEF = CONSTANT - VARLIST - EXPRESSION
    String constName = it.next().value;
    
    openScope(); //open new scope for the arguments and body for the constantDef
    
    AstNode varList = it.next();
    int argNum = varList.size();
    varDeclaringMode = true;  //set vardeclaring mode to true, 'cause the var's met in the constantDef are declarations
    visit(varList); //traverse the VARLIST (the arguments)
    varDeclaringMode = false;
    visit(it.next()); //traverse the expression
    closeScope();
    currentST.foundDeclConst(new ConstSymbolInfo(constName, true, false, argNum, node.line, node.offset));
    return null;
  }
  
  @Override
  protected Object visitCallSequence(AstNode node) throws StandardError {
    Iterator<AstNode> it = node.iterator();
    AstNode called = it.next();
    if (called.type == Type.CONSTANT){ //CALL_SEQUENCE = CONSTANT VARLIST
      String constName = called.value;  //CONSTNT
      int argNum = it.next().size();      //VARLIST
      currentST.foundUsedConst(new ConstSymbolInfo(constName, false, false, argNum, node.line, node.offset));
    }
    return null;
  }

  @Override
  protected Object visitVar(AstNode node) throws StandardError {
    if (varDeclaringMode){
      currentST.foundDeclVar(new VarSymbolInfo(node.value, node.line, node.offset));
    }
    else{
      currentST.foundUsedVar(new VarSymbolInfo(node.value, node.line, node.offset));
    }
    return null;
  }
}
