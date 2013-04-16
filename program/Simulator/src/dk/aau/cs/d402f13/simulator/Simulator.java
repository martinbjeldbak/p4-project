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
		
	Board board = null;
	Game game = null;
	Pieces pieces = new Pieces(10, 5, 0.9F);
	
	@Override
	public void init(GameContainer gc) throws SlickException  {
		board.gridImg = new Image("img/chessboard.png");
		pieces.PieceImg = new Image("img/chesspiece.png");
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 600);
		board.gridImg.draw(0, 0);
		g.setColor(Color.white);
		g.fillRect(600, 0, 800, 600);	
		pieces.PieceImg.draw(pieces.getCoordinatX(), pieces.getCoordinatY(), pieces.getScale());
	}

	@Override
	public void update(GameContainer gc, int d) throws SlickException {
		Input input = gc.getInput();
			
			if(input)
	}
	
	public Simulator(Game game, Board board) {
		super(game.getTitle());
		this.game = game;
		this.board = board;
	}

	public static void main(String[] args) throws SlickException { 
		AppGameContainer app = new AppGameContainer(new Simulator(new Game("Chess"), new Board(800, 600)));
		app.setDisplayMode(800, 600, false);
		app.start();
	}
}