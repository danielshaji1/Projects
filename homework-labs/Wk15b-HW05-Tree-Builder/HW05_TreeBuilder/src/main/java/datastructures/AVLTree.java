package datastructures;

public class AVLTree<T extends Comparable<T>> implements TreeInterface<T> {

    private AVLNode<T> root;
    private boolean usePredecessorForDelete = false;

    @Override
    public String toString() {
        return "AVL Tree";
    }

    public void setUsePredecessorForDelete(boolean usePredecessor) {
        this.usePredecessorForDelete = usePredecessor;
    }

    @Override
    public void insert(T value) {
        root = insertBST(root, value);
    }

    private AVLNode<T> insertBST(AVLNode<T> node, T value) {
        if (node == null) return new AVLNode<>(value);

        if (value.compareTo(node.getData()) < 0) {
            node.setLeft(insertBST((AVLNode<T>) node.getLeft(), value));
            if (node.getLeft() != null) node.getLeft().setParent(node);
        } else if (value.compareTo(node.getData()) > 0) {
            node.setRight(insertBST((AVLNode<T>) node.getRight(), value));
            if (node.getRight() != null) node.getRight().setParent(node);
        }

        updateHeight(node);
        return balanceNode(node);
    }

    @Override
    public void delete(T value) {
        root = deleteNode(root, value);
    }

    private AVLNode<T> deleteNode(AVLNode<T> node, T value) {
        if (node == null) return null;

        int cmp = value.compareTo(node.getData());

        if (cmp < 0) {
            node.setLeft(deleteNode((AVLNode<T>) node.getLeft(), value));
            if (node.getLeft() != null) node.getLeft().setParent(node);
        } else if (cmp > 0) {
            node.setRight(deleteNode((AVLNode<T>) node.getRight(), value));
            if (node.getRight() != null) node.getRight().setParent(node);
        } else {
            // Node to delete found
            if (node.getLeft() == null || node.getRight() == null) {
                AVLNode<T> temp = (AVLNode<T>) (node.getLeft() != null ? node.getLeft() : node.getRight());
                if (temp != null) temp.setParent(node.getParent());
                return temp;
            } else {
                if (usePredecessorForDelete) {
                    // predecessor (max in left subtree)
                    AVLNode<T> pred = (AVLNode<T>) node.getLeft();
                    while (pred.getRight() != null) pred = (AVLNode<T>) pred.getRight();
                    node.setData(pred.getData());
                    node.setLeft(deleteNode((AVLNode<T>) node.getLeft(), pred.getData()));
                    if (node.getLeft() != null) node.getLeft().setParent(node);
                } else {
                    // successor (min in right subtree)
                    AVLNode<T> succ = (AVLNode<T>) node.getRight();
                    while (succ.getLeft() != null) succ = (AVLNode<T>) succ.getLeft();
                    node.setData(succ.getData());
                    node.setRight(deleteNode((AVLNode<T>) node.getRight(), succ.getData()));
                    if (node.getRight() != null) node.getRight().setParent(node);
                }
            }
        }

        updateHeight(node);
        return balanceNode(node);
    }

    @Override
    public boolean contains(T value) {
        return containsNode(root, value);
    }

    private boolean containsNode(AVLNode<T> node, T value) {
        if (node == null) return false;
        int cmp = value.compareTo(node.getData());
        if (cmp == 0) return true;
        else if (cmp < 0) return containsNode((AVLNode<T>) node.getLeft(), value);
        else return containsNode((AVLNode<T>) node.getRight(), value);
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    private AVLNode<T> rotateLeft(AVLNode<T> x) {
        AVLNode<T> y = (AVLNode<T>) x.getRight();
        AVLNode<T> T2 = (AVLNode<T>) y.getLeft();

        y.setLeft(x);
        x.setRight(T2);

        if (T2 != null) T2.setParent(x);
        y.setParent(x.getParent());
        x.setParent(y);

        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private AVLNode<T> rotateRight(AVLNode<T> y) {
        AVLNode<T> x = (AVLNode<T>) y.getLeft();
        AVLNode<T> T2 = (AVLNode<T>) x.getRight();

        x.setRight(y);
        y.setLeft(T2);

        if (T2 != null) T2.setParent(y);
        x.setParent(y.getParent());
        y.setParent(x);

        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private void updateHeight(AVLNode<T> node) {
        int leftH = (node.getLeft() == null) ? 0 : ((AVLNode<T>) node.getLeft()).getHeight();
        int rightH = (node.getRight() == null) ? 0 : ((AVLNode<T>) node.getRight()).getHeight();
        node.setHeight(1 + Math.max(leftH, rightH));
    }

    private AVLNode<T> balanceNode(AVLNode<T> node) {
        int leftH = (node.getLeft() == null) ? 0 : ((AVLNode<T>) node.getLeft()).getHeight();
        int rightH = (node.getRight() == null) ? 0 : ((AVLNode<T>) node.getRight()).getHeight();
        int balance = leftH - rightH;

        if (balance > 1) { // left heavy
            AVLNode<T> left = (AVLNode<T>) node.getLeft();
            int lLeftH = (left.getLeft() == null) ? 0 : ((AVLNode<T>) left.getLeft()).getHeight();
            int lRightH = (left.getRight() == null) ? 0 : ((AVLNode<T>) left.getRight()).getHeight();
            if (lRightH > lLeftH) {
                node.setLeft(rotateLeft(left));
                if (node.getLeft() != null) node.getLeft().setParent(node);
            }
            return rotateRight(node);
        } else if (balance < -1) { // right heavy
            AVLNode<T> right = (AVLNode<T>) node.getRight();
            int rLeftH = (right.getLeft() == null) ? 0 : ((AVLNode<T>) right.getLeft()).getHeight();
            int rRightH = (right.getRight() == null) ? 0 : ((AVLNode<T>) right.getRight()).getHeight();
            if (rLeftH > rRightH) {
                node.setRight(rotateRight(right));
                if (node.getRight() != null) node.getRight().setParent(node);
            }
            return rotateLeft(node);
        }

        return node;
    }
}
