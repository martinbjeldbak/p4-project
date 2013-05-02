package dk.aau.cs.d402f13.scopechecker;
import java.util.HashMap;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.Data;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.VarSymbolInfo;

enum AccessType
{
  THIS, SUPER, ANY
}

public class UsesAreDeclaredVisitor extends DefaultVisitor {
  //This visitor checks that all variables, constants, functions and members used are actually declared somewhere 
  
  private SymbolTable currentST; //table of vars constants and functions in current scope
  private HashMap<String, TypeSymbolInfo> typeTable;
  private Boolean varDeclaringMode; //if true, next VAR seen is a declaration, otherwise a use
  private AccessType accessType; //set to THIS, SUPER or ANY depending on kind of member accessed
  private TypeSymbolInfo currentType; //if the visitor is traversing inside a type, else null
  
  public UsesAreDeclaredVisitor(HashMap<String, TypeSymbolInfo> typeTable){
    this.typeTable = typeTable;
    this.currentType = this.typeTable.get("#GLOBAL");
    this.accessType = AccessType.ANY;
    }
  
  public void openScope(){
    currentST = new SymbolTable(currentST); //make new symbolTable point to old symbolTable to preserve scope hierarchy
  }
  
  private void closeScope() throws ScopeError {
   if (this.currentST.getParent() == null)    //if exiting global scope,
    currentST = currentST.getParent();
  }
  
@Override
protected Object visitElement(AstNode node) throws StandardError {
  //CALL_SEQUENCE (MEMBER_ACCESS)+

  Iterator<AstNode> it = node.iterator();
  AstNode temp = it.next();
  
  if (temp.type == Type.THIS){
    this.accessType = AccessType.THIS;
    temp = it.next();
  }
  else if (temp.type == Type.SUPER){
    this.accessType = AccessType.SUPER;
    temp = it.next();
  }
  else if (temp.type != Type.MEMBER_ACCESS){
    visit(temp);        //visit CALL_SEQUENCE
    temp = it.next();
  }
  
  // (MEMBER_ACCESS)+
  visit(temp);
  // {MEMBER_ACCESS}
  
  //the flag shall only be present for the first element access, since that access can return
  //an arbitrary object which we dont know anything about
  this.accessType = AccessType.ANY;
  
  while (it.hasNext())
    visit(it.next());
  
  return null;
}
  
@Override
protected Object visitMemberAccess(AstNode node) throws StandardError {
/* when accessing a member $a.someFunc[], this member can be located in any type by default
 * if a type is referenced before a member access, like A[].someFunc, this visitMemberAccess() method
 * is skipped by the visitType() method
 */
  //CONSTANT [LIST]
   Iterator<AstNode> it = node.iterator();
   String name = it.next().value; //get CONSTANT name
   //Remaining: [LIST]
   
   int argNum = -1;
   if (it.hasNext()){
     AstNode list = it.next();
     argNum = list.size();
     visit(list);       //visit LIST to check that variables used in list have been declared
   }
   
   foundUsedConst(name, argNum, true, node.line,node.offset);
   
   return null; 
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
protected Object visitTypeBody(AstNode node) throws StandardError {
  //When visiting a type's body, the data members must be added to the symbol table, 
  //so functions and constants in the type body can use these data members 
  openScope(); //open new scope, so constructor args can be hided
  
  for (Data d : this.currentType.data)
    this.currentST.foundDeclVar(new VarSymbolInfo(d.name, d.line, d.offset));
  
  //visit the content of the type body
  visitChildren(node);
  
  closeScope();
  return null;
}

@Override
protected Object visitSetExpr(AstNode node) throws StandardError {
  //SET_EXPR = VAR EXPR
  Iterator<AstNode> it = node.iterator();
  
  //find data member name
  String name = it.next().value;
  
  //check that data member exists in the current type
  if (!this.currentType.data.contains(new Member(name)))
    throw new ScopeError("Data member " + name + " not defined in current type " + this.currentType.name, node.line, node.offset);
  
  return null; 
}

@Override
protected Object visitAbstractTypeDef(AstNode node) throws StandardError {
  //It doesn't matter if the type is abstract or not in the semantic checks performed
  //Only the interpreter uses that info
  return visitTypeDef(node);
}

@Override
protected Object visitTypeDef(AstNode node) throws StandardError{
  //TYPE  VARLIST       [TYPE         LIST]      [TYPE_BODY]
  //name cnstr_args  supertype  spr_cnstr_args     body
  
  openScope(); //the constructor, members ... are local to a type, so open a scope
  Iterator<AstNode> it = node.iterator();

  //Extract the type name from TYPE
  String name = it.next().value;
          //VARLIST [TYPE LIST] [TYPE_BODY]
  
  TypeSymbolInfo oldType = this.currentType;
  this.currentType = this.typeTable.get(name);  //lookup the type name to find type reference
  
  varDeclaringMode = true;  //The constructor args are declarations
  visit(it.next());         //visit VARLIST (constructor args)
  varDeclaringMode = false;
  
          //[TYPE LIST] [TYPE_BODY]
  
  if (it.hasNext()){
      //TYPE LIST [TYPE_BODY] or TYPE_BODY
    AstNode temp = it.next();
    if (temp.type == Type.TYPE){
      //TYPE LIST [TYPE_BODY]
      visit(temp);          //visit TYPE
      visit(it.next());     //visit LIST
      // [TYPE_BODY]
      if (it.hasNext())     //a new scope is opened in the visitTypeBody method
        visit(it.next());
      }
    else{
      //TYPE_BODY
      visit(temp);          ////a new scope is opened in the visitTypeBody method
    }
  }
       
  //set currentType back to old type (#GLOBAL type), 
  //so global functions and constants can be declared to that type.
  this.currentType = oldType; 
  
  closeScope();
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
      varDeclaringMode = false;
      visit(it.next());             //visit EXPRESSION
      closeScope();
    }
    else{
      visit(tempNode);             //visit EXPRESSION
    }
    return null;
  }
  
  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError {
    //skip everything, since when meeting the CONSTANT (name of the abstract), it will be treated as a use
    return null;
  }
  
