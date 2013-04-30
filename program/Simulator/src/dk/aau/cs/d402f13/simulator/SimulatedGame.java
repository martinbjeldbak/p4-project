package dk.aau.cs.d402f13.simulator;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

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

import dk.aau.cs.d402f13.utilities.types.Action;
import dk.aau.cs.d402f13.utilities.types.Game;
import dk.aau.cs.d402f13.utilities.types.Gridboard;

public class SimulatedGame extends BasicGame {
	//Drawing stuff
	Hashtable<String,Image> imgCache = new Hashtable<String,Image>();
	TrueTypeFont gtwFontSmall = null;
	TrueTypeFont gtwFontBig = null;
	
	//Game mechanics stuff
	SimulatedGridboard board = null;
	Game game;
	
	/**
	 * Loads an Image from path, however caches the Image so repeated calls only
	 * loads the image once.
	 * @param path The file address of the Image
	 * @return The loaded Image
	 * @throws SlickException
	 */
	public Image getImage( String path ) throws SlickException{
		if( imgCache.containsKey(path) ){
			return imgCache.get( path );
		}
		else{
			Image img = new Image( path );
			imgCache.put(path, img);
			return img;
		}
		//TODO: check if Image could not be loaded and recast exception
		//TODO: default Images?
	}
	
	/**
	 * Caches a scaled version of the Image returned by getImage()
	 * @param path The file address of the Image
	 * @param scale The wanted scale of the Image
	 * @return The scaled Image
	 * @throws SlickException
	 */
	public Image getImageScaled( String path, float scale ) throws SlickException{
		Image unscaled = getImage( path );

		int width = (int)(unscaled.getWidth() * scale);
		int height = (int)(unscaled.getHeight() * scale);
		
		String scaledName = path + ":" + width + "x" + height;
		if( imgCache.containsKey( scaledName ) )
			return imgCache.get( scaledName );
		else{
			Image scaled = unscaled.getScaledCopy( width, height );
			System.out.println( "Scaled image: " + scaledName );
			imgCache.put( scaledName, scaled );
			return scaled;
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
		gtwFontSmall = new TrueTypeFont( load.deriveFont( 18f ), false );
		gtwFontBig = new TrueTypeFont( load.deriveFont( 36f ), false );
	}
	
	private void renderMessage( Graphics g, int x, int y, int width, int height, String title, String message ) throws SlickException{
		Image paper = getImage( "img/message.png" );

		int posX = ( (width - x) - paper.getWidth() ) / 2 + x;
		int posY = ( (height - y) - paper.getHeight() ) / 2 + y;
		
		g.drawImage( paper, posX, posY );
		
		posX += 18;
		posY += 18;
		width = 292;
		height = 266;

		//Draw title
		int nextLine = TextHelper.drawCenteredText( g, gtwFontBig, title, posX, posY, width );
		

		g.setFont( gtwFontSmall );
		List<String> lines = TextHelper.wrapText( gtwFontSmall, message, width );
		for( String line : lines ){
			g.drawString( line, posX, nextLine );
			nextLine += gtwFontSmall.getLineHeight();
		}
		
	}
	
	private void renderSidebar( Graphics g, int x, int y, int width, int height ){
		g.setColor(Color.black);
		g.setFont(gtwFontBig);
		
		//Do stuff here, for now, just display the title...
		g.drawString( game.title(), x, y );

		g.setFont(gtwFontSmall);
		int position = game.players().indexOf( game.currentPlayer() );
		g.drawString( "Player " + Integer.toString( position + 1 ), x, y + 36 );
		
		
		int historyStart = y + gtwFontBig.getLineHeight();
		for( Action a : game.history() ){
			historyStart += gtwFontSmall.getLineHeight();
			g.drawString( ActionHelper.humanReadable( game, a ), x, historyStart );
		}
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
		
	//	String text = "A long message to test line breaks and stuff and break everything Elias made... ajwdf sfd aa jfklsda kfladsj";
	//	renderMessage( g, 0,0, displayWidth - side.getWidth(), displayHeight, "Title", text );
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

