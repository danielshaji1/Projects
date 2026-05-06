package visualization;

import datastructures.TreeInterface;
import datastructures.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

public class TreePanel<T extends Comparable<T>> extends JPanel {
    private final TreeInterface<T> tree;
    private final int nodeRadius        = 20;
    private final int verticalSpacing   = 50;

    // Optional functions to determine node fill and border colors
    private final Function<TreeNode<T>, Color> fillColorFunction;
    private final Function<TreeNode<T>, Color> borderColorFunction;

    // Constructor using default colors (fill: gray, border: none)
    public TreePanel(TreeInterface<T> tree) {
        this(tree, null, null);
    }

    // Constructor that allows specifying color functions.
    // For example, in a red-black tree, you could supply:
    //   fillColorFunction: node -> (node.color == RBNode.RED ? Color.RED : Color.BLACK)
    //   borderColorFunction: node -> Color.WHITE (or any desired color)
    public TreePanel(TreeInterface<T> tree,
                     Function<TreeNode<T>, Color> fillColorFunction,
                     Function<TreeNode<T>, Color> borderColorFunction) {
        this.tree = tree;
        this.fillColorFunction = fillColorFunction;
        this.borderColorFunction = borderColorFunction;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        drawGrid(g2d);

        TreeNode<T> root = tree.getRoot();
        if (root != null) {
            drawTree(g, root, getWidth() / 2, 30, getWidth() / 4);
        } else {
            drawNoTree(g);
        }
    }

    private void drawTree(Graphics g, TreeNode<T> node, int x, int y, int xOffset) {
        // Added check: if the node is null, nil, or its data is null, skip drawing it.
        if (node == null || node.isNil() || node.getData() == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        // For left child: only recurse if it's not nil.
        if (node.getLeft() != null && !node.getLeft().isNil()) {
            int childX = x - xOffset;
            int childY = y + verticalSpacing;
            g.setColor(Color.BLACK);
            g.drawLine(x, y, childX, childY);
            drawTree(g, node.getLeft(), childX, childY, xOffset / 2);
        }

        // For right child: only recurse if it's not nil.
        if (node.getRight() != null && !node.getRight().isNil()) {
            int childX = x + xOffset;
            int childY = y + verticalSpacing;
            g.setColor(Color.BLACK);
            g.drawLine(x, y, childX, childY);
            drawTree(g, node.getRight(), childX, childY, xOffset / 2);
        }

        // Determine fill color based on supplied function or default to gray.
        Color fillColor = (fillColorFunction != null) ? fillColorFunction.apply(node) : Color.GRAY;
        g.setColor(fillColor);
        g.fillOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

        // Determine border color if provided.
        if (borderColorFunction != null) {
            Color borderColor = borderColorFunction.apply(node);
            g.setColor(borderColor);
            g.drawOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
        }

        // Draw the node label in white.
        g.setColor(Color.WHITE);
        String label = node.getData().toString();
        FontMetrics fm = g.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelHeight = fm.getAscent();
        g.drawString(label, x - labelWidth / 2, y + labelHeight / 2);
    }

    private void drawNoTree(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("No tree to display", getWidth() / 2 - 50, getHeight() / 2);
    }

    /**
     * Draws a simple grid on the background.
     */
    private void drawGrid(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        Color lightGray = new Color(240, 240, 240);
        Color darkGray = new Color(210, 210, 210);

        Color color_treeAVL  = new Color(0xE2F0FA);
        Color color_treeRB  = new Color(0xFBF1F1);


        TreeNode<T> root = tree.getRoot();
        Color fillColor = (fillColorFunction != null) ?
                color_treeAVL: color_treeRB;

        g2d.setColor(fillColor);
        g2d.fillRect(0, 0, width, height);

        for (int x = 0; x < width; x += 10) {
            g2d.setColor(x % 100 == 0 ? darkGray : lightGray);
            g2d.drawLine(x, 0, x, height);
        }
        for (int y = 0; y < height; y += 10) {
            g2d.setColor(y % 100 == 0 ? darkGray : lightGray);
            g2d.drawLine(0, y, width, y);
        }
    }
}