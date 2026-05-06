// src/main/java/application/FlowerApplication.java
package application;

import gui.FlowerPanelGrid;

import javax.swing.*;
import java.awt.*;

public class FlowerApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quiz 03 • Swing warm-up: Flower Grid");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JLabel status = new JLabel("Selected: 0 • Mode: Flower");
            status.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

            FlowerPanelGrid panel = new FlowerPanelGrid();
            panel.setStatusLabel(status);

            frame.add(panel, BorderLayout.CENTER);
            frame.add(status, BorderLayout.SOUTH);

            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);

            panel.requestFocusInWindow();
        });
    }
}