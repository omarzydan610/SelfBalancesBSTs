package trees;

public interface ISelfBalancingTree<T extends Comparable<T>> {
  boolean insert(T key);

  boolean delete(T key);

  boolean search(T key);

  int getSize();

  int getHeight();

}
