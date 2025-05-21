package trees;

public interface ISelfBalancingTree {
  boolean insert(Object key);

  boolean delete(Object key);

  boolean search(Object key);

  int getSize();

  int getHeight();

}
