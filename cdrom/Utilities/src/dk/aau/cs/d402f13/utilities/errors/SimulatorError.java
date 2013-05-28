package dk.aau.cs.d402f13.utilities.errors;

@SuppressWarnings("serial")
public class SimulatorError extends Error {

	public SimulatorError(String msg) {
		super(msg);
	}

	@Override
	public int getColumn() { return 0; }

	@Override
	public int getLine() { return 0; }

}
