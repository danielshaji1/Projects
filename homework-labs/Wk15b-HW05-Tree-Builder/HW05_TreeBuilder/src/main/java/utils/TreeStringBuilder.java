package utils;

import datastructures.TreeNode;

public class TreeStringBuilder {

    /**
     * Builds a string representation of the tree in the format:
     * nodeValue(leftSubtree,rightSubtree)
     * where null children are represented by "null".
     * This representation is similar to a pre-order traversal with explicit null markers.
     */
    public static String build(TreeNode<?> root) {
        if (root == null) {
            return "null";  // return "null" for missing nodes
        }
        String left = build(root.getLeft());
        String right = build(root.getRight());
        return root.getData().toString() + "(" + left + "," + right + ")";
    }


    /**
     * Builds a pre-order traversal string of the tree.
     * Format: node left_subtree right_subtree (separated by spaces)
     */
    public static String buildPreorder(TreeNode<?> root) {
        if (root == null || root.getData() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(root.getData().toString()).append(" ");
        sb.append(buildPreorder(root.getLeft())).append(" ");
        sb.append(buildPreorder(root.getRight()));
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    /**
     * Builds an in-order traversal string of the tree.
     * Format: left_subtree node right_subtree (separated by spaces)
     */
    public static String buildInorder(TreeNode<?> root) {
        if (root == null || root.getData() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(buildInorder(root.getLeft()));
        if (!sb.toString().isEmpty()) {
            sb.append(" ");
        }
        sb.append(root.getData().toString());
        String right = buildInorder(root.getRight());
        if (!right.isEmpty()) {
            sb.append(" ").append(right);
        }
        return sb.toString().trim().replaceAll("\\s+", " ");
    }

    /**
     * Builds a post-order traversal string of the tree.
     * Format: left_subtree right_subtree node (separated by spaces)
     */
    public static String buildPostorder(TreeNode<?> root) {
        if (root == null || root.getData() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(buildPostorder(root.getLeft()));
        if (!sb.toString().isEmpty()) {
            sb.append(" ");
        }
        sb.append(buildPostorder(root.getRight()));
        if (!sb.toString().isEmpty()) {
            sb.append(" ");
        }
        sb.append(root.getData().toString());
        return sb.toString().trim().replaceAll("\\s+", " ");
    }
}