  @Override
  protected Object visitConstant(AstNode node) throws StandardError{
    foundUsedConst(node.value, -1, false, node.line, node.offset);
    return null; 
  }
  
  @Override
  protected Object visitProgram(AstNode node) throws StandardError{
    openScope();
    visitChildren(node);
    closeScope();
    return null;
  }
  
  @Override
  protected Object visitDataDef(AstNode node) throws StandardError{
    //DATA_DEF = VAR EXPRESSION
    Iterator<AstNode> it = node.iterator();
    it.next(); //skip VAR, which is saved in currentType.dataMembers (TypeVisitor did this)
    //visit EXPRESSION
    visit(it.next());
    return null;
  }
  
  @Override
  protected Object visitCallSequence(AstNode node) throws StandardError {
    //CALL_SEQUENCE = CONSTANT [LIST] | TYPE LIST
    Iterator<AstNode> it = node.iterator();
    AstNode temp = it.next();
    String name = temp.value;        //CONSTANT | TYPE
    //Remaining: [LIST]
    int argNum = -1;                    //means no parameter list
    if (it.hasNext()){                  
    AstNode list = it.next();           //LIST - the arguments
    argNum = list.size();               //number of arguments
                                        //variables that are arguments in a function call are uses
    visit(list);                        //visit LIST
    }
    if (temp.type == Type.CONSTANT){
        foundUsedConst(name, argNum, false, node.line, node.offset);
    }
    else {
      foundUsedType(name, argNum, node.line, node.offset);  //check that type exists
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
  public void foundUsedType(String name, int argNum, int line, int offset) throws ScopeError{
    //When a type is used, ensure that the type exists and constructor arg count matches
    TypeSymbolInfo tsi = this.typeTable.get(name);
    if (tsi == null){
     throw new ScopeError("Type " + name + " is used but not declared", line, offset);
    }
    else if (tsi.args != argNum){
      throw new ScopeError("Number of constructor arguments for type " + name + " mismatches. Received " + argNum + ", expected " + tsi.args, line, offset);
    }
  }
  
  
  private Boolean constDeclaredInType(String name, TypeSymbolInfo tsi){
    for (Member m : tsi.members){
      if (m.name.equals(name) && m.declaredInType == tsi)
        return true;
    }
    return false;
  }
  private Boolean constVisibleInType(String name, TypeSymbolInfo tsi){
    for (Member m : tsi.members){
      if (m.name.equals(name))
        return true;
    }
    return false;
  }
  private Boolean constVisibleInAnyType(String name){
    for (TypeSymbolInfo tsi : this.typeTable.values()){
      if (tsi.name == "#GLOBAL")    //skip check in global scope
        continue;
      for (Member m : tsi.members){
        if (m.name.equals(name) && m.declaredInType == tsi)
          return true;
      }
    }
    return false;
  }
  private Boolean constDeclaredInGlobal(String name, int line, int offset) throws ScopeError{
    TypeSymbolInfo globalType = this.typeTable.get("#GLOBAL");
    if (globalType.members.contains(new Member(name)))
      return true;
    return false;
  }
  
  public void foundUsedConst(String name, int argNum, Boolean member, int line, int offset) throws ScopeError{
    if (member){
      if (this.accessType == AccessType.THIS){
        //accept if visible in current type
        if (constDeclaredInType(name, this.currentType))
          return;
        throw new ScopeError("Member " + name + " used in type " +  this.currentType.name + " does not exist in type " + this.currentType.name, line, offset);
      }
      else if (this.accessType == AccessType.SUPER){
        //accept if visible in any super type of current type
        if (this.currentType.parent != null && constVisibleInType(name, this.currentType.parent))
          return;
        throw new ScopeError("Member " + name + " does not exist in a super type of " + this.currentType.name, line, offset);
      }
      else{
        //accept if visible in any type
        if (constVisibleInAnyType(name))
          return;
        throw new ScopeError("Member " + name + " used in type " +  this.currentType.name + " does not exist in any type ", line, offset);
      }
    }
    else{
      //not necessarily a member
      //accept if visible in global scope or visible in current type
      if (constDeclaredInGlobal(name, line, offset))
        return;
      if (constVisibleInType(name, this.currentType))
        return;
      throw new ScopeError("Member " + name + " used in type " + this.currentType + " does not exist in any type or global scope", line, offset);
    }
  }
}
