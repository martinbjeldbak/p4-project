package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.GridBoard;
import dk.aau.cs.d402f13.utilities.gameapi.Piece;
import dk.aau.cs.d402f13.utilities.gameapi.Square;
import dk.aau.cs.d402f13.utilities.gameapi.Board;

public class GridBoardWidget extends BoardWidget {
	private int size = 0;
	private int offsetX = 0;
	private int offsetY = 0; 
	
	private int invertY( int y ) throws StandardError{
		return board().getHeight() - y - 1;
	}
	
	public GridBoardWidget( SimulatedGame game, GridBoard b ) {
		super( game );
	}
	public GridBoard board() throws StandardError{
		return (GridBoard)game.getGame().getBoard();
		//TODO: check?
	}

	private int pieceXCoordiate( Piece p ) throws StandardError{
		return p.getX();
	}
	private int pieceYCoordiate( Piece p ) throws StandardError{
		return p.getY();
	}
	
	/**
	 * Draws a Piece using GridBoard coordinates
	 * @param g Graphics to draw with
	 * @param p Piece to draw
	 * @param x Horizontal GridBoard position
	 * @param y Vertical GridBoard position
	 * @param size Size of a Square
	 * @param offsetX Horizontal offset of Board
	 * @param offsetY Vertical offset of Board
	 * @throws StandardError 
	 * @throws SlickException
	 */
	protected void renderPieceLocal( Graphics g, Piece p
			,	int x, int y, int size, int offsetX, int offsetY
			) throws StandardError{
		renderPiece( g, p, (x-1) * size, invertY(y-1) * size, size, offsetX, offsetY );
	}
	
	/**
	 * Renders a board with pieces 
	 * @param g Graphics to render with
	 * @param width Available width
	 * @param height Available height
	 * @throws StandardError 
	 * @throws SlickException
	 */
	public void drawBoard( Graphics g, int width, int height ) throws StandardError{
		int numSquaresX = board().getWidth();
		int numSquaresY = board().getHeight();
		int size_x = (int) ((width) / (numSquaresX + 2.25));
		int size_y = (int) ((height) / (numSquaresY + 2.25));
		
		size = Math.min( Math.min(size_x, size_y), 64 );
		
		int borderWidth = size / 8;
		int borderHeight = size / 8;
		offsetX = (int) ((width - ((size + 0.25)*numSquaresX )) / 2);
		offsetY = (int) ((height - ((size + 0.25)*numSquaresY )) / 2);
		
		
		g.setColor(Color.gray);
		g.fillRect(offsetX - borderWidth, offsetY - borderHeight, size * numSquaresX + borderWidth * 2, size * numSquaresY + borderHeight * 2);
		
		//Draw squares
		for( int iy=0; iy<numSquaresY; iy++ )
			for( int ix=0; ix<numSquaresX; ix++ ){
				int posX = offsetX + ix * size;
				int posY = offsetY + invertY(iy) * size;
				renderSquare( g, board().getSquareAt( ix+1, iy+1 ), posX, posY, size );
			}
		
		//Draw pieces
		for( Piece piece : board().getPieces() )
			if( piece != dragged )
				renderPieceLocal( g, piece
						,	pieceXCoordiate( piece )
						,	pieceYCoordiate( piece )
						,	size, offsetX, offsetY
					);
		
		//Draw dragged
		if( dragged != null ){
			renderPieceLocal( g, dragged
					,	pieceXCoordiate( dragged )
					,	pieceYCoordiate( dragged )
					,	size, offsetX + dragOffsetX, offsetY + dragOffsetY
				);
		}
	}

	@Override
	public Square findSquare(int x, int y) throws StandardError {
		int posX = (x - offsetX) / size;
		int posY = (y - offsetY) / size;
		System.out.println( "findSquare: " + posX + "x" + posY );
		return board().getSquareAt( posX + 1, invertY(posY) + 1 );
	}
	
	@Override
	protected void alignDrag(){
		dragStartX = offsetX + size * ((dragStartX - offsetX) / size) + size / 2;
		dragStartY = offsetY + size * ((dragStartY - offsetY) / size) + size / 2;
	}

	@Override
	public void draw(Graphics g) throws StandardError {
		drawBoard( g, getWidth(), getHeight() );
	}

	@Override
	protected Square getSquareFromPiece( Board b, Piece p ) throws StandardError {
		if( !(b instanceof GridBoard ) )
			return null;
		GridBoard gb = (GridBoard)b;
		return gb.getSquareAt( p.getX(), p.getY() );
	}
}