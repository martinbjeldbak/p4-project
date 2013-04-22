package dk.aau.cs.d402f13.utilities.types;

import java.util.ArrayList;
import java.util.List;

public class Gridboard extends Board {
	
	int height;
	int width;
	
	public Gridboard( int width, int height ) throws CloneNotSupportedException{
		this.height = height;
		this.width = width;
		
		List<Square> types = squareTypes();
		int size = types.size();
		
		for( int iy=0; iy < height; iy++ ){
			for( int ix=0; ix < width; ix++ ){
				Square copy = types.get( (ix + iy % size ) % size );
				squares.add( copy.clone() );
			}
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int value) {
		height = value;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int value) {
		width = value;
	}
	
	public int squareCoordinateX( Square s ){
		int index = squares.indexOf(s);
		if( index == -1 )
			return -1;
		
		return index % getWidth();
	}
	
	public int squareCoordinateY( Square s ){
		int index = squares.indexOf(s);
		if( index == -1 )
			return -1;
		
		return index / getWidth();
	}
	
	public Square getSquareAt( int x, int y ){
		if( x >= width || y >= height || x < 0 || y < 0 )
			return null;
		return squares.get( x + y* width);
	}
	
	public List<Square> squareTypes(){
		List<Square> list = new ArrayList<Square>();
		list.add( new Square() );
		return list;
	}
}
