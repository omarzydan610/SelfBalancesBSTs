# this file is just for drafting purposes 

## Red-Black Properties

1. Every node is either red or black.
2. The root is black.
3. There are no two adjacent red nodes.
4. Every path from a node (including root) to any of its descendants NULL nodes has the same number of black nodes.
5. All leaves (NULL nodes) are black.

![img.png](img.png)

## Insertion

### Case 0: Inserting into an empty tree
1. Create a new black node as the root.

### Case 1: Inserting into a non-empty tree
#### Step 0: Insert as normal BST with node color red.
The red property might be violated (if the new node's parent is red).
##### Case 0: parent is black.
1. No violation.

#### Step 1: Fixing Red-Black Properties.
##### Case 1: parent is red.
**There is a violation**
###### Case 1a: uncle is black or NULL.
- Perform rotation on node, parent, and grandparent.

**steps:**
1. arrange node, parent, and grandparent as (A,B,C)
2. make B parent of A and C
3. color B black, A and C red
4. rejoin subtree to the main tree

_the steps above translate to the following in code:_

**subcase 1a.1: node is a right child:**
- perform left rotation on parent

**subcase 1a.2: node is a right child:**
- perform right rotation on grandparent and recolor

###### Case 1b: uncle is red.
1. recolor parent and uncle to black
2. recolor grandparent to red
3. perform fixing on grandparent recursively




## Resources
https://github.com/zarif98sjs/RedBlackTree-An-Intuitive-Approach
https://www.geeksforgeeks.org/introduction-to-red-black-tree/
https://chatgpt.com/share/68303b5b-9cbc-800b-a43e-3c7e9d5b9c9e