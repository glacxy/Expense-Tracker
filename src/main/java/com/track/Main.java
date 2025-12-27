package com.track;

import com.track.gui.TrackGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ensures GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            TrackGUI gui = new TrackGUI();
            gui.setVisible(true);
        });
    }
}
