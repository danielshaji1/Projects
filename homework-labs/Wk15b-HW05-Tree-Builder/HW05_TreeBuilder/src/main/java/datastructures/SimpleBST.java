package datastructures;

public class SimpleBST<T extends Comparable<T>> implements TreeInterface<T> {

    private BSTNode<T> root;

    @Override
    public String toString() {
        return "Simple Binary Search Tree";
    }

    @Override
    public void insert(T value) {
        root = insertNode(root, value);
    }

    private BSTNode<T> insertNode(BSTNode<T> node, T value) {
        if (node == null) {
            return new BSTNode<>(value);
        }
        // Standard BST insertion
        if (value.compareTo(node.getData()) < 0) {
            node.setLeft(insertNode((BSTNode<T>) node.getLeft(), value));
            if (node.getLeft() != null) {
                node.getLeft().setParent(node);
            }
        } else if (value.compareTo(node.getData()) > 0) {
            node.setRight(insertNode((BSTNode<T>) node.getRight(), value));
            if (node.getRight() != null) {
                node.getRight().setParent(node);
            }
        }
        // If the value is already present, do nothing.
        return node;
    }

    @Override
    public void delete(T value) {
        root = deleteNode(root, value);
    }

    private BSTNode<T> deleteNode(BSTNode<T> node, T value) {
        if (node == null) {
            return null; // Value not found
        }
        int cmp = value.compareTo(node.getData());
        if (cmp < 0) {
            node.setLeft(deleteNode((BSTNode<T>) node.getLeft(), value));
            if (node.getLeft() != null) {
                node.getLeft().setParent(node);
            }
        } else if (cmp > 0) {
            node.setRight(deleteNode((BSTNode<T>) node.getRight(), value));
            if (node.getRight() != null) {
                node.getRight().setParent(node);
            }
        } else {
            // Node to delete found
            if (node.getLeft() == null || node.getRight() == null) {
                BSTNode<T> temp = (BSTNode<T>) (node.getLeft() != null ? node.getLeft() : node.getRight());
                if (temp != null) {
                    temp.setParent(node.getParent());
                }
                return temp;
            } else {
                // Node with two children: find the inorder successor (minimum in right subtree)
                BSTNode<T> successor = findMin((BSTNode<T>) node.getRight());
                node.setData(successor.getData());
                node.setRight(deleteNode((BSTNode<T>) node.getRight(), successor.getData()));
                if (node.getRight() != null) {
                    node.getRight().setParent(node);
                }
            }
        }
        return node;
    }

    @Override
    public boolean contains(T value) {
        return containsNode(root, value);
    }

    private boolean containsNode(BSTNode<T> node, T value) {
        if (node == null) {
            return false;
        }
        int cmp = value.compareTo(node.getData());
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return containsNode((BSTNode<T>) node.getLeft(), value);
        } else {
            return containsNode((BSTNode<T>) node.getRight(), value);
        }
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    private BSTNode<T> findMin(BSTNode<T> node) {
        while (node.getLeft() != null) {
            node = (BSTNode<T>) node.getLeft();
        }
        return node;
    }
}