package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;


import dk.aau.cs.d402f13.helpers.ActionHelper;
import dk.aau.cs.d402f13.helpers.ResourceHelper;
import dk.aau.cs.d402f13.utilities.errors.SimulatorError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.utilities.gameapi.AddAction;
import dk.aau.cs.d402f13.utilities.gameapi.MoveAction;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Player;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.utilities.gameapi.Board;
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
	private Message gameEnded = null;
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
		
		gameEnded = new Message( "Game over", "", 0,0, getWidth(), getHeight() );
		waitForPlayer = new Message( "Please wait...", "Opponent ponders over his move", 0,0, getWidth(), getHeight() );
		
		gameEnded.startObserving( this );
		waitForPlayer.startObserving( this );
	}
	
	public void showMessage( Message m, String text ){
		m.setFixed( getWidth(), getHeight() );
		m.setText( text );
		addObject( m );
	}
	
	/**
	 * Find the Square at the current position in graphic coordinates.
	 * @param x Horizontal absolute coordinate
	 * @param y Vertical absolute coordinate
	 * @return The Square on the specified position, or null if none
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	public abstract Square findSquare(int x, int y) throws StandardError, SimulatorError;
	
	/**
	 * Finds the square the dragged piece currently hovers on.
	 * @return The square found
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	protected Square hoversOn() throws StandardError, SimulatorError{
		if( dragged == null )
			return null;
		return findSquare( dragStartX + dragOffsetX, dragStartY + dragOffsetY );
	}
	
	protected abstract Square getSquareFromPiece( Board g, Piece p ) throws StandardError;

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
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	@Override
	protected boolean handleMouseClicked( int button, int x, int y ) throws StandardError, SimulatorError{
        if( button == Input.MOUSE_LEFT_BUTTON ){
	    	Square s = findSquare(x, y);
	    	Action[] actions = game.getGame().getActions();
	    	
	    	if( selected != null && squareIsHinted( s ) ){
	    		//We already have selected a piece, and we are trying to
	    		//select an action

	    		//Find the actions from the all available ones
	    		List<Action> acts = new ArrayList<Action>();
	    		if( selected == s ){
	    			for( Action a : actions ){
	    				if( ActionHelper.isRemoveAction( a, s ) != null )
	    					acts.add( a );
	    				if( ActionHelper.isAddAction( a, s ) != null )
	    					acts.add( a );
	    			}
	    		}
	    		else{
	    			for( Action a : actions ){
	    				//Find actions when destination was selected
	    				for( Piece p : s.getPieces() )
		    				if( ActionHelper.isMoveAction( a, selected ) != null ){
	    						if( ActionHelper.isMoveAction( a, p ) != null )
		    						acts.add( a );
	    					}
    					
    					//Find actions when the piece was selected
    					for( Piece p : selected.getPieces() )
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
		        	for( Piece p : s.getPieces() ){
		        		//TODO: handle move if multiple pieces
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
		        			hintSquares.add( getSquareFromPiece( game.getGame().getBoard(), ma.getPiece() ) );
		        		if( ActionHelper.isRemoveAction( a, s ) != null )
		        			hintSquares.add( s );
		        		
	        			AddAction aa = ActionHelper.isAddAction( a, s );
	        			if( aa != null )
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
	 * @param oldX Previous horizontal position of mouse pointer
	 * @param oldY Previous vertical position of mouse pointer
	 * @param newX New horizontal position of mouse pointer
	 * @param newY New vertical position of mouse pointer
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
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	@Override
	public boolean handleMouseReleased( int button, int x, int y ) throws StandardError, SimulatorError{
		if( button == Input.MOUSE_LEFT_BUTTON ){
			if( dragged != null ){
				Square end = findSquare( dragStartX + dragOffsetX, dragStartY + dragOffsetY );
				if( squareIsHinted( end ) && end != selected ){
					//end == selected is handled in mousePressed!
					List<Action> actions = new ArrayList<Action>();
					for( Action a : game.getGame().getActions() )
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
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	private void executeActions( List<Action> actions ) throws StandardError, SimulatorError{
		//Check how many actions are found
		if( actions.size() > 1 ){
			System.out.println( "More than one action found: " + actions.size() );
			//TODO: give a prompt to the user to select move
			for( Action a : actions )
				System.out.println( ActionHelper.humanReadable( game.getGame(), a ) );
		}
		if( actions.size() == 0 )
			throw new SimulatorError( "Trying to apply action, however it could not be found!" );
		
		//Apply the action and remove hints
		Player previous = game.getGame().getCurrentPlayer();
		game.applyAction( actions.get(0) );
    	selected = null;
    	hintSquares.clear();
    	
    	//Check for winCondition
    	//TODO: make sure waitForPlayer is not shown?
    	if( previous.winCondition( game.getGame() ) ){
    		showMessage( gameEnded, previous.getName() + " won" );
    		return;
    	}
    	
    	game.nextTurn();
    	Player current = game.getGame().getCurrentPlayer();
		if( current.winCondition( game.getGame() ) ){
    		showMessage( gameEnded, current.getName() + " won" );
    		return;
    	}
    	else if( current.tieCondition( game.getGame() ) ){
    		showMessage( gameEnded, "It's a draw" );
    		return;
    	}
		
    	
    	if( game.getGame().getActions().length == 0 ){
    		showMessage( gameEnded, "No actions to do" );
    		return;
    	}
    	
    	if( game.getGame().getPlayers()[0].equals( game.getGame().getCurrentPlayer() ) ){
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
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	protected void renderPiece( Graphics g, Piece p, int x, int y, int size, int offsetX, int offsetY) throws StandardError, SimulatorError{
		if( !p.isOnBoard() )
			return;
		
		String imgPath = game.getGameFolder() + p.getImage();
		String imgFallback = "img/defaultPiece.png";
		
		Image img = ResourceHelper.getImage( imgPath, imgFallback );
		int imgMax = Math.max( img.getHeight(), img.getWidth() );
		
		int borderSize = (int) (size * 0.05);
		float scale = (size - borderSize * 2) / (float)imgMax;

		int imgYOffset = (int) ((imgMax - img.getHeight() ) * scale / 2);
		int imgXOffset = (int) ((imgMax - img.getWidth() ) * scale / 2);
		
		img = ResourceHelper.getImageScaled( imgPath, imgFallback, scale );

		img.draw( x + imgXOffset + offsetX + borderSize, y + imgYOffset + offsetY + borderSize );
	}
	
	/**
	 * Draw a Square
	 * @param g Graphics to draw with
	 * @param s Square to draw
	 * @param posX Horizontal position in pixels
	 * @param posY Vertical position in pixels
	 * @param size Size of Square
	 * @throws StandardError 
	 * @throws SimulatorError 
	 */
	protected void renderSquare( Graphics g, Square s, int posX, int posY, int size ) throws StandardError, SimulatorError{
		String imgPath = game.getGameFolder() + s.getImage();
		String imgFallback = "img/defaultSquare.png";
		Square hover = hoversOn();
		
		//Draw background for square
		Image img = ResourceHelper.getImage( imgPath, imgFallback );
		int imgMax = Math.max(img.getWidth(), img.getHeight());
		img = ResourceHelper.getImageScaled( imgPath, imgFallback, (float)size /(float) imgMax );
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
	public void accept( Widget obj, Event event ) throws StandardError, SimulatorError{
		if( event == Event.ACCEPT ){
			if( obj == gameEnded )
				game.restartGame();
			removeObject( obj );
		}
		else if( event == Event.MOVE_GENERATED ){
			executeActions( actionSelector.getAction() );
		}
	}

	private void executeActions(Action action) throws StandardError, SimulatorError {
		List<Action> actions = new ArrayList<Action>();
		actions.add( action );
		executeActions( actions );
	}
}
