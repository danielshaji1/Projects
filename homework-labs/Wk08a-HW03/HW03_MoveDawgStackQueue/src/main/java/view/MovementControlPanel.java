package view;

import model.DawgSquare;
import settings.Settings;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class MovementControlPanel extends JPanel {
    // Movement buttons
    private JButton upButton, downButton, leftButton, rightButton, undoButton, redoButton;

    // Animation mode radio buttons (Bounce vs. Box)
    private JRadioButton bounceRadioButton, boxRadioButton, noneRadioButton;

    // Slider for square size and labels for position/undo/redo counts.
    private JSlider squareSizeSlider;
    private JLabel positionLabel, undoLabel, redoLabel;

    // ADT choice combo boxes:
    // For Undo/Redo: 4 choices (Stack/Queue, Array/LinkedList)
    private JComboBox<String> undoChoiceCombo;
    private JComboBox<String> redoChoiceCombo;
    // For Trail: 4 choices now (Queue vs. Stack and Array vs. LinkedList)
    private JComboBox<String> trailChoiceCombo;

    private DrawPanel drawPanel;  // Reference to the drawing panel

    public MovementControlPanel(DrawPanel drawPanel) {
        this.drawPanel = drawPanel;
        setLayout(new BorderLayout());

        // --- Movement Button Panel ---
        JPanel movementPanel = new JPanel(new FlowLayout());
        upButton = new JButton("Up");
        downButton = new JButton("Down");
        leftButton = new JButton("Left");
        rightButton = new JButton("Right");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");

        movementPanel.add(upButton);
        movementPanel.add(downButton);
        movementPanel.add(leftButton);
        movementPanel.add(rightButton);
        movementPanel.add(undoButton);
        movementPanel.add(redoButton);

        // --- Animation Mode Panel ---
        JPanel animationPanel = new JPanel(new FlowLayout());
        bounceRadioButton   = new JRadioButton("Animate Bounce", true);
        boxRadioButton      = new JRadioButton("Animate Box", false);
        noneRadioButton     = new JRadioButton("Animate Static", false);

        ButtonGroup animationGroup = new ButtonGroup();

        animationGroup.add(bounceRadioButton);
        animationGroup.add(boxRadioButton);
        animationGroup.add(noneRadioButton);

        animationPanel.add(new JLabel("Animation Mode:"));
        animationPanel.add(bounceRadioButton);
        animationPanel.add(boxRadioButton);
        animationPanel.add(noneRadioButton);

        // Listeners for animation mode changes.
        bounceRadioButton.addActionListener(e -> {
            // Assume drawPanel supports setting the animation mode.
            drawPanel.setAnimationMode("bounce");
        });
        boxRadioButton.addActionListener(e -> {
            drawPanel.setAnimationMode("box");
        });
        noneRadioButton.addActionListener(e -> {
            drawPanel.setAnimationMode("none");
        });

        // --- ADT Choice Panel ---
        // Using a GridLayout with reduced gaps for a more compact look.
        JPanel adtChoicePanel = new JPanel(new GridLayout(3, 2, 2, 2));
        // Undo choices:
        adtChoicePanel.add(new JLabel("   Undo Implementation:"));
        String[] undoChoices = {"Stack (Array)", "Stack (LinkedList)", "Queue (Array)", "Queue (LinkedList)"};
        undoChoiceCombo = new JComboBox<>(undoChoices);
        adtChoicePanel.add(undoChoiceCombo);
        // Redo choices:
        adtChoicePanel.add(new JLabel("   Redo Implementation:"));
        String[] redoChoices = {"Stack (Array)", "Stack (LinkedList)", "Queue (Array)", "Queue (LinkedList)"};
        redoChoiceCombo = new JComboBox<>(redoChoices);
        adtChoicePanel.add(redoChoiceCombo);
        // Trail choices:
        adtChoicePanel.add(new JLabel("   Trail Implementation:"));
        String[] trailChoices = {"Queue (LinkedList)", "Queue (Array)", "Stack (LinkedList)", "Stack (Array)"};
        trailChoiceCombo = new JComboBox<>(trailChoices);
        adtChoicePanel.add(trailChoiceCombo);

        // --- Slider for square size ---
        squareSizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, Settings.SQUARE_SIZE);
        squareSizeSlider.setMajorTickSpacing(10);
        squareSizeSlider.setPaintTicks(true);
        squareSizeSlider.setPaintLabels(true);

        // --- Assemble Top Panel ---
        // We'll stack movementPanel, animationPanel, adtChoicePanel, and the slider vertically.
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(movementPanel);
        topPanel.add(animationPanel);
        topPanel.add(adtChoicePanel);
        //topPanel.add(squareSizeSlider);

        // --- Info Panel (Position & Counts) ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JPanel posPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        positionLabel = new JLabel("Position: (0000, 0000)");
        posPanel.add(positionLabel);
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        undoLabel = new JLabel("Undo Points: 0");
        redoLabel = new JLabel("Redo Points: 0");
        countPanel.add(undoLabel);
        countPanel.add(redoLabel);
        infoPanel.add(posPanel);
        infoPanel.add(countPanel);

        add(topPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        // --- Button Actions ---
        upButton.addActionListener(e -> moveSquare(0, -10));
        downButton.addActionListener(e -> moveSquare(0, 10));
        leftButton.addActionListener(e -> moveSquare(-10, 0));
        rightButton.addActionListener(e -> moveSquare(10, 0));
        undoButton.addActionListener(e -> {
            drawPanel.getSquare().undo();
            updateLabels();
            drawPanel.repaint();
        });
        redoButton.addActionListener(e -> {
            drawPanel.getSquare().redo();
            updateLabels();
            drawPanel.repaint();
        });

        // --- Combo Box Actions ---
        // Undo Implementation selection:
        undoChoiceCombo.addActionListener(e -> {
            String choice = (String) undoChoiceCombo.getSelectedItem();
            if (choice != null) {
                if (choice.startsWith("Stack")) {
                    drawPanel.getSquare().setUndoMode(true);
                    if (choice.contains("Array"))
                        drawPanel.getSquare().setUndoStackDelegate(new adts.ArrayStack<>());
                    else
                        drawPanel.getSquare().setUndoStackDelegate(new adts.LinkedListStack<>());
                } else { // Queue chosen.
                    drawPanel.getSquare().setUndoMode(false);
                    if (choice.contains("Array"))
                        drawPanel.getSquare().setUndoQueueDelegate(new adts.ArrayQueue<>());
                    else
                        drawPanel.getSquare().setUndoQueueDelegate(new adts.LinkedListQueue<>());
                }
            }
        });
        // Redo Implementation selection:
        redoChoiceCombo.addActionListener(e -> {
            String choice = (String) redoChoiceCombo.getSelectedItem();
            if (choice != null) {
                if (choice.startsWith("Stack")) {
                    drawPanel.getSquare().setRedoMode(true);
                    if (choice.contains("Array"))
                        drawPanel.getSquare().setRedoStackDelegate(new adts.ArrayStack<>());
                    else
                        drawPanel.getSquare().setRedoStackDelegate(new adts.LinkedListStack<>());
                } else {
                    drawPanel.getSquare().setRedoMode(false);
                    if (choice.contains("Array"))
                        drawPanel.getSquare().setRedoQueueDelegate(new adts.ArrayQueue<>());
                    else
                        drawPanel.getSquare().setRedoQueueDelegate(new adts.LinkedListQueue<>());
                }
            }
        });
        // Trail Implementation selection:
        trailChoiceCombo.addActionListener(e -> {
            String choice = (String) trailChoiceCombo.getSelectedItem();
            if (choice != null) {
                if (choice.startsWith("Queue")) {
                    drawPanel.getSquare().setTrailMode(true);
                    if (choice.contains("Array"))
                        drawPanel.getSquare().setTrailQueueDelegate(new adts.ArrayQueue<>());
                    else
                        drawPanel.getSquare().setTrailQueueDelegate(new adts.LinkedListQueue<>());
                } else { // "Stack" selected.
                    drawPanel.getSquare().setTrailMode(false);
                    if (choice.contains("Array"))
                        drawPanel.getSquare().setTrailStackDelegate(new adts.ArrayStack<>());
                    else
                        drawPanel.getSquare().setTrailStackDelegate(new adts.LinkedListStack<>());
                }
            }
        });

        // --- Slider Action ---
        squareSizeSlider.addChangeListener((ChangeEvent e) -> {
            drawPanel.getSquare().setSize(squareSizeSlider.getValue());
            drawPanel.repaint();
        });

        // --- Timer to update labels periodically ---
        Timer labelUpdateTimer = new Timer(100, e -> updateLabels());
        labelUpdateTimer.start();
    }

    /**
     * Moves the square and updates labels.
     */
    private void moveSquare(int dx, int dy) {
        drawPanel.getSquare().move(dx, dy);
        updateLabels();
        drawPanel.repaint();
    }

    /**
     * Updates the position, undo count, and redo count labels.
     */
    public void updateLabels() {
        DawgSquare square = drawPanel.getSquare();
        positionLabel.setText(String.format("Position: (%4d, %4d)", square.x, square.y));
        undoLabel.setText("Undo Points: " + square.getUndoCount());
        redoLabel.setText("Redo Points: " + square.getRedoCount());
        revalidate();
        repaint();
    }
}
