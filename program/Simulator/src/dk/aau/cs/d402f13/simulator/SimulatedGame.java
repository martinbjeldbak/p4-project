package dk.aau.cs.d402f13.simulator;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Hashtable;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.ResourceLoader;

import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;

public class SimulatedGame extends BasicGame {
	//Drawing stuff
	Hashtable<String,Image> imgCache = new Hashtable<String,Image>();
	TrueTypeFont gtwFont = null;
	
	//Game mechanics stuff
	SimulatedGridboard board = null;
	Game game;
	
	public Image getImage( String path ) throws SlickException{
		if( imgCache.containsKey(path) ){
			return imgCache.get( path );
		}
		else{
			Image img = new Image( path );
			imgCache.put(path, img);
			return img;
		}
	}
	
	public SimulatedGridboard getBoard(){
		return board;
	}
	
	public SimulatedGame( String path ) throws CloneNotSupportedException{
		super("Junta Simulator");
		
		//TODO: load from file system
		game = new ChessGame();
		Object obj = game.board();
		if( obj instanceof Gridboard )
			board = new SimulatedGridboard( this, (Gridboard)obj );
		else
			; //TODO:
	}
	
	public Game getGame(){
		return game;
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
		g.drawString( game.title(), x, y );
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		int displayWidth = gc.getWidth();
		int displayHeight = gc.getHeight();
		
		//Fill background
		g.setColor(Color.white);
		Shape shape = new Rectangle( 0,0, displayWidth, displayHeight );
		g.texture( shape, getImage( "img/wood.png" ) );
		
		
		//Draw side bar
		Image side = getImage("img/book.png");
		int side_x = displayWidth - side.getWidth();
		int side_y = (displayHeight - side.getHeight()) / 2;
		g.drawImage( side, side_x, side_y );
		
		//Find the area to draw the side bar in, based on book.png
		renderSidebar( g, side_x + 50, side_y + 50 //50px from the left and top
				,	side.getWidth() - 50 - 25 //only 25px from the right
				,	side.getHeight() - 50*2
			);
		
		
		//Draw board
		if( game != null && board != null ){
			board.drawBoard( g, displayWidth - side.getWidth(), displayHeight );
		}
	}

	@Override
	public void update(GameContainer gc, int d) throws SlickException {
        
	}
	
	@Override
	public void mousePressed( int button, int x, int y ){
    	//TODO: only call if we are on the board area
		board.mouseClicked(button, x, y);
    }
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy){
		//TODO: only call if we are on the board area
		board.mouseDragged(oldx, oldy, newx, newy);
	}
	
	public void mouseReleased( int button, int x, int y ){
		//TODO: only call if we are on the board area
		board.mouseReleased(button, x, y);
	}
	
	@Override
	public String getTitle(){
		return "Junta Simulator - " + game.title();
	}

	public static void main(String[] args) throws SlickException, CloneNotSupportedException {
		SimulatedGame simulator = new SimulatedGame( "chess.junta" );
		AppGameContainer app = new AppGameContainer( simulator );
		app.setDisplayMode( 1024, 600, false );
		app.start();
	}
}

