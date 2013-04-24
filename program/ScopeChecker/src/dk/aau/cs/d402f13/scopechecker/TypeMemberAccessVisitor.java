package dk.aau.cs.d402f13.scopechecker;

import java.util.HashMap;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;



public class TypeMemberAccessVisitor extends DefaultVisitor
{
  //This visitor checks:
  //For all member access, like $a.b.c[5].d it checks that the members accessed: b, c and d exists somewhere in any type
  //For all member access, like super.b.c[5].d it checks that the member b exists in any parent type and that c and d exists in any type
  //For all member access, like this.b.c[5].d it checks that the member b exists in the current type and that c and d exists somewhere in any type
  //For all member access, like b.c[5].d it checks that the member b, c and d exists in the current type or any type
  
  HashMap<String, TypeSymbolInfo> typeTable;
  TypeSymbolInfo currentType;
  Boolean thisMode;
  Boolean superMode;
  Boolean isMemberAccess;
  Boolean isFunction;
  Boolean canBeInAnyType;
  
  public void setTypeTable(HashMap<String, TypeSymbolInfo> typeTable){
    this.typeTable = typeTable;
  }
  
  @Override
  protected Object visitMemberAccess(AstNode node) throws StandardError {
    // CONSTANT [LIST]
    Iterator<AstNode> it = node.iterator();
    isMemberAccess = true;
    AstNode constant = it.next();
    if (it.hasNext()){ //it it has a list, it is a function
      this.isFunction = true;
    }
    visit(constant);
    this.isFunction = false;
    
    isMemberAccess = false;
    return null;
  }
  
  @Override
  protected Object visitElement(AstNode node) throws StandardError {
    canBeInAnyType = false;
    Iterator<AstNode> it = node.iterator();
    AstNode kind = it.next();
    if (kind.type == Type.SUPER)
      superMode = true;
    else if (kind.type == Type.THIS)
      thisMode = true;
    visit(kind);
    return null;
  }
  void checkExistence(String memberName) throws ScopeError{
    if (this.isFunction){   //function
      if (this.thisMode){
        if (findFunctionInThis(memberName))
          return;
      }
      else if (this.superMode){
        if (findFunctionInSuper(memberName))
          return;
      }
      else if (findFunctionAnywhere(memberName))
        return;
    }
    else{       //constant
      if (this.thisMode){
        if (findConstantInThis(memberName))
          return;
      }
      else if (this.superMode){
        if (findConstantInSuper(memberName))
          return;
      }
      else if (findConstantAnywhere(memberName))
        return;
    }
    throw new ScopeError("Could not find member " + memberName + " used in type " + this.currentType.name, this.currentType);
  }
  protected Object visitConstant(AstNode node) throws ScopeError{
    if (!isMemberAccess) return null;
    checkExistence(node.value);  //check that the member exists in this, super or any type
    superMode = false;
    thisMode = false;
    return null;
  }
  Boolean findFunctionInThis(String name) {
    for (Member m : this.currentType.concreteFunctions){
      if (m.name.equals(name)){
        return true;
      }
    }
    return false;
  }
  Boolean findFunctionInSuper(String name) {
    TypeSymbolInfo lookingIn = this.currentType.parent;
    while (lookingIn != null){
      for (Member m : this.currentType.concreteFunctions){
        if (m.name.equals(name)){
          return true;
        }
      }
   }
    return false;
  }
  Boolean findConstantInThis(String name) {
    for (Member m : this.currentType.concreteConstants){
      if (m.name.equals(name)){
        return true;
      }
    }
    return false;
  }
  Boolean findConstantInSuper(String name) {
    TypeSymbolInfo lookingIn = this.currentType.parent;
    while (lookingIn != null){
      for (Member m : this.currentType.concreteConstants){
        if (m.name.equals(name)){
          return true;
        }
      }
   }
    return false;
  }
  Boolean findConstantAnywhere(String name) {
    for (TypeSymbolInfo tsi : this.typeTable.values())
      for (Member m : tsi.concreteConstants){
        if (m.name.equals(name)){
          return true;
        }
      }
    return false;
  }
  Boolean findFunctionAnywhere(String name) {
    for (TypeSymbolInfo tsi : this.typeTable.values())
      for (Member m : tsi.concreteFunctions){
        if (m.name.equals(name)){
          return true;
        }
      }
    return false;
  }

  protected Object visitVar(AstNode node) {
    canBeInAnyType = true;
    superMode = false;
    thisMode = false;
    return null;
  }
  protected Object visitTypeDef(AstNode node) throws StandardError{
    setCurrentType(node.iterator().next().value);
    visitChildren(node);
    this.currentType = null;
    return null;
  }
  void setCurrentType(String typeName){
    this.currentType = typeTable.get(typeName);
  }
}

