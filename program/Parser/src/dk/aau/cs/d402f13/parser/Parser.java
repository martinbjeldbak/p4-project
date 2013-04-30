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
 */
public class Parser {
  private LinkedList<Token> tokens;
  private Token currentToken;
  private Token nextToken;

  /**
   * Begins parsing source code with program as its root.
   * @param tokens a linked list of tokens.
   * @return an AST with program as the root.
   * @throws SyntaxError if the current token was not expected.
   */
  public AstNode parse(LinkedList<Token> tokens) throws SyntaxError {
    this.tokens = tokens;
    currentToken = null;
    nextToken = tokens.peek();

    return program();
  }

  /**
   * Begins parsing source code with expression as its root.
   * @param tokens a linked list of tokens.
   * @return an AST with expression as the root.
   * @throws SyntaxError if the current token was not expected.
   */
  public AstNode parseAsExpression(LinkedList<Token> tokens) throws SyntaxError {
    this.tokens = tokens;
    currentToken = null;
    nextToken = tokens.peek();

    return expression();
  }

  /**
   * Begins parsing source code with definition as its root.
   * @param tokens a linked list of tokens.
   * @return an AST with defintion as the root.
   * @throws SyntaxError if the current token was not expected.
   */
  public AstNode parseAsDefinition(LinkedList<Token> tokens) throws SyntaxError {
    this.tokens = tokens;
    currentToken = null;
    nextToken = tokens.peek();

    return definition();
  }

  /**
   * Looks one token ahead and checks if this token is as expected. If so it pops
   * the token and returns true. Else false. There is no need for the expect() method
   * because this method pops the next token automatically.
   * @param type the next expected token.
   * @return true if the next token is as expected, else false.
   */
  private boolean accept(Token.Type type) {
    if (lookAhead(type)) {
      currentToken = pop();
      return true;
    }
    return false;
  }

  /**
   * A method for throwing SyntaxErrors. Takes a string as parameter and 
   * possibly a description of the current or next token and returns a 
   * descriptive error regarding the syntax.
   * @param expected a string with information about the syntactic error.  
   *        The parser has received something unexpected and therefore a syntax
   *        error will be thrown. The string received as input will be
   *        concatenated with the original error message and returned.
   * @return new SyntaxError with a description of what has happened.
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
   * Uses the accept() method to look one token ahead and check if the next 
   * token is as expected. If so, it returns the a new currentToken, else a 
   * SyntaxError is thrown.
   * @param type the next expected token.
   * @return new currentToken if the next token is as expected.
   * @throws SyntaxError if the next token is not as ecxpected.
   */
  private Token expect(Token.Type type) throws SyntaxError {
    if (accept(type)) {
      return currentToken;
    }
    throw unexpectedError(type.toString());
  }

  /**
   * Creates a new node of type AstNode to be combined with the rest of the 
   * nodes of the AST.
   * @param type the node type.
   * @param value the given node type's value.
   * @return new AstNode with type and value of the given node type. Placement is also given
   * with information about which line and offset the token is at in the source code.
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
   * Pops the next token. Refreshes the new nextToken.
   * @return next which was the next token.
   */
  private Token pop() {
    Token next = nextToken;
    tokens.poll();
    nextToken = tokens.peek();
    return next;
  }

  // DIFFERENT LOOKAHEAD METHODS
  /**
   * Looks ahead in the list of tokens to determine if the given type
   * matces the type of the next token.  
   * @param type the type which we want to determine is a match with the
   * type of the next token.
   * @return true if the nextToken is not null and if the types match. 
   * Else false.
   */
  private boolean lookAhead(Token.Type type) {
    return nextToken != null && nextToken.type == type;
  }

  /**
   * Checks if the next token is a literal value.
   * @return true if the next token is one of the four literals. Otherwise false.
   */
  private boolean lookAheadLiteral() {
    return lookAhead(Token.Type.LIT_INT) || lookAhead(Token.Type.LIT_DIR)
        || lookAhead(Token.Type.LIT_COORD) || lookAhead(Token.Type.LIT_STRING);
  }

  /**
   * Checks if the next token is an atomic value.
   * @return true if the next token is an atomic value. Otherwise false.
   */
  private boolean lookAheadAtomic() {
    return lookAhead(Token.Type.LPAREN) || lookAhead(Token.Type.VAR)
        || lookAhead(Token.Type.LBRACKET)
        || lookAhead(Token.Type.OP_DIV) || lookAhead(Token.Type.KEY_THIS)
        || lookAhead(Token.Type.KEY_SUPER) || lookAheadLiteral()
        || lookAhead(Token.Type.TYPE) || lookAhead(Token.Type.CONSTANT);
  }

