package datastructures;

/**
 * Red-Black Tree implementation compatible with:
 * - TreeNode<T>
 * - RBNode<T> (must provide isRed(), isBlack(), setColor(boolean))
 *
 * This version avoids calling RB methods on TreeNode-typed variables
 * by casting to RBNode<T> where needed.
 */
public class RedBlackTree<T extends Comparable<T>> implements TreeInterface<T> {

    private RBNode<T> root;
    private final RBNode<T> NIL;
    private boolean usePredecessorForDelete = false;

    public RedBlackTree() {
        NIL = new RBNode<>(null);
        NIL.setColor(RBNode.BLACK);
        NIL.nil = true;
        NIL.setLeft(NIL);
        NIL.setRight(NIL);
        NIL.setParent(NIL);
        root = NIL;
    }

    @Override
    public String toString() {
        return "Red-Black Tree";
    }

    public void setUsePredecessorForDelete(boolean usePredecessor) {
        this.usePredecessorForDelete = usePredecessor;
    }

    // -----------------------------
    // INSERT
    // -----------------------------
    @Override
    public void insert(T value) {
        if (value == null) return;

        RBNode<T> z = new RBNode<>(value);
        z.setLeft(NIL);
        z.setRight(NIL);
        z.setParent(NIL);
        z.setColor(RBNode.RED);

        RBNode<T> y = NIL;
        RBNode<T> x = root;

        while (x != NIL) {
            y = x;
            if (z.getData().compareTo(x.getData()) < 0) {
                x = (RBNode<T>) x.getLeft();
            } else {
                x = (RBNode<T>) x.getRight();
            }
        }

        z.setParent(y);
        if (y == NIL) {
            root = z;
        } else if (z.getData().compareTo(y.getData()) < 0) {
            y.setLeft(z);
        } else {
            y.setRight(z);
        }

        insertFixup(z);
    }

    private void insertFixup(RBNode<T> z) {
        while (z.getParent() != NIL && ((RBNode<T>) z.getParent()).isRed()) {
            RBNode<T> parent = (RBNode<T>) z.getParent();
            RBNode<T> grand = (RBNode<T>) parent.getParent();

            if (parent == grand.getLeft()) {
                RBNode<T> uncle = (RBNode<T>) grand.getRight();
                if (uncle != null && uncle.isRed()) {
                    parent.setColor(RBNode.BLACK);
                    uncle.setColor(RBNode.BLACK);
                    grand.setColor(RBNode.RED);
                    z = grand;
                } else {
                    if (z == parent.getRight()) {
                        z = parent;
                        leftRotate(z);
                        parent = (RBNode<T>) z.getParent();
                        grand = (RBNode<T>) parent.getParent();
                    }
                    parent.setColor(RBNode.BLACK);
                    grand.setColor(RBNode.RED);
                    rightRotate(grand);
                }
            } else {
                RBNode<T> uncle = (RBNode<T>) grand.getLeft();
                if (uncle != null && uncle.isRed()) {
                    parent.setColor(RBNode.BLACK);
                    uncle.setColor(RBNode.BLACK);
                    grand.setColor(RBNode.RED);
                    z = grand;
                } else {
                    if (z == parent.getLeft()) {
                        z = parent;
                        rightRotate(z);
                        parent = (RBNode<T>) z.getParent();
                        grand = (RBNode<T>) parent.getParent();
                    }
                    parent.setColor(RBNode.BLACK);
                    grand.setColor(RBNode.RED);
                    leftRotate(grand);
                }
            }
        }
        root.setColor(RBNode.BLACK);
    }

    // -----------------------------
    // ROTATIONS
    // -----------------------------
    private void leftRotate(RBNode<T> x) {
        RBNode<T> y = (RBNode<T>) x.getRight();
        x.setRight(y.getLeft());
        if (y.getLeft() != NIL) {
            y.getLeft().setParent(x);
        }
        y.setParent(x.getParent());
        if (x.getParent() == NIL) {
            root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }
        y.setLeft(x);
        x.setParent(y);
    }

