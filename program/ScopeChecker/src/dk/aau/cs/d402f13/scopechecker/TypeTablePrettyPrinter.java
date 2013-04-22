package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;

import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo;
import dk.aau.cs.d402f13.utilities.scopechecker.TypeSymbolInfo.Member;

public class TypeTablePrettyPrinter {

 
    public static void print(ArrayList<TypeSymbolInfo> typeTable){
      for (TypeSymbolInfo ci : typeTable){
        System.out.print("Type " + ci.name + "[");
        for (String s : ci.constructorArgs){
          System.out.print(s + " "); 
        }
        System.out.print("]");
        if (ci.parent != null){
          System.out.print("extends " + ci.parent.name);
          System.out.print("["); 
          for (String s : ci.parentVarArgs){
            System.out.print(s + " ");
          }
          System.out.println("] " + ci.parentArgs + " args total");
        }
        System.out.println("  Has abstract members:");
        for (Member m : ci.abstractMembers){
          System.out.print("  " + m.name + "[");
          for (String s : m.varArgs){
          System.out.print(s + " ");
          }
          System.out.println("]");
        }
         
        System.out.println("  Has constant members:");
        for (Member m : ci.constantMembers){
          System.out.print("  "+m.name + "[");
          for (String s : m.varArgs){
          System.out.print(s + " ");
          }
          System.out.println("]");
        } 
      }
    }
}