  /**
   * Checks if the next token is an operation.
   * @return true if the next token is part of an operation. Otherwise false.
   */
  private boolean lookAheadOperation() {
    return lookAhead(Token.Type.OP_MINUS) || lookAheadAtomic();
  }

  /**
   * Checks if the next token is an expression.
   * @return true of the next token is an expression. Otherwise false.
   */
  private boolean lookAheadExpression() {
    return lookAhead(Token.Type.LET) || lookAhead(Token.Type.KEY_SET) 
        || lookAhead(Token.Type.IF)
        || lookAhead(Token.Type.LAMBDA_BEGIN)
        || lookAhead(Token.Type.OP_NOT) || lookAheadOperation();
  }

  /**
   * Checks if the next token is a pattern.
   * @return true of the next token is a pattern. Otherwise false.
   */
  private boolean lookAheadPatternCheck() {
    return lookAhead(Token.Type.KEY_PATTERN) 
        || lookAhead(Token.Type.KEY_THIS)
        || lookAhead(Token.Type.TYPE);
  }

  /**
   * Checks if the next token is a pattern value.
   * @return true of the next token is a pattern value. Otherwise false.
   */
  private boolean lookAheadPatternValue() {
    return lookAhead(Token.Type.LIT_DIR) || lookAhead(Token.Type.VAR)
        || lookAheadPatternCheck() || lookAhead(Token.Type.OP_PATTERN_NOT)
        || lookAhead(Token.Type.LPAREN);
  }

