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
