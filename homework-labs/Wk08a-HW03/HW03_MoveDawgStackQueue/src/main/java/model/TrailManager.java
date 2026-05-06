package model;

import adts.*;
import settings.Settings;
import java.awt.*;

/**
 * Manages the trail points (either via queue or stack) and drawing logic.
 */
public class TrailManager {
    private CustomQueue<TrailPoint> trailQueue;
    private  CustomStack<TrailPoint> trailStack;
    private boolean useQueue = true;
    private boolean enabled = true;

    private  Color baseColor;
    private int squareSize;

    public TrailManager(Color baseColor, int squareSize) {
        this.baseColor = baseColor;
        this.squareSize = squareSize;

        // Initialize queue and stack with default implementations
        trailQueue = new CustomQueue<>(new LinkedListQueue<>());
        trailStack = new CustomStack<>(new ArrayStack<>());
    }

    // -----------------------------
    // Public methods
    // -----------------------------
    public void addTrailPoint(int x, int y) {
        if (!enabled) return;

        TrailPoint tp = new TrailPoint(x, y, System.currentTimeMillis());

        if (useQueue) {
            trailQueue.enqueue(tp);
        } else {
            trailStack.push(tp);
        }
    }

    public void drawTrail(Graphics2D g2d) {
        if (!enabled) return;

        Iterable<TrailPoint> activeTrail = useQueue ? trailQueue : trailStack;
        if (activeTrail == null) return;

        int total = 0;
        // First, count the total points
        for (TrailPoint tp : activeTrail) total++;

        if (total == 0) return;

        int index = 0;
        for (TrailPoint tp : activeTrail) {
            float ratio = (float) index / total; // 0 = front, 1 = oldest

            // Fade color and alpha
            int r = (int) (baseColor.getRed() * (1 - ratio));
            int g = (int) (baseColor.getGreen() * (1 - ratio));
            int b = (int) (baseColor.getBlue() * (1 - ratio));
            int alpha = (int) (255 * (1 - ratio)); // transparent for older points
            Color blended = new Color(r, g, b, alpha);

            g2d.setColor(blended);

            // Fade size: newest points are largest, oldest smallest
            int size = (int) (squareSize * (1 - 0.5 * ratio)); // scale down by 50%
            size = Math.max(1, size); // ensure at least 1 pixel

            g2d.fillRect(tp.x - size / 2, tp.y - size / 2, size, size);

            index++;
        }
    }


    private Color blendColor(Color base, float ratio) {
        // Lighter color for older points
        int r = (int) (base.getRed() * (1 - 0.5 * ratio) + 255 * 0.5 * ratio);
        int g = (int) (base.getGreen() * (1 - 0.5 * ratio) + 255 * 0.5 * ratio);
        int b = (int) (base.getBlue() * (1 - 0.5 * ratio) + 255 * 0.5 * ratio);
        return new Color(r, g, b);
    }

    public void clearOldTrailPoints() {
        if (!enabled) return;

        long currentTime = System.currentTimeMillis();
        if (useQueue) {
            clearOldTrailPointsIterativeQueue(currentTime);
        } else {
            clearOldTrailPointsIterativeStack(currentTime);
        }
    }

    private void clearOldTrailPointsIterativeQueue(long currentTime) {
        while (!trailQueue.isEmpty()) {
            TrailPoint tp = trailQueue.peek();
            if (currentTime - tp.timestamp > Settings.TRAIL_FADE_TIME) {
                trailQueue.dequeue();
            } else {
                break;
            }
        }
    }

    private void clearOldTrailPointsIterativeStack(long currentTime) {
        CustomStack<TrailPoint> tempStack = new CustomStack<>(new ArrayStack<>());
        while (!trailStack.isEmpty()) {
            TrailPoint tp = trailStack.pop();
            if (currentTime - tp.timestamp <= Settings.TRAIL_FADE_TIME) {
                tempStack.push(tp);
            }
        }
        // Restore points still valid
        while (!tempStack.isEmpty()) {
            trailStack.push(tempStack.pop());
        }
    }

    // -----------------------------
    // Setters / Configuration
    // -----------------------------
    public void setUseQueue(boolean useQueue) {
        this.useQueue = useQueue;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            trailQueue.clear();
            trailStack.clear();
        }
    }

    public void setSquareSize(int size) {
        this.squareSize = size;
    }

    public void setStackDelegate(IStack<TrailPoint> delegate) {
        trailStack.setDelegate(delegate);
    }

    public void setQueueDelegate(IQueue<TrailPoint> delegate) {
        trailQueue.setDelegate(delegate);
    }
}

class TrailPoint {
    int x, y;
    long timestamp;

    public TrailPoint(int x, int y, long timestamp) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TrailPoint{" +
                "x=" + x +
                ", y=" + y +
                ", timestamp=" + timestamp +
                '}';
    }
}
