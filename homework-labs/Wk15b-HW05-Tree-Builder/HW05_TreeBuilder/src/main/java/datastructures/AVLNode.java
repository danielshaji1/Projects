package datastructures;

public class AVLNode<T extends Comparable<T>> extends TreeNode<T> {
    private int height;

    public AVLNode(T data) {
        super(data);
        this.height = 1; // or 0, whichever convention you prefer
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}