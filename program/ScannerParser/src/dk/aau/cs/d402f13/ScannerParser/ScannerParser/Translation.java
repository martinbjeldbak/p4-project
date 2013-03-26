package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import dk.aau.cs.d402f13.ScannerParser.analysis.*;
import dk.aau.cs.d402f13.ScannerParser.node.*;

public class Translation extends DepthFirstAdapter {
  
  
  
  public void visit(Node node) {
    if (node != null)
      node.apply(this);
  }
}
