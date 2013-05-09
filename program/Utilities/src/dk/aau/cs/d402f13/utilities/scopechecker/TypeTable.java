package dk.aau.cs.d402f13.utilities.scopechecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import dk.aau.cs.d402f13.utilities.errors.ScopeError;

public class TypeTable implements Iterable<TypeSymbolInfo> {
  private ArrayList<TypeSymbolInfo> typeList;
  private HashMap<String, TypeSymbolInfo> typeHashMap;
  private TypeSymbolInfo globalType;
  public TypeTable(){
    this.typeList = new ArrayList<TypeSymbolInfo>();
    this.typeHashMap = new HashMap<String, TypeSymbolInfo>();
    this.globalType = new TypeSymbolInfo(null, "#global scope#", -1, 0);
  }
  public Boolean typeExists(String name){
    return this.typeHashMap.containsKey(name);
  }
  public TypeSymbolInfo getType(String name){
    return typeHashMap.get(name);
  }
  public TypeSymbolInfo getGlobal(){
    return this.globalType;
  }
  public void addType(TypeSymbolInfo tsi) throws ScopeError
  {
    if (typeHashMap.containsKey(tsi.name))
      throw new ScopeError("Type with name " + tsi.name + " already declared", tsi.line, tsi.offset);
    typeList.add(tsi);
    typeHashMap.put(tsi.name, tsi);
  }
  
  public void topologicalSort() throws ScopeError{
    ArrayList<TypeSymbolInfo> sorted = new ArrayList<TypeSymbolInfo>();
    for (TypeSymbolInfo tsi : this.typeList){
    if (tsi.parent != null)
      tsi.parent.children++;
    }
    for (TypeSymbolInfo tsi: this.typeList){
      while (tsi.children == 0){
        sorted.add(tsi);
        tsi.children = -1; //make sure this type is only added once
        if (tsi.parent == null){
          break;
        }
        else{
          tsi.parent.children--;
          tsi = tsi.parent;
        }
      }
    }
    Collections.reverse(sorted); //reverse list so we get super classes first
    if (sorted.size() != this.typeList.size()){
      for (TypeSymbolInfo sti : this.typeList){
        Boolean found = false;
        for (TypeSymbolInfo sortedSti : sorted){
          if (sti == sortedSti){
            found = true;
            break;
          }
        }
        if (!found)
          throw new ScopeError("Extend cycle found in type " + sti.name, sti.line, sti.offset );
      }
    }
    this.typeList = sorted;
  }
  @Override
  public Iterator<TypeSymbolInfo> iterator() {
    return this.typeList.iterator();
  }
}
