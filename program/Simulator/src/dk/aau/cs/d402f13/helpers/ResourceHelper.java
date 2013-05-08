package dk.aau.cs.d402f13.helpers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Hashtable;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class ResourceHelper {
	private static Hashtable<String,Image> imgCache = new Hashtable<String,Image>();
	private static Hashtable<String,TrueTypeFont> fontCache = new Hashtable<String,TrueTypeFont>();

	/**
	 * Loads an Image from path, however caches the Image so repeated calls only
	 * loads the image once.
	 * @param path The file address of the Image
	 * @return The loaded Image
	 * @throws SlickException
	 */
	public static Image getImage( String path ){
		if( imgCache.containsKey(path) ){
			return imgCache.get( path );
		}
		else{
			Image img;
			try {
				img = new Image( path );
				imgCache.put(path, img);
			} catch (SlickException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				img = null;
			}
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
	public static Image getImageScaled( String path, float scale ){
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

	public static TrueTypeFont getFont( String name, int size ){
		String cachedName = name + ":" + size;
		if( fontCache.containsKey( cachedName ) ){
			return fontCache.get( cachedName );
		}
		else{
			Font load;
			try {
				load = Font.createFont(
						Font.TRUETYPE_FONT
					,	ResourceLoader.getResourceAsStream( "img/" + name + ".ttf" )
					);
				
			} catch (FontFormatException | IOException e) {
				load = new Font( "Times New Roman", Font.PLAIN, 20 ); 
			}
			TrueTypeFont font = new TrueTypeFont( load.deriveFont( (float)size ), true );;
			fontCache.put( cachedName, font );
			return font;
		}
	}
}
