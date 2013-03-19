package dk.aau.cs.d402f13.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;
import dk.aau.cs.d402f13.parser.AstNode.Type;
import dk.aau.cs.d402f13.scanner.Scanner;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.errors.SyntaxError;

public class Parser {
  private LinkedList<Token> tokens;
  private Token currentToken;
  private Token nextToken;

  public AstNode parse(LinkedList<Token> tokens) throws SyntaxError {
    this.tokens = tokens;
    currentToken = null;
    nextToken = tokens.peek();

    return program();
  }
  
  private boolean accept(Token.Type type) {
    if (lookAhead(type)) {
      currentToken = pop();
      return true;
    }
    return false;
  }

  private SyntaxError unexpectedError(String expected) {
    if (nextToken == null) {
      if (currentToken != null) {
        return new SyntaxError("Unexpected end of file, expected " + expected, currentToken);
      }
      return new SyntaxError("Unexpected end of file, expected " + expected, null);
    }
    return new SyntaxError("Unexpected token " + nextToken.type + ", expected " + expected, nextToken);
  }
  
  private Token expect(Token.Type type) throws SyntaxError {
    if (accept(type)) { 
      return currentToken; 
    }
    throw unexpectedError(type.toString());
  }

  private AstNode astNode(Type type, String value) {
    if (currentToken != null) {
      return new AstNode(type, value, currentToken.line, currentToken.offset);
    }
    else if (nextToken != null) { 
      return new AstNode(type, value, nextToken.line, nextToken.offset); 
    }
    return new AstNode(type, value, 1, 1);
  }

  private Token pop() {
    Token next = nextToken;
    tokens.poll();
    nextToken = tokens.peek();
    return next;
  }

  //DIFFERENT LOOKAHEAD METHODS
  private boolean lookAhead(Token.Type type) {
    return nextToken != null && nextToken.type == type;
  }

  private boolean lookAheadLiteral() {
    return lookAhead(Token.Type.INT_LIT)
        || lookAhead(Token.Type.DIR_LIT)
        || lookAhead(Token.Type.COORD_LIT)
        || lookAhead(Token.Type.STRING_LIT);
  }

  private boolean lookAheadElement() {
    return lookAheadLiteral()
        || lookAhead(Token.Type.LPAREN)
        || lookAhead(Token.Type.VAR)
        || lookAhead(Token.Type.LBRACKET)
        || lookAhead(Token.Type.PATTERNOP)
        || lookAhead(Token.Type.KEYWORD)
        || lookAhead(Token.Type.THIS)
        || lookAhead(Token.Type.ID);
  }

  private boolean lookAheadExpression() {
    return lookAhead(Token.Type.FUNCTION)
        || lookAheadElement()
        || lookAhead(Token.Type.IF)
        || lookAhead(Token.Type.LAMBDABEGIN);
  }
  
  private boolean lookAheadPatterValue(){
    return lookAhead(Token.Type.DIR_LIT)
        || lookAhead(Token.Type.VAR)
        || lookAhead(Token.Type.PATTERN_KEYWORD)
        || lookAhead(Token.Type.ID)
        || lookAhead(Token.Type.NOT_OPERATOR)  
        || lookAhead(Token.Type.LPAREN);
  }

  // PROGRAM STRUCTURE
  private AstNode program() throws SyntaxError {
    AstNode root = astNode(Type.PROGRAM, "");
    while (lookAhead(Token.Type.DEFINE)) {
      root.addChild(functionDefinition());
    }
    root.addChild(gameDecleration());

    return root;
  }
  
  private AstNode varList() throws SyntaxError {
    AstNode node = astNode(Type.VARLIST, "");
    expect(Token.Type.LBRACKET);
    while (lookAhead(Token.Type.VAR)) {
      expect(Token.Type.VAR);
      node.addChild(astNode(Type.VAR, currentToken.value));
    }
    expect(Token.Type.RBRACKET);
    return node;
  }

  private AstNode functionDefinition() throws SyntaxError {
    AstNode node = astNode(Type.FUNC_DEF, "");
    expect(Token.Type.DEFINE);
    expect(Token.Type.FUNCTION);
    node.addChild(astNode(Type.FUNCTION, currentToken.value));
    node.addChild(varList());
    node.addChild(expression());

    return node;
  }

