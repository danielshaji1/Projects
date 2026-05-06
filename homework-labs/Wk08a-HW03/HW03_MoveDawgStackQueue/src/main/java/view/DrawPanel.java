package view;

import model.DawgSquare;
import settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DrawPanel extends JPanel implements ActionListener {
    private DawgSquare square;
    private Timer timer;

    // Bounce mode variables
    private int dx = 2, dy = 2;

    // Animation mode: "bounce" or "box"
    private String animationMode = "bounce";

    // Box mode variables for a square (box) pattern
    private int boxState = 0;           // 0: right, 1: down, 2: left, 3: up
    private int boxDistanceMoved = 0;   // how far we've moved in the current direction
    private int boxSideLength = Settings.BOX_SIZE;      // length of each side of the box pattern

    public DrawPanel() {
        setPreferredSize(new Dimension(Settings.PANEL_WIDTH, Settings.PANEL_HEIGHT));
        setBackground(Color.WHITE);

        // Initialize the square at the center.
        int startX = Settings.PANEL_WIDTH / 2;
        int startY = Settings.PANEL_HEIGHT / 2;
        square = new DawgSquare(
                startX, startY,
                Settings.SQUARE_SIZE,
                Settings.SQUARE_COLOR
        );

        // Create a timer for animation (but do not start it by default).
        timer = new Timer(30, this);
        setAnimation(true);
    }

    public DawgSquare getSquare() {
        return square;
    }

    /**
     * Enables or disables animation.
     */
    public void setAnimation(boolean animate) {
        if (animate) {
            if (!timer.isRunning()) {
                timer.start();
            }
        } else {
            timer.stop();
        }
    }


    /**
     * Sets the animation mode.
     * @param mode "bounce" for bouncing, "box" for moving in a square pattern.
     */
    public void setAnimationMode(String mode) {
        this.animationMode = mode;

        if (mode.equalsIgnoreCase("none")) {
            setAnimation(false);
            return;
        }
        setAnimation(true); // flaky putting it here too

        // Reset box mode state when switching to box mode.
        if(mode.equalsIgnoreCase("box")) {
            boxState = 0;
            boxDistanceMoved = 0;
        }
        setAnimation(true); // flaky putting it here too
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Enable antialiasing for smoother rendering.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
        square.draw(g2d);
    }

    /**
     * Draws a simple grid on the background.
     */
    private void drawGrid(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        Color lightGray = new Color(250, 250, 200);
        Color darkGray = new Color(210, 210, 210);
        for (int x = 0; x < width; x += 10) {
            g2d.setColor(x % 100 == 0 ? darkGray : lightGray);
            g2d.drawLine(x, 0, x, height);
        }
        for (int y = 0; y < height; y += 10) {
            g2d.setColor(y % 100 == 0 ? darkGray : lightGray);
            g2d.drawLine(0, y, width, y);
        }
    }

    /**
     * Timer callback for animation.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (animationMode.equalsIgnoreCase("bounce")) {
            // Bounce mode: reverse direction upon hitting panel edges.
            int newX = square.x + dx;
            int newY = square.y + dy;

            // Bounce off horizontal edges.
            if (newX < 0 || newX + square.size > getWidth()) {
                dx = -dx;
                newX = square.x + dx;
            }
            // Bounce off vertical edges.
            if (newY < 0 || newY + square.size > getHeight()) {
                dy = -dy;
                newY = square.y + dy;
            }
            square.move(dx, dy);
        } else if (animationMode.equalsIgnoreCase("box")) {
            int moveIncrement = 2; // desired movement speed per tick

            // When starting a new side, recalculate the maximum available distance.
            if (boxDistanceMoved == 0) {
                int available = 0;
                if (boxState == 0) { // moving right
                    available = getWidth() - (square.x + square.size);
                } else if (boxState == 1) { // moving down
                    available = getHeight() - (square.y + square.size);
                } else if (boxState == 2) { // moving left
                    available = square.x;
                } else if (boxState == 3) { // moving up
                    available = square.y;
                }
                // If the available distance is less than the default boxSideLength, use it.
                if (available < boxSideLength) {
                    boxSideLength = available;
                }
            }

            int remaining = boxSideLength - boxDistanceMoved;
            // Determine the safe move amount (so we don't go past the edge)
            int safeMove = moveIncrement;
            if (boxState == 0) { // moving right
                safeMove = Math.min(moveIncrement, getWidth() - (square.x + square.size));
            } else if (boxState == 1) { // moving down
                safeMove = Math.min(moveIncrement, getHeight() - (square.y + square.size));
            } else if (boxState == 2) { // moving left
                safeMove = Math.min(moveIncrement, square.x);
            } else if (boxState == 3) { // moving up
                safeMove = Math.min(moveIncrement, square.y);
            }

            int moveAmount = Math.min(safeMove, remaining);

            // If no move is possible (i.e. we're at the boundary), switch direction.
            if (moveAmount <= 0) {
                boxDistanceMoved = 0;
                boxState = (boxState + 1) % 4;
            } else {
                // Move the square in the current direction by moveAmount.
                switch (boxState) {
                    case 0: square.move(moveAmount, 0); break;
                    case 1: square.move(0, moveAmount); break;
                    case 2: square.move(-moveAmount, 0); break;
                    case 3: square.move(0, -moveAmount); break;
                }
                boxDistanceMoved += moveAmount;
                // If the current side has been fully traversed, switch direction.
                if (boxDistanceMoved >= boxSideLength) {
                    boxDistanceMoved = 0;
                    boxState = (boxState + 1) % 4;
                }
            }
        }
        repaint();
    }
}
