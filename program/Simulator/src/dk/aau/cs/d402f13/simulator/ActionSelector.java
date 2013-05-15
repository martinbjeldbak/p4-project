package dk.aau.cs.d402f13.simulator;

import dk.aau.cs.d402f13.utilities.errors.SimulatorError;
import dk.aau.cs.d402f13.utilities.errors.StandardError;
import dk.aau.cs.d402f13.utilities.gameapi.Action;
import dk.aau.cs.d402f13.widgets.Widget;

public abstract class ActionSelector {
	SimulatedGame game = null;
	
	ActionSelector( SimulatedGame game ){
		this.game = game;
	}
	
	public abstract void retriveAction( Widget callback ) throws StandardError, SimulatorError;
	public abstract Action getAction();
}
