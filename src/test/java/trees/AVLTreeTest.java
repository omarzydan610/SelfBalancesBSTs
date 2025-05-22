package trees;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AVLTreeTest {

  private AVLTree<Integer> avlTree;

  @BeforeEach
  public void setUp() {
    avlTree = new AVLTree<>();
  }

  @Test
  public void testEmptyTree() {
    assertEquals(0, avlTree.getSize());
    assertEquals(0, avlTree.getHeight());
    assertFalse(avlTree.search(10));
    assertFalse(avlTree.delete(10));
  }

  @Test
  public void testInsert() {
    assertTrue(avlTree.insert(10));
    assertEquals(1, avlTree.getSize());
    assertEquals(1, avlTree.getHeight());
    assertTrue(avlTree.search(10));

    assertTrue(avlTree.insert(20));
    assertEquals(2, avlTree.getSize());
    assertEquals(2, avlTree.getHeight());

    assertTrue(avlTree.insert(30));
    assertEquals(3, avlTree.getSize());
    assertEquals(2, avlTree.getHeight());

    assertFalse(avlTree.insert(10));
    assertEquals(3, avlTree.getSize());
  }

  @Test
  public void testDelete() {
    avlTree.insert(10);
    avlTree.insert(20);
    avlTree.insert(30);

    assertTrue(avlTree.delete(20));
    assertEquals(2, avlTree.getSize());
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(30));
    assertFalse(avlTree.search(20));

    assertFalse(avlTree.delete(40));
    assertEquals(2, avlTree.getSize());

    assertTrue(avlTree.delete(10));
    assertEquals(1, avlTree.getSize());
    assertEquals(1, avlTree.getHeight());

    assertTrue(avlTree.delete(30));
    assertEquals(0, avlTree.getSize());
    assertEquals(0, avlTree.getHeight());
  }

  @Test
  public void testLeftRotation() {
    avlTree.insert(10);
    avlTree.insert(20);
    avlTree.insert(30);

    assertEquals(2, avlTree.getHeight());
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(20));
    assertTrue(avlTree.search(30));
  }

  @Test
  public void testRightRotation() {
    avlTree.insert(30);
    avlTree.insert(20);
    avlTree.insert(10);

    assertEquals(2, avlTree.getHeight());
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(20));
    assertTrue(avlTree.search(30));
  }

  @Test
  public void testLeftRightRotation() {
    avlTree.insert(30);
    avlTree.insert(10);
    avlTree.insert(20);

    assertEquals(2, avlTree.getHeight());
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(20));
    assertTrue(avlTree.search(30));
  }

  @Test
  public void testRightLeftRotation() {
    avlTree.insert(10);
    avlTree.insert(30);
    avlTree.insert(20);

    assertEquals(2, avlTree.getHeight());
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(20));
    assertTrue(avlTree.search(30));
  }

  @Test
  public void testComplexInsertions() {
    int[] values = { 50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 55, 1 };

    for (int val : values) {
      avlTree.insert(val);
    }

    assertEquals(12, avlTree.getSize());

    int maxHeightForBalancedTree = (int) Math.ceil(1.44 * Math.log(12 + 2) / Math.log(2)) - 1;
    assertTrue(avlTree.getHeight() <= maxHeightForBalancedTree);

    for (int val : values) {
      assertTrue(avlTree.search(val));
    }
  }

  @Test
  public void testComplexDeletions() {
    int[] values = { 50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 55, 1 };

    for (int val : values) {
      avlTree.insert(val);
    }
    assertTrue(avlTree.delete(1));
    assertTrue(avlTree.delete(25));
    assertTrue(avlTree.delete(75));

    assertEquals(9, avlTree.getSize());

    assertFalse(avlTree.search(1));
    assertFalse(avlTree.search(25));
    assertFalse(avlTree.search(75));

    assertTrue(avlTree.search(50));
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(30));

    int maxHeightForBalancedTree = (int) Math.ceil(1.44 * Math.log(9 + 2) / Math.log(2)) - 1;
    assertTrue(avlTree.getHeight() <= maxHeightForBalancedTree);
  }

  @Test
  public void testDeleteRoot() {
    avlTree.insert(20);
    avlTree.insert(10);
    avlTree.insert(30);

    assertTrue(avlTree.delete(20));
    assertEquals(2, avlTree.getSize());
    assertTrue(avlTree.search(10));
    assertTrue(avlTree.search(30));
    assertFalse(avlTree.search(20));
  }
}
