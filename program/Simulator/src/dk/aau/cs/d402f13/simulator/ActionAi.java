package dk.aau.cs.d402f13.simulator;

import java.util.List;

import widgets.SceneObject;
import widgets.SceneObject.Event;

import dk.aau.cs.d402f13.utilities.types.Action;

public class ActionAi extends ActionSelector {
	private Action calculatedAction = null;
	
	ActionAi( SimulatedGame game ){
		super( game );
	}

	@Override
	public void retriveAction( SceneObject callback ){
		List<Action> actions = game.getGame().actions();
		
		int index = (int)( Math.random() * actions.size() );
		calculatedAction = actions.get( index );
		
		callback.acceptEvent( null, Event.MOVE_GENERATED );
	}

	@Override
	public Action getAction() {
		return calculatedAction;
	}

}
