package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.d402f13.utilities.types.Square;

public abstract class SimulatedBoard {
	protected SimulatedGame game;
	protected List<SimulatedPieces> pieces = new ArrayList<SimulatedPieces>();

	public SimulatedBoard(SimulatedGame game) {
		this.game = game;
	}
	
	public abstract Square findSquare(int x, int y);

}
