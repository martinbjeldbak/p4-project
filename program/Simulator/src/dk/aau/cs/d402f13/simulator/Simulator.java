package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Simulator extends BasicGame {
		
	SimulatedGame game = null;
	
	public void switchGame(SimulatedGame game) throws SlickException {
		
		this.game = game;
		
		Image img = new Image("img/chesspiece.png");
		
		game.addPiece(new SimulatedPieces(0, 1, img));
		game.addPiece(new SimulatedPieces(1, 1, img));
		game.addPiece(new SimulatedPieces(2, 1, img));
		game.addPiece(new SimulatedPieces(3, 1, img));
		game.addPiece(new SimulatedPieces(4, 1, img));
		game.addPiece(new SimulatedPieces(5, 1, img));
		game.addPiece(new SimulatedPieces(6, 1, img));
		game.addPiece(new SimulatedPieces(7, 1, img));
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException  {
		
	}
	
	public void renderBoard( Graphics g, int x, int y, int size, int width, int height){

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
	
	public void renderPiece( Graphics g, SimulatedPieces p, int x, int y, int size, int offsetX, int offsetY){
		
		int borderSize = (int) (size * 0.05);
		float scale = (size - borderSize * 2) / (float)p.imageSize();

		int imgYOffset = (int) ((p.imageSize() - p.PieceImg.getHeight() ) * scale / 2);
		int imgXOffset = (int) ((p.imageSize() - p.PieceImg.getWidth() ) * scale / 2);
		
		p.PieceImg.draw( x * size + imgXOffset + offsetX + borderSize, y * size + imgYOffset + offsetY + borderSize, scale);
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		int numSquaresX = 2;
		int numSquaresY = 3;
		int displayWidth = 800;
		int displayHeight = 600;
		int size = (int) ((displayHeight) / ((Math.max(numSquaresX, numSquaresY)) + 2.25));
			size = Math.min(size, 64);
		int borderWidth = size / 8;
		int borderHeight = size / 8;
		int offsetX = (int) ((displayHeight - ((size + 0.25)*numSquaresX )) / 2);
		int offsetY = (int) ((displayHeight - ((size + 0.25)*numSquaresY )) / 2);
		
		
		g.setColor(Color.white);
		g.fillRect(0, 0, displayWidth, displayHeight);
		
		
		
		g.setColor(Color.gray);
		g.fillRect(offsetX - borderWidth, offsetY - borderHeight, size * numSquaresX + borderWidth * 2, size * numSquaresY + borderHeight * 2);
		renderBoard(g, offsetX, offsetY, size, numSquaresX, numSquaresY);
		
		if( game != null && game.getPieces() != null ){
			for( SimulatedPieces piece : game.getPieces() )
				renderPiece( g, piece, piece.getCoordinatX(), piece.getCoordinatY(), size, offsetX, offsetY );
		}
		
		
		//board.gridImg.draw(0, 0);
		g.setColor(Color.green);
		g.fillRect(displayHeight, 0, (displayWidth - displayHeight), displayHeight);	
		
	}

	@Override
	public void update(GameContainer gc, int d) throws SlickException {
	}
	
	public Simulator(String gamePath) {
		super("Junta Simulator");
		load_game( gamePath );
	}
	
	public boolean load_game( String path ){
		game = new SimulatedGame();
		return false;
	}

	public static void main(String[] args) throws SlickException { 
		
		Simulator juntaSimulator = new Simulator("chess.junta");
		AppGameContainer app = new AppGameContainer(juntaSimulator);
		app.setDisplayMode(800, 600, false);
		app.start();
		juntaSimulator.switchGame(new SimulatedGame());
	}
}