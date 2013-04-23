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
	    	
	    	if( null != selected && squareIsHinted( s ) ){
	    		//Clicked on a square with an action
	    		//Apply the action
	    		Action act;
	    		if( selected == s )
	    			act = findDropAction( s );
	    		else
	    			act = findMoveAction( selected, s );
	    		game.getGame().applyAction( act );
	        	selected = null;
	        	hintSquares.clear();
	    	}
	    	else{
	        	selected = null;
	        	hintSquares.clear();
        	
		    	//Select a square if a piece is on it
		        if( s != null ){
		        	Piece p = game.getGame().board().findPieceOnSquare(s);
		        	if( p != null ){
		        		List<Action> actions = p.actions( game.getGame() );
		        		selected = s;
		        		
		        		//TODO: fix
		        		for( Action a : actions ){
		        			if( (Object)a instanceof MoveAction ){
		        				MoveAction ma = (MoveAction)a;
		        				hintSquares.add( ma.getTo() );
		        			}
		        		}
		        		
		        		//Start drag
		        		dragged = p;
		        		dragStartX = x;
		        		dragStartY = y;
		        		dragOffsetX = 0;
		        		dragOffsetY = 0;
		        		alignDrag();
		        	}
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
					Action act = findMoveAction( dragged.square(), end );
					game.getGame().applyAction( act );
					
			    	selected = null;
			    	hintSquares.clear();
				}
			}
		}

		dragged = null;
	}
	
	protected abstract void alignDrag();
	
	private Action findDropAction( Square s ){
		//TODO:
		System.out.println("Not implemented");
		return null;
	}
	
	private Action findMoveAction( Square start, Square end ){
		Piece p = game.getGame().board().findPieceOnSquare( start );
		if( p == null ){
			System.out.println("Couldn't find piece!!!");
			return null;
		}
		
		for( Action a : p.actions( game.getGame() ) ){
			Action startAction = a;
			do{
				if( (Object)a instanceof MoveAction ){
					MoveAction ma = (MoveAction)a;
					if( ma.getTo() == end && ma.getPiece() == p )
						return startAction;
				}
			} while( (a = a.next()) != null );
		}

		System.out.println("Couldn't find Action!!!");
		return null;
	}
}
