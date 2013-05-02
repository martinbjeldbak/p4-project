package dk.aau.cs.d402f13.scopechecker;

import java.util.HashMap;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.Data;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;

public class TypeVisitor extends DefaultVisitor
{
  /* This visitor creates a hashmap String -> TypeSymbolInfo
   * of all types defined in the program. For each type, the TypeSymbolInfo
   * contains the number og args for the construcer og super constructor call,
   * a list of its abstract constants, abstract functions, concrete constants,
   * concrete functions and data members. A type's super type is also found.
   */
  
  
  HashMap<String, TypeSymbolInfo> typeTable;    //all classes found in program are put here as a ref to its ClassInfo object
  TypeSymbolInfo currentType;                   //the class we are currently inside
  TypeSymbolInfo globalType;                    //the globalType is only used by the scopechecker to contain global constants and functinos
  public TypeVisitor(){
    this.typeTable = new HashMap<String, TypeSymbolInfo>();
    this.globalType = new TypeSymbolInfo(null, "#GLOBAL", -1, 0);
    this.typeTable.put("#GLOBAL", this.globalType);
    this.currentType = globalType;
    addStandardEnvironment();
  }
  void addStandardEnvironment(){
    typeTable.put("Piece", new TypeSymbolInfo(null, "Piece", 1, -1, 0 ) );
    typeTable.put("Player", new TypeSymbolInfo(null, "Player", 0, -1, 0 ) );
    typeTable.put("Game", new TypeSymbolInfo(null, "Game", 1, -1, 0 ) );
    typeTable.put("GridBoard", new TypeSymbolInfo(null, "GridBoard", 2, -1, 0 ) );
    typeTable.put("Square", new TypeSymbolInfo(null, "Square", 0, -1, 0 ) );
  }
  public HashMap<String, TypeSymbolInfo> getTypeTable(){
    return this.typeTable;
  }
  public void AddType(TypeSymbolInfo ci) throws ScopeError{
   if (this.typeTable.containsKey(ci.name))
        throw new ScopeError("Type with same name as type " + ci.name + " already declared", ci.line, ci.offset);
    this.typeTable.put(ci.name, ci);
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
    
    currentType = globalType; //after exiting type, set type to global type

    return null;
  }
  
  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError{
    //CONSTANT [VARLIST]
    if (currentType == globalType) throw new StandardError("Abstract definition outside type");  
    //this constantDefinition is inside a type
    Iterator<AstNode> it = node.iterator();

    //find name
    String name = it.next().value;
    Member member;
    //find varlist if any exist, which is the members arguments
    if (it.hasNext()){
      AstNode varList = it.next();
      member = new Member(name, varList.size(), this.currentType, node.line, node.offset);
    }
    else{   //if no varlist exists, the definition is an abstract constant
      member = new Member(name, this.currentType, node.line, node.offset);
    }
    member.abstrct = true;
    this.currentType.addMember(member);
    return null;
  }
  
  @Override
  protected Object visitConstantDef(AstNode node) throws StandardError{
    //CONSTANT [VARLIST] EXPRESSION
    
    //this constantDefinition is inside a type
    Iterator<AstNode> it = node.iterator();

    //find name
    String name = it.next().value;
    
    AstNode temp = it.next();
    if (temp.type == Type.VARLIST){  //if VARLIST exists, it is arguments for the function
      //CONSTANT VARLIST EXPRESSION
      currentType.addMember(new Member(name, temp.size(), this.currentType, node.line, node.offset));
    }
    else{                            //VARLIST does not exist, so this is a constant
      //CONSTANT EXPRESSION
      currentType.addMember(new Member(name, this.currentType, node.line, node.offset));
    }  
    return null;
  }
  

  @Override
  protected Object visitDataDef(AstNode node) throws StandardError{
    /* DATA_DEF = VAR EXPRESSION
    *  Add the variable name to the data members of the type
    *  The UsesAreDeclaredVisitor will check that tje data member accessed
    *  by a getter or setter actually exists
    */
    String varName = node.iterator().next().value;
    this.currentType.addData(new Data(varName, node.line, node.offset));
        return null;
  }
  
}

