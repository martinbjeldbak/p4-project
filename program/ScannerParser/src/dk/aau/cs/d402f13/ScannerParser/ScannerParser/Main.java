package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import dk.aau.cs.d402f13.ScannerParser.parser.*;
import dk.aau.cs.d402f13.ScannerParser.lexer.*;
import dk.aau.cs.d402f13.ScannerParser.node.*;
import java.io.*;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    try {
      System.out.println("Type the game you want to parse:");
      
      InputStreamReader converter = new InputStreamReader(System.in);
      BufferedReader reader = new BufferedReader(converter);
      
      /*Parser p =
        new Parser(
        new PrintLexer(
        new PushbackReader(
        //new InputStreamReader(System.in), 1024
        new FileReader("kent-game.game"), 1024))); */
      
      FileReader in = new FileReader(reader.readLine());
      
      Lexer lexer = new Lexer(new PushbackReader(new BufferedReader(in), 1020));
      Parser parser = new Parser(lexer);
      Node tree = parser.parse();
     
      converter.close();
      reader.close();
      in.close();
      
      tree.apply(new Interpreter());
   
      //AstNodeConverter asts = new AstNodeConverter();
      //tree.apply(asts);
      
      //DotVisitor toDot = new DotVisitor(new PrintWriter(new File("ast.dot")));
      //tree.apply(toDot);
      
      
      
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
    }
  }

}
