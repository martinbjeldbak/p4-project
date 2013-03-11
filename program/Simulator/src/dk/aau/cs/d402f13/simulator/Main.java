package dk.aau.cs.d402f13.simulator;

import java.awt.EventQueue;

import dk.aau.cs.d402f13.simulator.SimulatorGUI;

public class Main {

  public Main() {
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
