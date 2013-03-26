package dk.aau.cs.d402f13.scopechecker;

import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.scopechecker.errors.*;
import dk.aau.cs.d402f13.scopechecker.utilities.SymbolTable;
import dk.aau.cs.d402f13.scopechecker.utilities.SymbolTable.SymbolType;
import dk.aau.cs.d402f13.utilities.errors.StandardError;


public class ScopeChecker extends DefaultVisitor {
    private SymbolTable currentST;
    private boolean varDeclaringMode; //if true, next VAR seen is a declaration, else a use
   
    public void openScope(){
      currentST = new SymbolTable(currentST); //make new symbolTable point to old symbolTable to preserve scope hierarchy
      System.out.println(currentST.nestPrefix() + "BEGIN SCOPE");
    }
    
    private void closeScope() throws ScopeError {
      System.out.println(currentST.nestPrefix() + "END SCOPE");
      currentST.checkErrors(); //will throw a ScannerError exception if errors are found
      currentST = currentST.getParent();
    }
    
    public ScopeChecker(AstNode root) throws StandardError, ScopeError{
      this.currentST = new SymbolTable();
      varDeclaringMode = false;
      openScope();
      insertDefaultFunctions(); //findSquares, union, ...
      visit(root);
      closeScope();
    }
    
   void insertDefaultFunctions() throws ScopeError{ //the functions that exists in our language
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "andSquares", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "findSquares", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "union", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "forall", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "isEmpty", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "move", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "moveAndCapture", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "isCurrentPlayer", -1, 0);
     currentST.foundDeclaredSymbol(SymbolType.FUNCTION, "isFirstMove", -1, 0);
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
  protected Object visitFunction(AstNode node) throws StandardError {
    currentST.foundUsedSymbol(SymbolType.FUNCTION, node.value, node.line, node.offset);
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
  protected Object visitFuncDef(AstNode node) throws StandardError {
    //Save the name of the function defined
    Iterator<AstNode> it = node.iterator();
    
    //FUNC_DEF = FUNC - VARLIST - EXPRESSION
    
    String funcName = it.next().value;
    currentST.foundDeclaredSymbol(SymbolType.FUNCTION, funcName, node.line, node.offset);
    openScope(); //open new scope, so function arguments can be hidden
    varDeclaringMode = true;
    visit(it.next()); //traverse the VARLIST (the arguments)
    varDeclaringMode = false;
      visit(it.next()); //traverse the expression
    closeScope();
    return null;
  }

  @Override
  protected Object visitVar(AstNode node) throws StandardError {
    if (varDeclaringMode){
      currentST.foundDeclaredSymbol(SymbolType.VARIABLE, node.value, node.line, node.offset);
    }
    else{
      currentST.foundUsedSymbol(SymbolType.VARIABLE, node.value, node.line, node.offset);
    }
    return null;
  }

}
