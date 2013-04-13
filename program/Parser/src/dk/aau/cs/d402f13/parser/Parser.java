package dk.aau.cs.d402f13.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.LinkedList;
import dk.aau.cs.d402f13.scanner.Scanner;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.ast.AstNode.Type;
import dk.aau.cs.d402f13.utilities.errors.SyntaxError;

/**
 * @author d402f13
 * 
 */
public class Parser {
  private LinkedList<Token> tokens;
  private Token currentToken;
  private Token nextToken;

  /**
   * @param takes a linked list of tokens as parameters
   * @return returns an AST with program as the root.
   * @throws SyntaxError
   */
  public AstNode parse(LinkedList<Token> tokens) throws SyntaxError {
    this.tokens = tokens;
    currentToken = null;
    nextToken = tokens.peek();

    return program();
  }

//  /**
//   * @param takes
//   *          a linked list of tokens as parameters
//   * @return returns an AST with expression as the root.
//   * @throws SyntaxError
//   */
//  public AstNode parseAsExpression(LinkedList<Token> tokens) throws SyntaxError {
//    this.tokens = tokens;
//    currentToken = null;
//    nextToken = tokens.peek();
//
//    return expression();
//  }
//
//  /**
//   * @param takes
//   *          a linked list of tokens as parameters
//   * @return returns an AST with definition as the root.
//   * @throws SyntaxError
//   */
//  public AstNode parseAsDefinition(LinkedList<Token> tokens) throws SyntaxError {
//    this.tokens = tokens;
//    currentToken = null;
//    nextToken = tokens.peek();
//
//    return functionDefinition();
//  }

  /**
   * @param takes
   *          a token as a parameter
   * @return pops the next token, if that token matches the token recieved as
   *         input and returns true. Otherwise false.
   */
  private boolean accept(Token.Type type) {
    if (lookAhead(type)) {
      currentToken = pop();
      return true;
    }
    return false;
  }

//  /**
//   * @param takes
//   *          a token and a value as a parameter
//   * @return pops the next token, if that token and value match the token and
//   *         value recieved as input and returns true. Otherwise false.
//   */
//  private boolean accept(Token.Type type, String value) {
//    if (lookAhead(type, value)) {
//      currentToken = pop();
//      return true;
//    }
//    return false;
//  }

  /**
   * @param takes
   *          a string as input
   * @return returns a string with a description of what has happened. The
   *         parser has received something unexpected and therefore a syntax
   *         error will be thrown. The string received as input will be
   *         concatenated with the original error message and return.
   */
  private SyntaxError unexpectedError(String expected) {
    if (nextToken == null) {
      if (currentToken != null) {
        return new SyntaxError("Unexpected end of file, expected " + expected,
            currentToken);
      }
      return new SyntaxError("Unexpected end of file, expected " + expected,
          null);
    }
    return new SyntaxError("Unexpected token " + nextToken.type + ", expected "
        + expected, nextToken);
  }

  /**
   * @param type
   * @return
   * @throws SyntaxError
   */
  private Token expect(Token.Type type) throws SyntaxError {
    if (accept(type)) {
      return currentToken;
    }
    throw unexpectedError(type.toString());
  }

  /**
   * @param type
   * @param value
   * @return
   */
  private AstNode astNode(Type type, String value) {
    if (currentToken != null) {
      return new AstNode(type, value, currentToken.line, currentToken.offset);
    }
    else if (nextToken != null) {
      return new AstNode(type, value, nextToken.line, nextToken.offset);
    }
    return new AstNode(type, value, 1, 1);
  }

  /**
   * @return
   */
  private Token pop() {
    Token next = nextToken;
    tokens.poll();
    nextToken = tokens.peek();
    return next;
  }

  // DIFFERENT LOOKAHEAD METHODS
  /**
   * @param type
   * @return
   */
  private boolean lookAhead(Token.Type type) {
    return nextToken != null && nextToken.type == type;
  }

//  /**
//   * @param type
//   * @param value
//   * @return
//   */
//  private boolean lookAhead(Token.Type type, String value) {
//    return lookAhead(type) && nextToken.value.equals(value);
//  }

