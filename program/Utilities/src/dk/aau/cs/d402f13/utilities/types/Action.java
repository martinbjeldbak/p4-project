package dk.aau.cs.d402f13.utilities.types;

import java.util.List;

public abstract class Action {
	private Action nextAction = null;
	
	public void addActions( List<Action> actions ){
		if( actions.size() == 0 )
			return;
		
		if( nextAction == null )
			nextAction = actions.remove( 0 );
		
		//TODO: check for circular references
		nextAction.addActions( actions );
	}
	
	abstract void applyAction( Game g );
	
	public Action next(){
		return nextAction;
	}
}
