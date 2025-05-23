package trees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RedBlackTreeTest {
    private RedBlackTree<Integer> rbt;

    @BeforeEach
    public void setUp() {
        rbt = new RedBlackTree<>();
    }

    @Test
    @DisplayName("Insert duplicate element should return false")
    void testInsertDuplicate() {
        assertTrue(rbt.insert(10));
        assertFalse(rbt.insert(10));
        assertTrue(rbt.search(10));
    }

    @Test
    @DisplayName("Insert multiple elements")
    void testInsertMultiple() {
        int[] values = {10, 5, 15, 3, 7, 12, 18};
        for (int value : values)
            assertTrue(rbt.insert(value));

        for (int value : values)
            assertTrue(rbt.search(value));

        for (int value : values)
            assertFalse(rbt.insert(value));
    }

    @Test
    @DisplayName("Search empty tree should return false")
    void testSearchEmpty() {
        assertFalse(rbt.search(10));
    }

    @Test
    @DisplayName("search non-existing element")
    void testSearchNonExisting() {
        int[] insertValues = {10, 20, 30};
        int[] searchValues = {40, 50, 60};

        for (int value : insertValues)
            assertTrue(rbt.insert(value));

        for (int value : searchValues)
            assertFalse(rbt.search(value));
    }

    private boolean isRed(Object node) {
        if (node == null) return false;
        try {
            java.lang.reflect.Method isRedMethod = node.getClass().getMethod("isRed");
            return (boolean) isRedMethod.invoke(node);
        } catch (Exception e) {
            throw new RuntimeException("Could not check if node is red", e);
        }
    }

    private Object[] getChildren(Object node) {
        if (node == null) return null;
        try {
            java.lang.reflect.Field childField = node.getClass().getDeclaredField("child");
            childField.setAccessible(true);
            return (Object[]) childField.get(node);
        } catch (Exception e) {
            throw new RuntimeException("Could not get children", e);
        }
    }

    @SuppressWarnings("unchecked")
    private RedBlackTree.Node<Integer> getRoot() {
        try {
            java.lang.reflect.Field rootField = rbt.getClass().getDeclaredField("root");
            rootField.setAccessible(true);
            return (RedBlackTree.Node<Integer>) rootField.get(rbt);
        } catch (Exception e) {
            throw new RuntimeException("Could not get root", e);
        }
    }

    private void assertNoRedRedViolations(Object node) {
        if (node == null) return;
        Object[] children = getChildren(node);
        if (children[0] != null) {
            assertFalse(isRed(children[0]) && isRed(node));
            assertNoRedRedViolations(children[0]);
        }
        if (children[1] != null) {
            assertFalse(isRed(children[1]) && isRed(node));
            assertNoRedRedViolations(children[1]);
        }
    }

    @Test
    void testGetSize() {
        assertEquals(0, rbt.getSize());
        rbt.insert(10);
        assertEquals(1, rbt.getSize());

        int[] values = {5, 15, 3, 7, 12, 18};
        for (int value : values)
            rbt.insert(value);
        assertEquals(7, rbt.getSize());

        rbt.insert(10);
        assertEquals(7, rbt.getSize());
    }

    @Test
    void testGetHeight() {
        assertEquals(-1, rbt.getHeight());
        rbt.insert(10);
        assertEquals(0, rbt.getHeight());

        rbt.insert(5);
        rbt.insert(15);
        assertEquals(1, rbt.getHeight());

        int[] values = {3, 7, 12, 18, 1, 4, 6, 8};
        for (int value : values)
            rbt.insert(value);
        int size = rbt.getSize();
        int height = rbt.getHeight();
        assertTrue(height <= 2 * Math.log(size + 1) / Math.log(2));
    }

    @Test
    void testDeleteEmpty() {
        assertFalse(rbt.delete(10));
    }

    @Test
    void testDeleteNonExisting() {
        rbt.insert(5);
        rbt.insert(10);
        rbt.insert(15);
        assertFalse(rbt.delete(20));
        assertEquals(3, rbt.getSize());
    }

    @Test
    void testDeleteSingleRoot() {
        rbt.insert(10);
        assertTrue(rbt.delete(10));
        assertFalse(rbt.search(10));
        assertEquals(0, rbt.getSize());
        assertEquals(-1, rbt.getSize());
    }

    @Test
    void testDeleteAllElements() {
        int[] values = {40, 50, 25, 75, 10, 30, 60, 80, 5, 15, 32, 27, 44, 35};
        for (int value : values)
            rbt.insert(value);
        assertEquals(values.length, rbt.getSize());
        for (int value : values) {
            assertTrue(rbt.delete(value));
            assertFalse(rbt.search(value));
            if (rbt.getSize() > 0) {
                assertNoRedRedViolations(getRoot());
                assertFalse(isRed(getRoot()), "Root should remain black");
            }
        }
        assertEquals(0, rbt.getSize());
        assertEquals(-1, rbt.getHeight());
    }
}
