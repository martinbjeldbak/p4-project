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
	
	public SimulatedGridboard( SimulatedGame game, Gridboard b ) {
		super( game );
		board = b;
		for( Piece p : b.getPieces() ){
			pieces.add( new SimulatedPieces( p ) );
		}
	}

	private int pieceXCoordiate( SimulatedPieces p ){
		return board.squareCoordinateX( p.getSquare() );
	}
	private int pieceYCoordiate( SimulatedPieces p ){
		return board.squareCoordinateY( p.getSquare() );
	}
	

	private void renderBoard( Graphics g, int x, int y, int size, int width, int height) throws SlickException{

		for( int iy=0; iy<height; iy++ ){
			for( int ix=0; ix<width; ix++ ){
				Square s = board.getSquareAt( ix, iy );
				Image img = game.getImage( s.getImgPath() );
				
				int imgMax = Math.max(img.getWidth(), img.getHeight());
				img.draw( x + ix * size, y + iy * size, (float)size /(float) imgMax );
				
				if( s == getSelected() ){
					g.setColor( new Color(255,0,0,127) );
					g.fillRect(x + ix * size, y + iy * size, size, size);
				}
				
				if( squareIsHinted( s ) ){
					g.setColor( new Color(0,255,0,127) );
					g.fillRect(x + ix * size, y + iy * size, size, size);
				}
			}
		}
	}
	
	private void renderPiece( Graphics g, SimulatedPieces p, int x, int y, int size, int offsetX, int offsetY) throws SlickException{
		p.setImage( game.getImage( p.getImgPath() ) );
		
		int borderSize = (int) (size * 0.05);
		float scale = (size - borderSize * 2) / (float)p.imageSize();

		int imgYOffset = (int) ((p.imageSize() - p.PieceImg.getHeight() ) * scale / 2);
		int imgXOffset = (int) ((p.imageSize() - p.PieceImg.getWidth() ) * scale / 2);

		p.PieceImg.draw( x * size + imgXOffset + offsetX + borderSize, y * size + imgYOffset + offsetY + borderSize, scale);
		
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
		
		for( SimulatedPieces piece : pieces )
			renderPiece( g, piece, pieceXCoordiate( piece ), pieceYCoordiate( piece ), size, offsetX, offsetY );
		
	}

	@Override
	public Square findSquare(int x, int y) {
		int dx = x - offsetX;
		int posX = dx / size;
		int dy = y - offsetY;
		int posY = dy / size;
		
		System.out.println("Sqaure at " +posX +"x" + posY);
		return board.getSquareAt(posX, posY);
	}
}