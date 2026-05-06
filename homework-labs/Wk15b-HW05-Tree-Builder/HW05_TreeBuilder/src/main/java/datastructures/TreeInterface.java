package datastructures;

public interface TreeInterface<T extends Comparable<T>> {
    void insert(T value);
    void delete(T value);
    boolean contains(T value);
    TreeNode<T> getRoot();
}