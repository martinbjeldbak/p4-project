package dk.aau.cs.d402f13.widgets;

public class CenterContainer extends Widget {
	public CenterContainer( Widget w ){
		addObject( w );
		scaleHeight( w.getHeight() );
		scaleWidth( w.getWidth() );
	}

	@Override
	public void adjustSizes(){
		if( widgets.size() == 1 ){
			Widget w = widgets.get( 0 );
			int width = getWidth() - w.getWidth();
			int height = getHeight() - w.getHeight();
			
			w.setPosition( width / 2, height / 2 );

			System.out.println( "w: " + w.getWidth() + "x" + w.getHeight() );
			System.out.println( "this: " + getWidth() + "x" + getHeight() );
			System.out.println( "result: " + width / 2 + "x" + height / 2 );
		}
		
		super.adjustSizes();
	}
}
