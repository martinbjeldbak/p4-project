package dk.aau.cs.d402f13.simulator;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import dk.aau.cs.d402f13.utilities.types.Game;

public class Simulator extends BasicGame {
		
	SimulatedGame game = null;
	
	public void switchGame(SimulatedGame game) throws SlickException {
		
		this.game = game;
		
	//	Image img = new Image("img/chesspiece.png");
		
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException  {
		
	}
	

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		int displayWidth = gc.getWidth();
		int displayHeight = gc.getHeight();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, displayWidth, displayHeight);
		
		if( game != null && game.getBoard() != null ){
			game.getBoard().drawBoard( g, displayHeight, displayHeight );
		}
		

		g.setColor(Color.green);
		g.fillRect(displayHeight, 0, (displayWidth - displayHeight), displayHeight);	
	}

	@Override
	public void update(GameContainer gc, int d) throws SlickException {
        Input input = gc.getInput();

        int x, y;
        if ( input.isMousePressed(input.MOUSE_LEFT_BUTTON) ) {
            x = input.getMouseX();
            y = input.getMouseY();
            System.out.println("Mouse click detected: " + x + "x" + y);
        }
	}
	
	public Simulator(String gamePath) {
		super("Junta Simulator");
		game = new SimulatedGame( new ChessGame() );
	}
	
	@Override
	public String getTitle(){
		return "Junta Simulator - " + game.getTitle();
	}

	public static void main(String[] args) throws SlickException { 
		
		Simulator juntaSimulator = new Simulator("chess.junta");
		AppGameContainer app = new AppGameContainer(juntaSimulator);
		app.setDisplayMode(800, 600, false);
		app.start();
	}
}