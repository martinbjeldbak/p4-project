package dk.aau.cs.d402f13.utilities.scopechecker;


public class ConstSymbolInfo extends SymbolInfo{
  Boolean isAbstract;
  int numberOfArgs;
  Boolean declared;
  public ConstSymbolInfo(String name,  boolean declared, Boolean isAbstract, int numberOfArgs, int line, int offset) {
    super(name, declared, line, offset);
    this.isAbstract = isAbstract;
    this.numberOfArgs = numberOfArgs;
    this.declared = declared;
  }
}
