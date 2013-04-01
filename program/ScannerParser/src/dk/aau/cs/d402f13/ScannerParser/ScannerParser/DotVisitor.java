package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import java.io.PrintWriter;
import java.util.HashMap;

import dk.aau.cs.d402f13.ScannerParser.analysis.DepthFirstAdapter;
import dk.aau.cs.d402f13.ScannerParser.node.*;

public class DotVisitor extends DepthFirstAdapter {

  private PrintWriter out;
  private HashMap<Node,Integer> mNodeToNum 
               = new HashMap<Node,Integer>();
  private int count = 0;
  private Node root = null;
  
  /** Constructor takes a PrintWriter, and stores in instance var. */
  public DotVisitor(PrintWriter out) {
     this.out = out;
  }

  
  /** 
   Create a preorder number for nodes.
   
   Upon entering each node in AST, check if this node is the root
   to generate start of .dot file, output the dot output for the node.
  */
  public void defaultIn(Node node) {

      // preorder nodes
      mNodeToNum.put(node,count);
      count++;
  
      // check if root node
      if (this.root == null) {
          this.root = node;
          out.println("digraph ASTGraph {");
      }

      
  }
  
  /* All of the "out" methods generate a
   * dot node and the dot edges to the children
   * nodes.
   */
  public void outACorElement(ACorElement node) {
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Coordinate: " + node.getCoordinate().getText() + "\" ];");

    defaultOut(node);    
  }
  
  public void outADeclstrucStructure(ADeclstrucStructure node) {
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"DeclStruct\" ];");

    defaultOut(node);    
  }
  
  public void outADeclStruct(ADeclStruct node) {
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"DeclStruct\" ];");

    defaultOut(node);
  }
  
  public void outADirElement(ADirElement node) {
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Direction: " + node.getDirection().getText() + "\" ];");

    defaultOut(node);    
  }
  
  public void outADirPatternVal(ADirPatternVal node) {
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Direction: " + node.getDirection().getText() + "\" ];");
    
    defaultOut(node);
  }
  
  public void outAElExpression(AElExpression node) {
    // node with label
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"ElList\" ];");
    
    defaultOut(node);
 }
  
  public void outAElopexpExpression(AElopexpExpression node) {
    // node with label
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"ElOpExp\" ];");

    defaultOut(node);
 }
  
  public void outAEmarkpatcheckPatternVal(AEmarkpatcheckPatternVal node ) {
    // node with label
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Not\" ];");

    defaultOut(node);
 } 
  
  public void outAEmptyPatternCheck(AEmptyPatternCheck node ) {
    // node with label
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Empty\" ];");

    defaultOut(node);
 } 
  
  public void outAExprElement(AExprElement node ) {
    // node with label
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Expression\" ];");
    defaultOut(node);
 } 
 
 public void outAExprStructure(AExprStructure node ) {
    // node with label
    out.print(mNodeToNum.get(node));
    out.print(" [ label=\"Expression\" ];");
    defaultOut(node);
 }
 
 public void outAFoePatternCheck(AFoePatternCheck node ) {
   // node with label
   out.print(mNodeToNum.get(node));
   out.print(" [ label=\"" + node.getFoe().getText()  + "\" ];");
   defaultOut(node);
}

 public void outAFriendPatternCheck(AFriendPatternCheck node ) {
   // node with label
   out.print(mNodeToNum.get(node));
   out.print(" [ label=\"" + node.getFriend().getText()  + "\" ];");
   defaultOut(node);
}

 public void outAFuncElement(AFuncElement node ) {
   // node with label
   out.print(mNodeToNum.get(node));
   out.print(" [ label=\"Function\" ];");
   defaultOut(node);
}
 
 public void outAFunctionDef(AFunctionDef node ) {
   // node with label
   out.print(mNodeToNum.get(node));
   out.print(" [ label=\"Function definition: " + node.getFunction().getText() + "\" ];");
   defaultOut(node);
}
 
// ................................................................ OSV OSV FOR ALLE NODES
 
 public void outAAssignExpression(AAssignExpression node) {
      // node with label
      out.print(mNodeToNum.get(node));
      out.print(" [ label=\"AssignExp\" ];");
      defaultOut(node);       
 }
 
 public void outAIfExpression(AIfExpression node) {
   // node with label
   out.print(mNodeToNum.get(node));
   out.print(" [ label=\"IfExp\" ];");

   defaultOut(node);       
}

public void outAGameDecl(AGameDecl node) {
   // node with label
   out.print(mNodeToNum.get(node));
   out.print(" [ label=\"GameDecl\" ];");

   defaultOut(node);       
}
 
 public void outALambdaExpression(ALambdaExpression node) {
      // node with label
      out.print(mNodeToNum.get(node));
      out.print(" [ label=\"LambdaExp\" ];");
      
      defaultOut(node);
 }

 public void outANotExpression(ANotExpression node) {
      // node with label
      out.print(mNodeToNum.get(node));
      out.println(" [ label=\"NotExp\" ];");
             
      defaultOut(node);
 }

 public void outToken(Token node) {
      // node with label
      out.print(mNodeToNum.get(node));
      out.print(" [ label=\"Token");
      out.print("\\n");
      out.print(node.getText());
      out.println(" \" ]; ");
             
      defaultOut(node);
 }

  public void defaultOut(Node node) {
      // print out edge from parent
      if (node.parent()!=null) {
        out.print(mNodeToNum.get(node.parent()));
        out.print(" -> ");
        out.print(mNodeToNum.get(node));
        out.println(";");
      }
      
      // finish output if we are root node
      if (this.root == node) {
          out.println("}");
      }
      out.flush();
  }
  

}