  private AstNode gameDecleration() throws SyntaxError {
    AstNode node = astNode(Type.GAME_DECL, "");
    expect(Token.Type.GAME);
    node.addChild(declerationStruct());

    return node;
  }

  private AstNode declerationStruct() throws SyntaxError {
    AstNode node = astNode(Type.DECL_STRUCT, "");
    expect(Token.Type.LBRACE);
    node.addChild(decleration());
    while (lookAhead(Token.Type.KEYWORD) || lookAhead(Token.Type.ID)) {
      node.addChild(decleration());
    }
    expect(Token.Type.RBRACE);

    return node;
  }

  private AstNode decleration() throws SyntaxError {
    AstNode node = astNode(Type.DECL, "");
    if (accept(Token.Type.KEYWORD)) {
      node.addChild(astNode(Type.KEYWORD, currentToken.value));
    }
    else {
      expect(Token.Type.ID);
      node.addChild(astNode(Type.ID, currentToken.value));
    }

    node.addChild(structure());

    return node;
  }

  private AstNode structure() throws SyntaxError {
    if (lookAhead(Token.Type.LBRACE)) {
      return declerationStruct();
    }
    else {
      return expression();
    }
  }

  // EXPRESSIONS
  private AstNode expression() throws SyntaxError {
    if (lookAhead(Token.Type.FUNCTION)) {
      return functionCall();
    }
    else if (accept(Token.Type.NOT_OPERATOR)) {
      AstNode operation = astNode(Type.NOT_OPERATOR, "");
      operation.addChild(expression());
      return operation;
    }
    else if (lookAhead(Token.Type.LET)) {
      return assignment();
    }
    else if (lookAhead(Token.Type.IF)) {
      return ifExpression();
    }
    else if (lookAhead(Token.Type.LAMBDABEGIN)) {
      return lambdaExpression();
    }
    else if (lookAheadElement()) {
      AstNode element = element();
      if (accept(Token.Type.NORMAL_OPERATOR) || accept(Token.Type.SHARED_OPERATOR)) {
        AstNode operation = astNode(Type.OPERATOR, currentToken.value);
        operation.addChild(element);
        operation.addChild(expression());
        return operation;
      }
      else {
        return element;
      }
    }
    throw unexpectedError("an expression");
  }

  private AstNode element() throws SyntaxError {
    AstNode node = astNode(Type.ELEM, "");
    if (accept(Token.Type.LPAREN)) {
      node = expression();
      expect(Token.Type.RPAREN);
    }
    else if (accept(Token.Type.VAR)) {
      node = astNode(Type.VAR, currentToken.value);
    }
    else if (lookAhead(Token.Type.LBRACKET)) {
      node = list();
    }
    else if (accept(Token.Type.PATTERNOP)) {
      node = pattern();
      expect(Token.Type.PATTERNOP);
    }
    else if (accept(Token.Type.KEYWORD)) {
      node = astNode(Type.KEYWORD, currentToken.value);
    }
    else if (accept(Token.Type.THIS)) {
      node = astNode(Type.KEYWORD, "this");
    }
    else if (accept(Token.Type.DIR_LIT)) {
      node = astNode(Type.DIR_LIT, currentToken.value);
    }
    else if (accept(Token.Type.COORD_LIT)) {
      node = astNode(Type.COORD_LIT, currentToken.value);
    }
    else if (accept(Token.Type.INT_LIT)) {
      node = astNode(Type.INT_LIT, currentToken.value);
    }
    else if (accept(Token.Type.STRING_LIT)) {
      node = astNode(Type.STRING_LIT, currentToken.value);
    }
    else if (accept(Token.Type.ID)) {
      node = astNode(Type.ID, currentToken.value);
    }
    else {
      throw unexpectedError("an element");
    }
    return node;
  }

  private AstNode functionCall() throws SyntaxError {
    AstNode node = astNode(Type.FUNC_CALL, "");
    expect(Token.Type.FUNCTION);
    node.addChild(astNode(Type.FUNCTION, currentToken.value));
    node.addChild(list());

    return node;
  }
  
