package trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PerformanceTest {
    private static final int SMALL_SIZE = 10_000;
    private static final int MEDIUM_SIZE = 50_000;
    private static final int LARGE_SIZE = 100_000;
    
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        System.out.println("===== Tree Performance Comparison =====");
        
        // Run tests with different sizes
         runTest("Small Dataset", SMALL_SIZE);
         runTest("Medium Dataset", MEDIUM_SIZE);
         runTest("Large Dataset", LARGE_SIZE);
    }
    
    private static void runTest(String testName, int size) {
        System.out.println("\n=============== " + testName + " (Size: " + size + ") ==============");
        
        // Generate random numbers for testing
        List<Integer> numbers = generateRandomNumbers(size);
        List<Integer> searchNumbers = getSearchSample(numbers, size / 10);
        List<Integer> deleteNumbers = getSearchSample(numbers, size / 10);
        
        // --------------------- Test AVL Tree ---------------------
        System.out.println("\n-- AVL Tree --");
        AVLTree<Integer> avlTree = new AVLTree<>();
        
        // Test insert
        long startTime = System.currentTimeMillis();
        for (Integer num : numbers) {
            avlTree.insert(num);
        }
        long avlInsertTime = System.currentTimeMillis() - startTime;
        System.out.println("Insert time: " + avlInsertTime + " ms");
        
        // Test search
        startTime = System.currentTimeMillis();
        for (Integer num : searchNumbers) {
            avlTree.search(num);
        }
        long avlSearchTime = System.currentTimeMillis() - startTime;
        System.out.println("Search time: " + avlSearchTime + " ms");
        
        // Test delete
        startTime = System.currentTimeMillis();
        for (Integer num : deleteNumbers) {
            avlTree.delete(num);
        }
        long avlDeleteTime = System.currentTimeMillis() - startTime;
        System.out.println("Delete time: " + avlDeleteTime + " ms");
        System.out.println("Final tree height: " + avlTree.getHeight());
        
        // ------------------- Test Red-Black Tree -------------------
        System.out.println("\n-- Red-Black Tree --");
        RedBlackTree<Integer> rbTree = new RedBlackTree<>();
        
        // Test insert
        startTime = System.currentTimeMillis();
        for (Integer num : numbers) {
            rbTree.insert(num);
        }
        long rbInsertTime = System.currentTimeMillis() - startTime;
        System.out.println("Insert time: " + rbInsertTime + " ms");
        
        // Test search
        startTime = System.currentTimeMillis();
        for (Integer num : searchNumbers) {
            rbTree.search(num);
        }
        long rbSearchTime = System.currentTimeMillis() - startTime;
        System.out.println("Search time: " + rbSearchTime + " ms");
        
        // Test delete
        startTime = System.currentTimeMillis();

        for (Integer num : deleteNumbers) {
            rbTree.delete(num);
        }
        long rbDeleteTime = System.currentTimeMillis() - startTime;
        System.out.println("Delete time: " + rbDeleteTime + " ms");
        System.out.println("Final tree height: " + rbTree.getHeight());

        
        // Compare results
        System.out.println("\n-- Comparison --");
        System.out.println("Insert: AVL " + avlInsertTime + "ms vs RB " + rbInsertTime + "ms, Ratio: " + String.format("%.2f", (double)avlInsertTime / rbInsertTime));
        System.out.println("Search: AVL " + avlSearchTime + "ms vs RB " + rbSearchTime + "ms, Ratio: " + String.format("%.2f", (double)avlSearchTime / rbSearchTime));
        System.out.println("Delete: AVL " + avlDeleteTime + "ms vs RB " + rbDeleteTime + "ms, Ratio: " + String.format("%.2f", (double)avlDeleteTime / rbDeleteTime));
    }
    
    private static List<Integer> generateRandomNumbers(int size) {
        List<Integer> numbers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            numbers.add(random.nextInt(size * 10)); // Range is 10 times the size to allow some duplicates
        }
        return numbers;
    }
    
    private static List<Integer> getSearchSample(List<Integer> numbers, int sampleSize) {
        List<Integer> sample = new ArrayList<>(sampleSize);
        
        // Add some numbers that exist in the list
        for (int i = 0; i < sampleSize / 2; i++) {
            int index = random.nextInt(numbers.size());
            sample.add(numbers.get(index));
        }
        
        // Add some random numbers (may or may not exist)
        for (int i = 0; i < sampleSize / 2; i++) {
            sample.add(random.nextInt(numbers.size() * 10));
        }
        
        return sample;
    }
}
