package dk.aau.cs.d402f13.scopechecker;

import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.SymbolTable;
import dk.aau.cs.d402f13.utilities.SymbolTable.SymbolType;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;


public class ScopeChecker extends DefaultVisitor {
    private SymbolTable currentST;
    private boolean varDeclaringMode; //if true, next VAR seen is a declaration, else a use
   
    public void openScope(){
      currentST = new SymbolTable(currentST); //make new symbolTable point to old symbolTable to preserve scope hierarchy
    }
    
    private void closeScope() throws ScopeError {
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
  protected Object visitFunction(AstNode node) throws StandardError {
    currentST.foundUsedSymbol(SymbolType.FUNCTION, node.value, node.line, node.offset);
    System.out.println("Found use of func: " + node.value + " on line: " + node.line + " offset " + node.offset);
    return null;
  }
  
  @Override
  protected Object visitLambdaExpr(AstNode node) throws StandardError {
    //VARLIST - EXPR
    Iterator<AstNode> it = node.iterator();
    varDeclaringMode = true;
    visit(it.next()); //visit VARLIST
    varDeclaringMode = false;
    visit(it.next()); //visit EXPR
    return null; 
  }
  
  @Override
  protected Object visitFuncDef(AstNode node) throws StandardError {
    //Save the name of the function defined
    Iterator<AstNode> it = node.iterator();
    
    //FUNC_DEF = FUNC - VARLIST - EXPRESSION
    
    String funcName = it.next().value;
    currentST.foundDeclaredSymbol(SymbolType.FUNCTION, funcName, node.line, node.offset);
    System.out.println("Found decl of func: " + funcName + " on line: " + node.line + " offset " + node.offset);
    varDeclaringMode = true;
    visit(it.next()); //traverse the VARLIST
    varDeclaringMode = false;
    openScope(); //open new scope, so function arguments can be hidden
      visit(it.next()); //traverse the expression
    closeScope();
    return null;
  }

  @Override
  protected Object visitVar(AstNode node) throws StandardError {
    if (varDeclaringMode){
      currentST.foundDeclaredSymbol(SymbolType.VARIABLE, node.value, node.line, node.offset);
      System.out.println("Found decl of var: " + node.value + " on line: " + node.line + " offset " + node.offset);
    }
    else{
      currentST.foundUsedSymbol(SymbolType.VARIABLE, node.value, node.line, node.offset);
      System.out.println("Found use of var: " + node.value + " on line: " + node.line + " offset " + node.offset);
    }
    return null;
  }

}
