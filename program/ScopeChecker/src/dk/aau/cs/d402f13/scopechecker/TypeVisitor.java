package dk.aau.cs.d402f13.scopechecker;

import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.ast.DefaultVisitor;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.scopechecker.ConstantMember;
import dk.aau.cs.d402f13.utilities.scopechecker.Data;
import dk.aau.cs.d402f13.utilities.scopechecker.FunctionMember;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class TypeVisitor extends DefaultVisitor
{
  /* This visitor creates a hashmap String -> TypeSymbolInfo
   * of all types defined in the program. For each type, the TypeSymbolInfo
   * contains the number og args for the construcer og super constructor call,
   * a list of its abstract constants, abstract functions, concrete constants,
   * concrete functions and data members. A type's super type is also found.
   */
  private TypeTable tt;
  private TypeSymbolInfo currentType;
  
  
  public TypeVisitor(TypeTable tt){   
    this.tt = tt;
  }

  @Override
  protected Object visitProgram(AstNode node) throws StandardError {
   this.currentType = tt.getGlobal();
   visitChildren(node);
   return null;
  }
  
  @Override
  protected Object visitTypeDef(AstNode node) throws StandardError{
    //TYPE  VARLIST       [TYPE         LIST]      [TYPE_BODY]
    //name cnstr_args  supertype  spr_cnstr_args     body
    Iterator<AstNode> it = node.iterator();
    
    //get type name
    TypeSymbolInfo ci = new TypeSymbolInfo(node, it.next().value, node.line, node.offset );
    //the ASTNode node is saved so the typeDef can be changed to abstractTypeDef if ScopeChecker determines the type is abstract
    
    this.tt.addType(ci); //adds type and checks if another type with same name exists
    
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
    
    this.currentType = this.tt.getGlobal(); //after exiting type, set type to global type

    return null;
  }
  
  @Override
  protected Object visitAbstractDef(AstNode node) throws StandardError{
    //CONSTANT [VARLIST]
    if (this.currentType == tt.getGlobal()) throw new StandardError("Abstract definition outside type");  
    //this constantDefinition is inside a type
    Iterator<AstNode> it = node.iterator();

    //find name
    String name = it.next().value;
    Member member;
    //find varlist if any exist, which is the members arguments
    if (it.hasNext()){
      AstNode varList = it.next();
      member = new FunctionMember(name, varList.size(), node.line, node.offset);
    }
    else{   //if no varlist exists, the definition is an abstract constant
      member = new Member(name, node.line, node.offset);
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
      currentType.addMember(new FunctionMember(name, temp.size(), node.line, node.offset));
    }
    else{                            //VARLIST does not exist, so this is a constant
      //CONSTANT EXPRESSION
      currentType.addMember(new ConstantMember(name, node.line, node.offset));
    }  
    return null;
  }
  

  @Override
  protected Object visitDataDef(AstNode node) throws StandardError{
    /* DATA_DEF = VAR EXPRESSION
    *  Add the variable name to the data members of the type
    *  The UsesAreDeclaredVisitor will check that the data member accessed
    *  by a getter or setter actually exists
    */
    String varName = node.iterator().next().value;
    this.currentType.addData(new Data(varName, node.line, node.offset));
        return null;
  }
  
}

