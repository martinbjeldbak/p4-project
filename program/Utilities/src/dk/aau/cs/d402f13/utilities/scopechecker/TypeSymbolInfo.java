package dk.aau.cs.d402f13.utilities.scopechecker;

import java.util.ArrayList;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;

public class TypeSymbolInfo extends SymbolInfo{
  public AstNode node; //the node from which this symbol was created
  public TypeSymbolInfo parent;
  public String parentName;
  public int args;
  public ArrayList<String> parentVarArgs;       //var args for call to supers constructor
  public int parentCallArgs;                    //number of arguments in call to supers constructor
  public ArrayList<Member> abstractConstants;   //constants with no impl
  public ArrayList<Member> concreteConstants;   //constant with impl
  public ArrayList<Member> abstractFunctions;   //functions with no impl
  public ArrayList<Member> concreteFunctions;   //functions with impl
  public ArrayList<Member> dataMembers;         //data members

  public int children; //number of children extending this type in a direct link
 
  
  public TypeSymbolInfo(AstNode node, String name, int line, int offset) {
   super(name, line, offset);
   this.node = node;
   this.children = 0;
   this.parentName = "";
   this.parentCallArgs = 0;
   this.args = 0; //constructor args
   this.abstractConstants = new ArrayList<Member>();
   this.concreteConstants = new ArrayList<Member>();
   this.abstractFunctions = new ArrayList<Member>();
   this.concreteFunctions = new ArrayList<Member>();
   this.dataMembers = new ArrayList<Member>();
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
  public void addConcreteFunction(Member member){
    this.concreteFunctions.add(member);
  }
  public void addAbstractFunction(Member member){
    this.abstractFunctions.add(member);
  }
  public void addConcreteConstant(Member member){
    this.concreteConstants.add(member);
  }
  public void addAbstractConstant(Member member){
    this.abstractConstants.add(member);
  }
  public void addDataMember(Member member){
    this.dataMembers.add(member);
  }
  public void incrArgCount(){
    this.args++;
  }
  public void incrSuperArgCount(){
    this.parentCallArgs++;
  }

  public void markASTnodeAsAbstract() { //the scopechecker marks abstract types for the interpreter
   this.node.type = Type.ABSTRACT_TYPE_DEF; 
  }
}
