package dk.aau.cs.d402f13.evaluation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.CoordValue;
import dk.aau.cs.d402f13.values.DirValue;
import dk.aau.cs.d402f13.values.PatternKeyValue;
import dk.aau.cs.d402f13.values.Value;



public class DFA {
  public State StartState;
  public ArrayList<DFAState> States = new ArrayList<DFAState>();
  public ArrayList<DFAState> AcceptStates = new ArrayList<DFAState>();
  public ArrayList<Transition> Transitions = new ArrayList<Transition>();

  public DFA(NFA nfa){
    this.StartState = new State();
  }
  
  public DFA(DFAState startState, ArrayList<DFAState> states, ArrayList<DFAState> acceptStates, ArrayList<Transition> transitions){
    this.StartState = startState;
    this.States = states;
    this.AcceptStates = acceptStates;
    this.Transitions = transitions;
  }

  public boolean recognizes(Game game, CoordValue currentCoord) throws StandardError {

    State currentState = this.StartState;
    Square currentSqaure = game.getBoard().getSquareAt(currentCoord.getX(), currentCoord.getY());

    for(Transition edge : this.Transitions) {
      State from = edge.from;
      State to = edge.to;
      Value v = edge.val;

      if(currentState == from) {
        if(v instanceof PatternKeyValue) {
          PatternKeyValue val = (PatternKeyValue)v;


          if(val.toString().toLowerCase() == "friend") {
            if(currentSqaure.isEmpty())
              continue;

            for (Piece piece : currentSqaure.getPieces())
              if (game.getCurrentPlayer() == piece.getOwner()) {
                currentState = to;
                break;
              }
          }
          else if(val.toString().toLowerCase() == "foe" ) {
            if(currentSqaure.isEmpty())
              continue;
          }
          else if(val.toString().toLowerCase() == "empty") {
            if(currentSqaure.isEmpty())
              currentState = to;
          }
        }

        // Move to that state
        //currentState = to;
      }
    }

    return false;
  }
  
  public void toDot(String fileName) {
    Path file = createFile(fileName);

    try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.defaultCharset())) {
      writeLine("digraph NFA {", writer);

      //writeLine("rankdir=LR;", writer);
      writeLine("  node[shape = circle];", writer);

      // Print out the label for each state
      for(State s : this.States) {
        if(this.AcceptStates.contains(s))
          writeLine("  " + s.hashCode() + label(s.getName()) + " [shape = doublecircle];", writer);
        else
          writeLine("  " + s.hashCode() + label(s.getName()) + ";", writer);
      }

      writeLine("", writer);

      // For every edge
      for(Transition tra : this.Transitions) {
        State from = tra.from;
        State to = tra.to;
        Value v = tra.val;

        if(v == null)
          writeLine("  " + from.hashCode() + " -> " + to.hashCode() + label("&#949;") + ";", writer);
        else
          writeLine("  " + from.hashCode() + " -> " + to.hashCode() + label(v.toString()) + ";", writer);
      }

      writeLine("}", writer);
      writer.close();
    }
    catch(IOException ex) {
      System.out.println("Error writing to file");
    }
  }

  private Path createFile(String fileName) {
    Path file = Paths.get(fileName);
    try {
      Files.deleteIfExists(file);
      file = Files.createFile(file);
    } catch (IOException e) {
      System.out.println("Error creating file");
    }
    return file;
  }

  private void writeLine(CharSequence s, BufferedWriter writer) {
    try {
      writer.append(s);
      writer.newLine();
    } catch (IOException e) {
      System.out.print("Failed to write line");
    }
  }

  private String label(String label) {
    return " [label=\"" + label + "\"]";
  }
  
}
