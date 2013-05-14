package dk.aau.cs.d402f13.evaluation;

public class NFAState extends State {
  String name;
  static int nextId = 0; //id used for printing dot files for nicely comparing NFA to equivalent DFA
  static void resetNaming() {
    nextId = 0;
  }
  public NFAState(){
    this.name = ""+nextId;
    nextId++;
  }
  @Override
  public String getName(){
    return this.name;
  }
}
