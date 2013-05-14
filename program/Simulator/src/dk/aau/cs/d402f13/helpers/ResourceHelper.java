package dk.aau.cs.d402f13.helpers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.Hashtable;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import dk.aau.cs.d402f13.utilities.errors.SimulatorError;

public class ResourceHelper {
	private static Hashtable<String,Image> imgCache = new Hashtable<String,Image>();
	private static Hashtable<String,TrueTypeFont> fontCache = new Hashtable<String,TrueTypeFont>();

	/**
	 * Loads an Image from path, however caches the Image so repeated calls only
	 * loads the image once.
	 * @param path The file address of the Image
	 * @return The loaded Image
	 * @throws SimulatorError 
	 */
	public static Image getImage( String path, String fallback ) throws SimulatorError{
		if( imgCache.containsKey(path) ){
			return imgCache.get( path );
		}
		else{
			Image img;
			try {
				try {
					img = new Image( path );
				} catch (java.lang.RuntimeException e){
					//Image not found, use fall back
					try {
						img = new Image( fallback );
					} catch (java.lang.RuntimeException e2) {
						throw new SimulatorError( "Could not load fallback image: " + fallback );
					}
					path = fallback;
				}
			} catch (SlickException e) {
				throw new SimulatorError( "Could not load image: " + e.getMessage() );
			}
			imgCache.put(path, img);
			return img;
		}
	}
	
	public static Image getImage( String path ) throws SimulatorError{
		return getImage( path, path );
	}
	
	/**
	 * Caches a scaled version of the Image returned by getImage()
	 * @param path The file address of the Image
	 * @param scale The wanted scale of the Image
	 * @return The scaled Image
	 * @throws SimulatorError 
	 */
	public static Image getImageScaled( String path, String fallback, float scale ) throws  SimulatorError{
		Image unscaled = getImage( path, fallback );

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
