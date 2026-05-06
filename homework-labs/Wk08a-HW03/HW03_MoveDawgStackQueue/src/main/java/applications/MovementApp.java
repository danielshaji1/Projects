package applications;

import settings.Settings;
import view.DrawPanel;
import view.MovementControlPanel;

import javax.swing.*;
import java.awt.*;

public class MovementApp extends JFrame {

    private DrawPanel drawPanel;
    private MovementControlPanel controlPanel;

    public MovementApp() {
        super("Move with Dawg");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the drawing panel and set its preferred size to 600x600.
        drawPanel = new DrawPanel();
        drawPanel.setPreferredSize(new Dimension(Settings.PANEL_WIDTH, Settings.PANEL_HEIGHT));
        add(drawPanel, BorderLayout.CENTER);

        // Create the control panel.
        controlPanel = new MovementControlPanel(drawPanel);
        add(controlPanel, BorderLayout.SOUTH);

        // Pack the frame so that the drawing area is 600x600 and the control panel gets added below.
        pack();

        // Optionally, disable resizing to keep the layout consistent.
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovementApp::new);
    }
}