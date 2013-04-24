package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

public class SimulatedGridboard extends SimulatedBoard {
	private Gridboard board;
	private int size = 0;
	private int offsetX = 0;
	private int offsetY = 0; 
	
	public int getWidth() {
		return board.getWidth();
	}
	
	public int getHeight() {
		return board.getHeight();
	}
	
	private int invertY( int y ){
		return getHeight() - y - 1;
	}
	
	public SimulatedGridboard( SimulatedGame game, Gridboard b ) {
		super( game );
		board = b;
	}

	private int pieceXCoordiate( Piece p ){
		return board.squareCoordinateX( p.square() );
	}
	private int pieceYCoordiate( Piece p ){
		return board.squareCoordinateY( p.square() );
	}
	
	private Square hoversOn(){
		if( dragged == null )
			return null;

		int x = pieceXCoordiate( dragged ) * size + offsetX + dragOffsetX + size / 2;
		int y = invertY(pieceYCoordiate( dragged )) * size + offsetY + dragOffsetY + size / 2;
		return findSquare( x, y );
	}

	private void renderBoard( Graphics g, int x, int y, int size, int width, int height) throws SlickException{
		Square hover = hoversOn();
		
		//Draw squares
		for( int iy=0; iy<height; iy++ ){
			for( int ix=0; ix<width; ix++ ){
				Square s = board.getSquareAt( ix, iy );
				int posX = x + ix * size;
				int posY = y + invertY(iy) * size;
				
				//Draw background for square
				Image img = game.getImage( s.getImgPath() );
				int imgMax = Math.max(img.getWidth(), img.getHeight());
				img = game.getImageScaled( s.getImgPath(), (float)size /(float) imgMax );
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
		}
	}
	
	private void renderPiece( Graphics g, Piece p, int x, int y, int size, int offsetX, int offsetY) throws SlickException{
		Image img = game.getImage( p.getImgPath() );
		int imgMax = Math.max( img.getHeight(), img.getWidth() );
		
		int borderSize = (int) (size * 0.05);
		float scale = (size - borderSize * 2) / (float)imgMax;

		int imgYOffset = (int) ((imgMax - img.getHeight() ) * scale / 2);
		int imgXOffset = (int) ((imgMax - img.getWidth() ) * scale / 2);
		
		img = game.getImageScaled( p.getImgPath(), scale );

		img.draw( x * size + imgXOffset + offsetX + borderSize, invertY(y) * size + imgYOffset + offsetY + borderSize );
		
	}
	
	
	public void drawBoard( Graphics g, int width, int height ) throws SlickException{
		int numSquaresX = board.getWidth();
		int numSquaresY = board.getHeight();
		int size_x = (int) ((width) / (numSquaresX + 2.25));
		int size_y = (int) ((height) / (numSquaresY + 2.25));
		
		size = Math.min( Math.min(size_x, size_y), 64 );
		
		int borderWidth = size / 8;
		int borderHeight = size / 8;
		offsetX = (int) ((width - ((size + 0.25)*numSquaresX )) / 2);
		offsetY = (int) ((height - ((size + 0.25)*numSquaresY )) / 2);
		
		
		g.setColor(Color.gray);
		g.fillRect(offsetX - borderWidth, offsetY - borderHeight, size * numSquaresX + borderWidth * 2, size * numSquaresY + borderHeight * 2);
		renderBoard(g, offsetX, offsetY, size, numSquaresX, numSquaresY);
		
		for( Piece piece : board.getPieces() )
			if( piece != dragged )
				renderPiece( g, piece
						,	pieceXCoordiate( piece )
						,	pieceYCoordiate( piece )
						,	size, offsetX, offsetY
					);
		
		if( dragged != null ){
			//renderDragged( g, dragged );
			renderPiece( g, dragged
					,	pieceXCoordiate( dragged )
					,	pieceYCoordiate( dragged )
					,	size, offsetX + dragOffsetX, offsetY + dragOffsetY
				);
		}
	}

	@Override
	public Square findSquare(int x, int y) {
		int posX = (x - offsetX) / size;
		int posY = (y - offsetY) / size;
		return board.getSquareAt(posX, invertY(posY));
	}
	
	@Override
	protected void alignDrag(){
		dragStartX = offsetX + size * ((dragStartX - offsetX) / size) + size / 2;
		dragStartY = offsetY + size * ((dragStartY - offsetY) / size) + size / 2;
	}
}