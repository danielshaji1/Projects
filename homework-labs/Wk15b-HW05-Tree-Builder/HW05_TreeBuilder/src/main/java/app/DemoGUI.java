package app;

import datastructures.*;
import visualization.TreePanel;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class DemoGUI extends JFrame {
    private JTextField nodesTextField;
    private JButton insertButton;
    private JTextField deleteTextField;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton printButton;
    private JCheckBox predecessorCheckBox;

    // Tree instances for BST, AVL, and RB
    private TreeInterface<Integer> bstTree;
    private TreeInterface<Integer> avlTree;
    private TreeInterface<Integer> rbTree;

    // Panels that display trees along with their selection menu
    private TreeDisplayPanel leftDisplayPanel;
    private TreeDisplayPanel rightDisplayPanel;

    // Container for the two tree display panels
    private JPanel treesPanel;

    // Test sequences from CSV or hardcoded values
    private static final Map<String, String[]> SEQUENCES = new HashMap<>();
    static {
        SEQUENCES.put("ABC 11, 3, 15, 1, 8, 6, 9", new String[]{"11", "3", "15", "1", "8", "6", "9"});
        SEQUENCES.put("BBB 10, 20, 30, 25, 35, delete 10", new String[]{"10", "20", "30", "25", "35", "delete 10"});
        SEQUENCES.put("DDD 11, 3, 15, 1, 8, 6, 9, 5, delete 10", new String[]{"11", "3", "15", "1", "8", "6", "9", "5"});
    }

    public DemoGUI() {
        super("\uD83C\uDF32 Tree Visualizer (BST, AVL, RB) \uD83C\uDF32");
        loadSequencesFromCSV();
        initTrees();
        initUI();
    }

    private void initTrees() {
        // Initialize your tree instances.
        bstTree = new SimpleBST<>();
        avlTree = new AVLTree<>();
        rbTree = new RedBlackTree<>();
    }

    private void loadSequencesFromCSV() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("input/playlist.csv");
            if (in == null) {
                System.err.println("Could not find playlist.csv in resources/input/");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;
                String key = parts[0].trim();
                String[] steps = new String[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    steps[i - 1] = parts[i].trim();
                }
                SEQUENCES.put(key, steps);
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    private void initUI() {
        // --- Top Control Panel ---
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        predecessorCheckBox = new JCheckBox("Predecessor ");
        JPanel row1Panel = new JPanel(new BorderLayout());
        clearButton = new JButton("Clear Tree");
        JPanel clearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        clearPanel.add(clearButton);
        row1Panel.add(clearPanel, BorderLayout.WEST);
        row1Panel.add(predecessorCheckBox);

        JPanel insertPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nodesTextField = new JTextField(20);
        nodesTextField.setText("40, 20, 60, 10, 25, 50, 55, 70");
        insertButton = new JButton("Insert");
        insertPanel.add(new JLabel("Nodes (comma separated):"));
        insertPanel.add(nodesTextField);
        insertPanel.add(insertButton);
        row1Panel.add(insertPanel, BorderLayout.EAST);

        JPanel row2Panel = new JPanel(new BorderLayout());
        printButton = new JButton("Print Traversals");
        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        printPanel.add(printButton);
        row2Panel.add(printPanel, BorderLayout.WEST);

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteTextField = new JTextField(5);
        deleteButton = new JButton("Delete");
        deletePanel.add(new JLabel("Value to Delete:"));
        deletePanel.add(deleteTextField);
        deletePanel.add(deleteButton);
        row2Panel.add(deletePanel, BorderLayout.EAST);

        JPanel row3Panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] sequenceNames = SEQUENCES.keySet().toArray(new String[0]);
        Arrays.sort(sequenceNames);
        JComboBox<String> sequencesCombo = new JComboBox<>(sequenceNames);
        JButton runSequenceButton = new JButton("Run Sequence");
        row3Panel.add(new JLabel("Pick Test Seq:"));
        row3Panel.add(sequencesCombo);
        row3Panel.add(runSequenceButton);

        controlPanel.add(row1Panel);
        controlPanel.add(row2Panel);
        controlPanel.add(row3Panel);

        // --- Tree Display Panels with Dropdown Menus ---
        treesPanel = new JPanel(new GridLayout(1, 2));
        // Default left panel shows AVL and right panel shows RB
        leftDisplayPanel = new TreeDisplayPanel(avlTree);
        rightDisplayPanel = new TreeDisplayPanel(rbTree);
        treesPanel.add(leftDisplayPanel);
        treesPanel.add(rightDisplayPanel);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(treesPanel, BorderLayout.CENTER);

        // --- Button Actions ---
        insertButton.addActionListener(e -> insertNodes());
        deleteButton.addActionListener(e -> deleteNode());
        clearButton.addActionListener(e -> clearTrees());
        printButton.addActionListener(e -> printTraversals());

        predecessorCheckBox.addActionListener(e -> {
            boolean usePred = predecessorCheckBox.isSelected();
            if (rbTree instanceof RedBlackTree) {
                ((RedBlackTree<Integer>) rbTree).setUsePredecessorForDelete(usePred);
            }
            if (avlTree instanceof AVLTree) {
                ((AVLTree<Integer>) avlTree).setUsePredecessorForDelete(usePred);
            }
        });

        runSequenceButton.addActionListener(e -> {
            String sel = (String) sequencesCombo.getSelectedItem();
            if (sel != null) {
                runSequence(sel);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    // Inner class for displaying a tree along with a selection menu 😊
    private class TreeDisplayPanel extends JPanel {
        private TreePanel<Integer> treePanel;
        private JComboBox<String> treeSelector;

        public TreeDisplayPanel(TreeInterface<Integer> initialTree) {
            setLayout(new BorderLayout());
            treePanel = createTreePanel(initialTree);
            add(treePanel, BorderLayout.CENTER);
            treeSelector = new JComboBox<>(new String[] {"BST", "AVL", "RB"});
            treeSelector.setSelectedItem(getTreeName(initialTree));
            treeSelector.addActionListener(e -> updateTreePanel((String) treeSelector.getSelectedItem()));
            add(treeSelector, BorderLayout.SOUTH);
        }

        private void updateTreePanel(String treeType) {
            TreeInterface<Integer> selectedTree = getTreeByType(treeType);
            remove(treePanel);
            treePanel = createTreePanel(selectedTree);
            add(treePanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }

        public void refreshPanel() {
            String selected = (String) treeSelector.getSelectedItem();
            updateTreePanel(selected);
        }
    }

    // Helper: Create a TreePanel with special settings for RB trees.
    private TreePanel<Integer> createTreePanel(TreeInterface<Integer> tree) {
        if (tree instanceof RedBlackTree) {
            return new TreePanel<>(tree,
                    node -> {
                        if (node instanceof RBNode) {
                            RBNode<Integer> rbNode = (RBNode<Integer>) node;
                            return rbNode.isRed() ? Color.RED : Color.BLACK;
                        }
                        return Color.GRAY;
                    },
                    node -> Color.WHITE);
        } else {
            return new TreePanel<>(tree);
        }
    }

    // Helper: Return tree type name based on the instance.
    private String getTreeName(TreeInterface<Integer> tree) {
        if (tree instanceof SimpleBST) {
            return "BST";
        } else if (tree instanceof AVLTree) {
            return "AVL";
        } else if (tree instanceof RedBlackTree) {
            return "RB";
        }
        return "RB"; // default
    }

    // Helper: Return the tree instance corresponding to a given type.
    private TreeInterface<Integer> getTreeByType(String type) {
        switch (type) {
            case "BST": return bstTree;
            case "AVL": return avlTree;
            case "RB": return rbTree;
            default: return rbTree;
        }
    }

    private void refreshTrees() {
        // Refresh both display panels to reflect updates
        leftDisplayPanel.refreshPanel();
        rightDisplayPanel.refreshPanel();
    }

    private void insertNodes() {
        String text = nodesTextField.getText();
        if (text == null || text.trim().isEmpty()) return;
        String[] parts = text.split(",");
        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                if (part.toLowerCase().startsWith("del")) {
                    // Delete operation
                    String numStr = part.replaceFirst("(?i)del(ete)?", "").trim();
                    try {
                        int value = Integer.parseInt(numStr);
                        bstTree.delete(value);
                        avlTree.delete(value);
                        rbTree.delete(value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid delete number: " + part);
                    }
                } else {
                    try {
                        int value = Integer.parseInt(part);
                        bstTree.insert(value);
                        avlTree.insert(value);
                        rbTree.insert(value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid number: " + part);
                    }
                }
            }
        }
        refreshTrees();
    }

    private void deleteNode() {
        String input = deleteTextField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                bstTree.delete(value);
                avlTree.delete(value);
                rbTree.delete(value);
                refreshTrees();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number: " + input);
            }
        }
    }

    private void clearTrees() {
        bstTree = new SimpleBST<>();
        avlTree = new AVLTree<>();
        rbTree = new RedBlackTree<>();
        refreshTrees();
    }

    private void printTraversals() {
        System.out.println("=== BST Traversals ===");
        List<Integer> bstInOrder = new ArrayList<>();
        collectInOrder(bstTree.getRoot(), bstInOrder);
        System.out.println("BST Inorder: " + String.join(", ", bstInOrder.stream().map(Object::toString).toArray(String[]::new)));
        List<Integer> bstPreOrder = new ArrayList<>();
        collectPreOrder(bstTree.getRoot(), bstPreOrder);
        System.out.println("BST Preorder: " + String.join(", ", bstPreOrder.stream().map(Object::toString).toArray(String[]::new)));

        System.out.println("=== AVL Tree Traversals ===");
        List<Integer> avlInOrder = new ArrayList<>();
        collectInOrder(avlTree.getRoot(), avlInOrder);
        System.out.println("AVL Inorder: " + String.join(", ", avlInOrder.stream().map(Object::toString).toArray(String[]::new)));
        List<Integer> avlPreOrder = new ArrayList<>();
        collectPreOrder(avlTree.getRoot(), avlPreOrder);
        System.out.println("AVL Preorder: " + String.join(", ", avlPreOrder.stream().map(Object::toString).toArray(String[]::new)));

        System.out.println("=== Red-Black Tree Traversals ===");
        List<Integer> rbInOrder = new ArrayList<>();
        collectInOrder(rbTree.getRoot(), rbInOrder);
        System.out.println("RB Inorder: " + String.join(", ", rbInOrder.stream().map(Object::toString).toArray(String[]::new)));
        List<Integer> rbPreOrder = new ArrayList<>();
        collectPreOrder(rbTree.getRoot(), rbPreOrder);
        System.out.println("RB Preorder: " + String.join(", ", rbPreOrder.stream().map(Object::toString).toArray(String[]::new)));

        List<Integer> redNodes = new ArrayList<>();
        collectRedNodes(rbTree.getRoot(), redNodes);
        System.out.println("Red Nodes (inorder): " + redNodes);
    }

    private void collectPreOrder(TreeNode<Integer> node, List<Integer> list) {
        if (node == null) return;
        if (node instanceof RBNode && ((RBNode<Integer>) node).isNil()) return;
        list.add(node.data);
        collectPreOrder(node.left, list);
        collectPreOrder(node.right, list);
    }

    private void collectInOrder(TreeNode<Integer> node, List<Integer> list) {
        if (node == null) return;
        if (node instanceof RBNode && ((RBNode<Integer>) node).isNil()) return;
        collectInOrder(node.left, list);
        list.add(node.data);
        collectInOrder(node.right, list);
    }

    private void collectRedNodes(TreeNode<Integer> node, List<Integer> redNodes) {
        if (node == null) return;
        if (node instanceof RBNode && ((RBNode<Integer>) node).isNil()) return;
        if (node instanceof RBNode && ((RBNode<Integer>) node).isRed()) {
            redNodes.add(node.data);
        }
        collectRedNodes(node.left, redNodes);
        collectRedNodes(node.right, redNodes);
    }

    private void runSequence(String seqName) {
        String[] steps = SEQUENCES.get(seqName);
        if (steps == null) return;
        for (String step : steps) {
            step = step.trim();
            if (step.toLowerCase().startsWith("delete")) {
                String[] parts = step.split("\\s+");
                if (parts.length == 2) {
                    int val = Integer.parseInt(parts[1]);
                    bstTree.delete(val);
                    avlTree.delete(val);
                    rbTree.delete(val);
                }
            } else {
                int val = Integer.parseInt(step);
                bstTree.insert(val);
                avlTree.insert(val);
                rbTree.insert(val);
            }
        }
        refreshTrees();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DemoGUI().setVisible(true));
    }
}