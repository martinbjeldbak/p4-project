package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import dk.aau.cs.d402f13.ScannerParser.node.*;
import dk.aau.cs.d402f13.ScannerParser.analysis.*;
import dk.aau.cs.d402f13.ScannerParser.Values.Value;

public class Interpreter extends DepthFirstAdapter {

  private Value currentResult;

  
  private void visit(Node node) {
    if (node != null)
      node.apply(this);
  }
  
  private Value eval(Node node) {
    this.currentResult = null;
    visit(node);
    Value result = this.currentResult;
    this.currentResult = null;
    return result;
  }
   
}
