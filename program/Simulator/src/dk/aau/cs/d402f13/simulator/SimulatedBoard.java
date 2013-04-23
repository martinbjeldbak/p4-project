package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Input;

import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.MoveAction;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

public abstract class SimulatedBoard {
	protected SimulatedGame game;
	
	protected List<Square> hintSquares = new ArrayList<Square>();
	protected Square selected;
	protected Piece dragged = null;
	protected int dragStartX = 0;
	protected int dragStartY = 0;
	protected int dragOffsetX = 0;
	protected int dragOffsetY = 0;

	public SimulatedBoard(SimulatedGame game) {
		this.game = game;
	}
	
	public abstract Square findSquare(int x, int y);

	public boolean squareIsHinted( Square s ){
		return hintSquares.indexOf( s ) != -1;
	}
	
	public void mouseClicked( int button, int x, int y ){
        if( button == Input.MOUSE_LEFT_BUTTON ){
	    	Square s = findSquare(x, y);
	    	List<Action> actions = game.getGame().actions();
	    	
	    	if( selected != null && squareIsHinted( s ) ){
	    		//We already have selected a piece, and we are trying to
	    		//select an action

	    		//Find the actions from the all available ones
	    		List<Action> acts = new ArrayList<Action>();
	    		if( selected == s ){
	    			for( Action a : actions )
	    				if( ActionHelper.isDropAction( a, s ) )
	    					acts.add( a );
	    		}
	    		else{
	    			for( Action a : actions ){
	    				//Find actions when destination was selected
	    				Piece p = game.getGame().board().findPieceOnSquare(s);
    					if( ActionHelper.isMoveAction( a, selected ) ){
    						if( ActionHelper.isMoveAction( a, p ) )
	    						acts.add( a );
    					}
    					
    					//Find actions when the piece was selected
    					p = game.getGame().board().findPieceOnSquare(selected);
    					if( ActionHelper.isMoveAction( a, s ) ){
    						if( ActionHelper.isMoveAction( a, p ) )
	    						acts.add( a );
    					}
	    			}
	    		}
	    		
	    		executeActions( acts );
	    	}
	    	else{
	        	hintSquares.clear();
        	
		    	//Select a square if a piece is on it
		        if( s != null ){
		        	//Find hints for a piece on this square
		        	Piece p = game.getGame().board().findPieceOnSquare(s);
		        	if( p != null ){
		        		for( Action a : actions ){
		        			if( ActionHelper.isMoveAction( a, p ) )
		        				hintSquares.add( ((MoveAction)a).getTo() );
		        				//TODO: this wouldn't work for sequences!!
		        		}
		        		
		        		//Start drag if actions found
		        		if( hintSquares.size() > 0 ){
			        		dragged = p;
			        		dragStartX = x;
			        		dragStartY = y;
			        		dragOffsetX = 0;
			        		dragOffsetY = 0;
			        		alignDrag();
		        		}
		        	}
		        	
		        	//Add related squares
		        	for( Action a : actions ){
		        		if( ActionHelper.isMoveAction( a, s ) ){
		        			hintSquares.add( ((MoveAction)a).getPiece().square() );
		        			//TODO: this wouldn't work for sequences!!
		        		}
		        		if( ActionHelper.isDropAction( a, s ) )
		        			hintSquares.add( s );
		        	}
		        	
		        	//Select square if actions are possible
		        	if( hintSquares.size() > 0 )
		        		selected = s;
		        }
	    	}
        }
	}
	
	public void mouseDragged(int oldx, int oldy, int newx, int newy){
		if( dragged != null ){
			dragOffsetX += newx - oldx;
			dragOffsetY += newy - oldy;
		}
	}
	
	public void mouseReleased( int button, int x, int y ){
		if( button == Input.MOUSE_LEFT_BUTTON ){
			if( dragged != null ){
				//TODO: check position and act
				Square end = findSquare( dragStartX + dragOffsetX, dragStartY + dragOffsetY );
				if( squareIsHinted( end ) && end != selected ){
					//end == selected is handled in mousePressed!
					List<Action> actions = new ArrayList<Action>();
					for( Action a : game.getGame().actions() )
						if( ActionHelper.isMoveAction( a, end ) )
							if( ActionHelper.isMoveAction( a, dragged ) )
								actions.add( a );
					executeActions( actions );
					
			    	selected = null;
			    	hintSquares.clear();
				}
			}
		}

		dragged = null;
	}
	
	private void executeActions( List<Action> actions ){
		//Check how many actions are found
		if( actions.size() > 1 ){
			System.out.println( "More than one action found: " + actions.size() );
			//TODO: give a prompt to the user to select move
			for( Action a : actions )
				ActionHelper.debugAction( a );
		}
		if( actions.size() == 0 )
			System.out.println( "No actions found !!!" );
			//TODO: throw exception
		
		//Apply the action and remove hints
		game.getGame().applyAction( actions.get(0) );
    	selected = null;
    	hintSquares.clear();
	}
	
	protected abstract void alignDrag();
	
}
