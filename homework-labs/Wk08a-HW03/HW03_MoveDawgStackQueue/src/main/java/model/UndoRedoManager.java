package model;

import adts.*;
import java.awt.*;

/**
 * Manages undo/redo functionality using either stacks or queues.
 */
public class UndoRedoManager {
    private CustomStack<Point> undoStack;
    private CustomStack<Point> redoStack;
    private CustomQueue<Point> undoQueue;
    private CustomQueue<Point> redoQueue;

    private boolean useUndoStack = true;
    private boolean useRedoStack = true;

    public UndoRedoManager() {
        // Initialize the 4 undo and redo data structures
        // Default to Array for Stacks, and Linked Lists for Queues
        undoStack = new CustomStack<>(new ArrayStack<>());
        redoStack = new CustomStack<>(new ArrayStack<>());
        undoQueue = new CustomQueue<>(new LinkedListQueue<>());
        redoQueue = new CustomQueue<>(new LinkedListQueue<>());
    }


    // -----------------------------
    // Public Methods
    // -----------------------------

    // Hint: "useUndoStack" to determine if Stack or Queue is active

    public void recordCurrentPosition(Point current) {
        // Record current position for undo
        if (current == null) {
            return;
        }
        // store a copy so external mutation doesn't affect stored history
        Point copy = new Point(current);
        if (useUndoStack) {
            undoStack.push(copy);
        } else {
            undoQueue.enqueue(copy);
        }
    }

    // Clear the redo history
    public void clearRedo() {
        // Clear the appropriate redo data structure.
        if (useRedoStack) {
            redoStack.clear();
        } else {
            redoQueue.clear();
        }
    }

    /**
     * Performs an UNDO operation.
     * Saves the current position for a potential redo.
     * @param currentPos The current position.
     * @return The previous point to move to, or null if undo is not available.
     */
    public Point undo(Point currentPos, TrailManager trailManager) {
        Point prev = null;

        if (useUndoStack) {
            // Stack-based undo
            if (!undoStack.isEmpty()) {
                prev = undoStack.pop();       // Last recorded position
                if (useRedoStack) redoStack.push(prev);  // Push undone position
                else redoQueue.enqueue(prev);
                trailManager.clearOldTrailPoints();
            }
        } else {
            // Queue-based undo
            if (!undoQueue.isEmpty()) {
                CustomStack<Point> tempStack = new CustomStack<>(new ArrayStack<>());

                // Move all elements to stack to access the last element
                while (!undoQueue.isEmpty()) {
                    tempStack.push(undoQueue.dequeue());
                }

                // Pop last element (the one to undo)
                prev = tempStack.pop();

                // Restore remaining elements to undoQueue in original order
                CustomStack<Point> reverseStack = new CustomStack<>(new ArrayStack<>());
                while (!tempStack.isEmpty()) {
                    reverseStack.push(tempStack.pop());
                }
                while (!reverseStack.isEmpty()) {
                    undoQueue.enqueue(reverseStack.pop());
                }

                // Push undone position to redo
                if (useRedoStack) redoStack.push(prev);
                else redoQueue.enqueue(prev);

                trailManager.clearOldTrailPoints();
            }
        }

        return prev;
    }





    /**
     * Performs a REDO operation.
     * Saves the current position for a potential undo.
     * @param currentPos The current position.
     * @return The next point to move to, or null if redo is not available.
     */
    public Point redo(Point currentPos) {
        Point next = null;

        if (useRedoStack) {
            // Stack-based redo
            if (!redoStack.isEmpty()) {
                next = redoStack.pop();
                if (currentPos != null) {
                    Point copy = new Point(currentPos);
                    if (useUndoStack) undoStack.push(copy);
                    else undoQueue.enqueue(copy);
                }
            }
        } else {
            // Queue-based redo (simulate LIFO)
            if (!redoQueue.isEmpty()) {
                CustomStack<Point> tempStack = new CustomStack<>(new ArrayStack<>());
                CustomStack<Point> restoreStack = new CustomStack<>(new ArrayStack<>());

                // Move all redoQueue elements to tempStack
                while (!redoQueue.isEmpty()) {
                    tempStack.push(redoQueue.dequeue());
                }

                // Pop the last element (most recently undone)
                next = tempStack.pop();

                // Restore the remaining elements back into redoQueue
                while (!tempStack.isEmpty()) {
                    restoreStack.push(tempStack.pop());
                }
                while (!restoreStack.isEmpty()) {
                    redoQueue.enqueue(restoreStack.pop());
                }

                // Save currentPos into undo history
                if (currentPos != null) {
                    Point copy = new Point(currentPos);
                    if (useUndoStack) undoStack.push(copy);
                    else undoQueue.enqueue(copy);
                }
            }
        }

        return next == null ? null : new Point(next);
    }



    // -----------------------------
    // Getters
    // -----------------------------

    /**
     * Returns the count of available undo operations.
     * @return The size of the current undo data structure.
     */
    public int getUndoCount() {
        // Return the size from either undoStack or undoQueue based on useUndoStack.
        return useUndoStack ? undoStack.size() : undoQueue.size();
    }

    /**
     * Returns the count of available redo operations.
     * @return The size of the current redo data structure.
     */
    public int getRedoCount() {
        // Return the size from either redoStack or redoQueue based on useRedoStack.
        return useRedoStack ? redoStack.size() : redoQueue.size();
    }

    // -----------------------------
    // Setters
    // -----------------------------

    /**
     * Sets whether to use a stack for undo operations.
     * @param useUndoStack True to use a stack; false to use a queue.
     */
    public void setUseUndoStack(boolean useUndoStack) {
        this.useUndoStack = useUndoStack;
        // Clear both undo structures to avoid confusion when switching modes
        undoStack.clear();
        undoQueue.clear();
    }

    /**
     * Sets whether to use a stack for redo operations.
     * @param useRedoStack True to use a stack; false to use a queue.
     */
    public void setUseRedoStack(boolean useRedoStack) {
        this.useRedoStack = useRedoStack;
        // Clear both redo structures to avoid confusion when switching modes
        redoStack.clear();
        redoQueue.clear();
    }

    /**
     * Sets the delegate for the undo stack.
     * @param delegate The delegate to use.
     */
    public void setUndoStackDelegate(IStack<Point> delegate) {
        // Set the delegate for the undoStack.
        undoStack.setDelegate(delegate);
    }

    /**
     * Sets the delegate for the undo queue.
     * @param delegate The delegate to use.
     */
    public void setUndoQueueDelegate(IQueue<Point> delegate) {
        // Set the delegate for the undoQueue.
        undoQueue.setDelegate(delegate);
    }

    /**
     * Sets the delegate for the redo stack.
     * @param delegate The delegate to use.
     */
    public void setRedoStackDelegate(IStack<Point> delegate) {
        // Set the delegate for the redoStack.
        redoStack.setDelegate(delegate);
    }

    /**
     * Sets the delegate for the redo queue.
     * @param delegate The delegate to use.
     */
    public void setRedoQueueDelegate(IQueue<Point> delegate) {
        // Set the delegate for the redoQueue.
        redoQueue.setDelegate(delegate);
    }
}
