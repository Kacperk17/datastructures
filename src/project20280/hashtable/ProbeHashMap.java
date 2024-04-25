package project20280.hashtable;

import project20280.interfaces.Entry;

import java.util.ArrayList;

public class ProbeHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table;
    private final MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null);

    public ProbeHashMap() {
        super();
    }

    /**
     * Creates a hash table with given capacity and prime factor 109345121.
     */
    public ProbeHashMap(int cap) {
        super(cap);
    }

    /**
     * Creates a hash table with the given capacity and prime factor.
     */
    public ProbeHashMap(int cap, int p) {
        super(cap, p);
    }

    @Override
    protected void createTable() {
        table = new MapEntry[capacity];
    }

    int findSlot(int h, K k) {


        // cyclical "probe" search to find available slot

        int availableSlot = -1; // return this value if
        int i = h; // start index at hashcode h


        do { // while i is not j

            if(table[i] == null || table[i].equals(DEFUNCT)) {
                if (availableSlot == -1) {
                    availableSlot = i;
                }

                if(table[i] == null) {
                    break;
                }
            }

            // if there is already a key in this slot with the key we're looking for
            if (table[i].getKey().equals(k)) {
                return i;
            }

            i = (i + 1) % capacity;

        } while(i != h);
        return -(availableSlot + 1); // search failed
    }


    @Override
    protected V bucketGet(int h, K k) {


        // find slot for k given hashcode h
        int j = findSlot(h, k);

        if(j < 0) {
            return null;
        }

        return table[j].getValue();
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        // Find available slot
        int i = findSlot(h, k);

        if (i >= 0) { // Slot contains an existing entry
            return table[i].setValue(v); // Update the value and return it
        }

        table[-(i + 1)] = new MapEntry<>(k, v);

        // Increment size only if adding a new entry
        n++;
        return null;
    }

    @Override
    protected V bucketRemove(int h, K k) {


        int i = findSlot(h, k); // find slot to remove

        if(i < 0) { // nothing to remove
            return null;
        }

        V valueToReturn = table[i].getValue();
        table[i] = DEFUNCT;
        n--;

        return valueToReturn;
    }


    @Override
    public Iterable<Entry<K, V>> entrySet() {

        ArrayList<Entry<K, V>> entrySet = new ArrayList<>();

        for(int i = 0; i < capacity; i++) {

            if(!(table[i] == null || table[i] == DEFUNCT)) {
                Entry<K, V> currEntry = table[i];

                entrySet.add(currEntry);
            }
        }




        return entrySet;
    }


}
