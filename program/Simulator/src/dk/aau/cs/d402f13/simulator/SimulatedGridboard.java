package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.types.Gridboard;
import dk.aau.cs.d402f13.utilities.types.Piece;

public class SimulatedGridboard extends SimulatedBoard {
	private Gridboard board;
	
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
	

	private void renderBoard( Graphics g, int x, int y, int size, int width, int height){

		for( int iy=0; iy<height; iy++ ){
			for( int ix=0; ix<width; ix++ ){
				if (((iy + ix) % 2) == 0) {
					g.setColor(Color.white);
				}
				else {
					g.setColor(Color.black);
				}	
				g.fillRect(x + ix * size, y + iy * size, size, size);
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
		
		int size = Math.min( Math.min(size_x, size_y), 64 );
		
		int borderWidth = size / 8;
		int borderHeight = size / 8;
		int offsetX = (int) ((width - ((size + 0.25)*numSquaresX )) / 2);
		int offsetY = (int) ((height - ((size + 0.25)*numSquaresY )) / 2);
		
		
		g.setColor(Color.gray);
		g.fillRect(offsetX - borderWidth, offsetY - borderHeight, size * numSquaresX + borderWidth * 2, size * numSquaresY + borderHeight * 2);
		renderBoard(g, offsetX, offsetY, size, numSquaresX, numSquaresY);
		
		for( SimulatedPieces piece : pieces )
			renderPiece( g, piece, pieceXCoordiate( piece ), pieceYCoordiate( piece ), size, offsetX, offsetY );
		
	}
}