  private AstNode assignment() throws SyntaxError {
    AstNode node = astNode(Type.ASSIGNMENT, "");
    expect(Token.Type.LET);
    expect(Token.Type.VAR);
    node.addChild(astNode(Type.VAR, currentToken.value));
    expect(Token.Type.ASSIGN);
    node.addChild(expression());
    while (accept(Token.Type.COMMA)) {
      AstNode assignment = astNode(Type.ASSIGNMENT, "");
      expect(Token.Type.VAR);
      assignment.addChild(astNode(Type.VAR, currentToken.value));
      expect(Token.Type.ASSIGN);
      assignment.addChild(expression());
      node.addChild(assignment);
    }
    expect(Token.Type.IN);
    node.addChild(expression());
    return node;
  }

  private AstNode ifExpression() throws SyntaxError {
    AstNode node = astNode(Type.IF_EXPR, "");
    expect(Token.Type.IF);
    node.addChild(expression());
    expect(Token.Type.THEN);
    node.addChild(expression());
    expect(Token.Type.ELSE);
    node.addChild(expression());

    return node;
  }

  private AstNode lambdaExpression() throws SyntaxError {
    AstNode node = astNode(Type.LAMBDA_EXPR, "");
    expect(Token.Type.LAMBDABEGIN);
    node.addChild(varList());
    expect(Token.Type.LAMBDAOP);
    node.addChild(expression());

    return node;
  }

  private AstNode list() throws SyntaxError {
    AstNode node = astNode(Type.LIST, "");
    expect(Token.Type.LBRACKET);
    while (lookAheadElement()) {
      node.addChild(element());
    }
    expect(Token.Type.RBRACKET);

    return node;
  }

  private AstNode pattern() throws SyntaxError {
    AstNode node = astNode(Type.PATTERN, "");
    node.addChild(patternExpression());
    while (lookAheadPatterValue()) {
      node.addChild(patternExpression());
    }

    return node;
  }

  private AstNode patternExpression() throws SyntaxError {
    AstNode subject = patternValue();
    if (accept(Token.Type.PATTERNOR)) {
      AstNode node = astNode(Type.PATTERN_OR, "");
      node.addChild(subject);
      node.addChild(patternExpression());
      return node;
    }
    else if (accept(Token.Type.PATTERN_OPERATOR) || accept(Token.Type.SHARED_OPERATOR)) {
      AstNode node = astNode(Type.PATTERN_OPERATOR, currentToken.value);
      node.addChild(subject);
      return node;
    }
    return subject;
  }

  private AstNode patternValue() throws SyntaxError {
    AstNode node;
    if (accept(Token.Type.DIR_LIT)) {
      node = astNode(Type.DIR_LIT, currentToken.value);
    }
    else if (accept(Token.Type.VAR)) {
      node = astNode(Type.VAR, currentToken.value);
    }
    else if (lookAhead(Token.Type.PATTERN_KEYWORD) || lookAhead(Token.Type.THIS) || lookAhead(Token.Type.ID)) {
      node = patternCheck();
    }
    else if (accept(Token.Type.PATTERN_NOT)) {
      node = astNode(Type.PATTERN_NOT, "");
      node.addChild(patternCheck());
    }
    else if (accept(Token.Type.LPAREN)) {
      node = pattern();
      expect(Token.Type.RPAREN);
      if (accept(Token.Type.INT_LIT)) {
        AstNode mult = astNode(Type.PATTERN_MULTIPLITER, currentToken.value);
        mult.addChild(node);
        node = mult;
      }
    }
    else {
      throw unexpectedError("a pattern value");
    }
    return node;
  }

  private AstNode patternCheck() throws SyntaxError {
    AstNode node;
    if (accept(Token.Type.PATTERN_KEYWORD)) {
      node = astNode(Type.PATTERN_KEYWORD, currentToken.value);
    }
    else if (accept(Token.Type.THIS)) {
      node = astNode(Type.THIS, currentToken.value);
    }
    else if (accept(Token.Type.ID)) {
      node = astNode(Type.ID, currentToken.value);
    }
    else {
      throw unexpectedError("a pattern operator or an identifier");
    }
    return node;
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
              ast.print();
              OutputStreamWriter f = new OutputStreamWriter(
                  new FileOutputStream(
                  new File("ast.dot"), false)
              );
              ast.export(f);
              f.close();
            }
          }
          catch (SyntaxError e) {
            System.out.flush();
            if (e.getToken() == null) {
              System.err.println("Syntax error: " + e.getMessage());
            }
            else {
              System.err.println("Syntax error: " + e.getMessage()
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
          }
          input = "";
          break;
        default:
          input += line + "\n";
      }
    }
  }
}