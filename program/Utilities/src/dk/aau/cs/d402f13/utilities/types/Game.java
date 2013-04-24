package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
	private int currentPlayer = 0;
	private List<Action> history = new ArrayList<Action>();
	private String title;
	
	public Game( String title ){
		this.title = title;
	}
	
	public String title() {
		return title;
	}
	
	public void applyAction( Action action ){
		history.add( action );
		action.applyAction( this );
		
		//Next player
		currentPlayer++;
		if( players().size() <= currentPlayer )
			currentPlayer = 0;
	}
	
	public abstract Board board();
	public abstract List<Player> players();
	public List<Player> turnOrder(){
		return players();
	}
	
	public Player currentPlayer(){ return players().get(currentPlayer); }
	public List<Action> history(){ return history; }
	
	
	public List<Action> actions(){
		List<Action> acts = new ArrayList<Action>();
		
		for( Piece p : board().getPieces() ){
			if( p.player() == currentPlayer() )
				acts.addAll( p.actions(this) );
		}
		
		//TODO: add actions by player
		
		return acts;
	}
}
