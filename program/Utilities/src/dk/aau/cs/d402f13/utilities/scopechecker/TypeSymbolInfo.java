package dk.aau.cs.d402f13.utilities.scopechecker;

import java.util.ArrayList;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class TypeSymbolInfo extends SymbolInfo{
  public AstNode node; //the node from which this symbol was created
  public TypeSymbolInfo parent;
  public String parentName;
  public int args;
  public ArrayList<String> parentVarArgs;   //var args for call to supers constructor
  public int parentCallArgs;                    //number of arguments in call to supers constructor
  public ArrayList<Member> concreteMembers;
  public ArrayList<Member> abstractMembers;
  public int children; //number of children extending this type in a direct link
 
  
  public TypeSymbolInfo(AstNode node, String name, int line, int offset) {
   super(name, true, line, offset);
   this.node = node;
   this.children = 0;
   this.parentName = "";
   this.parentCallArgs = 0;
   this.args = 0; //constructor args
   this.concreteMembers = new ArrayList<Member>();
   this.abstractMembers = new ArrayList<Member>();
  }

  public void setParent(TypeSymbolInfo parent){
    this.parent = parent;
  }
  public void setParentName(String parentName){
    this.parentName = parentName;
  }
  public void addConcreteMember(Member member){
    concreteMembers.add(member);
  }
  public void addAbstractMember(Member member){
    abstractMembers.add(member);
  }
  public void incrArgCount(){
    this.args++;
  }
  public void incrSuperArgCount(){
    this.parentCallArgs++;
  }

  public void markASTnodeAsAbstract() {
   this.node.type = Type.ABSTRACT_TYPE_DEF; 
  }
}
