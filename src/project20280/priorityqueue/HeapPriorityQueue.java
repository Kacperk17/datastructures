package project20280.priorityqueue;

/*
 */

import project20280.interfaces.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


/**
 * An implementation of a priority queue using an array-based heap.
 */

public class HeapPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {

    protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

    /**
     * Creates an empty priority queue based on the natural ordering of its keys.
     */
    public HeapPriorityQueue() {
        super();
    }

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the priority queue
     */
    public HeapPriorityQueue(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Creates a priority queue initialized with the respective key-value pairs. The
     * two arrays given will be paired element-by-element. They are presumed to have
     * the same length. (If not, entries will be created only up to the length of
     * the shorter of the arrays)
     *
     * @param keys   an array of the initial keys for the priority queue
     * @param values an array of the initial values for the priority queue
     */
    public HeapPriorityQueue(K[] keys, V[] values) {


        // if the arrays are the same length
        if(keys.length == values.length) {

            for(int i = 0; i < keys.length; i++) {

                insert(keys[i], values[i]);
            }


        }

        // if the arrays are not the same length
        else {

            // if values is a smaller array
            if(keys.length > values.length) {

                for(int i = 0; i < values.length; i++) {
                    insert(keys[i], values[i]);
                }

            }
            // if keys is a smaller array
            else {

                for(int i = 0; i < keys.length; i++) {
                    insert(keys[i], values[i]);
                }

            }

        }



    }

    // protected utilities
    protected int parent(int j) {

        return (j-1)/2;
    }

    protected int left(int j) {

        return (2*j) + 1;
    }

    protected int right(int j) {

        return (2*j) + 2;
    }

    protected boolean hasLeft(int j) {

        if(left(j) < size()) {
            return heap.get(left(j)) != null;
        }
        else {
            return false;
        }
    }

    protected boolean hasRight(int j) {

        if(right(j) < size()) {
            return heap.get(right(j)) != null;
        }
        else {
            return false;
        }
    }

    // method which returns 0 if left child is smaller and 1 if right child is smaller
    private int chooseDirection(int j) {
        int result;
        // if j has both a left and a right
        if(hasLeft(j) && hasRight(j)) {

            Entry<K, V> left = heap.get(left(j));
            Entry<K, V> right = heap.get(right(j));

            if(compare(left, right) < 0) {
                return 0;
            }
            else {
                return 1;
            }

        }
        else {
            throw new IllegalArgumentException("j must have left and right child");
        }

    }

    /**
     * Exchanges the entries at indices i and j of the array list.
     */
    protected void swap(int i, int j) {

        Entry<K, V> tempi = heap.get(i);
        Entry<K, V> tempj = heap.get(j);
        heap.set(i, tempj);
        heap.set(j, tempi);


    }

    /**
     * Moves the entry at index j higher, if necessary, to restore the heap
     * property.
     */
    protected void upheap(int j) {


        while(j > 0) {
            int p = parent(j);

            if(compare(heap.get(j), heap.get(p)) >= 0) {
                break;
            }
            swap(j, p);
            j = p;
        }

    }

    // method to compare left node
    private boolean compareLeft(int j) {
        Entry<K, V> entry = heap.get(j);
        Entry<K, V> leftChild;

        try {
            leftChild = heap.get(left(j));
        } catch (Exception e)  { return false;}

        return (compare(entry, leftChild) > 0);
    }

    private boolean compareRight(int j) {
        Entry<K, V> entry = heap.get(j);
        Entry<K, V> rightChild;


        try {
            rightChild = heap.get(right(j));
        } catch (Exception e) { return false; }


        return (compare(entry, rightChild) > 0);
    }

    /**
     * Moves the entry at index j lower, if necessary, to restore the heap property.
     */
    protected void downheap(int j) {

        while(hasLeft(j)) {
            int leftIndex = left(j);
            int smallChildIndex = leftIndex;

            if(hasRight(j)) {
                int rightIndex = right(j);

                if(compare(heap.get(leftIndex), heap.get(rightIndex)) > 0) {
                    smallChildIndex = rightIndex;
                }

            }
            if(compare(heap.get(smallChildIndex), heap.get(j)) >= 0) {
                break;
            }
            swap(j, smallChildIndex);
            j = smallChildIndex;

        }



    }

    /**
     * Performs a bottom-up construction of the heap in linear time.
     */
    protected void heapify() {

        int startIndex = parent(size() - 1);

        for(int j = startIndex; j >= 0; j--) {
            downheap(j);
        }

    }

    // public methods

    /**
     * Returns the number of items in the priority queue.
     *
     * @return number of items
     */
    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Returns (but does not remove) an entry with minimal key.
     *
     * @return entry having a minimal key (or null if empty)
     */
    @Override
    public Entry<K, V> min() {
        return heap.get(0);
    }

    /**
     * Inserts a key-value pair and return the entry created.
     *
     * @param key   the key of the new entry
     * @param value the associated value of the new entry
     * @return the entry storing the new key-value pair
     * @throws IllegalArgumentException if the key is unacceptable for this queue
     */
    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {


        PQEntry<K, V> newEntry = new PQEntry<>(key, value);

        heap.add(newEntry);

        int indexOfNewEntry = heap.size() - 1;
        upheap(indexOfNewEntry);

        return newEntry;
    }

    /**
     * Removes and returns an entry with minimal key.
     *
     * @return the removed entry (or null if empty)
     */
    @Override
    public Entry<K, V> removeMin() {


        Entry<K, V> entryToRemove = heap.get(0);
        // if the queue has only 1 entry
        if(size() == 1) {
            heap.remove(0);
            return entryToRemove;
        }
        else {

            int indexOfRightmostNode = heap.size() - 1;

            swap(0, indexOfRightmostNode);
            heap.remove(indexOfRightmostNode);

            downheap(0);
        }

        return entryToRemove;
    }

    public String toString() {
        return heap.toString();
    }

    /**
     * Used for debugging purposes only
     */
    private void sanityCheck() {
        for (int j = 0; j < heap.size(); j++) {
            int left = left(j);
            int right = right(j);
            //System.out.println("-> " +left + ", " + j + ", " + right);
            Entry<K, V> e_left, e_right;
            e_left = left < heap.size() ? heap.get(left) : null;
            e_right = right < heap.size() ? heap.get(right) : null;
            if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0) {
                System.out.println("Invalid left child relationship");
                System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
            }
            if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0) {
                System.out.println("Invalid right child relationship");
                System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
            }
        }
    }

    public static void main(String[] args) {
        Integer[] rands = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        HeapPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>(rands, rands);
        //pq.chooseDirection(3);
        System.out.println("elements: " + rands);
        System.out.println("after adding elements: " + pq);

        System.out.println("min element: " + pq.min());

        pq.removeMin();
        System.out.println("after removeMin: " + pq);
        // [             1,
        //        2,            4,
        //   23,     21,      5, 12,
        // 24, 26, 35, 33, 15]
    }
}