  /**
   * @return returns true if the next token is one of the four literals.
   *         Otherwise false.
   */
  private boolean lookAheadLiteral() {
    return lookAhead(Token.Type.INT_LIT) || lookAhead(Token.Type.DIR_LIT)
        || lookAhead(Token.Type.COORD_LIT) || lookAhead(Token.Type.STRING_LIT);
  }

  /**
   * @return
   */
  private boolean lookAheadAtomic() {
    return lookAhead(Token.Type.LPAREN) || lookAhead(Token.Type.VAR)
        || lookAhead(Token.Type.LBRACKET)
        || lookAhead(Token.Type.PATTERN_BEGIN) || lookAhead(Token.Type.THIS)
        || lookAhead(Token.Type.SUPER) || lookAheadLiteral()
        || lookAhead(Token.Type.TYPE) || lookAhead(Token.Type.CONSTANT);
  }

  /**
   * @return
   */
  private boolean lookAheadOperation() {
    return lookAhead(Token.Type.NOT_OPERATOR) || lookAheadAtomic();
  }

  /**
   * @return returns true of the next topen is an expression. Otherwise false.
   */
  private boolean lookAheadExpression() {
    return lookAhead(Token.Type.LET) || lookAhead(Token.Type.IF)
        || lookAhead(Token.Type.LAMBDA_BEGIN)
        || lookAhead(Token.Type.NOT_OPERATOR) || lookAheadOperation();
  }

  /**
   * @return
   */
  private boolean lookAheadPatternCheck() {
    return lookAhead(Token.Type.PATTERN_KEYWORD) || lookAhead(Token.Type.THIS)
        || lookAhead(Token.Type.TYPE);
  }

  /**
   * @return
   */
  private boolean lookAheadPatternValue() {
    return lookAhead(Token.Type.DIR_LIT) || lookAhead(Token.Type.VAR)
        || lookAheadPatternCheck() || lookAhead(Token.Type.PATTERN_NOT)
        || lookAhead(Token.Type.LPAREN);
  }

