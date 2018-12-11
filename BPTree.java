//package application;

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
        	return children.size() > branchingFactor; // FIXME: Keys or children?
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
//        		child = insertHelper(key, value, root); // Old
        		
        		child = insertHelper(key); // New
        		
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
        		// index needs + 1 because the key we add to the parent already exists.
        		children.add(index + 1, sibling);
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
        		
        		newINRoot.children.add(this); // FIXME: 'this' used in place of children?
//        		newINRoot.children.addAll(children);
//        		newINRoot.children.add(children.get(0));
        		
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
            // Variable declaration
        	int halfKeys; // Holds index for the half-way position of keys.
        	int halfChildren; // Holds index for the half-way position of children.
        	InternalNode sibling = new InternalNode(); // Holds new sibling node.
        	
        	halfKeys = keys.size() / 2;
        	halfChildren = children.size() / 2;
        	
        	// subList allows us to add keys from the half-way index to the end.
        	// (halfKeys + 1) is used because we no longer need the key at halfKeys index, since
        	// it was pushed up. 
        	sibling.keys.addAll(keys.subList(halfKeys + 1, keys.size()));
        	// Clears keys from original keys that we added to the new sibling.
        	keys.subList(halfKeys, keys.size()).clear();
        	
        	// Adding new sibling's children.
        	sibling.children.addAll(children.subList(halfChildren, children.size()));
        	children.subList(halfChildren, children.size()).clear();
        	
            return sibling;
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
         * NEW
         * @param key The key to insert.
         * @param value The value to insert.
         * @param node The current node.
         * @return Node The node to insert into.
         */
        private Node insertHelper(K key) {
        	// Variable declaration
        	int i = 0; // Loop index.
        	K firstKey; // Holds first key in keys.
        	K lastKey; // Holds last key in keys.
        	K curKey; // Holds key at current index in keys.
        	K nextKey; // Holds key at next index in keys.
        	final int FIRST_INDEX = 0;
        	final int LAST_INDEX = keys.size() - 1;
        	
        	firstKey = keys.get(FIRST_INDEX);
        	lastKey = keys.get(LAST_INDEX);
        	
        	// Case: Key is less than or equal to the first key -> go down first child.
        	if (key.compareTo(firstKey) <= 0) {
        		return children.get(FIRST_INDEX);
        	}
        	
        	// Case: Key is greater than or equal to the last key -> go down last child.
        	if (key.compareTo(lastKey) >=  0) {
        		
        		return children.get(LAST_INDEX + 1);
        	}
        	
        	// Case: Otherwise, go through all keys to find possible children.
        	for (i = 0; i < LAST_INDEX; i++) {
        		curKey = keys.get(i);
        		nextKey = keys.get(i + 1);
        		
        		// Key must be greater than current index key and <= to the next index key.
        		if (key.compareTo(curKey) > 0 && key.compareTo(nextKey) <= 0) {
        			return children.get(i + 1);
        		}
        	}
        	
        	// If no cases hold, return null. FIXME: Change this case's return?
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
        	
        	// Case: Key is less than or equal to current key.
    		if (key.compareTo(keys.get(0)) <= 0) {
    			return i;
    		}
    		// Case: Key is greater than the last key.
    		else if (key.compareTo(lastKey) > 0) {
    			return numKeys/* - 1*/; // FIXME: Shouldn't this be - 1?
    			
    		}
        	
        	// Loop through the keys list to find a position.
        	for (i = 0; i < numKeys - 1; i++) { // FIXME: Requires -1?
        		key1 = keys.get(i);
        		key2 = keys.get(i + 1);
        		
        		// Case: Key is greater than current but less than or equal to next key.
        		if (key.compareTo(key1) > 0 && key.compareTo(key2) <= 0) {
        			return i + 1;
        		}
        	}
        	// FIXME: What to return if no cases match?
        	return -1;
        }
    
        
        /**	NOTE: Doesn't work... Unable to obtain reference to new children.
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
        	lastKey = node.keys.get(numKeys - 1); // Getting last key.
        	
        	// Case: If the key is less than or equal to the first key, go down left.
        	if (key.compareTo(node.keys.get(0)) <= 0) {
        		return insertHelper(key, value, children.get(0));
        	}
        	
        	// Case: If the key is greater than or equal to the last key, go down right.
        	else if (key.compareTo(lastKey) >= 0) {
        		return insertHelper(key, value, children.get(children.size() - 1));
        	}
        		
        	// Else, traverse down the correct path.
        	for (i = 0; i < numKeys - 1; i++) { // FIXME Add case where -1 not applicable?
        		key1 = node.keys.get(i);
        		key2 = node.keys.get(i + 1);
        		
        		// Case: The key is less than or equal to the first key.
        		if (key.compareTo(key1) <= 0) {
        			insertHelper(key, value, children.get(i));
        		}
        	
        		// Case: The key is greater than the first key, but less or equal to the second.
        		if (key.compareTo(key1) > 0 && key.compareTo(key2) < 0) {
        			return insertHelper(key, value, children.get(i + 1));
        		}
        	
        		// Case: The key is greater than the last key. FIXME: Is this correct?
        		else if (key.compareTo(lastKey) > 0) {
        			insertHelper(key, value, children.get(numKeys - 1));
        		}
        	}
        	// FIXME: What to return if none of the cases are met?
        	return node;
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
            values = new ArrayList<V>();
            leaf = true;
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return values.size() >= branchingFactor; // FIXME: Keys or values?
            										 // FIXME: Needs > or >=?
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
        	// Variable declaration
        	Node child; // To hold the leaf node to insert key into.
        	Node sibling; // To hold the sibling node from splitting.
        	InternalNode newINRoot; // To hold the new root if the root has overflow.
        	K siblingKey; // To hold the sibling's first key.
        	
        	if (keys.size() == 0) {
        		keys.add(key);
        		values.add(value);
        	}
        	else {
        		insertHelper(key, value);
        	}
        	
			// Checking if the root has overflow, then splitting if so.
			if (root.isOverflow()) {
				sibling = null; // FIXME: Required?
				// Must create a new InternalNode to hold the children.
				newINRoot = new InternalNode();

				// Getting sibling's key.
				sibling = split();
				siblingKey = sibling.getFirstLeafKey();

				// Putting children into the newly created InternalNode.
				newINRoot.keys.add(siblingKey);
				newINRoot.children.add(this); // FIXME: 'this' used in place of children?
//            		newINRoot.children.addAll(children);
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
            // Variable declaration
        	int halfKeys; // Holds index for the half-way position of keys.
        	int halfVals; // Holds index for the half-way position of values.
        	LeafNode sibling = new LeafNode(); // Holds new sibling node.
        	
        	halfKeys = keys.size() / 2;
        	halfVals = values.size() / 2;
        	
        	// subList allows us to add keys from the half-way index to the end. 
        	sibling.keys.addAll(keys.subList(halfKeys, keys.size()));
        	// Clears keys from original keys that we added to the new sibling.
        	keys.subList(halfKeys, keys.size()).clear();
        	
        	// Adding new sibling's values.
        	sibling.values.addAll(values.subList(halfVals, values.size()));
        	values.subList(halfVals, values.size()).clear();
        	
        	// Updating sibling links.
        	if (next != null) {
        		next.previous = sibling; // FIXME: Correct?
        		sibling.next = next;
            	next = sibling;
        	}
        	else {
        		next = sibling;
        	}
        	
            return sibling;
        }
        
       
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            /*// TODO : Complete
            List<V> values = new LinkedList<>();
            
            if(key == null) {
                return values;
            }
            if(comparator.equals(null) || comparator.equals("")) {
                return values;
            }
            if(!comparator.equals("<=") && !comparator.equals("==") && !comparator.equals(">=")) {
                return values;
            }
            //traverse bpt check if each value >=,==,<=
            LeafNode node = keys.get(root.keys.get(0));
            while(node.next != null) {
                List<K> keys = node.keys;
                List<V> keyValues = node.values;
                //for(V value : list) {  
                //}
                for(K currentKey : keys) {                      //Traverses keys of a node
                    if(comparator.equals("<=")) {               //If comparator is <= check if currnetkey of node are <= key
                        if(currentKey.compareTo(key) <= 0) {
                            values.add(keyValues.get(keys.indexOf(currentKey)));
                        }
                    }
                    if(comparator.equals("==")) {               //If comparator is == check if currentkey of node are equal to key
                        if(node.equals(key)) {
                            values.add(keyValues.get(keys.indexOf(currentKey)));
                        }
                    }
                    if(comparator.equals(">=")) {               //If comparator is >= check if currentkey of node are >= key
                        if(currentKey.compareTo(key) >= 0) {          
                            values.add(keyValues.get(keys.indexOf(currentKey)));
                        }
                    }
                }   
                node = node.next;
            }*/
            
            
            
            
			return values;
		}
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /* Private helper methods */
        
        /**
         * Private helper method that assists in inserting a key, value pair.
         * 
         * @param key The key of value we wish to insert.
         * @param value The value we wish to insert.
         */
        private void insertHelper(K key, V value) {
        	// Variable declaration
        	int i = 0; // Loop index.
        	K key1; // K objects will hold the keys in current and next position.
        	K key2;
        	K lastKey; // K object will hold the key in the last index.
        	int numKeys; // Holds number of keys in list.
        	boolean loop; // True if key isn't <= to first or >= to last.
        	
        	numKeys = keys.size();
        	lastKey = keys.get(numKeys - 1);
        	loop = true;
        	
        	// Add to beginning if less than or equal to the first.
        	if (key.compareTo(keys.get(0)) <= 0) {
        		keys.add(0, key);
        		values.add(0, value); // FIXME: Do I need a separate method for vals?
        		loop = false;
        	}
        	// Add to end if greater than or equal to last.
        	else if (key.compareTo(lastKey) >= 0) {
        		keys.add(key);
        		values.add(value); // FIXME: Do I need a separate method for vals?
        		loop = false;
        	}
        	
        	if (loop) {
	        	for (i = 0; i < numKeys - 1; i++) { // FIXME: Requires -1?
	        		key1 = keys.get(i);
	        		key2 = keys.get(i + 1);
	        		
	        		/*// Case: Key is less than or equal to current key. // FIXME: Required?
	        		if (key.compareTo(key1) <= 0) {
	        			keys.add(i, key);
	        			values.add(i, value);
	        			break;
	        		}*/
	        		
	        		// Case: Key is greater than current but less than or equal to next key.
	        		if (key.compareTo(key1) > 0 && key.compareTo(key2) <= 0) {
	        			keys.add(i + 1, key);
	        			values.add(i + 1, value); // FIXME: Do I need a separate method for vals?
	        			
	        		}
	        		
	        		/*// Case: Key is greater than the last key. // FIXME: Required?
	        		else if (key.compareTo(lastKey) > 0) {
	        			keys.add(numKeys - 1, key);
	        			values.add(numKeys - 1, value);
	        		} */
	        	}
        	}
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
        /*// create empty BPTree with branching factor of 3
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
        System.out.println("Filtered values: " + filteredValues.toString());*/
    	

    	BPTree<Integer, Integer> bpTree = new BPTree<>(3);
    	bpTree.insert(0, 1);System.out.println("Tree structure: (Insert 0)\n" + bpTree.toString());
    	bpTree.insert(5, 6);System.out.println("Tree structure: (Insert 5)\n" + bpTree.toString());
    	bpTree.insert(10, 11);System.out.println("Tree structure: (Insert 10)\n" + bpTree.toString());
    	bpTree.insert(3, 4);System.out.println("Tree structure: (Insert 3)\n" + bpTree.toString());
    	bpTree.insert(8, 9);System.out.println("Tree structure: (Insert 8)\n" + bpTree.toString());
    	bpTree.insert(6, 7);System.out.println("Tree structure: (Insert 6)\n" + bpTree.toString());
    	bpTree.insert(9, 10);System.out.println("Tree structure: (Insert 9)\n" + bpTree.toString());
    	bpTree.insert(1, 2);System.out.println("Tree structure: (Insert 1)\n" + bpTree.toString());
//    	bpTree.insert(1, 3);System.out.println("Tree structure: (Insert 2)\n" + bpTree.toString());

//    	BPTree<Integer, Integer> bpTree = new BPTree<>(3);
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
//    	bpTree.insert(5, 5);System.out.println("Tree structure:\n" + bpTree.toString());
    }

} // End of class BPTree
