package datastructures.concrete;


import datastructures.interfaces.IPriorityQueue;

import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int LENGTH_MODIFIER = 10;
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int numElements;
    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(NUM_CHILDREN + LENGTH_MODIFIER);
        numElements = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (numElements <= 0) {
            throw new EmptyContainerException();
        }
        
        // swapping
        T min = heap[0];
        numElements -= 1;
        heap[0] = heap[numElements];
        heap[numElements] = null;
        percolateDown(0);
        return min;
    }
    
    private void percolateDown(int index) {
        int firstChildIndex = getChildIndex(index, 0);
        int minChildIndex = firstChildIndex;
        // check if there are children
        if (minChildIndex < numElements) {
            int secondChild = getChildIndex(index, 1);
            // find the index of the smallest child
            for (int i = secondChild; i < firstChildIndex + NUM_CHILDREN && i < numElements; i++) {
                if (heap[i].compareTo(heap[minChildIndex]) < 0) {
                    minChildIndex = i;
                }
            }
            
            // swapping only if the heap is not already in order
            if (heap[index].compareTo(heap[minChildIndex]) > 0) {
                T minChild = heap[minChildIndex];
                heap[minChildIndex] = heap[index];
                heap[index] = minChild;
            }
            percolateDown(minChildIndex);

        }
    }
    
    @Override
    public T peekMin() {
        if (numElements > 0) {
            return heap[0];
        }
        throw new EmptyContainerException();
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (numElements >= heap.length) {
            resize();
        }
        heap[numElements] = item;
        
        percolateUp(numElements);
        numElements += 1;
    }
    
    private void percolateUp(int index) {
        
        int parentIndex = getParentIndex(index);
        
        if (parentIndex >= 0 && heap[index].compareTo(heap[parentIndex]) < 0) {
            T parent = heap[parentIndex];
            heap[parentIndex] = heap[index];
            heap[index] = parent;
            percolateUp(parentIndex);
        }
    }
    
    private int getParentIndex(int index) {
        return (index - 1) / NUM_CHILDREN;
    }
    
    private int getChildIndex(int index, int childNum) {
        return NUM_CHILDREN * index + childNum + 1; 
    }
    
    private void resize() {
        T[] newHeap = makeArrayOfT(heap.length * 2);
        for (int i = 0; i < heap.length; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }
    
    @Override
    public int size() {
        return numElements;
    }
}
//hi - Sara Young
