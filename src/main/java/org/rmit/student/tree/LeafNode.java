package org.rmit.student.tree;

import java.util.ArrayList;
import java.util.List;

public class LeafNode<K extends Comparable<K>, V> extends Node<K> {

    private LeafNode<K, V> rightSibling;
    private LeafNode<K, V> leftSibling;

    // Sync this with the keys
    // private List<L> keys from Node class
    private List<V> dataEntries;

    LeafNode(int branchingFactor) {
        this.keys = new ArrayList<>();
        this.dataEntries = new ArrayList<>();
        this.branchingFactor = branchingFactor;
        this.nodeType = NodeType.LEAF;
    }

    public void insert(K key, V data) {
        int insertIndex = getInsertionIndex(key);
        // Ensures the keys stay in sorted order
        keys.add(insertIndex, key);
        // Ensure the data entry uses has the same index as its key
        dataEntries.add(insertIndex, data);
        if (isOverflow()) {
            handleOverflow();
        }
    }

    // The node will get split into 2
    @Override
    public void handleOverflow() {
        // Because decimals get truncated automatically no need to do ceiling and -1 to get the middle index
        int mid = this.keys.size() / 2;
        K midKey = this.keys.get(mid);

        // Split the keys and data entries into two arrays for the two nodes
        List<K> keysLeft = new ArrayList<>(keys.subList(0, mid));
        List<K> keysRight = new ArrayList<>(keys.subList(mid, keys.size()));
        List<V> dataEntriesLeft = new ArrayList<>(dataEntries.subList(0, mid));
        List<V> dataEntriesRight = new ArrayList<>(dataEntries.subList(mid, dataEntries.size()));

        // Create a new node for the right sibling
        LeafNode<K, V> newNodeRight = new LeafNode<>(branchingFactor);

        // Set the keys and their data entries
        this.keys = keysLeft;
        this.dataEntries = dataEntriesLeft;
        newNodeRight.setKeys(keysRight);
        newNodeRight.setDataEntries(dataEntriesRight);

        // Create a parent node (parents must always be internal nodes)
        // if it does not already exist
        if (this.getParent() == null) {
            this.setParent(new InternalNode<>(branchingFactor));
            this.parent.getChildren().add(0, this);
        }
        newNodeRight.setParent(this.getParent());

        // Add the nodes to the parents children
        int childIndex = this.parent.getIndex(this);
        // the left (current) node maintains its position
        // the right (new) node takes up the next space in array
        // every other child gets pushed to the right
        this.parent.getChildren().add(childIndex + 1, newNodeRight);

        // prev and next nodes (sibling nodes) need to be set and maintained
        if (this.rightSibling != null) {
            this.rightSibling.setLeftSibling(newNodeRight);
            newNodeRight.setRightSibling(this.rightSibling);
        }
        newNodeRight.setLeftSibling(this);
        this.setRightSibling(newNodeRight);

        // push up a key to parent internal node
        this.parent.insertKey(midKey);
    }

    public void setRightSibling(LeafNode<K, V> rightSibling) {
        this.rightSibling = rightSibling;
    }

    public void setLeftSibling(LeafNode<K, V> leftSibling) {
        this.leftSibling = leftSibling;
    }

    public void setDataEntries(List<V> dataEntries) {
        this.dataEntries = dataEntries;
    }

    public LeafNode<K, V> getRightSibling() {
        return this.rightSibling;
    }

    public LeafNode<K, V> getLeftSibling() {
        return this.leftSibling;
    }
}
