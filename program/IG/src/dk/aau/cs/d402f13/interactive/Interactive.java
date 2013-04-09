package dk.aau.cs.d402f13.interactive;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;

import dk.aau.cs.d402f13.parser.Parser;
import dk.aau.cs.d402f13.scanner.Scanner;
import dk.aau.cs.d402f13.utilities.PrettyPrinter;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.Error;
import dk.aau.cs.d402f13.scopechecker.ScopeChecker;


public class Interactive {

  private Interactive() {
    // TODO Auto-generated constructor stub
  }

  public static void main(String[] args) throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line = "";
    String input = "";
    while (true) {
      line = br.readLine();
      if (line == null) { return; }
      line = line.replace('\t', ' ');
      switch (line) {
        case ":q":
          System.exit(0);
          break;
        case ":s":
        case ":p":
        case ":o":
        case ":k":
          try {
            ByteArrayInputStream bais = new ByteArrayInputStream(
              input.getBytes("UTF-8")
            );
            System.out.println("Scanning...");
            Date start = new Date();
            Scanner s = new Scanner(bais);
            LinkedList<Token> tokens = new LinkedList<Token>();
            Token ts;
            while ((ts = s.scan()).type != Token.Type.EOF) {
              tokens.add(ts);
            }
            long time = new Date().getTime() - start.getTime();
            System.out.println("Scanning took " + time + " ms");
            if (line.equals(":s")) {
              for (Token t : tokens) {
                System.out.println("" + t.type + " (" + t.value + ") <" + t.line + ":" + t.offset + ">");                
              }
            }
            else {
              System.out.println("Parsing...");
              start = new Date();
              Parser p = new Parser();
              AstNode ast = p.parse(tokens);
              time = new Date().getTime() - start.getTime();
              System.out.println("Parsing took " + time + " ms");
              if (line.equals(":p")) {
                ast.print();
                OutputStreamWriter f = new OutputStreamWriter(
                    new FileOutputStream(
                    new File("ast.dot"), false)
                );
                ast.export(f);
                f.close();
              }
              else {
                if (line.equals(":o")) {
                System.out.println("Pretty printing...");
                start = new Date();
                PrettyPrinter pp = new PrettyPrinter();
                String code = pp.visit(ast);
                time = new Date().getTime() - start.getTime();
                System.out.println("Pretty printing took " + time + " ms");
                System.out.println(code);
                }
                else{
                  System.out.println("Scope checking printing...");
                  start = new Date();
                  ScopeChecker sc = new ScopeChecker();
                  sc.visit(ast);
                  time = new Date().getTime() - start.getTime();
                  System.out.println("Scope checking took " + time + " ms");
                }
              }
            }
          }
          catch (Error e) {
            System.out.flush();
            System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage()
                + " on input line " + e.getLine() + " column "
                + e.getColumn() + ":");
            String[] lines = input.split("\n");
            if (lines.length >= e.getLine()) {
              System.err.println(lines[e.getLine() - 1]);
              for (int i = 1; i < e.getColumn(); i++) {
                System.err.print("-");
              }
              System.err.println("^");
            }
          }
          input = "";
          break;
        default:
          input += line + "\n";
      }
    }
  }

}
