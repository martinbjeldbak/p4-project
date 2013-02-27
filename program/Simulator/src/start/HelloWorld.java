
package start;

import javax.swing.*;

public class HelloWorld {
	private static void createAndShowGUI() {
		// Create and set up window
		JFrame frame = new JFrame("HelloWorld");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add label
		JLabel label = new JLabel("Hello World");
		frame.getContentPane().add(label);
		
		// Display the window
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
