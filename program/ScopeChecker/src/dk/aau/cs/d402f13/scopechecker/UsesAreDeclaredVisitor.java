package dk.aau.cs.d402f13.scopechecker;
import java.util.HashMap;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.ConstSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.FuncSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.VarSymbolInfo;

enum MemberAccessType{
  THIS, SUPER, ANY, THISSUPERGLOBAL
}

public class UsesAreDeclaredVisitor extends DefaultVisitor {
  //This visitor checks that all variables, constants, functions and members used are actually declared somewhere 
  
  private SymbolTable currentST; //table of vars constants and functions in current scope
  private HashMap<String, TypeSymbolInfo> typeTable;
  private boolean varDeclaringMode; //if true, next VAR seen is a declaration, otherwise a use
  private MemberAccessType memberAccessType;
  private TypeSymbolInfo currentType; //if the visitor is traversing inside a type, else null
  
  public UsesAreDeclaredVisitor(HashMap<String, TypeSymbolInfo> typeTable){
    this.typeTable = typeTable;
    this.currentType = typeTable.get("#GLOBAL");
    this.memberAccessType = MemberAccessType.THISSUPERGLOBAL; //next member found can be contained in this type, a parent type or the global scope
  }
  
  public void openScope(){
    currentST = new SymbolTable(currentST); //make new symbolTable point to old symbolTable to preserve scope hierarchy
  }
  
  private void closeScope() throws ScopeError {
   if (this.currentST.getParent() == null)    //if exiting global scope,
    currentST = currentST.getParent();
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
protected Object visitAbstractTypeDef(AstNode node) throws StandardError {
  Iterator<AstNode> it = node.iterator();

  String name = it.next().value;        //do NOT visit this CONSTANT, since it will be treated as a use
  TypeSymbolInfo oldType = this.currentType;
  this.currentType = this.typeTable.get(name);
  
  while(it.hasNext())
    visit(it.next());
    
  this.currentType = oldType;
  return null;
}

@Override
protected Object visitTypeDef(AstNode node) throws StandardError{
  Iterator<AstNode> it = node.iterator();

  String name = it.next().value;          //do NOT visit this CONSTANT, since it will be treated as a use
  TypeSymbolInfo oldType = this.currentType;
  this.currentType = this.typeTable.get(name);
  
  while(it.hasNext())
    visit(it.next());
  
  this.currentType = oldType;
  return null;
}

  @Override
  protected Object visitConstantDef(AstNode node) throws StandardError {
    Iterator<AstNode> it = node.iterator();
    //CONSTANT_DEF = CONSTANT - [VARLIST] - EXPRESSION
    it.next();                      //skip CONSTANT(name), since it will be treated as a use when visitConstant visits it
    AstNode tempNode = it.next();
    if (tempNode.type == Type.VARLIST){
      openScope();                  //open scope for the arguments if they exist
      varDeclaringMode = true;      //the vars in the arguments are declarations
      visit(tempNode);              //visit VARLIST (arguments)
      visit(it.next());             //visit EXPRESSION
      varDeclaringMode = false;
      closeScope();
    }
    else{
      visit(tempNode);             //visit EXPRESSION
    }
    return null;
  }
  
  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError {
    //skip everything, since when meeting the CONSTANT (name og the abstract), it will be treated as a use
    return null;
  }
  
  @Override
  protected Object visitConstant(AstNode node) throws StandardError{
    foundUsedConst(node.value, node.line, node.offset); 
    return null;
  }
  
  @Override
  protected Object visitCallSequence(AstNode node) throws StandardError {
    //CALL_SEQUENCE = CONSTANT [LIST]
    Iterator<AstNode> it = node.iterator();
    AstNode constant = it.next();
    String constName = constant.value;    //CONSTANT
    int argNum = 0;
    if (it.hasNext()){                    //then it is a function call
    AstNode tempNode = it.next();         //LIST - the arguments
    argNum = tempNode.size();             //number of arguments
                                          //variables that are arguments in a function call are uses
    visit(tempNode);                      //visit LIST
    }
    foundUsedFunc(constName, argNum, node.line, node.offset);
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
  
  public void foundUsedConst(String name, int line, int offset) throws ScopeError{
    if (this.memberAccessType == MemberAccessType.THISSUPERGLOBAL ||
        this.memberAccessType == MemberAccessType.THIS)
    {
      //return if found in current type
     for (Member m : this.currentType.concreteConstants){
       if (m.name.equals(name) && m.declaredInType == this.currentType)
         return;
     }
    }
    if (this.memberAccessType == MemberAccessType.THISSUPERGLOBAL)
    {
      //return if found in global type
      for (Member m : this.typeTable.get("#GLOBAL").concreteConstants){
        if (m.name.equals(name))
          return;
      }
    }
    if (this.memberAccessType == MemberAccessType.THISSUPERGLOBAL ||
        this.memberAccessType == MemberAccessType.SUPER)
    {
      //return if found in super type
      for (Member m : this.currentType.concreteConstants){
        if (m.name.equals(name) && m.declaredInType != this.currentType)
          return;
      }
    }
    //if not found, throw exception
    throw new ScopeError("Used but not declared", new ConstSymbolInfo(name, line, offset));
  }
  public void foundUsedFunc(String name, int argNum,int line, int offset) throws ScopeError{
    if (this.memberAccessType == MemberAccessType.THISSUPERGLOBAL ||
        this.memberAccessType == MemberAccessType.THIS)
    {
      //return if found in current type
      for (Member m : (this.currentType.concreteFunctions)){
        if (m.name.equals(name) && m.declaredInType == this.currentType){
          if (m.args == argNum)
            return;
          else
            throw new ScopeError("Number of arguments does not match, expected " + m.args + " got " + argNum, new FuncSymbolInfo(name, line, offset));
        }
      }
   }
    if (this.memberAccessType == MemberAccessType.THISSUPERGLOBAL)
    {
      //return if found in global type
      for (Member m : this.typeTable.get("#GLOBAL").concreteFunctions){
        if (m.name.equals(name)){
          if (m.args == argNum)
            return;
          else
            throw new ScopeError("Number of arguments does not match, expected " + m.args + " got " + argNum,new FuncSymbolInfo(name, line, offset));
        }
      }
    }
    if (this.memberAccessType == MemberAccessType.THISSUPERGLOBAL ||
        this.memberAccessType == MemberAccessType.SUPER)
    {
      //return if found in super type
     for (Member m : this.currentType.concreteFunctions){
         if (m.name.equals(name)){
           if (m.args == argNum)
             return;
           else
             throw new ScopeError("Number of arguments does not match, expected " + m.args + " got " + argNum,new FuncSymbolInfo(name, line, offset));
         }
      }
    }
    //if not found, throw exception
    throw new ScopeError("Used but not declared", new FuncSymbolInfo(name, line, offset));
  }
}
