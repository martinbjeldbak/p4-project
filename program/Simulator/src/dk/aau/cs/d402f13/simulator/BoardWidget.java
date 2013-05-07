package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


import dk.aau.cs.d402f13.helpers.ActionHelper;
import dk.aau.cs.d402f13.helpers.ResourceHelper;
import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.MoveAction;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;
import dk.aau.cs.d402f13.widgets.Message;
import dk.aau.cs.d402f13.widgets.Widget;

/**
 * SimulatedBoard represents and visualizes the game board and its pieces.
 * It makes it possible move pieces by either D&D, or clicking on two squares 
 * in succession.
 * @author Spiller
 *
 */
public abstract class BoardWidget extends Widget {
	protected SimulatedGame game;
	
	protected List<Square> hintSquares = new ArrayList<Square>();
	protected Square selected;
	protected Piece dragged = null;
	protected int dragOffsetX = 0;
	protected int dragOffsetY = 0;
	
	private Message waitForPlayer = null;
	private ActionSelector actionSelector = null;
	
	
	/**
	 * Constructs a SimulatedBoard
	 * @param game SimulatedGame to visualize
	 */
	public BoardWidget( SimulatedGame game ){
		scaleWidth( 0 );
		scaleHeight( 0 );
		this.game = game;
		actionSelector = new ActionAi( game );
		
	//	addObject( new Message( "test", "test2", 0,0, width, height ));
		waitForPlayer = new Message( "Please wait...", "Opponent ponders over his move", 0,0, getWidth(), getHeight() );
		waitForPlayer.startListening( this );
	}
	
	public void setError( String title, String text ){
		Message msg = new Message( title, text, 0,0, getWidth(), getHeight() ); 
		addObject( msg );
	}
	
	/**
	 * Find the Square at the current position in graphic coordinates.
	 * @param x Horizontal absolute coordinate
	 * @param y Vertical absolute coordinate
	 * @return The Square on the specified position, or null if none
	 */
	public abstract Square findSquare(int x, int y);
	
	/**
	 * Finds the square the dragged piece currently hovers on.
	 * @return The square found
	 */
	protected Square hoversOn(){
		if( dragged == null )
			return null;
		return findSquare( dragStartX + dragOffsetX, dragStartY + dragOffsetY );
	}

	/**
	 * Check if a Square is contained in the List of hints
	 * @param s The Square to find
	 * @return True if the square is hinted
	 */
	public boolean squareIsHinted( Square s ){
		return hintSquares.indexOf( s ) != -1;
	}
	
