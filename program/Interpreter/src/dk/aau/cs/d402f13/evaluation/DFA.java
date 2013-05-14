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
import java.util.List;
import java.util.Stack;

import dk.aau.cs.d402f13.values.Value;



public class DFA{

  DFAState startState;
  ArrayList<DFAState> states;
  ArrayList<DFAState> acceptStates;
  ArrayList<Transition> transitions;
  
  public DFA(DFAState startState, ArrayList<DFAState> states, ArrayList<DFAState> acceptStates, ArrayList<Transition> transitions){
    this.startState = startState;
    this.states = states;
    this.acceptStates = acceptStates;
    this.transitions = transitions;
  }

  public void toDot(String fileName) {
    Path file = createFile(fileName);

    try(BufferedWriter writer = Files.newBufferedWriter(file, Charset.defaultCharset())) {
      writeLine("digraph NFA {", writer);

      //writeLine("rankdir=LR;", writer);
      writeLine("  node[shape = circle];", writer);

      // Print out the label for each state
      for(State s : this.states) {
        if(this.acceptStates.contains(s))
          writeLine("  " + s.hashCode() + label("" + s.getName()) + " [shape = doublecircle];", writer);
        else
          writeLine("  " + s.hashCode() + label("" + s.getName()) + ";", writer);
      }

      writeLine("", writer);

      // For every edge
      for(Transition tra : this.transitions) {
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
