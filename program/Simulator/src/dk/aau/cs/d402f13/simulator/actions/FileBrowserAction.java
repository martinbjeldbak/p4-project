package dk.aau.cs.d402f13.simulator.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;

public class FileBrowserAction extends AbstractAction {
  JFileChooser fc = new JFileChooser();

  @Override
  public void actionPerformed(ActionEvent e) {
    int option = fc.showOpenDialog(null);
    if (option == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
    }
  }

}
