package dk.aau.cs.d402f13.scopechecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import dk.aau.cs.d402f13.scanner.Scanner;
import dk.aau.cs.d402f13.utilities.SymbolTable;
import dk.aau.cs.d402f13.utilities.Token;
import dk.aau.cs.d402f13.utilities.errors.ScopeError;
import dk.aau.cs.d402f13.utilities.errors.SyntaxError;
import dk.aau.cs.d402f13.parser.AstNode;
import dk.aau.cs.d402f13.parser.Parser;

public class ScopeChecker {
  
  static SymbolTable symTable = new SymbolTable();

  //Must be called after the AST has been created
 public static void CheckScopes(AstNode root) throws ScopeError{
   symTable.Empty();
   InsertDefaultFunctions();
   Traverse(root);
   //symTable.CheckErrors(); //will throw a ScannerError exception if errors are found
   symTable.Print();
 }
 
 static void InsertDefaultFunctions(){ //the functions that exists in our language
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "andSquares", 1);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "findSquares", 1);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "toActions", 1);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "union", -1);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "forall", 2);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "isEmpty", 1);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "move", 2);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "moveAndCapture", 2);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "isCurrentPlayer", 1);
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, "isFirstMove", 1);
 }

 static void Traverse(AstNode node){
   switch (node.type){
     case FUNC_DEF: FuncDef(node); break;
     case FUNC_CALL: FuncCall(node); break;
   }
   
   //save the iterator, since node.iterator() will return the same iterator even though .next() has been called
   Iterator<AstNode> it = node.iterator(); while (it.hasNext()){
     Traverse(it.next());
   }
 }
 
 static void FuncDef(AstNode node){
   Iterator<AstNode> it = node.iterator();
   String funcName = it.next().value;
   Iterator<AstNode> argIt = it.next().iterator();
   int argNum = 0;
   while (argIt.hasNext()){
     argIt.next();
     argNum++;
   }
   symTable.FoundDeclaredSymbol(Token.Type.FUNCTION, funcName, argNum);
   System.out.println("Found decl of func: " + funcName);
 }
 static void FuncCall(AstNode node){
   Iterator<AstNode> it = node.iterator();
   int argNum = 0;
   while (it.hasNext()){
     it.next();
     argNum++;
   }
   symTable.FoundUsedSymbol(Token.Type.FUNCTION, node.value, argNum);
   System.out.println("Found call to func: " + node.value);
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
       case ":c":
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
             if (line.equals(":c")){
               System.out.println("hej");
               CheckScopes(ast);
               
             }
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
