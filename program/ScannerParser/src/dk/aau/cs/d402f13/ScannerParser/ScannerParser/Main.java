package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import dk.aau.cs.d402f13.ScannerParser.parser.*;
import dk.aau.cs.d402f13.ScannerParser.lexer.*;
import dk.aau.cs.d402f13.ScannerParser.node.*;
import java.io.*;

public class Main {

  public static void main(String[] args) {
    try {
      System.out.println("Type an arithmetic expression:");
      
      Parser p =
        new Parser(
        new PrintLexer(
        new PushbackReader(
        //new InputStreamReader(System.in), 1024
        new FileReader("kent-game.game"), 1024)));
    
      Start tree = p.parse();
      
      //Apply the translation
      tree.apply( new Translation() );
      
      //AstNodeConverter asts = new AstNodeConverter();
      //tree.apply(asts);
      
      DotVisitor toDot = new DotVisitor(new PrintWriter(new File("test123.dot")));
      tree.apply(toDot);
      
      
      
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
