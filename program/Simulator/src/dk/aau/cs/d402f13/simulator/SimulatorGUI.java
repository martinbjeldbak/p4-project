package dk.aau.cs.d402f13.simulator;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

import dk.aau.cs.d402f13.simulator.actions.*;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class SimulatorGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public SimulatorGUI() {
		setTitle("Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 505, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		JMenuBar menuBar = new JMenuBar();

		contentPane.add(menuBar, BorderLayout.NORTH);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmBrowseGame = new JMenuItem("Import game file...");
		mntmBrowseGame.addActionListener(new FileBrowserAction());

		mnFile.add(mntmBrowseGame);
		
		Board board = new Board();
		contentPane.add(board, BorderLayout.CENTER);
		
		JTextPane textPane = new JTextPane();
		JScrollPane jsp = new JScrollPane(textPane);
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		textPane.setText("test");
		contentPane.add(textPane, BorderLayout.EAST);
		
		board.setDimensions(4, 2);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
