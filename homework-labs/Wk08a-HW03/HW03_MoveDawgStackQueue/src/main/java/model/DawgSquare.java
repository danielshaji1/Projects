package model;

import adts.*;
import settings.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DawgSquare {
    public int x;
    public int y;
    public int size;
    private Color color;
    private Image dawgImage;

    // Managers for separate concerns
    private TrailManager        trailManager;
    private UndoRedoManager     undoRedoManager;

    public DawgSquare(int startX, int startY, int size, Color color) {
        this.x = startX;
        this.y = startY;
        this.size = size;
        this.color = color;

        // Instantiate managers with defaults
        this.trailManager = new TrailManager(color, size);
        this.undoRedoManager = new UndoRedoManager();

        loadImage(size); // loads the dog image
    }

    private void loadImage(int newSize) {
        try {
            BufferedImage originalImage = ImageIO.read(
                    getClass().getResource("/images/uga_dawg.png")
            );
            int newWidth = (int) (newSize * 1.5);
            int newHeight = (int) (newSize * 1.5);
            dawgImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error: Could not load the dawg image.");
        }
    }

    // ----------------------
    // Public API
    // ----------------------
    public void setSize(int newSize) {
        this.size = newSize;
        loadImage(newSize);
        trailManager.setSquareSize(newSize);  // Let the trail manager know, if it scales trail sizes
    }

    public void move(int dx, int dy) {
        // 1) Record the current position for undo
        undoRedoManager.recordCurrentPosition(new Point(x, y));

        // 2) Clear redo history because we made a new move
        undoRedoManager.clearRedo();

        // 3) If trail is enabled, add a trail point
        trailManager.addTrailPoint(x, y);

        // 4) Move the square
        x += dx;
        y += dy;

        // 5) Clean up old trail points
        trailManager.clearOldTrailPoints();
    }

    public void undo() {
        Point prev = undoRedoManager.undo(new Point(x, y), trailManager); // pass your TrailManager
        if (prev != null) {
            x = prev.x;
            y = prev.y;
        }
    }


    public void redo() {
        Point next = undoRedoManager.redo(new Point(x, y));
        if (next != null) {
            x = next.x;
            y = next.y;
        }
    }

    // Draw the square and delegate trail drawing
    public void draw(Graphics2D g2d) {
        trailManager.drawTrail(g2d);

        g2d.setColor(color);
        g2d.fillRect(x, y, size, size);
        g2d.setColor(new Color(255, 250, 50));
        g2d.drawRect(x, y, size, size);

        if (dawgImage != null) {
            g2d.drawImage(dawgImage, x, y, size, size, null);
        }
    }

    // ----------------------
    // Getters (For UI)
    // ----------------------
    public int getUndoCount() {
        return undoRedoManager.getUndoCount();
    }
    public int getRedoCount() {
        return undoRedoManager.getRedoCount();
    }

    // ----------------------
    // Delegation Setters
    // ----------------------
    public void setTrailEnabled(boolean enabled) {
        trailManager.setEnabled(enabled);
    }

    public void setTrailMode(boolean useQueue) {
        trailManager.setUseQueue(useQueue);
    }

    public void setUndoMode(boolean useStack) {
        undoRedoManager.setUseUndoStack(useStack);
    }

    public void setRedoMode(boolean useStack) {
        undoRedoManager.setUseRedoStack(useStack);
    }

    // Optionally expose direct delegate switching:
    public void setTrailStackDelegate(IStack<TrailPoint> delegate) {
        trailManager.setStackDelegate(delegate);
    }

    public void setTrailQueueDelegate(IQueue<TrailPoint> delegate) {
        trailManager.setQueueDelegate(delegate);
    }

    public void setUndoStackDelegate(IStack<Point> delegate) {
        undoRedoManager.setUndoStackDelegate(delegate);
    }

    public void setUndoQueueDelegate(IQueue<Point> delegate) {
        undoRedoManager.setUndoQueueDelegate(delegate);
    }

    public void setRedoStackDelegate(IStack<Point> delegate) {
        undoRedoManager.setRedoStackDelegate(delegate);
    }

    public void setRedoQueueDelegate(IQueue<Point> delegate) {
        undoRedoManager.setRedoQueueDelegate(delegate);
    }
}
