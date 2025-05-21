package trees;

public class Node<T extends Comparable<T>> {
  public T val;
  public int height;
  public Node<T> left, right;

  public Node(T key) {
    this.val = key;
    this.height = 1;
    this.left = null;
    this.right = null;
  }
}
