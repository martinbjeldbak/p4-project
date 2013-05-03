package dk.aau.cs.d402f13.simulator;

import widgets.SceneObject;
import dk.aau.cs.d402f13.utilities.types.Action;

public abstract class ActionSelector {
	SimulatedGame game = null;
	
	ActionSelector( SimulatedGame game ){
		this.game = game;
	}
	
	public abstract void retriveAction( SceneObject callback );
	public abstract Action getAction();
}
