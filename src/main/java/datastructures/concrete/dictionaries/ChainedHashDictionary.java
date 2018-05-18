package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int elements;
    
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(10); 
        elements = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % chains.length);
        }
        IDictionary<K, V> dict = chains[index];
        if (dict != null && dict.containsKey(key)) {
            return dict.get(key);
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public void put(K key, V value) {
        if (elements / chains.length > 1) {
            resize();
        }
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % chains.length);
        }        
        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<K, V>();
        }
        if (!chains[index].containsKey(key)) {
            elements += 1;
        }
        chains[index].put(key, value);
    }

    @Override
    public V remove(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % chains.length);
        }        
        IDictionary<K, V> dict = chains[index];
        if (dict != null && dict.containsKey(key)) {
            elements -= 1;
            return dict.remove(key);
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index;
        if (key == null) {
            index = 0;
        } else {
            index = Math.abs(key.hashCode() % chains.length);
        }        
        IDictionary<K, V> dict = chains[index];
        return (dict != null && dict.containsKey(key));
    }

    @Override
    public int size() {
        return elements;
    }
    
    private void resize() {
        IDictionary<K, V>[] chains2 = makeArrayOfChains(chains.length * 2);
        for (int i = 0; i < chains.length; i++) {
            IDictionary<K, V> bucket = chains[i];
            if (bucket != null) {
                Iterator<KVPair<K, V>> iter = bucket.iterator();
                while (iter.hasNext()) {
                    KVPair<K, V> pair = iter.next();
                    int index = Math.abs(pair.getKey().hashCode() % chains2.length);
                    if (chains2[index] == null) {
                        chains2[index] = new ArrayDictionary<K, V>();
                    }
                    chains2[index].put(pair.getKey(), pair.getValue());
                }
            }
        }
        chains = chains2;
    }
    
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 4. Think about what exactly your *invariants* are. As a 
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    
    
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int currentMajorIndex;
        private Iterator<KVPair<K, V>> currentBucketIter;
        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            int index = -1;
            if (chains.length > 0) {
                currentMajorIndex = findNext(0);
                index = findNext(0);
            }
            if (index < chains.length && index >= 0) {
                currentBucketIter = chains[index].iterator();
            }
            
            
        }
        
        // Find the index of the next bucket that isn't null.
        private int findNext(int index){
            while (index < chains.length && chains[index] == null) {
                index += 1;
            }
            return index;
        }

        @Override
        public boolean hasNext() {
            return currentBucketIter != null &&
                    (findNext(currentMajorIndex + 1) < chains.length || currentBucketIter.hasNext());
        }

        @Override
        public KVPair<K, V> next() {
            if (currentBucketIter == null) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> pair = null;
            if (currentBucketIter.hasNext()) {
                pair = currentBucketIter.next();
            }
            else {
                currentMajorIndex = findNext(currentMajorIndex + 1);
                
                // 2 outcomes:
                // A: No more buckets are left, findNext(currentMajorIndex + 1) >= chains.length, will 
                //    not pass 2nd check of hasNext() again.
                // B: Found another bucket, set the new currentBucketIter, will pass the checks of hasNext().
                if (currentMajorIndex < chains.length) {
                    currentBucketIter = chains[currentMajorIndex].iterator();
                    pair = currentBucketIter.next();
                } else {
                    throw new NoSuchElementException();
                }
            }
            return pair;
        }
    }
}
