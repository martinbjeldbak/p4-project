package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.SymbolTable.SymbolType;

class Member{
  ArrayList<String> varArgs;
  String name;
  public Member(String name){
    this.name = name;
    this.varArgs = new ArrayList<String>();
  }
  public void AddArg(String varName){
    this.varArgs.add(varName);
  }
}

class TypeInfo
{
  int line;
  int offset;
  TypeInfo parent;
  String parentName;
  String name;
  ArrayList<String> constructorArgs;
  ArrayList<String> superVarArgs;   //var args for call to supers constructor
  int superArgs;                    //number of arguments in call to supers constructor
  ArrayList<Member> constantMembers;
  ArrayList<Member> abstractMembers;
  public TypeInfo(String name, int line, int offset){
    this.name = name;
    this.line = line;
    this.offset = offset;
    this.parentName = "";
    constantMembers = new ArrayList<Member>();
    abstractMembers = new ArrayList<Member>();
    constructorArgs = new ArrayList<String>();
    superVarArgs = new ArrayList<String>();
    superArgs = 0;
  }
  public void SetParent(TypeInfo parent){
    this.parent = parent;
  }
  public void SetParentName(String parentName){
    this.parentName = parentName;
  }
  public void AddConstantMember(Member member){
    constantMembers.add(member);
  }
  public void AddAbstractMember(Member member){
    abstractMembers.add(member);
  }
  public void AddConstructorArg(String name){
    constructorArgs.add(name);
  }
  public void AddSuperArg(String name){
    superVarArgs.add(name);
  }
  public void IncrSuperArgCount(){
    this.superArgs++;
  }  
}

public class TypeVisitor extends DefaultVisitor
{
  ArrayList<TypeInfo> typeTable; //all classes found in program are put here as a ref to its ClassInfo object
  TypeInfo currentType; //the class we are currently inside
  public TypeVisitor(){
    typeTable = new ArrayList<TypeInfo>();
    currentType = null;
  }
  public ArrayList<TypeInfo> getTypeTable(){
    return this.typeTable;
  }
  public void AddType(TypeInfo ci) throws ScopeError{
    for (TypeInfo c : this.typeTable){
      if (ci.name == c.name){
        throw new ScopeError("Type with same name already declared", new SymbolInfo(SymbolType.TYPE, ci.name, ci.line, ci.offset));
      }
    }
    this.typeTable.add(ci);
  }

  @Override
  protected Object visitTypeDef(AstNode node) throws StandardError{
    //TYPE  VARLIST       [TYPE         LIST]      [TYPE_BODY]
    //name cnstr_args  supertype  spr_cnstr_args     body
    Iterator<AstNode> it = node.iterator();
    
    //get class name
    TypeInfo ci = new TypeInfo(it.next().value, node.line, node.offset );
    this.AddType(ci); //adds type and checks if another type with same name exists
    
    //get constructor arguments
    Iterator<AstNode> varlistIt = it.next().iterator();
    while (varlistIt.hasNext()){
      ci.AddConstructorArg(varlistIt.next().value);
    }
    
    if (!it.hasNext()) //having a type body is optional
      return null;
    //if the type extend another type, the next node is a TYPE node and then a LIST node. If not, next node is TYPE_BODY
    AstNode parentOrBody = it.next();
    if (parentOrBody.type == Type.TYPE){
      //get the class it extends
      ci.SetParentName(parentOrBody.value);
      
      //get the parameters to the supertype' constructor call
      //not all these parameters are var's, some could be int. Only save the vars, since we want to check they have been declared somewhere
      Iterator<AstNode> superArglistIt = it.next().iterator();
      while (superArglistIt.hasNext()){
        ci.IncrSuperArgCount(); //save the number of args used when calling the parent constructor. We later check that this number match the number of args
        AstNode next = superArglistIt.next();
        if (next.type == Type.VAR)
          ci.AddSuperArg(next.value);   //found a variable arg, save it
      }
      
      if (!it.hasNext()) //having a body is optional
        return null;
      parentOrBody = it.next();
    }
    
    currentType = ci; //set the current type so the visitBody method can see which type the body members belong to
    
    //visit body, which adds the class members
    visit(parentOrBody);
    
    currentType = null; //we are exiting this type now

    return null;
  }
  
  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError{
    //CONSTANT [VARLIST]
    if (currentType == null) throw new StandardError("Abstract definition outside class");  
    //this constantDefinition is inside a type
    Iterator<AstNode> it = node.iterator();

    //find name
    Member member = new Member(it.next().value);
    
    //find varlist if any exist, which is the members arguments
    if (it.hasNext()){
      Iterator<AstNode> varListIt = it.next().iterator();
      while (varListIt.hasNext()){
        member.AddArg(varListIt.next().value); 
      }
      currentType.AddAbstractMember(member);
    }
    return null;
  }
  
  
  @Override
  protected Object visitConstantDef(AstNode node) throws StandardError{
    //COSTANT VARLIST
    
    if (currentType == null){ //if a definition outside a type
      this.visitChildren(node);
      return null;
    }
    
    //this constantDefinition is inside a type
    Iterator<AstNode> it = node.iterator();

    //find name
    Member member = new Member(it.next().value);
    
    //find varlist, which is the members arguments
    Iterator<AstNode> varListIt = it.next().iterator();
    while (varListIt.hasNext()){
      member.AddArg(varListIt.next().value);
    }
    currentType.AddConstantMember(member);
    
    return null;
  }
        
}