  // PROGRAM STRUCTURE
  /**
   * @return returns an abstract syntax tree
   * @throws SyntaxError
   */
  private AstNode program() throws SyntaxError {
    AstNode root = astNode(Type.PROGRAM, "");
    while (lookAhead(Token.Type.DEFINE) || lookAhead(Token.Type.TYPE)) {
      if (lookAhead(Token.Type.DEFINE)) {
        root.addChild(constantDef());
      }
      else if (lookAhead(Token.Type.TYPE)) {
        root.addChild(typeDef());
      }
    }
    return root;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode constantDef() throws SyntaxError {
    AstNode node = astNode(Type.CONSTANT_DEF, "");
    expect(Token.Type.DEFINE);
    expect(Token.Type.CONSTANT);
    if (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(varList());
    }
    expect(Token.Type.ASSIGN_OPERATOR);
    node.addChild(expression());
    return node;
  }

  private AstNode abstractDef() throws SyntaxError {
    AstNode node = astNode(Type.ABSTRACT_DEF, currentToken.value);
    expect(Token.Type.DEFINE);
    expect(Token.Type.ABSTRACT);
    expect(Token.Type.CONSTANT);
    node.addChild(astNode(Type.CONSTANT, currentToken.value));
    if (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(varList());
    }
    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode typeDef() throws SyntaxError {
    AstNode node = astNode(Type.TYPE_DEF, currentToken.value);
    expect(Token.Type.tTYPE); // "type"
    expect(Token.Type.TYPE);  // type
    node.addChild(astNode(Type.TYPE, currentToken.value));
    node.addChild(varList());
    if (accept(Token.Type.EXTENDS)) {
      expect(Token.Type.TYPE);
      node.addChild(astNode(Type.TYPE, currentToken.value));
      node.addChild(list());
    }
    if (lookAhead(Token.Type.LBRACE)) {
      node.addChild(typeBody());
    }
    return node;
  }

  private AstNode typeBody() throws SyntaxError {
    AstNode typebody = astNode(Type.TYPE_BODY, "");
    expect(Token.Type.LBRACE);
    while (accept(Token.Type.DEFINE)) {
      if (accept(Token.Type.ABSTRACT)) {
        typebody.addChild(abstractDef());
      }
      else {
        typebody.addChild(constantDef());
      }
    }
    expect(Token.Type.RBRACE);
    return typebody;
  }
  
  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode varList() throws SyntaxError {
    AstNode node = astNode(Type.VARLIST, "");
    expect(Token.Type.LBRACKET);
    if (accept(Token.Type.VAR)) {
      node.addChild(astNode(Type.VAR, currentToken.value));
      while (accept(Token.Type.COMMA)) {
        expect(Token.Type.VAR);
        node.addChild(astNode(Type.VAR, currentToken.value));
      }
      if (accept(Token.Type.COMMA)) {
        node.addChild(vars());
      }
    }
    else if (lookAhead(Token.Type.TRIPLEDOTS)) {
      node.addChild(vars());
    }
    expect(Token.Type.RBRACKET);
    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode vars() throws SyntaxError {
    AstNode node = astNode(Type.VARS, "");
    expect(Token.Type.TRIPLEDOTS);
    expect(Token.Type.VAR);
    node.value = currentToken.value;
    return node;
  }

  // EXPRESSIONS
  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode expression() throws SyntaxError {
    if (lookAhead(Token.Type.LET)) {
      return assignment();
    }
    else if (lookAhead(Token.Type.IF)) {
      return ifExpression();
    }
    else if (lookAhead(Token.Type.LAMBDA_BEGIN)) {
      return lambdaExpression();
    }
    else if (accept(Token.Type.NOT_OPERATOR)) {
      if (lookAheadExpression()) {
        // is this correct?
        AstNode notExpression = astNode(Type.NOT_OPERATOR, "");
        notExpression.addChild(expression());
        return notExpression;
      }
      else {
        throw unexpectedError("an expression");
      }
    }
    else if (lookAheadAtomic() || lookAhead(Token.Type.NOT_OPERATOR)) {
      return operation();
    }
    else {
      throw unexpectedError("an expression");
    }
  }

  private AstNode operation() throws SyntaxError {
    AstNode operation = astNode(Type.OPERATION, "");
    operation.addChild(negation());
    if (accept(Token.Type.SHARED_OPERATOR)){
      operation.addChild(astNode(Type.OPERATOR, currentToken.value));
      operation.addChild(expression());
    }
    return operation;
  }
  
  private AstNode negation() throws SyntaxError {
    AstNode negation = null;
    if (accept(Token.Type.NOT_OPERATOR)) { // "-"?
      negation = astNode(Type.NOT_OPERATOR, "");
      negation.addChild(negation());
      return negation;
    }
    else if (lookAheadAtomic()) {
      negation = astNode(Type.ELEMENT, "");
      negation.addChild(element());
      return negation;
    }
    else{
      throw unexpectedError("a negation");
    }
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode element() throws SyntaxError {
    AstNode node = callSequence();
    while (lookAhead(Token.Type.DOT_OPERATOR)) {
      node.addChild(memberAccess());
    }
    return node;
  }

  private AstNode callSequence() throws SyntaxError {
    AstNode node = atomic();
    while (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(list());
    }
    return node;
  }

  private AstNode memberAccess() throws SyntaxError {
    AstNode node = astNode(Type.MEMBER_ACCESS, "");
    expect(Token.Type.DOT_OPERATOR);
    expect(Token.Type.CONSTANT);
    node.addChild(astNode(Type.CONSTANT, currentToken.value));
    while (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(list());
    }
    return node;
  }

  private AstNode atomic() throws SyntaxError {
    AstNode node = null;
    if(accept(Token.Type.LPAREN)){
      node = expression();
      expect(Token.Type.RPAREN);
      return node;
    }
    else if (accept(Token.Type.VAR)){
      node = astNode(Type.VAR, currentToken.value);
      return node;
    }
    else if (lookAhead(Token.Type.LBRACKET)){
      node = list();
      return node;
    }
    else if (accept(Token.Type.PATTERN_BEGIN)){
      node = pattern();
      return node;
    }
    else if(accept(Token.Type.THIS)){
      node = astNode(Type.THIS, "");
      return node;
    }
    else if(accept(Token.Type.SUPER)){
      node = astNode(Type.SUPER, "");
      return node;
    }
    else if(lookAheadLiteral()){ 
      if(accept(Token.Type.DIR_LIT)){ 
        node = astNode(Type.DIR_LIT, currentToken.value);
        return node;
      }
      else if(accept(Token.Type.COORD_LIT)){
        node = astNode(Type.COORD_LIT, currentToken.value);
        return node;
      }
      else if(accept(Token.Type.INT_LIT)){
        node = astNode(Type.INT_LIT, currentToken.value);
        return node;
      }
      else{
        expect(Token.Type.STRING_LIT);
        node = astNode(Type.STRING_LIT, currentToken.value);
        return node;
      }
    }
    else if(accept(Token.Type.TYPE)){
      node = astNode(Type.TYPE, currentToken.value);
      return node;
    }
    else if(accept(Token.Type.CONSTANT)){
      node = astNode(Type.CONSTANT, currentToken.value);
      return node;
    }
    else{
      throw unexpectedError("an atomic value");
    }
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode assignment() throws SyntaxError {
    AstNode node = astNode(Type.ASSIGNMENT, "");
    expect(Token.Type.LET);
    expect(Token.Type.VAR);
    node.addChild(astNode(Type.VAR, currentToken.value));
    expect(Token.Type.ASSIGN_OPERATOR);
    node.addChild(expression());
    while (accept(Token.Type.COMMA)) {
      AstNode assignment = astNode(Type.ASSIGNMENT, "");
      expect(Token.Type.VAR);
      assignment.addChild(astNode(Type.VAR, currentToken.value));
      expect(Token.Type.ASSIGN_OPERATOR);
      assignment.addChild(expression());
      node.addChild(assignment);
    }
    expect(Token.Type.IN);
    node.addChild(expression());
    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
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

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode lambdaExpression() throws SyntaxError {
    AstNode node = astNode(Type.LAMBDA_EXPR, "");
    expect(Token.Type.LAMBDA_BEGIN);
    node.addChild(varList());
    expect(Token.Type.LAMBDA_OPERATOR);
    node.addChild(expression());
    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode list() throws SyntaxError {
    AstNode node = astNode(Type.LIST, "");
    expect(Token.Type.LBRACKET);
    if (lookAheadExpression()) {
      node.addChild(expression());
      while (accept(Token.Type.COMMA)) {
        node.addChild(expression());
      }
    }
    expect(Token.Type.RBRACKET);

    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode pattern() throws SyntaxError {
    AstNode node = astNode(Type.PATTERN, "");
    node.addChild(patternExpression());
    while (lookAheadPatternValue()) {
      node.addChild(patternExpression());
    }

    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode patternExpression() throws SyntaxError {
    AstNode subject = patternValue();
    if (accept(Token.Type.PATTERN_OR)) {
      AstNode node = astNode(Type.PATTERN_OR, "");
      node.addChild(subject);
      node.addChild(patternExpression());
      return node;
    }
    else if (accept(Token.Type.PATTERN_OPERATOR)
        || accept(Token.Type.SHARED_OPERATOR)) {
      AstNode node = astNode(Type.PATTERN_OPERATOR, currentToken.value);
      node.addChild(subject);
      return node;
    }
    return subject;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode patternValue() throws SyntaxError {
    AstNode node = null;
    if (accept(Token.Type.DIR_LIT)) {
      node = astNode(Type.DIR_LIT, currentToken.value);
    }
    else if (accept(Token.Type.VAR)) {
      node = astNode(Type.VAR, currentToken.value);
    }
    else if (lookAhead(Token.Type.PATTERN_KEYWORD)
        || lookAhead(Token.Type.THIS) || lookAhead(Token.Type.TYPE)) {
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
        AstNode mult = astNode(Type.PATTERN_MULTIPLIER, currentToken.value);
        mult.addChild(node);
        node = mult;
      }
    }
    else {
      throw unexpectedError("a pattern value");
    }
    return node;
  }

  /**
   * @return
   * @throws SyntaxError
   */
  private AstNode patternCheck() throws SyntaxError {
    AstNode node = null;
    if (accept(Token.Type.PATTERN_KEYWORD)) {
      node = astNode(Type.PATTERN_KEYWORD, currentToken.value);
    }
    else if (accept(Token.Type.THIS)) {
      node = astNode(Type.THIS, "");
    }
    else if (accept(Token.Type.TYPE)) {
      node = astNode(Type.TYPE, currentToken.value);
    }
    else {
      throw unexpectedError("a pattern keyword, this, or a type");
    }
    return node;
  }
}