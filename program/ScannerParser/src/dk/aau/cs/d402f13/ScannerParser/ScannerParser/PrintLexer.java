package dk.aau.cs.d402f13.ScannerParser.ScannerParser;

import java.io.*;
import dk.aau.cs.d402f13.ScannerParser.node.*;
import dk.aau.cs.d402f13.ScannerParser.lexer.*;

public class PrintLexer extends Lexer {

  public PrintLexer(PushbackReader in) {
    super(in);
  }

  protected void filter() {
    System.out.println(token.getClass() +
        ", state : " + state.id() +
        ", text : [" + token.getText() + "]");
  }

}