  // PROGRAM STRUCTURE
  /**
   * Creates a node of the type program.
   * @return root which is the root of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode program() throws SyntaxError {
    AstNode root = astNode(Type.PROGRAM, "");
    while (lookAhead(Token.Type.KEY_DEFINE) || lookAhead(Token.Type.KEY_TYPE)) {
      root.addChild(definition());
    }
    if (nextToken != null) {
      throw unexpectedError("definitions");
    }
    return root;
  }
  
  /**
   * Does not create a node for the type definition. It passes control to either
   * constantDef() or typeDef() where more specific nodes will be created.
   * @return a node for either a constant or type definition of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode definition() throws SyntaxError {
    if (accept(Token.Type.KEY_DEFINE)) {
      return constantDef();
    }
    else if (lookAhead(Token.Type.KEY_TYPE)) {
      return typeDef();
    }
    else {
      throw unexpectedError("a definition");
    }
  }

  /**
   * Creates a node of the type constant definition.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode constantDef() throws SyntaxError {
    AstNode node = astNode(Type.CONSTANT_DEF, "");
    expect(Token.Type.CONSTANT); //constant eller function?
    node.addChild(astNode(Type.CONSTANT, currentToken.value));
    if (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(varList());
    }
    expect(Token.Type.OP_ASSIGN);
    node.addChild(expression());
    return node;
  }

  /**
   * Creates a node of the type abstract definition.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode abstractDef() throws SyntaxError {
    AstNode node = astNode(Type.ABSTRACT_DEF, "");
    expect(Token.Type.KEY_ABSTRACT);
    expect(Token.Type.CONSTANT);
    node.addChild(astNode(Type.CONSTANT, currentToken.value));
    if (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(varList());
    }
    return node;
  }
  
  /**
   * Creates a node of the type data definition.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode dataDef() throws SyntaxError {
    AstNode node = astNode(Type.DATA_DEF, "");
    expect(Token.Type.KEY_DATA);
    expect(Token.Type.VAR); 
    node.addChild(astNode(Type.VAR, currentToken.value));
    expect(Token.Type.OP_ASSIGN);
    node.addChild(expression());
    return node;
  }

  /**
   * Creates a node of the type type definition.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode typeDef() throws SyntaxError {
    AstNode node = astNode(Type.TYPE_DEF, "");
    expect(Token.Type.KEY_TYPE); // "type"
    expect(Token.Type.TYPE);  // type
    node.addChild(astNode(Type.TYPE, currentToken.value));
    node.addChild(varList());
    if (accept(Token.Type.KEY_EXTENDS)) {
      expect(Token.Type.TYPE);
      node.addChild(astNode(Type.TYPE, currentToken.value));
      node.addChild(list());
    }
    if (lookAhead(Token.Type.LBRACE)) {
      node.addChild(typeBody());
    }
    return node;
  }

  /**
   * Creates a node of the type type body.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode typeBody() throws SyntaxError {
    AstNode typeBody = astNode(Type.TYPE_BODY, "");
    expect(Token.Type.LBRACE);
    while (lookAhead(Token.Type.KEY_DEFINE)
        || lookAhead(Token.Type.KEY_DATA)) {
      if (accept(Token.Type.KEY_DEFINE)) {
        if (lookAhead(Token.Type.KEY_ABSTRACT)) {
          typeBody.addChild(abstractDef());
        }
        else {
          typeBody.addChild(constantDef());
        }
      }
      else {
        typeBody.addChild(dataDef());
      }
    }
    expect(Token.Type.RBRACE);
    return typeBody;
  }
  
  /**
   * Creates a node of the type variable list.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode varList() throws SyntaxError {
    AstNode node = astNode(Type.VARLIST, "");
    expect(Token.Type.LBRACKET);
    if (accept(Token.Type.VAR)) {
      node.addChild(astNode(Type.VAR, currentToken.value));
      while (accept(Token.Type.COMMA)) {
        if (lookAhead(Token.Type.OP_DOT_DOT_DOT)) {
          node.addChild(vars());
          break;
        }
        expect(Token.Type.VAR);
        node.addChild(astNode(Type.VAR, currentToken.value));
      }
    }
    else if (lookAhead(Token.Type.OP_DOT_DOT_DOT)) {
      node.addChild(vars());
    }
    expect(Token.Type.RBRACKET);
    return node;
  }

  /**
   * Creates a node of the type vars.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode vars() throws SyntaxError {
    AstNode node = astNode(Type.VARS, "");
    expect(Token.Type.OP_DOT_DOT_DOT);
    expect(Token.Type.VAR);
    node.value = currentToken.value;
    return node;
  }

  // EXPRESSIONS
  /**
   * Does not create a node for the type expression. It passes 
   * control to one of the types an expression can be. Within 
   * these methods a more specific node will be created.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode expression() throws SyntaxError {
    if (lookAhead(Token.Type.LET)) {
      return assignment();
    }
    else if (lookAhead(Token.Type.KEY_SET)) {
      return setExpression();
    }
    else if (lookAhead(Token.Type.IF)) {
      return ifExpression();
    }
    else if (lookAhead(Token.Type.LAMBDA_BEGIN)) {
      return lambdaExpression();
    }
    else if (accept(Token.Type.OP_NOT)) {
      AstNode notExpression = astNode(Type.NOT_OPERATOR, "");
      notExpression.addChild(expression());
      return notExpression;
    }
    else if (lookAheadAtomic() || lookAhead(Token.Type.OP_MINUS)) {
      return loSequence();
    }
    else {
      throw unexpectedError("an expression");
    }
  }

  /**
   * Either creates a node for a logical operation or falls 
   * through to the next sequence.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode loSequence() throws SyntaxError {
    AstNode next = eqSequence();
    if (accept(Token.Type.OP_AND) || accept(Token.Type.OP_OR)) {
      AstNode sequence = astNode(Type.LO_SEQUENCE, "");
      String operation = currentToken.value;
      sequence.addChild(next);
      sequence.addChild(eqSequence());
      sequence.getLast().operation = operation;
      while (accept(Token.Type.OP_AND) || accept(Token.Type.OP_OR)) {
        operation = currentToken.value;
        sequence.addChild(eqSequence());
        sequence.getLast().operation = operation;
      }
      return sequence;
    }
    return next;
  }
  
  /**
   * Either creates a node for a equality operation or falls 
   * through to the next sequence.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode eqSequence() throws SyntaxError {
    AstNode next = cmSequence();
    if (accept(Token.Type.OP_EQUALS) || accept(Token.Type.OP_NOT_EQUALS)
        || accept(Token.Type.OP_IS)) {
      AstNode sequence = astNode(Type.EQ_SEQUENCE, "");
      String operation = currentToken.value;
      sequence.addChild(next);
      sequence.addChild(cmSequence());
      sequence.getLast().operation = operation;
      while (accept(Token.Type.OP_EQUALS) || accept(Token.Type.OP_NOT_EQUALS)
          || accept(Token.Type.OP_IS)) {
        operation = currentToken.value;
        sequence.addChild(cmSequence());
        sequence.getLast().operation = operation;
      }
      return sequence;
    }
    return next;
  }
  
  /**
   * Either creates a node for a comparison operation or falls 
   * through to the next sequence.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode cmSequence() throws SyntaxError {
    AstNode next = asSequence();
    if (accept(Token.Type.OP_LESS_THAN) || accept(Token.Type.OP_LESS_OR_EQUALS)
        || accept(Token.Type.OP_GREATER_THAN) || accept(Token.Type.OP_GREATER_OR_EQUALS)) {
      AstNode sequence = astNode(Type.CM_SEQUENCE, "");
      String operation = currentToken.value;
      sequence.addChild(next);
      sequence.addChild(asSequence());
      sequence.getLast().operation = operation;
      while (accept(Token.Type.OP_LESS_THAN) || accept(Token.Type.OP_LESS_OR_EQUALS)
          || accept(Token.Type.OP_GREATER_THAN) || accept(Token.Type.OP_GREATER_OR_EQUALS)) {
        operation = currentToken.value;
        sequence.addChild(asSequence());
        sequence.getLast().operation = operation;
      }
      return sequence;
    }
    return next;
  }
  
  /**
   * Either creates a node for an addition/subtraction operation or falls 
   * through to the next sequence.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode asSequence() throws SyntaxError {
    AstNode next = mdSequence();
    if (accept(Token.Type.OP_PLUS) || accept(Token.Type.OP_MINUS)) {
      AstNode sequence = astNode(Type.AS_SEQUENCE, "");
      String operation = currentToken.value;
      sequence.addChild(next);
      sequence.addChild(mdSequence());
      sequence.getLast().operation = operation;
      while (accept(Token.Type.OP_PLUS) || accept(Token.Type.OP_MINUS)) {
        operation = currentToken.value;
        sequence.addChild(mdSequence());
        sequence.getLast().operation = operation;
      }
      return sequence;
    }
    return next;
  }
  
  /**
   * Either creates a node for a multiplication/division/modulo operation 
   * or falls through to the next sequence.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode mdSequence() throws SyntaxError {
    AstNode next = negation();
    if (accept(Token.Type.OP_MULT) || accept(Token.Type.OP_DIV)
        || accept(Token.Type.OP_MODULO)) {
      AstNode sequence = astNode(Type.MD_SEQUENCE, "");
      String operation = currentToken.value;
      sequence.addChild(next);
      sequence.addChild(negation());
      sequence.getLast().operation = operation;
      while (accept(Token.Type.OP_MULT) || accept(Token.Type.OP_DIV)
          || accept(Token.Type.OP_MODULO)) {
        operation = currentToken.value;
        sequence.addChild(negation());
        sequence.getLast().operation = operation;
      }
      return sequence;
    }
    return next;
  }
  
  /**
   * Either creates a node of the type negation or returns a node
   * for an element type.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode negation() throws SyntaxError {
    AstNode negation = null;
    if (accept(Token.Type.OP_MINUS)) { // "-"?
      negation = astNode(Type.NEGATION, "");
      negation.addChild(negation());
      return negation;
    }
    return element();
  }

  /**
   * Creates a node for a call sequence. If there is a dot operator
   * then a new node is created where the original node is attached to as 
   * a child to the new node.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode element() throws SyntaxError {
    AstNode next = callSequence();
    if (lookAhead(Token.Type.OP_DOT)) {
      AstNode element = astNode(Type.ELEMENT, "");
      element.addChild(next);
      element.addChild(memberAccess());
      while (lookAhead(Token.Type.OP_DOT)) {
        element.addChild(memberAccess());
      }
      return element;
    }
    return next;
  }

  /**
   * Creates a node for atomic. If there is a left bracket
   * then a new node is created where the original node is attached to as 
   * a child to the new node.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode callSequence() throws SyntaxError {
    AstNode next = atomic();
    if (lookAhead(Token.Type.LBRACKET)) {
      AstNode sequence = astNode(Type.CALL_SEQUENCE, "");
      sequence.addChild(next);
      sequence.addChild(list());
      while (lookAhead(Token.Type.LBRACKET)) {
        sequence.addChild(list());
      }
      return sequence;
    }
    return next;
  }

  /**
   * Creates a node for member access with optional child as a node of type list.
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode memberAccess() throws SyntaxError {
    AstNode node = astNode(Type.MEMBER_ACCESS, "");
    expect(Token.Type.OP_DOT);
    expect(Token.Type.CONSTANT);
    node.addChild(astNode(Type.CONSTANT, currentToken.value));
    while (lookAhead(Token.Type.LBRACKET)) {
      node.addChild(list());
    }
    return node;
  }

  /**
   * Creates a node for an atomic value. Depending on which token is the next
   * in the list, the method creates a node specifically for it. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
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
    else if (accept(Token.Type.OP_DIV)){
      node = pattern();
      expect(Token.Type.OP_DIV);
      return node;
    }
    else if(accept(Token.Type.KEY_THIS)){
      node = astNode(Type.THIS, "");
      return node;
    }
    else if(accept(Token.Type.KEY_SUPER)){
      node = astNode(Type.SUPER, "");
      return node;
    }
    else if(lookAheadLiteral()){ 
      if(accept(Token.Type.LIT_DIR)){ 
        node = astNode(Type.DIR_LIT, currentToken.value);
        return node;
      }
      else if(accept(Token.Type.LIT_COORD)){
        node = astNode(Type.COORD_LIT, currentToken.value);
        return node;
      }
      else if(accept(Token.Type.LIT_INT)){
        node = astNode(Type.INT_LIT, currentToken.value);
        return node;
      }
      else{
        expect(Token.Type.LIT_STRING);
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
   * Creates a node for an assignment. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode assignment() throws SyntaxError {
    AstNode node = astNode(Type.ASSIGNMENT, "");
    expect(Token.Type.LET);
    expect(Token.Type.VAR);
    node.addChild(astNode(Type.VAR, currentToken.value));
    expect(Token.Type.OP_ASSIGN);
    node.addChild(expression());
    while (accept(Token.Type.COMMA)) {
      AstNode assignment = astNode(Type.ASSIGNMENT, "");
      expect(Token.Type.VAR);
      assignment.addChild(astNode(Type.VAR, currentToken.value));
      expect(Token.Type.OP_ASSIGN);
      assignment.addChild(expression());
      node.addChild(assignment);
    }
    expect(Token.Type.IN);
    node.addChild(expression());
    return node;
  }

  /**
   * Creates a node for the set expression. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode setExpression() throws SyntaxError {
    AstNode node = astNode(Type.SET_EXPR, "");
    expect(Token.Type.KEY_SET);
    expect(Token.Type.VAR);
    node.addChild(astNode(Type.VAR, currentToken.value));
    expect(Token.Type.OP_ASSIGN);
    node.addChild(expression());
    while (accept(Token.Type.COMMA)) {
      AstNode assignment = astNode(Type.SET_EXPR, "");
      expect(Token.Type.VAR);
      assignment.addChild(astNode(Type.VAR, currentToken.value));
      expect(Token.Type.OP_ASSIGN);
      assignment.addChild(expression());
      node.addChild(assignment);
    }
    return node;
  }

  /**
   * Creates a node for the if expression. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
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
   * Creates a node for the lambda expression. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode lambdaExpression() throws SyntaxError {
    AstNode node = astNode(Type.LAMBDA_EXPR, "");
    expect(Token.Type.LAMBDA_BEGIN);
    node.addChild(varList());
    expect(Token.Type.OP_LAMBDA);
    node.addChild(expression());
    return node;
  }

  /**
   * Creates a node for the list expression. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
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
  
  //PATTERNS
  /**
   * Creates a node for a pattern. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
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
   * Creates a node for a pattern expression. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode patternExpression() throws SyntaxError {
    AstNode subject = patternValue();
    if (accept(Token.Type.OP_PATTERN_OR)) {
      AstNode node = astNode(Type.PATTERN_OR, "");
      node.addChild(subject);
      node.addChild(patternExpression());
      return node;
    }
    else if (accept(Token.Type.OP_PATTERN_QUESTION) || accept(Token.Type.OP_PLUS)
        || accept(Token.Type.OP_MULT)) {
      AstNode node = astNode(Type.PATTERN_OPERATOR, currentToken.value);
      node.addChild(subject);
      return node;
    }
    return subject;
  }

  /**
   * Creates a node for the pattern value. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode patternValue() throws SyntaxError {
    AstNode node = null;
    if (accept(Token.Type.LIT_DIR)) {
      node = astNode(Type.DIR_LIT, currentToken.value);
    }
    else if (accept(Token.Type.VAR)) {
      node = astNode(Type.VAR, currentToken.value);
    }
    else if (lookAhead(Token.Type.KEY_PATTERN)
        || lookAhead(Token.Type.KEY_THIS) || lookAhead(Token.Type.TYPE)) {
      node = patternCheck();
    }
    else if (accept(Token.Type.OP_PATTERN_NOT)) {
      node = astNode(Type.PATTERN_NOT, "");
      node.addChild(patternCheck());
    }
    else if (accept(Token.Type.LPAREN)) {
      node = pattern();
      expect(Token.Type.RPAREN);
      if (accept(Token.Type.LIT_INT)) {
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
   * Creates a node for the pattern check. 
   * @return node of an AST.
   * @throws SyntaxError if a given token was not expected.
   */
  private AstNode patternCheck() throws SyntaxError {
    AstNode node = null;
    if (accept(Token.Type.KEY_PATTERN)) {
      node = astNode(Type.PATTERN_KEYWORD, currentToken.value);
    }
    else if (accept(Token.Type.KEY_THIS)) {
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