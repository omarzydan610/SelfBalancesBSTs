package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import trees.AVLTree;
import trees.ISelfBalancingTree;
import trees.RedBlackTree;

public class EnglishDictionary implements IDictionary {
    private ISelfBalancingTree tree;

    public EnglishDictionary(String type) {
        if (type.equalsIgnoreCase("AVL"))
            tree = new AVLTree();
        else if (type.equalsIgnoreCase("Red-Black"))
            tree = new RedBlackTree();
        else
            throw new IllegalArgumentException("Unknown tree type: " + type);
    }

    @Override
    public boolean insert(String word) {
        return tree.insert(word);
    }

    @Override
    public boolean delete(String word) {
        return tree.delete(word);
    }

    @Override
    public boolean search(String word) {
        return tree.search(word);
    }

    @Override
    public int[] batchInsert(String filePath) {
        int[] result = new int[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty())
                    if (insert(line))
                        result[0]++;
                    else
                        result[1]++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int[] batchDelete(String filePath) {
        int[] result = new int[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty())
                    if (delete(line))
                        result[0]++;
                    else
                        result[1]++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int getSize() {
        return tree.getSize();
    }

    @Override
    public int getHight() {
        return tree.getHeight();
    }
}
