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
    	selected = null;
    	hintSquares.clear();
    	

        if( button == Input.MOUSE_LEFT_BUTTON ){
	    	//Select a square if a piece is on it
	    	Square s = findSquare(x, y);
	        if( s != null ){
	        	Piece p = game.getGame().getBoard().findPieceOnSquare(s);
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
				if( squareIsHinted( end ) ){
					System.out.println("Stopped on valid square");
				}
			}
		}
		
		dragged = null;
	}
	
	protected abstract void alignDrag();
}
