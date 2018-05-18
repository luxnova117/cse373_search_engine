package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
//import datastructures.concrete.dictionaries.ArrayDictionary.Pair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    private int size;
    private int numPairs;
    // You're encouraged to add extra fields (and helper methods) though!

    public ArrayDictionary() {
        pairs = makeArrayOfPairs(1);
        size = 1;
        numPairs = 0;
    }
    
    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplif y this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modif y this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            return pairs[indexOfKey(key)].value;
        } 
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        //long startTime = System.nanoTime();
        if (containsKey(key)) {
            pairs[indexOfKey(key)].value = value; 
            
        } else if  (size == numPairs) {
            increaseArraySize();    
            pairs[numPairs] = new Pair<K, V>(key, value);
            numPairs += 1; 
        } else {
            pairs[numPairs] = new Pair<K, V>(key, value);
            numPairs += 1;      
        }
        //long endTime = System.nanoTime();
        //System.out.println(endTime - startTime);
    }

    private void increaseArraySize() {
        size = size * 2;
        Pair<K, V>[] newArray = makeArrayOfPairs(size);
        for (int i = 0; i < numPairs; i++) {
            newArray[i] = pairs[i];
        }
        pairs = newArray;     
    }
    
    @Override
    public V remove(K key) {
        if (numPairs == 0 || size == 0) {
            throw new NoSuchKeyException();
        }
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        } else {
            V value = pairs[indexOfKey(key)].value;
            for (int i = indexOfKey(key); i < numPairs - 1; i++) {
                pairs[i] = pairs[i + 1];
            }
            pairs[numPairs - 1] = null;
            numPairs -= 1;
            return value;
        }       
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < numPairs; i++) {
            if (key == null) {
                if (pairs[i].key == null) {
                    return true;
                }
            } if (key == "") {
                if (pairs[i].key == "") {
                    return true;
                }
            } else if (pairs[i].key == key || pairs[i].key.equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    private int indexOfKey(K key) {
        for (int i = 0; i < numPairs; i++) {
            if (key == null) {
                if (pairs[i].key == null){
                    return i;
                }
            }
            if (key == "") {
                if (pairs[i].key.equals("")){
                    return i;
                }
            }
            if (pairs[i].key == key || pairs[i].key.equals(key)) {
                return i;
            }
        } 
        throw new NoSuchKeyException();
    }

    @Override
    public int size() {
        return numPairs;
    }
    
    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(pairs, numPairs);   
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private Pair<K, V>[] pairs;
        private int numPairs;
        private int index;
        
        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int numPairs) {
            this.pairs = pairs;
            this.numPairs = numPairs; 
            this.index = 0;
        }
        
        public boolean hasNext() {
            return !(index >= numPairs);
        }
        
        public KVPair<K, V> next() {
            if (this.hasNext()) {
                KVPair<K, V> nextPair = new KVPair<K, V>(this.pairs[index].key, this.pairs[index].value);
                index++;
                return nextPair;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}




   