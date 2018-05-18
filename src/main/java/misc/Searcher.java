package misc;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

public class Searcher {
    /**
     * This method takes the input list and returns the top k elements
     * in sorted order.
     *
     * So, the first element in the output list should be the "smallest"
     * element; the last element should be the "biggest".
     *
     * If the input list contains fewer then 'k' elements, return
     * a list containing all input.length elements in sorted order.
     *
     * This method must not modify the input list.
     *
     * @throws IllegalArgumentException  if k < 0
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        // Implementation notes:
        //
        // - This static method is a _generic method_. A generic method is similar to
        //   the generic methods we covered in class, except that the generic parameter
        //   is used only within this method.
        //
        //   You can implement a generic method in basically the same way you implement
        //   generic classes: just use the 'T' generic type as if it were a regular type.
        //
        // - You should implement this method by using your ArrayHeap for the sake of
        //   efficiency.

        if (k < 0) {
            throw new IllegalArgumentException();
        }
        IPriorityQueue<T> sort = new ArrayHeap<>();
        IList<T> result = new DoubleLinkedList<>();
        
        // if size is 0 or if k is 0, return empty list.
        if (k == 0 || input.isEmpty()) {
            return result;
        }
        
        // Basically, rather than keeping a heap of all the input values, you only need a heap of k number of
        // of values. That size k heap represents the k biggest values in the input list, 
        // and you only need to constantly
        // update the heap when needed. So instead of calling perculateDown() on every element we only call it on a 
        // small fraction which significantly reduces runtime. 
        if (k > input.size()) { 
            int size = input.size();
            for (int i = 0; i < size; i++) {
                sort.insert(input.remove());
            }
            
            for (int j = 0; j < size; j++) {
                result.add(sort.removeMin());
            }
        } else {
            // forms initial "largest" heap of size k
            int size = input.size();
            for (int i = 0; i < k; i++) {
                sort.insert(input.remove());
            }
            
            
            
            // important loop right here
            for (int j = k; j < size; j++) {
                T item = input.remove();
                if (item.compareTo(sort.peekMin()) > 0) {
                    sort.removeMin();
                    sort.insert(item);
                }
            }
           
            for (int a = 0; a < k; a++) {
                result.add(sort.removeMin());
            }
            
        }
        
        return result;
    }
}
