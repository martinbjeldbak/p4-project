package dk.aau.cs.d402f13.simulator;

import java.util.List;


import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.widgets.Widget;
import dk.aau.cs.d402f13.widgets.Widget.Event;

public class ActionAi extends ActionSelector {
	private Action calculatedAction = null;
	
	ActionAi( SimulatedGame game ){
		super( game );
	}

	@Override
	public void retriveAction( Widget callback ){
		List<Action> actions = game.getGame().getActions();
		
		int index = (int)( Math.random() * actions.size() );
		calculatedAction = actions.get( index );
		
		callback.acceptEvent( null, Event.MOVE_GENERATED );
	}

	@Override
	public Action getAction() {
		return calculatedAction;
	}

}
