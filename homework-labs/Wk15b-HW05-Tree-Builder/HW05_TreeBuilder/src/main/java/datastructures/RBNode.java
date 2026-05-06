package datastructures;

public class RBNode<T extends Comparable<T>> extends TreeNode<T> {
    public static final boolean RED = true;
    public static final boolean BLACK = false;

    private boolean color; // true=RED, false=BLACK

    public RBNode(T data) {
        super(data);
        this.color = RED; // new nodes often start red
    }

    public boolean isRed() {
        return color == RED;
    }
    public boolean isBlack() {
        return color == BLACK;
    }
    public void setColor(boolean color) {
        this.color = color;
    }
    public boolean getColor() {
        return color;
    }
}