	/**
	 * Handles mouse button clicks for a Simulated board
	 * @param button The mouse button pressed
	 * @param x Horizontal position of the click
	 * @param y Vertical position of the click
	 * @return 
	 */
	@Override
	protected boolean handleMouseClicked( int button, int x, int y ){
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
	    				if( ActionHelper.isDropAction( a, s ) != null )
	    					acts.add( a );
	    		}
	    		else{
	    			for( Action a : actions ){
	    				//Find actions when destination was selected
	    				Piece p = game.getGame().board().findPieceOnSquare(s);
    					if( ActionHelper.isMoveAction( a, selected ) != null ){
    						if( ActionHelper.isMoveAction( a, p ) != null )
	    						acts.add( a );
    					}
    					
    					//Find actions when the piece was selected
    					p = game.getGame().board().findPieceOnSquare(selected);
    					if( ActionHelper.isMoveAction( a, s ) != null ){
    						if( ActionHelper.isMoveAction( a, p ) != null )
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
		        			MoveAction ma = ActionHelper.isMoveAction( a, p ); 
		        			if( ma != null )
		        				hintSquares.add( ma.getTo() );
		        		}
		        		
		        		//Start drag if actions found
		        		if( hintSquares.size() > 0 ){
		        			startDragging( x, y );
			        		dragged = p;
			        		dragOffsetX = 0;
			        		dragOffsetY = 0;
			        		alignDrag();
		        		}
		        	}
		        	
		        	//Add related squares
		        	for( Action a : actions ){
		        		MoveAction ma = ActionHelper.isMoveAction( a, s ); 
		        		if( ma != null )
		        			hintSquares.add( ma.getPiece().square() );
		        		if( ActionHelper.isDropAction( a, s ) != null )
		        			hintSquares.add( s );
		        	}
		        	
		        	//Select square if actions are possible
		        	if( hintSquares.size() > 0 )
		        		selected = s;
		        }
	    	}
	    	return true;
        }
        else
        	return false;
	}
	
	/**
	 * Handles mouse dragging
	 * @param oldx Previous horizontal position of mouse pointer
	 * @param oldy Previous vertical position of mouse pointer
	 * @param newx New horizontal position of mouse pointer
	 * @param newy New vertical position of mouse pointer
	 */
	@Override
	public boolean handleMouseDragged( int oldX, int oldY, int newX, int newY ){
		dragOffsetX += newX - oldX;
		dragOffsetY += newY - oldY;
		return true;
	}
	
	/**
	 * Handles mouse button release
	 * @param button Button which was released
	 * @param x Horizontal position of mouse pointer
	 * @param y Vertical position of mouse pointer
	 */
	@Override
	public boolean handleMouseReleased( int button, int x, int y ){
		if( button == Input.MOUSE_LEFT_BUTTON ){
			if( dragged != null ){
				Square end = findSquare( dragStartX + dragOffsetX, dragStartY + dragOffsetY );
				if( squareIsHinted( end ) && end != selected ){
					//end == selected is handled in mousePressed!
					List<Action> actions = new ArrayList<Action>();
					for( Action a : game.getGame().actions() )
						if( ActionHelper.isMoveAction( a, end ) != null )
							if( ActionHelper.isMoveAction( a, dragged ) != null )
								actions.add( a );
					executeActions( actions );
					
			    	selected = null;
			    	hintSquares.clear();
				}
			}
		}

		dragged = null;
		return true;
	}
	
	/**
	 * Apply an action to the Game, prompt the user to select one if
	 * ambitious.
	 * @param actions
	 */
	private void executeActions( List<Action> actions ){
		//Check how many actions are found
		if( actions.size() > 1 ){
			System.out.println( "More than one action found: " + actions.size() );
			//TODO: give a prompt to the user to select move
			for( Action a : actions )
				ActionHelper.debugAction( a );
		}
		if( actions.size() == 0 ){
			System.out.println( "No actions found !!!" );
			//TODO: throw exception
			setError( "No actions found!", "Something went very wrong :\\" );
			return;
		}
		
		//Apply the action and remove hints
		game.getGame().applyAction( actions.get(0) );
    	selected = null;
    	hintSquares.clear();
    	
    	if( game.getGame().players().get(0) == game.getGame().currentPlayer() ){
    		removeObject( waitForPlayer );
    	}
    	else{
    		addObject( waitForPlayer );
    		actionSelector.retriveAction( this );
    	}
	}
	
	/**
	 * When starting a drag, align the cursor to the center of the piece,
	 * in order to make it drop on the Square which is centered below the
	 * piece, and not at the position at cursor is at.
	 */
	protected abstract void alignDrag();
	

	
	/**
	 * Draw a Piece using board coordinates
	 * @param g Graphics to draw with
	 * @param p Piece to draw
	 * @param x Horizontal board coordinate
	 * @param y Vertical board coordinate
	 * @param size Square size
	 * @param offsetX Horizontal offset of board
	 * @param offsetY Vertical offset of board
	 * @throws SlickException
	 */
	protected void renderPiece( Graphics g, Piece p, int x, int y, int size, int offsetX, int offsetY){
		Image img = ResourceHelper.getImage( p.getImgPath() );
		int imgMax = Math.max( img.getHeight(), img.getWidth() );
		
		int borderSize = (int) (size * 0.05);
		float scale = (size - borderSize * 2) / (float)imgMax;

		int imgYOffset = (int) ((imgMax - img.getHeight() ) * scale / 2);
		int imgXOffset = (int) ((imgMax - img.getWidth() ) * scale / 2);
		
		img = ResourceHelper.getImageScaled( p.getImgPath(), scale );

		img.draw( x + imgXOffset + offsetX + borderSize, y + imgYOffset + offsetY + borderSize );
	}
	
	/**
	 * Draw a Square
	 * @param g Graphics to draw with
	 * @param s Square to draw
	 * @param posX Horizontal position in pixels
	 * @param posY Vertical position in pixels
	 * @param size Size of Square
	 * @throws SlickException
	 */
	protected void renderSquare( Graphics g, Square s, int posX, int posY, int size ){
		Square hover = hoversOn();
		
		//Draw background for square
		Image img = ResourceHelper.getImage( s.getImgPath() );
		int imgMax = Math.max(img.getWidth(), img.getHeight());
		img = ResourceHelper.getImageScaled( s.getImgPath(), (float)size /(float) imgMax );
		img.draw( posX, posY );
		
		if( s == selected ){
			g.setColor( new Color(0,0,127,63) );
			g.fillRect( posX, posY, size, size);
		}
		
		if( squareIsHinted( s ) ){
			if( s == hover )
				g.setColor( new Color(0,255,0,191) );
			else
				g.setColor( new Color(0,255,0,63) );
			g.fillRect( posX, posY, size, size);
		}
		else{
			if( s == hover ){
				g.setColor( new Color(255,0,0,127) );
				g.fillRect( posX, posY, size, size );
			}
		}
	}
	
	@Override
	public void acceptEvent( Widget obj, Event event ){
		if( event == Event.ACCEPT ){
			removeObject( obj );
		}
		else if( event == Event.MOVE_GENERATED ){
			executeActions( actionSelector.getAction() );
		}
	}

	private void executeActions(Action action) {
		List<Action> actions = new ArrayList<Action>();
		actions.add( action );
		executeActions( actions );
	}
}
