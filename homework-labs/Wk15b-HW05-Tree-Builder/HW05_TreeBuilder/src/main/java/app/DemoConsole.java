package app;

import datastructures.RedBlackTree;
import datastructures.SimpleBST;
import java.util.*;
import java.lang.reflect.*;

public class DemoConsole {

    // Defaults: tree type = bst, data type = int.
    private static String currentTreeType = "bst";   // Options: bst, avl, rb
    private static String currentDataType = "int";     // Options: int, double, string
    private static Object currentTree = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        currentTree = createTree(currentTreeType, currentDataType);
        System.out.println("DemoConsole started. Type 'help' for commands.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null) break;
            line = line.trim();
            if (line.equalsIgnoreCase("exit")) break;
            if (line.isEmpty()) continue;

            String[] tokens = line.split("\\s+", 2);
            String command = tokens[0].toLowerCase();
            String argument = tokens.length > 1 ? tokens[1].trim() : "";

            switch (command) {
                case "help":
                    printHelp();
                    break;
                case "tree":
                    // Usage: tree <bst, avl, rb>
                    if (!argument.isEmpty()) {
                        currentTreeType = argument.toLowerCase();
                        currentTree = createTree(currentTreeType, currentDataType);
                        System.out.println("Tree type set to " + currentTreeType);
                    } else {
                        System.out.println("Usage: tree <bst, avl, rb>");
                    }
                    break;
                case "type":
                    // Usage: type <int, double, string>
                    if (!argument.isEmpty()) {
                        currentDataType = argument.toLowerCase();
                        currentTree = createTree(currentTreeType, currentDataType);
                        System.out.println("Data type set to " + currentDataType);
                    } else {
                        System.out.println("Usage: type <int, double, string>");
                    }
                    break;
                case "build":
                    // Build a new tree from comma-separated list (insert & delete commands)
                    currentTree = createTree(currentTreeType, currentDataType);
                    processListCommand(argument);
                    System.out.println("Tree built from list.");
                    break;
                case "insert":
                    // Insert (or delete using 'del') commands from comma-separated list
                    processListCommand(argument);
                    System.out.println("Insert commands processed.");
                    break;
                case "delete":
                    // Delete a single value
                    processSingleDelete(argument);
                    System.out.println("Delete command processed.");
                    break;
                case "print":
                case "list":
                    processPrint();
                    break;
                case "status":
                    processStatus();
                    break;
                case "clear":
                    currentTree = createTree(currentTreeType, currentDataType);
                    System.out.println("Tree cleared.");
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for list of commands.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Exiting DemoConsole.");
    }

    private static void printHelp() {
        System.out.println("Commands:");
        System.out.println("  tree <bst, avl, rb>            - Set the tree type (default: bst)");
        System.out.println("  type <int, double, string>     - Set the data type (default: int)");
        System.out.println("  build <list>                   - Build tree from comma separated list (supports 'del' for deletion)");
        System.out.println("  insert <list>                  - Insert (or delete using 'del') from comma separated list");
        System.out.println("  delete <value>                 - Delete a single value");
        System.out.println("  print                          - Print tree traversals (inorder, preorder, postorder, red nodes if RB)");
        System.out.println("  status                         - Show current tree status (tree type, data type, and content)");
        System.out.println("  clear                          - Clear the tree");
        System.out.println("  exit                           - Exit the console");
    }

    // Create a new tree instance based on current tree and data type.
    private static Object createTree(String treeType, String dataType) {
        try {
            if (dataType.equals("int")) {
                if (treeType.equals("rb")) {
                    return new RedBlackTree<Integer>();
                } else {
                    return new SimpleBST<Integer>();
                }
            } else if (dataType.equals("double")) {
                if (treeType.equals("rb")) {
                    return new RedBlackTree<Double>();
                } else {
                    return new SimpleBST<Double>();
                }
            } else if (dataType.equals("string")) {
                if (treeType.equals("rb")) {
                    return new RedBlackTree<String>();
                } else {
                    return new SimpleBST<String>();
                }
            } else {
                System.out.println("Unknown data type. Defaulting to int.");
                if (treeType.equals("rb")) {
                    return new RedBlackTree<Integer>();
                } else {
                    return new SimpleBST<Integer>();
                }
            }
        } catch (Exception e) {
            System.out.println("Error creating tree: " + e.getMessage());
            return null;
        }
    }

    // Process a comma-separated list of values. Supports deletion if token starts with 'del' or 'delete'.
    private static void processListCommand(String argument) {
        if (argument.isEmpty()) {
            System.out.println("No values provided.");
            return;
        }
        String[] parts = argument.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            boolean isDelete = false;
            if (part.toLowerCase().startsWith("del")) {
                isDelete = true;
                part = part.replaceFirst("(?i)del(ete)?", "").trim();
            }
            try {
                if (currentDataType.equals("int")) {
                    Integer value = Integer.parseInt(part);
                    if (isDelete) {
                        callDelete(currentTree, value);
                    } else {
                        callInsert(currentTree, value);
                    }
                } else if (currentDataType.equals("double")) {
                    Double value = Double.parseDouble(part);
                    if (isDelete) {
                        callDelete(currentTree, value);
                    } else {
                        callInsert(currentTree, value);
                    }
                } else if (currentDataType.equals("string")) {
                    String value = part;
                    if (isDelete) {
                        callDelete(currentTree, value);
                    } else {
                        callInsert(currentTree, value);
                    }
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid value: " + part);
            }
        }
    }

    // Process a single delete command.
    private static void processSingleDelete(String argument) {
        if (argument.isEmpty()) {
            System.out.println("No value provided for deletion.");
            return;
        }
        argument = argument.trim();
        try {
            if (currentDataType.equals("int")) {
                Integer value = Integer.parseInt(argument);
                callDelete(currentTree, value);
            } else if (currentDataType.equals("double")) {
                Double value = Double.parseDouble(argument);
                callDelete(currentTree, value);
            } else if (currentDataType.equals("string")) {
                callDelete(currentTree, argument);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid value: " + argument);
        }
    }

    private static void processPrint() {
        String treeName = currentTree.toString();
        if (currentTree instanceof datastructures.TreeInterface) {
            utils.TreePrinter.printTree((datastructures.TreeInterface<?>) currentTree, treeName);
        } else {
            System.out.println("Current tree does not implement TreeInterface.");
        }
    }

    // New method to print the status of the current tree.
    private static void processStatus() {
        System.out.println("=== Current Tree Status ===");
        System.out.println("Tree Type: " + currentTreeType);
        System.out.println("Data Type: " + currentDataType);
        System.out.println("Tree Identity: " + currentTree.toString());
        if (currentTree instanceof datastructures.TreeInterface) {
            String content = utils.TreeStringBuilder.build(((datastructures.TreeInterface<?>) currentTree).getRoot());
            System.out.println("Tree Content (structure): " + content);
        } else {
            System.out.println("Tree Content: N/A");
        }
    }

    // Helper: call insert on the tree
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void callInsert(Object tree, T value) {
        if (tree instanceof RedBlackTree) {
            ((RedBlackTree<T>) tree).insert(value);
        } else if (tree instanceof SimpleBST) {
            ((SimpleBST<T>) tree).insert(value);
        }
    }

    // Helper: call delete on the tree
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void callDelete(Object tree, T value) {
        if (tree instanceof RedBlackTree) {
            ((RedBlackTree<T>) tree).delete(value);
        } else if (tree instanceof SimpleBST) {
            ((SimpleBST<T>) tree).delete(value);
        }
    }
}