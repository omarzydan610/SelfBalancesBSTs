package trees;

public class RedBlackTree<T extends Comparable<T>> implements ISelfBalancingBST<T> {

    static class MagicNumbers {
        static final int RED = 0;
        static final int BLACK = 1;
        static final int LEFT = 0;
        static final int RIGHT = 1;
    }

    static class Node<T> {
        // Remove final to allow in-place updates during deletion
        private T data;
        private int color;

        // 0 for left child
        // 1 for right child
        Node<T>[] child = new Node[2];

        public Node(T data, int color) {
            this.data = data;
            this.color = color;
            this.child[0] = null;
            this.child[1] = null;
        }

        public T getData() {
            return data;
        }

        // Add setter for data to support in-place updates
        public void setData(T data) {
            this.data = data;
        }

        public boolean isRed() {
            return color == MagicNumbers.RED;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    private Node<T> root;
    private int size;

    public RedBlackTree() {
        this.root = null;
    }

    private boolean isRed(Node<T> node) {
        return node != null && node.isRed();
    }

    private void colorFlip(Node<T> node) {
        node.setColor(1 - node.color);
        node.child[0].setColor(1 - node.child[0].color);
        node.child[1].setColor(1 - node.child[1].color);
    }

    /**
     * Rotates the subtree rooted at the given node in the specified direction.
     *
     * @param node      the root of the subtree to rotate
     * @param direction 0 for left rotation, 1 for right rotation
     * @return the new root of the rotated subtree
     */
    private Node<T> rotate(Node<T> node, int direction) {
        Node<T> temp = node.child[1 - direction];
        node.child[1 - direction] = temp.child[direction];
        temp.child[direction] = node;

        temp.setColor(node.color);
        node.setColor(MagicNumbers.RED);

        return temp;
    }

    /**
     * Performs a double rotation to align red nodes and then rotate.
     * This is used when we have "LR RL" pattern that needs to be straightened
     * before performing the main rotation.
     *
     * @param node      the root of the subtree to double rotate
     * @param direction 0 for left-right rotation, 1 for right-left rotation
     * @return the new root of the double rotated subtree
     */
    private Node<T> alignRotate(Node<T> node, int direction) {
        node.child[1 - direction] = rotate(node.child[1 - direction], 1 - direction);
        return rotate(node, direction);
    }

    /**
     * @param node      the node to fix up
     * @param direction 0 for left, 1 for right
     * @return the (potentially new) root of the subtree after fix-up
     */
    private Node<T> insertFixUp(Node<T> node, int direction) {
        if (isRed(node.child[direction])) {
            // Case 1: both children are red => any one child has 2 reds in a row (LL LR RL
            // RR) => flip colors
            if (isRed(node.child[1 - direction])) {
                if (isRed(node.child[direction].child[direction]) || isRed(node.child[direction].child[1 - direction]))
                    colorFlip(node);
            } else {
                // Case 2: only one child is red
                if (isRed(node.child[direction].child[direction]))
                    // any one child has 2 reds in a row (LL RR) => rotate
                    node = rotate(node, 1 - direction);
                else if (isRed(node.child[direction].child[1 - direction]))
                    // any one child has 2 reds in a row (LR RL) => align first, then rotate
                    node = alignRotate(node, 1 - direction);
            }
        }
        return node;
    }

    private Node<T> insert(Node<T> node, T key) {
        if (node == null)
            return new Node<>(key, MagicNumbers.RED);
        int direction = key.compareTo(node.getData()) < 0 ? MagicNumbers.LEFT : MagicNumbers.RIGHT;
        node.child[direction] = insert(node.child[direction], key);
        return insertFixUp(node, direction);
    }

    @Override
    public boolean insert(T key) {
        if (search(key))
            return false;
        root = insert(root, key);
        root.setColor(MagicNumbers.BLACK);
        size++;
        return true;
    }

    /**
     * Helper method for restoring Red-Black tree properties after deletion
     *
     * @param node  the node to fix
     * @param dir   the direction (left or right) from which a black node was
     *              removed
     * @param okRef reference to boolean indicating if tree is balanced
     * @return the potentially new root of the subtree
     */
    private Node<T> deleteFixUp(Node<T> node, boolean dir, boolean[] okRef) {
        Node<T> parent = node; // saving for later red sibling fixing case
        Node<T> sibling = node.child[dir ? MagicNumbers.LEFT : MagicNumbers.RIGHT];

        // Red Sibling Case => Reduce to Black Sibling Case
        if (isRed(sibling)) {
            node = rotate(node, dir ? MagicNumbers.RIGHT : MagicNumbers.LEFT);
            sibling = parent.child[dir ? MagicNumbers.LEFT : MagicNumbers.RIGHT];
        }

        if (sibling != null) {
            // Black Sibling Case, Part 1: Black Sibling with only black children
            if (!isRed(sibling.child[MagicNumbers.LEFT]) && !isRed(sibling.child[MagicNumbers.RIGHT])) {
                if (isRed(parent)) okRef[0] = true; // will color it black and sibling subtree will not have imbalance
                parent.setColor(MagicNumbers.BLACK); // if not ok, we proceed further
                sibling.setColor(MagicNumbers.RED);
            } else { // Black Sibling Case, Part 2: Black Sibling with red children
                int initcol_parent = parent.color;
                boolean isRedSiblingReduction = !(node == parent);

                if (isRed(sibling.child[dir ? MagicNumbers.LEFT : MagicNumbers.RIGHT])) { // RR, LL
                    parent = rotate(parent, dir ? MagicNumbers.RIGHT : MagicNumbers.LEFT); // single rotation
                } else {
                    parent = alignRotate(parent, dir ? MagicNumbers.RIGHT : MagicNumbers.LEFT); // align and rotate
                }

                parent.setColor(initcol_parent); // color will be the same as initial parent
                parent.child[MagicNumbers.LEFT].setColor(MagicNumbers.BLACK);
                parent.child[MagicNumbers.RIGHT].setColor(MagicNumbers.BLACK);

                if (isRedSiblingReduction) {
                    node.child[dir ? MagicNumbers.RIGHT : MagicNumbers.LEFT] = parent; // fixing the child for proper bottom up fixing later
                } else {
                    node = parent; // usual black case
                }
                okRef[0] = true;
            }
        }
        return node;
    }

    /**
     * Helper method for deleting a node
     *
     * @param node  the current node in the recursive deletion
     * @param key   the key to delete
     * @param okRef reference to boolean indicating if tree is balanced
     * @return the potentially new root of the subtree
     */
    private Node<T> deleteNode(Node<T> node, T key, boolean[] okRef) {
        if (node == null) {
            okRef[0] = true;
            return null;
        }

        // found the delete key
        if (node.getData().compareTo(key) == 0) {
            // has one or less child
            if (node.child[MagicNumbers.LEFT] == null || node.child[MagicNumbers.RIGHT] == null) {
                Node<T> temp = null;
                if (node.child[MagicNumbers.LEFT] != null) temp = node.child[MagicNumbers.LEFT];
                if (node.child[MagicNumbers.RIGHT] != null) temp = node.child[MagicNumbers.RIGHT];

                if (isRed(node)) { // the node is red => just delete it
                    okRef[0] = true;
                } else if (isRed(temp)) { // only child is red => replace with that red child and recolor black
                    temp.setColor(MagicNumbers.BLACK);
                    okRef[0] = true;
                }

                return temp;
            } else { // has 2 children => replace with inorder predecessor and recurse for that
                Node<T> temp = findMax(node.child[MagicNumbers.LEFT]); // inorder predecessor: maximum value in the left subtree
                node.setData(temp.getData());
                key = temp.getData(); // updating with predecessor data as this is the one to delete now
            }
        }

        boolean dir = key.compareTo(node.getData()) > 0;
        node.child[dir ? MagicNumbers.RIGHT : MagicNumbers.LEFT] = deleteNode(node.child[dir ? MagicNumbers.RIGHT : MagicNumbers.LEFT], key, okRef); // recurse

        return okRef[0] ? node : deleteFixUp(node, dir, okRef);
    }

    private Node<T> findMax(Node<T> node) {
        if (node == null) {
            return null;
        }

        while (node.child[MagicNumbers.RIGHT] != null) {
            node = node.child[MagicNumbers.RIGHT];
        }

        return node;
    }

    @Override
    public boolean delete(T key) {
        if (!search(key)) {
            return false;
        }

        boolean[] ok = { false };
        root = deleteNode(root, key, ok);
        if (root != null) {
            root.setColor(MagicNumbers.BLACK);
        }
        size--;
        return true;
    }


    @Override
    public boolean search(T key) {
        Node<T> curr = root;
        while (curr != null) {
            int cmp = key.compareTo(curr.getData());
            if (cmp == 0) {
                return true;
            }
            curr = cmp < 0 ? curr.child[MagicNumbers.LEFT] : curr.child[MagicNumbers.RIGHT];
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHeight(Node<T> node) {
        if (node == null) {
            return 0; // Empty tree has height 0
        }

        int leftHeight = getHeight(node.child[MagicNumbers.LEFT]);
        int rightHeight = getHeight(node.child[MagicNumbers.RIGHT]);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    @Override
    public int getHeight() {
        return getHeight(root);
    }

}
