package trees;

public class AVLTree<T extends Comparable<T>> implements ISelfBalancingTree<T> {
  private class Node {
    T key;
    int height;
    Node left, right;

    public Node(T key) {
      this.key = key;
      this.height = 1;
      this.left = null;
      this.right = null;
    }
  }

  private Node root;
  private int size;

  public AVLTree() {
    this.root = null;
    this.size = 0;
  }

  private int height(Node node) {
    return node == null ? 0 : node.height;
  }

  private void updateHeight(Node node) {
    node.height = 1 + Math.max(height(node.left), height(node.right));
  }

  private int getBalance(Node node) {
    return node == null ? 0 : height(node.left) - height(node.right);
  }

  private Node leftRotate(Node node) {
    Node R = node.right;
    Node temp = R.left;
    R.left = node;
    node.right = temp;
    updateHeight(node);
    updateHeight(R);
    return R;
  }

  private Node rightRotate(Node node) {
    Node L = node.left;
    Node temp = L.right;
    L.right = node;
    node.left = temp;
    updateHeight(node);
    updateHeight(L);
    return L;
  }

  private Node getMinNode(Node node) {
    Node currNode = node;
    while (currNode.left != null) {
      currNode = currNode.left;
    }
    return currNode;
  }

  @Override
  public boolean insert(T key) {
    if (search(key)) {
      return false;
    }
    root = insert(root, key);
    size++;
    return true;
  }

  public Node insert(Node currNode, T key) {
    // Normal BST insertion
    if (currNode == null) {
      return new Node(key);
    }

    int cmp = key.compareTo(currNode.key);
    if (cmp < 0) {
      currNode.left = insert(currNode.left, key);
    } else {
      currNode.right = insert(currNode.right, key);
    }

    updateHeight(currNode);
    int balance = getBalance(currNode);

    // Left Left Case
    if (balance > 1 && key.compareTo(currNode.left.key) < 0) {
      return rightRotate(currNode);
    }
    // Right Right Case
    if (balance < -1 && key.compareTo(currNode.right.key) > 0) {
      return leftRotate(currNode);
    }
    // Left Right Case
    if (balance > 1 && key.compareTo(currNode.left.key) > 0) {
      currNode.left = leftRotate(currNode.left);
      return rightRotate(currNode);
    }
    // Right Left Case
    if (balance < -1 && key.compareTo(currNode.right.key) < 0) {
      currNode.right = rightRotate(currNode.right);
      return leftRotate(currNode);
    }

    return currNode;
  }

  @Override
  public boolean delete(T key) {
    if (!search(key)) {
      return false;
    }
    root = delete(root, key);
    size--;
    return true;
  }

  private Node delete(Node currNode, T key) {
    if (currNode == null) {
      return currNode;
    }
    int cmp = key.compareTo(currNode.key);
    if (cmp < 0) {
      currNode.left = delete(currNode.left, key);
    } else if (cmp > 0) {
      currNode.right = delete(currNode.right, key);
    } else {
      if (currNode.left == null) {
        Node temp = currNode.right;
        currNode = null;
        return temp;
      } else if (currNode.right == null) {
        Node temp = currNode.left;
        currNode = null;
        return temp;
      } else {
        Node temp = getMinNode(currNode.right);
        currNode.key = temp.key;
        currNode.right = delete(currNode.right, temp.key);
      }
    }

    updateHeight(currNode);
    int balance = getBalance(currNode);

    // Left Left Case
    if (balance > 1 && getBalance(currNode.left) >= 0) {
      return rightRotate(currNode);
    }

    // Right Right Case
    if (balance < -1 && getBalance(currNode.right) <= 0) {
      return leftRotate(currNode);
    }

    // Left Right Case
    if (balance > 1 && getBalance(currNode.left) < 0) {
      currNode.left = leftRotate(currNode.left);
      return rightRotate(currNode);
    }

    // Right Left Case
    if (balance < -1 && getBalance(currNode.right) > 0) {
      currNode.right = rightRotate(currNode.right);
      return leftRotate(currNode);
    }

    return currNode;
  }

  @Override
  public boolean search(T key) {
    Node curr = root;
    while (curr != null) {
      int cmp = key.compareTo(curr.key);
      if (cmp == 0) {
        return true;
      }
      curr = cmp < 0 ? curr.left : curr.right;
    }
    return false;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getHeight() {
    return height(root);
  }

}
