package org.rmit.student.tree;

public class BPlusTree<K extends Comparable<K>, V> {

    // Must be >= 3
    private int branchingFactor;

    private Node<K> root;

    public BPlusTree(int branchingFactor) {
        this.branchingFactor = branchingFactor;
        this.root = new LeafNode<K, V>(branchingFactor);
    }

    /**
     * Basic node insertion, traverses down the tree until it finds a leaf node.
     * Inserts the leaf node into the tree and then updates the root if it has changed.
     */
    public void insert(K key, V data) {
        // Traverse the tree until you reach a leaf node
        LeafNode<K, V> leafNode = findLeafNode(key);
        leafNode.insert(key, data);

        // Check if the root node has changed
        while(root.getParent() != null) {
            this.root = root.getParent();
        }
    }

    /**
     * Finds the leaf node by traversing to the bottom of the tree using the key
     */
    private LeafNode<K, V> findLeafNode(K key) {
        Node<K> node = this.root;
        while (node.getNodeType() != NodeType.LEAF) {
            // Uses a binary search to find the right child node to traverse to
            int index =  node.getInsertionIndex(key);

            InternalNode<K> internalNode = (InternalNode<K>) node;
            node = internalNode.getChildren().get(index);
        }
        return (LeafNode<K, V>) node;
    }

    // TODO
    public void delete() {

    }
}
