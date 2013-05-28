package dk.aau.cs.d402f13.widgets;

public class ScaleContainer extends Widget {
	private Boolean dir; //true == vertical, false == horizontal
	
	public ScaleContainer( Boolean dir ) {
		this.dir = dir;
	}

	//Padding
	private int padTop = 0, padBottom = 0;
	private int padLeft = 0, padRight = 0;
	
	public void setPadding( int top, int bottom, int left, int right ){
		padTop = top;
		padBottom = bottom;
		padLeft = left;
		padRight = right;
	}

	
	//Methods to abstract away from direction
	private int getMin( Widget o, Boolean dir ){
		return dir ? o.getMinHeight() : o.getMinWidth();
	}
	private int getMax( Widget o, Boolean dir ){
		return dir ? o.getMaxHeight() : o.getMaxWidth();
	}
	private Boolean isVariable( Widget o, Boolean dir ){
		return getMin( o, dir ) != getMax( o, dir );
	}
	private int available( Boolean dir ){
		int available = dir ? getHeight() : getWidth();
		return available - ( dir ? padTop + padBottom : padLeft + padRight );
	}
	
	
	/**
	 * Moves and resizes the objects. Starts at the edge specified
	 * by dir, places each object side by side as specified by the
	 * width/height of the objects. If it is variable, all are given
	 * an fair amount of space.
	 */
	@Override
	public void adjustSizes(){
		//In the following code, 'I' is dir==true, 'J' is dir==false
		
		//Calculate the minimum required space
		int minimumI = 0;
		for( Widget o : widgets )
			minimumI += getMin( o, dir );
		
		//Find amount of variable height objects
		int varAmount = 0;
		for( Widget o : widgets )
			if( isVariable( o, dir ) )
				varAmount++;
		
		//Get available space to work with
		int availableI = available( dir ) - minimumI;
		int availableJ = available( !dir );
		if( availableI < 0 ){
			System.out.println( "Warning, not enought space to fit objects!" );
			availableI = 0;
		}
		
		//Adjust the size of the objects
		int top = dir ? padTop : padLeft;
		for( Widget o : widgets ){
			//Center in the J direction
			int sizeJ = Math.min( getMax( o, !dir ), availableJ );
			int offset = ( availableJ - sizeJ ) / 2;
			
			//Find how large the object should be in the I direction
			int sizeI = getMin( o, dir );
			if( isVariable( o, dir ) ){
				//Split the available space evenly
				int extraSpace = availableI / varAmount;
				
				//If that space would be more than Max, reduce it
				if( sizeI + extraSpace > getMax( o, dir ) )
					extraSpace = getMax( o, dir ) - sizeI;
				sizeI += extraSpace;
				
				//Update the available space and distribution
				availableI -= extraSpace;
				varAmount--;
			}

			//Adjust the position and sizing
			if( dir ){
				o.setPosition( padLeft + offset, top );
				o.setSize( sizeJ, sizeI );
			}
			else{
				o.setPosition( top, padTop + offset );
				o.setSize( sizeI, sizeJ );
			}
			
			top += sizeI;
		}
		
		super.adjustSizes();
	}
}
