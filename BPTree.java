import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    // FIXME: Add default branching factor?
    
    /**
     * Public constructor
     * 
     * Since a new node is a leaf node, the root of a new BPTree is a LeafNode.
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        // TODO : Complete
        this.branchingFactor = branchingFactor;
        root = new LeafNode();
    }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    /**
     * Inserts a key and value pair into the BPTree.
     * 
     * Note: key-value pairs with duplicate keys can be inserted into the tree.
     * 
     * @param key The key to insert.
     * @param value The value to insert.
     */
    @Override
    public void insert(K key, V value) {
        root.insert(key, value);
    }
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        // TODO : Complete
        return null;
    }
    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        
        // True if the node is a leaf, false otherwise.
        boolean leaf;
        
        /**
         * Package constructor
         */
        Node() {
            keys = new ArrayList<K>();
            leaf = false;
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * Returns true if the node has more keys than the branching factor.
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            children = new ArrayList<Node>();
        }
        
        /**
         * Private helper method that returns the node where a key resides.
         * 
         * @param key The key to look up.
         * @param node The root node.
         * @return Node The node we are trying to find.
         *//*
        private Node search(K key, Node node) {
        	FIXME: Necessary to complete?
        	return null;
        }*/
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return children.get(0).getFirstLeafKey();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return keys.size() > branchingFactor;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
        	// Variable declaration
        	Node child; // To hold the leaf node to insert key into.
        	Node sibling; // To hold the sibling node from splitting.
        	InternalNode newINRoot; // To hold the new root if the root has overflow.
        	K siblingKey; // To hold the sibling's first key.
        	int index; // Holds index to place a new key and its children into.
        	
        	// Inserts into root if the root is empty. // FIXME: Necessary?
        	if (root.keys.size() == 0) {
        		root.insert(key, value);
        		child = null; // FIXME: Set child to root?
        	}
        	// Otherwise, calls insertHelper to find the child to insert key into.
        	else {
        		child = insertHelper(key, value, root);
        		child.insert(key, value);
        	}
        	
        	// Checking if there is overflow in the child node, then splitting if so. 
        	if (child.isOverflow()) {
        		sibling = child.split();
        		// Getting sibling's key and putting it into its parent node.
        		siblingKey = sibling.getFirstLeafKey();
        		// Making sure to insert the key into the correct index.
        		index = positionFinder(siblingKey);
        		keys.add(index, siblingKey);
        		children.add(index, sibling); // FIXME: needs index + 1?
        	}
        	
        	sibling = null;
        	// Checking if the root has overflow, then splitting if so.
        	if (root.isOverflow()) {
        		// If the root has overflow, must create a new InternalNode to hold the children.
        		newINRoot = new InternalNode();
        		
        		// Getting sibling's key.
        		sibling = split();
        		siblingKey = sibling.getFirstLeafKey();
        		
        		// Putting children into the newly created InternalNode.
        		newINRoot.keys.add(siblingKey);
//        		newINRoot.children.add(this); // FIXME: 'this' used in place of children?
        		newINRoot.children.addAll(children);
        		newINRoot.children.add(sibling);
        		
        		// Newly created InternalNode is now the root.
        		root = newINRoot;
        	}
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // TODO : Complete
            return null;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
        
        /* Private helper methods */
        
        /**
         * Private helper method that assists insert by returning the node to insert into.
         * Must consider certain cases.
         * 
         * @param key The key to insert
         * @param node
         * @return
         */
        private Node insertHelper(K key, V value, Node node) {
        	// Variable declaration
        	int i = 0; // Loop index
        	K key1; // K objects will hold the keys in current and next position.
        	K key2;
        	K lastKey; // K object will hold the last key index.
        	int numKeys; // Holds number of keys in node.
        	
        	// If the node is a leaf, then we have found the node to insert into.
        	if (node.leaf) {
        		return node;
        	}
        	
        	numKeys = node.keys.size();
        	lastKey = node.keys.get(numKeys - 1); // Getting last key index.
        	
        	// Else, traverse down the correct path.
        	for (i = 0; i < numKeys - 1; i++) { // FIXME Add case where -1 not applicable?
        		key1 = node.keys.get(i);
        		key2 = node.keys.get(i + 1);
        		
        		// Case: The key is less than or equal to the first key.
        		if (key.compareTo(key1) <= 0) {
        			insertHelper(key, value, children.get(i));
        		}
        		// Case: The key is greater than the first key, but less or equal to the second.
        		else if (key.compareTo(key1) > 0 && key.compareTo(key2) < 0) {
        			insertHelper(key, value, children.get(i + 1));
        		}
        		// Case: The key is greater than the last key. FIXME: Is this correct?
        		else if (key.compareTo(lastKey) > 0) {
        			insertHelper(key, value, children.get(numKeys - 1));
        		}
        	}
        	// FIXME: What to return if none of the cases are met?
        	return null;
        }
        
        /**
         * Private helper method that returns the position in the list that a key should be
         * added to
         * 
         * FIXME: Can we optimize this by checking the first and last keys first?
         * 
         * @param key The key to insert.
         * @return The int position in the list.
         */
        private int positionFinder(K key) {
        	// Variable declaration
        	int i = 0; // Loop index.
        	K key1; // K objects will hold the keys in current and next position.
        	K key2;
        	K lastKey; // K object will hold the key in the last index.
        	int numKeys; // Holds number of keys in list.
        	
        	numKeys = keys.size();
        	lastKey = keys.get(numKeys - 1);
        	// Loop through the keys list to find a position.
        	for (i = 0; i < numKeys - 1; i++) { // FIXME: Requires -1?
        		key1 = keys.get(i);
        		key2 = keys.get(i + 1);
        		
        		// Case: Key is less or equal to current key.
        		if (key.compareTo(key1) <= 0) {
        			return i;
        		}
        		// Case: Key is greater than current but less than or equal to next key.
        		else if (key.compareTo(key1) > 0 && key.compareTo(key2) <= 0) {
        			return i + 1;
        		}
        		// Case: Key is greater than the last key.
        		else if (key.compareTo(lastKey) > 0) {
        			return numKeys + 1;
        			
        		}
        	}
        	// FIXME: What to return if no cases match?
        	return -1;
        }
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        // 
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            leaf = true;
            // TODO : Complete
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // TODO : Complete
            return null;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // TODO : Complete
            return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            // TODO : Complete
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // TODO : Complete
            return null;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
    }

} // End of class BPTree