    private void rightRotate(RBNode<T> y) {
        RBNode<T> x = (RBNode<T>) y.getLeft();
        y.setLeft(x.getRight());
        if (x.getRight() != NIL) {
            x.getRight().setParent(y);
        }
        x.setParent(y.getParent());
        if (y.getParent() == NIL) {
            root = x;
        } else if (y == y.getParent().getRight()) {
            y.getParent().setRight(x);
        } else {
            y.getParent().setLeft(x);
        }
        x.setRight(y);
        y.setParent(x);
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    @Override
    public void delete(T value) {
        if (value == null) return;
        RBNode<T> z = search(root, value);
        if (z == NIL) return;
        deleteNode(z);
    }

    private void deleteNode(RBNode<T> z) {
        RBNode<T> y = z; // node to be removed or moved
        boolean yWasBlack = y.isBlack();
        RBNode<T> x; // child that replaces y in the tree

        if (z.getLeft() == NIL) { // no left child
            x = (RBNode<T>) z.getRight();
            transplant(z, (RBNode<T>) z.getRight());
        } else if (z.getRight() == NIL) { // no right child
            x = (RBNode<T>) z.getLeft();
            transplant(z, (RBNode<T>) z.getLeft());
        } else {
            // Node has two children: choose replacement
            RBNode<T> replacement;
            if (usePredecessorForDelete) {
                replacement = maximum((RBNode<T>) z.getLeft());
                x = (RBNode<T>) (replacement.getLeft() != NIL ? replacement.getLeft() : NIL);
            } else {
                replacement = minimum((RBNode<T>) z.getRight());
                x = (RBNode<T>) (replacement.getRight() != NIL ? replacement.getRight() : NIL);
            }

            y = replacement;
            yWasBlack = y.isBlack();

            // Case when replacement is direct child of z
            if (y.getParent() == z) {
                x.setParent(y);
            } else {
                transplant(y, x); // replace y with its child
                if (usePredecessorForDelete) {
                    y.setLeft(z.getLeft()); // move z's left subtree to y
                    if (y.getLeft() != NIL) y.getLeft().setParent(y);
                } else {
                    y.setRight(z.getRight()); // move z's right subtree to y
                    if (y.getRight() != NIL) y.getRight().setParent(y);
                }
            }

            // Transplant y into z's position
            transplant(z, y);

            // Attach remaining children
            if (usePredecessorForDelete) {
                y.setRight(z.getRight());
                if (y.getRight() != NIL) y.getRight().setParent(y);
            } else {
                y.setLeft(z.getLeft());
                if (y.getLeft() != NIL) y.getLeft().setParent(y);
            }

            y.setColor(z.isRed() ? RBNode.RED : RBNode.BLACK);
        }

        if (yWasBlack) {
            deleteFixup(x);
        }
    }


    private void deleteFixup(RBNode<T> x) {
        while (x != root && x.isBlack()) {
            if (x == x.getParent().getLeft()) {
                RBNode<T> w = (RBNode<T>) x.getParent().getRight();
                if (w.isRed()) {
                    w.setColor(RBNode.BLACK);
                    ((RBNode<T>) x.getParent()).setColor(RBNode.RED);
                    leftRotate((RBNode<T>) x.getParent());
                    w = (RBNode<T>) x.getParent().getRight();
                }
                if (((RBNode<T>) w.getLeft()).isBlack() && ((RBNode<T>) w.getRight()).isBlack()) {
                    w.setColor(RBNode.RED);
                    x = (RBNode<T>) x.getParent();
                } else {
                    if (((RBNode<T>) w.getRight()).isBlack()) {
                        ((RBNode<T>) w.getLeft()).setColor(RBNode.BLACK);
                        w.setColor(RBNode.RED);
                        rightRotate(w);
                        w = (RBNode<T>) x.getParent().getRight();
                    }
                    // copy parent's color to w
                    if (((RBNode<T>) x.getParent()).isRed()) w.setColor(RBNode.RED);
                    else w.setColor(RBNode.BLACK);

                    ((RBNode<T>) x.getParent()).setColor(RBNode.BLACK);
                    ((RBNode<T>) w.getRight()).setColor(RBNode.BLACK);
                    leftRotate((RBNode<T>) x.getParent());
                    x = root;
                }
            } else {
                RBNode<T> w = (RBNode<T>) x.getParent().getLeft();
                if (w.isRed()) {
                    w.setColor(RBNode.BLACK);
                    ((RBNode<T>) x.getParent()).setColor(RBNode.RED);
                    rightRotate((RBNode<T>) x.getParent());
                    w = (RBNode<T>) x.getParent().getLeft();
                }
                if (((RBNode<T>) w.getRight()).isBlack() && ((RBNode<T>) w.getLeft()).isBlack()) {
                    w.setColor(RBNode.RED);
                    x = (RBNode<T>) x.getParent();
                } else {
                    if (((RBNode<T>) w.getLeft()).isBlack()) {
                        ((RBNode<T>) w.getRight()).setColor(RBNode.BLACK);
                        w.setColor(RBNode.RED);
                        rotateLeftHelper(w); // local left on w
                        w = (RBNode<T>) x.getParent().getLeft();
                    }
                    if (((RBNode<T>) x.getParent()).isRed()) w.setColor(RBNode.RED);
                    else w.setColor(RBNode.BLACK);

                    ((RBNode<T>) x.getParent()).setColor(RBNode.BLACK);
                    ((RBNode<T>) w.getLeft()).setColor(RBNode.BLACK);
                    rightRotate((RBNode<T>) x.getParent());
                    x = root;
                }
            }
        }
        x.setColor(RBNode.BLACK);
    }

    // Helper for the mirrored left-rotation used inside deleteFixup's else branch.
    // We separate so we don't accidentally call the wrong method name in other places.
    private void rotateLeftHelper(RBNode<T> x) {
        leftRotate(x);
    }

    private void transplant(RBNode<T> u, RBNode<T> v) {
        if (u.getParent() == NIL) {
            root = v;
        } else if (u == u.getParent().getLeft()) {
            u.getParent().setLeft(v);
        } else {
            u.getParent().setRight(v);
        }
        v.setParent(u.getParent());
    }

    // -----------------------------
    // SEARCH / CONTAINS / HELPERS
    // -----------------------------
    @Override
    public boolean contains(T value) {
        if (value == null) return false;
        return search(root, value) != NIL;
    }

    private RBNode<T> search(RBNode<T> node, T value) {
        RBNode<T> cur = node;
        while (cur != NIL) {
            int cmp = value.compareTo(cur.getData());
            if (cmp == 0) return cur;
            cur = cmp < 0 ? (RBNode<T>) cur.getLeft() : (RBNode<T>) cur.getRight();
        }
        return NIL;
    }

    @Override
    public TreeNode<T> getRoot() {
        return root;
    }

    private RBNode<T> minimum(RBNode<T> node) {
        RBNode<T> cur = node;
        while (cur.getLeft() != NIL) cur = (RBNode<T>) cur.getLeft();
        return cur;
    }

    private RBNode<T> maximum(RBNode<T> node) {
        RBNode<T> cur = node;
        while (cur.getRight() != NIL) cur = (RBNode<T>) cur.getRight();
        return cur;
    }
}
