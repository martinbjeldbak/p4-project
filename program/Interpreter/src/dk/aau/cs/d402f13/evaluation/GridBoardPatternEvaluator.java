package dk.aau.cs.d402f13.evaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import dk.aau.cs.d402f13.gal.wrappers.GameWrapper;
import dk.aau.cs.d402f13.gal.wrappers.PieceWrapper;
import dk.aau.cs.d402f13.gal.wrappers.SquareWrapper;
import dk.aau.cs.d402f13.interpreter.stdenv.game.GameEnvironment;
import dk.aau.cs.d402f13.utilities.SimpleDir;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Game;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.values.*;

public class GridBoardPatternEvaluator {
  
  private GameWrapper game;
  private boolean negate; //when using a '!' in a pattern, this is changed

  public boolean doesPatternMatch(GameWrapper game, PatternValue pv, CoordValue squarePos) throws StandardError{
    this.negate = false;
    this.game = game;
    HashSet<SimpleDir> workingSet = new HashSet<SimpleDir>();
    workingSet.add(new SimpleDir(squarePos.getX(),squarePos.getY()));
    evaluate(pv, workingSet);
    if (workingSet.size() == 0){
//      System.out.println("Pattern does not match");
    	return false;	
    }
    else{
//      System.out.println("Pattern matches (" + squarePos.getX() + "," + squarePos.getY()+") " + pv);
    	return true;
    }
    
  }
  
  private Player currentPlayer() throws StandardError{
    return this.game.getCurrentPlayer();
  }
  
  private void evaluate(Value v, HashSet<SimpleDir> workingSet) throws StandardError{
    if (v instanceof PatternOrValue)
      evaluatePatternOrValue((PatternOrValue)v, workingSet);
    else if (v instanceof DirValue)
      addDirValue((DirValue)v, workingSet); //adds direction to all SimpleDir in current set
    else if (v instanceof PatternKeyValue)
      foundPatternKeyValue((PatternKeyValue)v, workingSet); //adds dirValue to all dirs in current set
    else if (v instanceof PatternNotValue){
      this.negate = true;
      evaluate(((PatternNotValue)v).getValue(), workingSet);
      this.negate = false;
    }
    else if (v instanceof PatternPlusValue)
      evaluatePatternPlusValue((PatternPlusValue)v, workingSet);
    else if (v instanceof PatternMultValue)
      evaluatePatternMultValue((PatternMultValue)v, workingSet);
    else if (v instanceof PatternOptValue)
      evaluatePatternOptValue((PatternOptValue)v, workingSet);
    else if (v instanceof PatternValue)
      evaluatePatternValue((PatternValue)v, workingSet);
    else if (v instanceof TypeValue)
      foundTypeValue((TypeValue)v, workingSet);
    else if (v instanceof ObjectValue)
      foundObjectValue((ObjectValue)v, workingSet);
    else
      throw new StandardError("Not intended PatternValue");
  }
  private void evaluatePatternOptValue(PatternOptValue pov, HashSet<SimpleDir> workingSet) throws StandardError{
	  //PatternOptValue contains only 1 value
	  //evaluate the value once on a clone, so it does not effect the set containing no evaluation 
	  HashSet<SimpleDir> clone = makeClone(workingSet);
	  evaluatePatternValue(new PatternValue(pov.getValue()), clone);
	  //unite the set after evaluation once and the original set.
	  unionOnFirst(workingSet, clone);
  }
  private void evaluatePatternPlusValue(PatternPlusValue pmv, HashSet<SimpleDir> workingSet) throws StandardError{
	  //PatternPlusValue contains only 1 value
	  //evaluate the value once
	  evaluatePatternValue(new PatternValue(pmv.getValue()), workingSet);
	  //evaluate the value 0 to many times
	  evaluatePatternMultValue(new PatternMultValue(pmv.getValue()), workingSet);
  }
  private void evaluatePatternMultValue(PatternMultValue pmv, HashSet<SimpleDir> workingSet) throws StandardError{
   if (pmv.getTimes() == 0) //kleenee-star
     evaluateKleeneeStar(pmv, workingSet);
   else{
     for (int i = 0; i < pmv.getTimes(); i++){
       evaluate(pmv.getValue(), workingSet);
     }
   }
  }
  private void evaluateKleeneeStar(PatternMultValue pmv, HashSet<SimpleDir> workingSet) throws StandardError{
    HashSet<SimpleDir> applyKleeneeOn = workingSet;
    HashSet<SimpleDir> lastSet;
    do
    { 
      lastSet = makeClone(workingSet); 
      
      //Ensure that kleenee-star is not applied to all sequences but only those generated by last run
      applyKleeneeOn = makeClone(applyKleeneeOn); //fully cloned set and values
      evaluate(pmv.getValue(), applyKleeneeOn);
      
      //add the newly found sequences to the original set
      unionOnFirst(workingSet, applyKleeneeOn);
    }
    //stop is something new has not been added or if anything has gone out of board
    while (!setsAreEqual(workingSet, lastSet));
  }
  
  private boolean setsAreEqual(HashSet<SimpleDir> set1, HashSet<SimpleDir> set2){
    int hash1 = 0, hash2 = 0;
    for (SimpleDir sd : set1)
      hash1 ^= sd.x * 117 + sd.y;
    for (SimpleDir sd : set2)
      hash2 ^= sd.x * 117 + sd.y;
    if (hash1 != hash2)
      return false;
    for (SimpleDir sd : set1)
      if (!set2.contains(sd))
        return false;
    return true;
  }
  
