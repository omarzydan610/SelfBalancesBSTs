package trees;

public class RedBlackTree<T extends Comparable<T>> implements ISelfBalancingTree<T> {

    static class Node {
        int data;
        boolean color;

        // 0 for left child
        // 1 for right child
        Node[] child = new Node[2];

        public Node(int data, boolean color) {
            this.data = data;
            this.color = color;
            this.child[0] = null;
            this.child[1] = null;
        }
    }

    public RedBlackTree() {

    }

    @Override
    public boolean insert(T key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public boolean delete(T key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public boolean search(T key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'search'");
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
