package trees;

public class RedBlackTree<T extends Comparable<T>> implements ISelfBalancingBST<T> {

    static class MagicNumbers {
        static final int RED = 0;
        static final int BLACK = 1;
        static final int LEFT = 0;
        static final int RIGHT = 1;
    }

    static class Node<T> {
        private final T data;
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

        public boolean isRed() {
            return color == MagicNumbers.RED;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    private Node<T> root;

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
        return true;
    }

    @Override
    public boolean delete(T key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSize'");
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHeight'");
    }

}
