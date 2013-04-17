package dk.aau.cs.d402f13.simulator;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import dk.aau.cs.d402f13.simulator.actions.FileBrowserAction;
import javax.swing.JTextPane;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.SlickException;

public class SimulatorGUI extends JFrame {

  private JPanel contentPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          SimulatorGUI frame = new SimulatorGUI();
          frame.setVisible(true);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public SimulatorGUI() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 300);
    
    // MENU BAR
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    
    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);
    
    JMenuItem mntmOpenFile = new JMenuItem("Open file...");
    mntmOpenFile.addActionListener(new FileBrowserAction());
    
    mnFile.add(mntmOpenFile);
    
    // MAIN Content pane
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);
  
    // --Add game container to main content pane
    BoardGame boardGame = new BoardGame();
    CanvasGameContainer gameContainer;
    try {
      gameContainer = new CanvasGameContainer(boardGame);
      contentPane.add(gameContainer, BorderLayout.CENTER);
      
      gameContainer.setSize(200, 200);
      gameContainer.start();
    } catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // --Right JTextPane
    JTextPane textPane = new JTextPane();
    textPane.setContentType("text/html");
    contentPane.add(textPane, BorderLayout.EAST);
  }

}
