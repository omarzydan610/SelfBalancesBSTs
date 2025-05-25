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
            // Case 1: both children are red => any one child has 2 reds in a row (LL LR RL RR) => flip colors
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
     * @param node the node to fix
     * @param dir the direction (left or right) from which a black node was removed
     * @param okRef reference to boolean indicating if tree is balanced
     * @return the potentially new root of the subtree
     */
    private Node<T> deleteFixUp(Node<T> node, int dir, boolean[] okRef) {
        if (node == null) {
            // Empty tree case
            okRef[0] = true;
            return null;
        }

        Node<T> sibling = node.child[1 - dir];

        // Case 1: Red Sibling
        // Convert to black sibling case via rotation
        if (isRed(sibling)) {
            node = rotate(node, dir);
            node.child[dir].setColor(MagicNumbers.BLACK);
            sibling = node.child[dir].child[1 - dir]; // Update sibling after rotation
        }

        // At this point, sibling must be black or null
        if (sibling == null) {
            // No sibling to share black height - propagate the problem upwards
            // The parent will need to be fixed in the next recursive call
            return node;
        }

        // Case 2: Black Sibling with at least one red child
        if (isRed(sibling.child[MagicNumbers.LEFT]) || isRed(sibling.child[MagicNumbers.RIGHT])) {
            // Restructure to borrow a black node

            // Case 2a: Black sibling with red child on the outside
            if (isRed(sibling.child[1 - dir])) {
                // Single rotation
                Node<T> newRoot = rotate(node, dir);

                // Fix colors to preserve black height
                newRoot.setColor(node.color);
                node.setColor(MagicNumbers.BLACK);
                sibling.child[1 - dir].setColor(MagicNumbers.BLACK);

                okRef[0] = true; // Problem fixed
                return newRoot;
            }
            // Case 2b: Black sibling with red child on the inside
            else {
                // Double rotation (align and rotate)
                node.child[1 - dir] = rotate(sibling, 1 - dir);
                Node<T> newRoot = rotate(node, dir);

                // Fix colors to preserve black height
                newRoot.setColor(node.color);
                node.setColor(MagicNumbers.BLACK);
                newRoot.child[1 - dir].setColor(MagicNumbers.BLACK);

                okRef[0] = true; // Problem fixed
                return newRoot;
            }
        }

        // Case 3: Black sibling with two black children

        // Recolor the sibling to red
        sibling.setColor(MagicNumbers.RED);

        if (isRed(node)) {
            // Case 3a: If parent is red, we can color it black and be done
            node.setColor(MagicNumbers.BLACK);
            okRef[0] = true; // Problem fixed
        }
        // Case 3b: If parent is black, we've reduced its black height
        // The black deficit problem moves up one level
        // This will be handled in the next recursive call

        return node;
    }

    /**
     * Helper method for deleting a node
     *
     * @param node the current node in the recursive deletion
     * @param key the key to delete
     * @param okRef reference to boolean indicating if tree is balanced
     * @return the potentially new root of the subtree
     */
    private Node<T> deleteNode(Node<T> node, T key, boolean[] okRef) {
        if (node == null) {
            okRef[0] = true;
            return null;
        }

        // Found the delete key
        if (key.compareTo(node.getData()) == 0) {
            // Case 1: Node has at most one child
            if (node.child[MagicNumbers.LEFT] == null || node.child[MagicNumbers.RIGHT] == null) {
                // Determine which child exists, if any
                int childDir = (node.child[MagicNumbers.LEFT] == null) ? MagicNumbers.RIGHT : MagicNumbers.LEFT;
                Node<T> child = node.child[childDir];

                // Case 1a: Red node with no children - just remove it
                if (isRed(node) && child == null) {
                    okRef[0] = true;
                    return null;
                }

                // Case 1b: Black node with red child - replace and recolor
                if (!isRed(node) && isRed(child)) {
                    child.setColor(MagicNumbers.BLACK);
                    okRef[0] = true;
                    return child;
                }

                // Case 1c: Black node with no children or black child
                // This creates a black height violation that will need fixing
                okRef[0] = false;
                return child; // might be null
            }
            // Case 2: Node has two children
            else {
                // Find and use inorder predecessor (maximum in left subtree)
                Node<T> predecessor = findMax(node.child[MagicNumbers.LEFT]);

                // Copy the predecessor's data
                T predecessorData = predecessor.getData();

                // Recursively delete the predecessor
                node.child[MagicNumbers.LEFT] = deleteNode(node.child[MagicNumbers.LEFT], predecessorData, okRef);
                
                // Update current node's data
                node.setData(predecessorData);

                // If deletion caused a black height violation, fix it
                if (!okRef[0]) {
                    node = deleteFixUp(node, MagicNumbers.LEFT, okRef);
                }

                return node;
            }
        }
        // Key not found at current node, continue searching
        else {
            // Determine search direction
            int dir = key.compareTo(node.getData()) > 0 ? MagicNumbers.RIGHT : MagicNumbers.LEFT;
            
            // Recursive delete in appropriate subtree
            node.child[dir] = deleteNode(node.child[dir], key, okRef);
            
            // Fix up if necessary
            if (!okRef[0]) {
                node = deleteFixUp(node, dir, okRef);
            }

            return node;
        }
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
        
        boolean[] ok = {false};
        root = deleteNode(root, key, ok);
        if (root != null) {
            root.setColor(MagicNumbers.BLACK);
        }
        size--;
        return true;
    }

    private Node<T> search(Node<T> node, T key) {
        if (node == null || key.compareTo(node.getData()) == 0)
            return node;
        int direction = key.compareTo(node.getData()) < 0 ? MagicNumbers.LEFT : MagicNumbers.RIGHT;
        return search(node.child[direction], key);
    }

    @Override
    public boolean search(T key) {
        return search(root, key) != null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int getHeight(Node<T> node) {
        if (node == null) {
            return -1; // Empty tree has height -1
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
