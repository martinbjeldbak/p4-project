package dk.aau.cs.d402f13.simulator;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.ResourceLoader;

import dk.aau.cs.d402f13.utilities.types.Piece;
import dk.aau.cs.d402f13.utilities.types.Square;

public class Simulator extends BasicGame {
		
	private SimulatedGame game = null;
	TrueTypeFont gtwFont = null;
	
	public Simulator(String gamePath) throws CloneNotSupportedException {
		super("Junta Simulator");
		game = new SimulatedGame( new ChessGame() );
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException  {
		//Load GNU typewriter font
		Font load;
		try {
			load = Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("img/gtw.ttf"));
			
		} catch (FontFormatException | IOException e) {
			load = new Font( "Times New Roman", Font.PLAIN, 20 ); 
		}
		gtwFont = new TrueTypeFont( load.deriveFont( 36f ), false );
	}
	
	private void renderSidebar( Graphics g, int x, int y, int width, int height ){
		g.setColor(Color.black);
		g.setFont(gtwFont);
		
		//Do stuff here, for now, just display the title...
		g.drawString( game.getTitle(), x, y );
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.setAntiAlias(true);
		int displayWidth = gc.getWidth();
		int displayHeight = gc.getHeight();
		
		//Fill background
		g.setColor(Color.white);
		Shape shape = new Rectangle( 0,0, displayWidth, displayHeight );
		g.texture( shape, game.getImage( "img/wood.png" ) );
		
		
		//Draw side bar
		Image side = game.getImage("img/book.png");
		int side_x = displayWidth - side.getWidth();
		int side_y = (displayHeight - side.getHeight()) / 2;
		g.drawImage( side, side_x, side_y );
		
		//Find the area to draw the side bar in, based on book.png
		renderSidebar( g, side_x + 50, side_y + 50 //50px from the left and top
				,	side.getWidth() - 50 - 25 //only 25px from the right
				,	side.getHeight() - 50*2
			);
		
		
		//Draw board
		if( game != null && game.getBoard() != null ){
			game.getBoard().drawBoard( g, displayWidth - side.getWidth(), displayHeight );
		}
	}

	@Override
	public void update(GameContainer gc, int d) throws SlickException {
        Input input = gc.getInput();

        int x, y;
        if ( input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ) {
            x = input.getMouseX();
            y = input.getMouseY();
            
            Square s = game.getBoard().findSquare(x, y);
            if( s != null ){
            	Piece p = game.getGame().getBoard().findPieceOnSquare(s);
            	if( p != null ){
            		game.getBoard().setSelected(s);
            		System.out.println("Square img: " + p.getImgPath());
            	}
            	else
            		game.getBoard().setSelected(null);
            }
        	else
        		game.getBoard().setSelected(null);
            System.out.println("Mouse click detected: " + x + "x" + y);
        }
	}
	
	@Override
	public String getTitle(){
		return "Junta Simulator - " + game.getTitle();
	}

	public static void main(String[] args) throws SlickException, CloneNotSupportedException { 
		
		Simulator juntaSimulator = new Simulator("chess.junta");
		AppGameContainer app = new AppGameContainer(juntaSimulator);
		app.setDisplayMode(1024, 600, false);
		app.start();
	}
}