  private void evaluatePatternOrValue(PatternOrValue v, HashSet<SimpleDir> workingSet) throws StandardError {
    HashSet<SimpleDir> clone = makeClone(workingSet); //make sure the side effects from evaluating the left side is not visible when evaluating the right side of or
    evaluate(v.getLeft(), workingSet);
    evaluate(v.getRight(), clone);
    unionOnFirst(workingSet, clone);
  }
 
  private void evaluatePatternValue(PatternValue pv, HashSet<SimpleDir> workingSet) throws StandardError{
    for (Value v : pv.getValues()){
      evaluate(v, workingSet);
    }
  }
  
  private void addDirValue(DirValue dirVal, HashSet<SimpleDir> workingSet) throws StandardError{
    //when changing the x and y coord of a simpledir, it must be reinserted into the HashSet so the hashValues are mapped correct
    ArrayList<SimpleDir> insertAgain = new ArrayList<SimpleDir>();
    for (SimpleDir sd : workingSet){
      sd.x += dirVal.getX();
      sd.y += dirVal.getY();
      if (!outOfBoard(sd.x, sd.y)){ 
		 insertAgain.add(sd);
      }
	}
    workingSet.clear();
    workingSet.addAll(insertAgain);
  }
  
  private void foundPatternKeyValue(PatternKeyValue keyVal, HashSet<SimpleDir> workingSet) throws StandardError{
    //check that they key really complies with the found key value, e.g. 'empty', 'friend', 'foe', or a type
    Iterator<SimpleDir> it = workingSet.iterator();
    while (it.hasNext()){   	
        if (keyIsOk(keyVal, it.next()) == this.negate){
        	it.remove();    
        }
    }
  }
  
  private void foundTypeValue(TypeValue typeVal, HashSet<SimpleDir> workingSet) throws StandardError{
    //Can be a Piece type or a Square type
    Iterator<SimpleDir> it = workingSet.iterator();
    while (it.hasNext()){       
      if (typeIsOk(typeVal, it.next()) == this.negate){
        it.remove();    
      }
    }
  }
  
  private void foundObjectValue(ObjectValue objectVal, HashSet<SimpleDir> workingSet) throws StandardError{
    //Returns a specific Object, e.g. a specific piece by using this
    Iterator<SimpleDir> it = workingSet.iterator();
    while (it.hasNext()){       
      if (objectIsOk(objectVal, it.next()) == this.negate){
        it.remove();    
      }
    }
  }
  
  private boolean objectIsOk(ObjectValue objectVal, SimpleDir position) throws StandardError{
    //A specific object, e.g. a Piece or a Square. 
    //Occurs when using the 'this' keyword inside a specific object
    SquareWrapper foundSquare = this.game.getBoard().getSquareAt(position.x, position.y);
    if (foundSquare.equals(objectVal)){
      return true;
    }
    for (Piece p : foundSquare.getPieces()){
      if (((PieceWrapper)p).getObject().equals(objectVal)){
        return true;
      }
    }
    
    return false;
  }

  private boolean typeIsOk(TypeValue tv, SimpleDir position) throws StandardError{
    //can both mean that the current Square is a given TypeValue or that the current Square
    //contains a Piece of the given TypeValue
    SquareWrapper foundSquare = this.game.getBoard().getSquareAt(position.x, position.y);
    if (foundSquare.getObject().is(tv)){
      return true;
    }
    else{
      for (Piece p : foundSquare.getPieces()){
        if (((PieceWrapper)p).getObject().is(tv)){
          return true;
        }
      }
    }
    return false;
  }


  private boolean keyIsOk(PatternKeyValue pv, SimpleDir position) throws StandardError{
    Square foundSquare = this.game.getBoard().getSquareAt(position.x, position.y);
    switch (pv.toString()){
      case "friend":
        if (!friend(foundSquare))
          return false;
        break;
      case "foe":
        if (!foe(foundSquare))
          return false;
        break;
      case "empty":
        if (!empty(foundSquare))
          return false;
        break;
      default:
        throw new StandardError("Unrecognised pattern keyword: " + pv.toString());
    }
    return true;
  }
  
  private boolean friend(Square s) throws StandardError{
    for (Piece p : s.getPieces()){
      if (p.getOwner().equals(currentPlayer()))
        return true;
    }
    return false;
  }
  private boolean foe(Square s) throws StandardError{
    for (Piece p : s.getPieces()){
      if (!p.getOwner().equals(currentPlayer()))
        return true;
    }
    return false;
  }
  private boolean empty(Square s) throws StandardError{
    return s.isEmpty();
  }
  
  private HashSet<SimpleDir> makeClone(HashSet<SimpleDir> set) throws StandardError{
    //Makes a clone of a set of SimpleDirs (clones the SimpleDirs as well)
    HashSet<SimpleDir> result = new HashSet<SimpleDir>();
    for (SimpleDir ds : set)
      result.add(new SimpleDir(ds.x, ds.y));
    return result;
  }
  
  private void unionOnFirst (HashSet<SimpleDir> first, HashSet<SimpleDir> second){
    //adds all elements in the second second set to the first set  
    //setLeft = {n, w, ee}
    //setRight = {e, s, w}
    //modifies setRight to {n, w, ee, e, s}
    for (SimpleDir ds : second)
      first.add(ds);
  }
  
  private boolean outOfBoard(int x, int y) throws StandardError{
    //simple check if a position is outside board
    if (x < 1 || y < 1 || x > this.game.getBoard().getWidth() || y > this.game.getBoard().getHeight()){
      return true;
    }
    return false;
  }

}
