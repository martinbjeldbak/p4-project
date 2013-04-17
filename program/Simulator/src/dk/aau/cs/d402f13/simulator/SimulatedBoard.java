package dk.aau.cs.d402f13.simulator;

import java.util.ArrayList;
import java.util.List;

public class SimulatedBoard {
	protected SimulatedGame game;
	protected List<SimulatedPieces> pieces = new ArrayList<SimulatedPieces>();

	public SimulatedBoard(SimulatedGame game) {
		this.game = game;
	}

}
