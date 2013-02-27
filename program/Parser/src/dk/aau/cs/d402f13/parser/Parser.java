package dk.aau.cs.d402f13.parser;
import java.util.LinkedList;

import dk.aau.cs.d402f13.parser.AstNode.Type;
import dk.aau.cs.d402f13.scanner.Token;


public class Parser {

	/**
	 * @param args
	 */
	
	private LinkedList<Token> tokens;
	private Token currentToken;
	private Token nextToken;
	
	private Token peek(){
		return nextToken;
	}
	
	private Token pop(){
		Token next = nextToken;
		tokens.poll();
		nextToken = tokens.peek();
		return next;
	}
	
	private boolean lookAhead(Token.Type type){
		return nextToken != null && nextToken.type == type;
	}
	
	private boolean lookAheadLiteral(){
		return lookAhead(Token.Type.INT_LIT)
		    || lookAhead(Token.Type.DIR_LIT)
		    || lookAhead(Token.Type.COORD_LIT)
		    || lookAhead(Token.Type.STRING_LIT);
	}
	
	private boolean lookAheadKeyword(){
	  return lookAhead(Token.Type.GAME)
        || lookAhead(Token.Type.PIECE)
        || lookAhead(Token.Type.THIS)
        || lookAhead(Token.Type.WIDTH)
        || lookAhead(Token.Type.HEIGHT)
        || lookAhead(Token.Type.TITLE)
        || lookAhead(Token.Type.PLAYERS)
        || lookAhead(Token.Type.TURNORDER)
        || lookAhead(Token.Type.BOARD)
        || lookAhead(Token.Type.GRID)
        || lookAhead(Token.Type.SETUP)
        || lookAhead(Token.Type.WALL)
        || lookAhead(Token.Type.NAME)
        || lookAhead(Token.Type.POSSIBLE_DROPS)
        || lookAhead(Token.Type.POSSIBLE_MOVES)
        || lookAhead(Token.Type.WIN_CONDITION)
        || lookAhead(Token.Type.TIE_CONDITION);
	}
	
	private boolean lookAheadElement(){
		return lookAheadLiteral()
			  || lookAhead(Token.Type.LPAREN)
			  || lookAhead(Token.Type.VAR)
			  || lookAhead(Token.Type.LBRACKET)
			  || lookAhead(Token.Type.PATTERNOP)
		    || lookAheadKeyword()
		    || lookAhead(Token.Type.ID);
	}
	
	private boolean accept(Token.Type type){
	  if(lookAhead(type)){
	    currentToken = pop();
	    return true;
	  }
	  return false;
	}
	
	private Token expect(Token.Type type) throws SyntaxError {
	  if(accept(type)){
	    return currentToken;
	  }
	  if(nextToken == null){
	    throw new SyntaxError("Empty token stream, expected " + type, null);
	  }
	  throw new SyntaxError("Unexpected token " + nextToken.type + ", expected " + type, nextToken);
	}
  //done.
	private AstNode program() throws SyntaxError{
	  AstNode root = new AstNode(Type.PROGRAM, "");
	  while(lookAhead(Token.Type.DEFINE)){
	    root.addChild(functionDef());
	  }
	  if(lookAhead(Token.Type.GAME)){
	    root.addChild(gameDecleration());
	  }
	  
	  return root;
	}
  //done.
	private AstNode functionDef() throws SyntaxError {
	  AstNode node = new AstNode(Type.FUNC_DEF, "");
    expect(Token.Type.DEFINE);
    expect(Token.Type.FUNCTION);
    node.addChild(new AstNode(Type.FUNCTION, currentToken.value));
    expect(Token.Type.LBRACKET);
    while(accept(Token.Type.VAR)){
      node.addChild(new AstNode(Type.VAR, currentToken.value));
    }
    expect(Token.Type.RBRACKET);
    node.addChild(expression());
    
    return node;
  }
	//done.
	private AstNode gameDecleration() throws SyntaxError {
    AstNode node = new AstNode(Type.GAME_DECL, "");
    expect(Token.Type.GAME);
    if(lookAhead(Token.Type.LBRACE)){
      node.addChild(declerationStruct());
    }
	  
    return node;
  }
  //done.
  private AstNode declerationStruct() throws SyntaxError {
    AstNode node = new AstNode(Type.DECL_STRUCT, "");
    expect(Token.Type.LBRACE);
    if(lookAheadKeyword() || lookAhead(Token.Type.ID)){
      node.addChild(decleration());
    }
    while(lookAheadKeyword() || lookAhead(Token.Type.ID)){
      node.addChild(decleration());
    }
    expect(Token.Type.RBRACE);
    
    return node;
  }
  //done.
  private AstNode decleration() throws SyntaxError {
    AstNode node = new AstNode(Type.DECL, "");
    //skal der være mindst én her?
    while(lookAheadKeyword() || lookAhead(Token.Type.ID)){
      if(lookAheadKeyword()){
        node.addChild(new AstNode(Type.KEYWORD, currentToken.value));
      }
      else if (lookAhead(Token.Type.ID)){
        node.addChild(new AstNode(Type.ID, currentToken.value));
      }
    }
    node.addChild(structure());
    
    return node;
  }

  private AstNode structure() throws SyntaxError{
    AstNode node = new AstNode(Type.STRUCT, "");
    if(lookAhead(Token.Type.LBRACKET)){
      node.addChild(declerationStruct());
    }
    else if(isExpression()){
      node.addChild(expression());
    }
    
    return node;
  }
  //done.
  private boolean isExpression(){
    if(lookAhead(Token.Type.FUNCTION) 
        || lookAhead(Token.Type.LPAREN) 
        || lookAhead(Token.Type.IF) 
        || lookAhead(Token.Type.LAMBDABEGIN)){
      return true;
    }
    else return false;
  }

  private AstNode expression() {
    // TODO Auto-generated method stub
    return null;
  }

  public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	  /*
	  try {
      AstNode ast = p.parse(tokens);
      ast.print();
      OutputStreamWriter f = new OutputStreamWriter(
        new FileOutputStream(new File("ast.dot"), false)
      );
      ast.export(f);
      f.close();
    }
    catch (SyntaxError e) {
      System.out.flush();
      if (e.getToken() == null) {
        System.err.println("Syntax error: " + e.getMessage());
      }
      else {
        System.err.println("Syntax error: " + e.getMessage()
            + " on input line " + e.getToken().line
            + " offset " + e.getToken().offset + ":");
        System.err.println(input.split("\n")[e.getToken().line - 1]);
        for (int i = 1; i < e.getToken().offset; i++) {
          System.err.print("-");
        }
        System.err.println("^");
      }
    }
    */
	}

}
