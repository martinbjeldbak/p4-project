package dk.aau.cs.d402f13.simulator;

import java.awt.EventQueue;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

	  System.setProperty("apple.laf.useScreenMenuBar", "true");
	  
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				  System.setProperty("apple.laf.useScreenMenuBar", "true");
					SimulatorGUI frame = new SimulatorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
