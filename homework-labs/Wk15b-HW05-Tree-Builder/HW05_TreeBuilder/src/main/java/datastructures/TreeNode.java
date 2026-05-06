package datastructures;

/**
 * A minimal node class for any binary tree.
 */
public class TreeNode<T extends Comparable<T>> {
    public T data;
    public TreeNode<T> left, right, parent;
    protected boolean nil = false; // false by default

    public TreeNode(T data) {
        this.data = data;
    }

    public boolean isNil() {
        return nil;
    }

    // Getters/Setters
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public TreeNode<T> getLeft()  { return left; }
    public TreeNode<T> getRight() { return right; }
    public TreeNode<T> getParent(){ return parent; }

    public void setLeft(TreeNode<T> left)   { this.left = left; }
    public void setRight(TreeNode<T> right) { this.right = right; }
    public void setParent(TreeNode<T> parent) { this.parent = parent; }
}