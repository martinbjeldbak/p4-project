package dk.aau.cs.d402f13.simulator;

import java.awt.EventQueue;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimulatorGUI frame = new SimulatorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
