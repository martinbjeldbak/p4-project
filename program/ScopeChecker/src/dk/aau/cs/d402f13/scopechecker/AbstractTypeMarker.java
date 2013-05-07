package dk.aau.cs.d402f13.scopechecker;

import dk.aau.cs.d402f13.utilities.scopechecker.Member;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeTable;

public class AbstractTypeMarker {
  TypeTable tt;
 public AbstractTypeMarker(TypeTable tt){
   this.tt = tt;
 }
  void markAbstractTypes() {
    //if a type contains any abstract member (also propagated members), it must be marked as abstract for use in interpreter
    for (TypeSymbolInfo tsi : this.tt){
     for (Member m : tsi.members){
       if (m.abstrct){
        tsi.markASTnodeAsAbstract();
        continue;
       }
     }
    }
  }
}
