package dk.aau.cs.d402f13.gal;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import dk.aau.cs.d402f13.gal.wrappers.GameWrapper;
import dk.aau.cs.d402f13.interpreter.Interpreter;
import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.parser.Parser;
import dk.aau.cs.d402f13.scanner.Scanner;
import dk.aau.cs.d402f13.scopechecker.ScopeChecker;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.ast.AstNode;
import dk.aau.cs.d402f13.utilities.errors.Error;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.values.TypeValue;

public class GameAbstractionLayer {

  private GameEnvironment env = new GameEnvironment();
  private Interpreter interpreter = new Interpreter(env);
  
  public GameAbstractionLayer(InputStream input) throws Error {
    Scanner s = new Scanner(input);
    LinkedList<Token> tokens = new LinkedList<Token>();
    Token ts;
    while ((ts = s.scan()).type != Token.Type.EOF) {
      tokens.add(ts);
    }
    Parser p = new Parser();
    AstNode ast = p.parse(tokens);
    ScopeChecker scopeChecker = new ScopeChecker();
    scopeChecker.visit(ast);
    interpreter.visit(ast);
  }
  
  public GameWrapper getGame() throws StandardError {
    TypeValue gameType = env.findGameType();
    if (gameType == null) {
      return null;
    }
    return new GameWrapper(env, gameType.getInstance(interpreter));
  }
  
  public GameWrapper[] getGameVariants() throws StandardError {
    List<TypeValue> gameTypes = env.findGameTypes();
    GameWrapper[] variants = new GameWrapper[gameTypes.size()];
    for (int i = 0; i < variants.length; i++) {
      variants[i] = new GameWrapper(env, gameTypes.get(i).getInstance(interpreter));
    }
    return variants;
  }

}
