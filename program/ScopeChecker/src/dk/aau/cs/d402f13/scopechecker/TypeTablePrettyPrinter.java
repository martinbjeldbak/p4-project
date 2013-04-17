package dk.aau.cs.d402f13.scopechecker;

import java.util.ArrayList;

public class TypeTablePrettyPrinter {

 
    public static void print(ArrayList<TypeInfo> typeTable){
      for (TypeInfo ci : typeTable){
        System.out.print("Type " + ci.name + "[");
        for (String s : ci.constructorArgs){
          System.out.print(s + " "); 
        }
        System.out.print("]");
        if (ci.parent != null){
          System.out.print("extends " + ci.parent.name);
          System.out.print("["); 
          for (String s : ci.superVarArgs){
            System.out.print(s + " ");
          }
          System.out.println("] " + ci.superArgs + " args total");
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
