package utils;

import datastructures.TreeInterface;
import datastructures.TreeNode;
import datastructures.RedBlackTree; // Assumes you have this class

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreePrinter {

    /**
     * Prints the tree traversals to the console in a formatted style.
     * @param tree The tree implementing TreeInterface.
     * @param treeName A descriptive name for the tree (e.g., "Red-Black Tree").
     */
    public static void printTree(TreeInterface<?> tree, String treeName) {
        System.out.println("=== " + treeName + " Traversals ===");
        TreeNode<?> root = tree.getRoot();
        String inorder = commaSeparated(TreeStringBuilder.buildInorder(root));
        String preorder = commaSeparated(TreeStringBuilder.buildPreorder(root));
        String postorder = commaSeparated(TreeStringBuilder.buildPostorder(root));
        System.out.println("Inorder: " + inorder);
        System.out.println("Preorder: " + preorder);
        System.out.println("Postorder: " + postorder);

        if (tree instanceof RedBlackTree) {
            String redNodes = commaSeparated(getRedNodes((RedBlackTree<?>) tree));
            System.out.println("Red Nodes (inorder): " + redNodes);
        }
    }

    /**
     * Helper method to convert a space-separated string to a comma-separated string.
     */
    private static String commaSeparated(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String[] parts = input.trim().split("\\s+");
        return String.join(", ", parts);
    }

    /**
     * Collects red nodes from a RedBlackTree in inorder fashion.
     * Assumes that each node has methods: getData(), getLeft(), getRight(), isRed().
     */
    private static String getRedNodes(RedBlackTree<?> tree) {
        List<Comparable> redNodes = new ArrayList<>();
        try {
            Method m = tree.getClass().getMethod("getRoot");
            Object root = m.invoke(tree);
            collectRedNodes(root, redNodes);
        } catch (Exception e) {
            return "";
        }
        Collections.sort(redNodes);
        StringBuilder sb = new StringBuilder();
        for (Object data : redNodes) {
            sb.append(data).append(" ");
        }
        return sb.toString().trim().replaceAll("\\s+", ", ");
    }

    private static void collectRedNodes(Object node, List<Comparable> redNodes) {
        if (node == null) return;
        if (node instanceof datastructures.TreeNode) {
            datastructures.TreeNode<?> tnode = (datastructures.TreeNode<?>) node;
            // If this is a sentinel node (data is null), then stop recursion
            if (tnode.getData() == null) return;
            // If the node is an RBNode, check its color
            if (node instanceof datastructures.RBNode) {
                datastructures.RBNode<?> rbNode = (datastructures.RBNode<?>) node;
                if (rbNode.isRed()) {
                    redNodes.add((Comparable) rbNode.getData());
                }
            }
            // Recurse on left and right children
            collectRedNodes(tnode.getLeft(), redNodes);
            collectRedNodes(tnode.getRight(), redNodes);
        }
    }
}