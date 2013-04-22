package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;

public class TypeVisitor extends DefaultVisitor
{
  ArrayList<TypeSymbolInfo> typeTable; //all classes found in program are put here as a ref to its ClassInfo object
  TypeSymbolInfo currentType; //the class we are currently inside
  public TypeVisitor(){
    typeTable = new ArrayList<TypeSymbolInfo>();
    currentType = null;
    //Add default types
    typeTable.add(new TypeSymbolInfo(null, "Piece", -1, 0));
  }
  public ArrayList<TypeSymbolInfo> getTypeTable(){
    return this.typeTable;
  }
  public void AddType(TypeSymbolInfo ci) throws ScopeError{
    for (TypeSymbolInfo c : this.typeTable){
      if (ci.name == c.name){
        throw new ScopeError("Type with same name already declared", ci);
      }
    }
    this.typeTable.add(ci);
  }

  @Override
  protected Object visitTypeDef(AstNode node) throws StandardError{
    //TYPE  VARLIST       [TYPE         LIST]      [TYPE_BODY]
    //name cnstr_args  supertype  spr_cnstr_args     body
    Iterator<AstNode> it = node.iterator();
    
    //get type name
    TypeSymbolInfo ci = new TypeSymbolInfo(node, it.next().value, node.line, node.offset );
    //the ASTNode node is saved so the typeDef can be changed to abstractTypeDef if ScopeChecker determines the type is abstract
    
    this.AddType(ci); //adds type and checks if another type with same name exists
    
    //get constructor arguments
    Iterator<AstNode> varlistIt = it.next().iterator();
    while (varlistIt.hasNext()){
      ci.incrArgCount(); //increase number of arguments in constructor
      varlistIt.next();
    }
    
    if (!it.hasNext()) //having a type body is optional
      return null;
    //if the type extend another type, the next node is a TYPE node and then a LIST node. If not, next node is TYPE_BODY
    AstNode parentOrBody = it.next();
    if (parentOrBody.type == Type.TYPE){
      //get the class it extends
      ci.setParentName(parentOrBody.value);
      
      //get the parameters to the supertype' constructor call
      //not all these parameters are var's, some could be int. Only save the vars, since we want to check they have been declared somewhere
      Iterator<AstNode> superArglistIt = it.next().iterator();
      while (superArglistIt.hasNext()){
        ci.incrSuperArgCount(); //save the number of args used when calling the parent constructor. We later check that this number match the number of args
        superArglistIt.next();
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
        member.IncrArg(); 
        varListIt.next();
      }
      currentType.addAbstractMember(member);
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
      member.IncrArg();
      varListIt.next();
    }
    currentType.addConcreteMember(member);
    
    return null;
  }
        
}

