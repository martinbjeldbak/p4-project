package dk.aau.cs.d402f13.utilities.scopechecker;

import java.util.ArrayList;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;

public class TypeSymbolInfo extends SymbolInfo{
  public AstNode node; //the node from which this symbol was created
  public TypeSymbolInfo parent;
  public String parentName;
  public int args;
  public ArrayList<String> parentVarArgs;       //var args for call to supers constructor
  public int parentCallArgs;                    //number of arguments in call to supers constructor
  public ArrayList<Member> members;   //constants with no impl
  public ArrayList<Data> data;         //data members
  public boolean varArgs; //if it has varArgs, args still means the needed number of args
  public int children; //number of children extending this type in a direct link
 
  
  public TypeSymbolInfo(AstNode node, String name, int line, int offset) {
   super(name, line, offset);
   this.node = node;
   this.children = 0;
   this.parent = null;
   this.parentName = "";
   this.parentCallArgs = 0;
   this.args = 0; //constructor args
   this.members = new ArrayList<Member>();
   this.data = new ArrayList<Data>();
  }
  
  public TypeSymbolInfo(AstNode node, String name, int argCount, int line, int offset){
    this(node, name, line, offset);
    this.args = argCount;
  }

  public void setParent(TypeSymbolInfo parent){
    this.parent = parent;
  }
  public void setParentName(String parentName){
    this.parentName = parentName;
  }
  public void addMember(Member member) throws ScopeError{
    if (this.members.contains(member))
      throw new ScopeError("Type " + this.name + " already contains a declaration of member " + member.name, member.line, member.offset);
    this.members.add(member);
  }
  public void addData(Data data){
    this.data.add(data);
  }
  public void incrArgCount(){
    this.args++;
  }
  public void setSuperCallArgCount(int val){
    this.parentCallArgs = val;
  }
  public void setVarArgs(boolean val){
    this.varArgs = val;
  }
  public void setArgCount(int val){
    this.args = val;
  }

  public void markASTnodeAsAbstract() { //the scopechecker marks abstract types for the interpreter
   this.node.type = Type.ABSTRACT_TYPE_DEF;
  }
}
