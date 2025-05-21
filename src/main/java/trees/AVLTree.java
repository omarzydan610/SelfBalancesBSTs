package trees;

public class AVLTree<T extends Comparable<T>> implements ISelfBalancingTree<T> {
  private Node<T> root;
  private int size;

  public AVLTree() {
    this.root = null;
    this.size = 0;
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
