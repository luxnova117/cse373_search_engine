package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Random;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
  
    @Test(timeout=SECOND)
    public void testPeak() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        heap.insert(0);
        assertEquals(0, heap.peekMin());
        heap.removeMin();
        heap.removeMin();
        
        try {
            heap.peekMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException ");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertRandom() {
        int[] nums = new int[25];
        IPriorityQueue<Integer> heap = this.makeInstance();
        Random r = new Random();
        for (int i = 0; i < 25; i++) {
            nums[i] = r.nextInt(100);
            heap.insert(nums[i]);
        }
       
        Arrays.sort(nums);
        assertEquals(25, heap.size());
        for (int i = 0; i < nums.length; i++) {
            assertEquals(nums[i], heap.removeMin());
        }
        
    }
    
    @Test(timeout=SECOND)
    public void testInsertSorted() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 25; i++) {
            heap.insert(i);
        }
        assertEquals(25, heap.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertReverseSorted() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 24; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(25, heap.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertMostlySorted() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 3; i < 25; i++) {
            heap.insert(i);
        }
        heap.insert(1);
        heap.insert(0);
        heap.insert(2);
        assertEquals(25, heap.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsertDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 25; i++) {
            heap.insert(1);
        }
        assertEquals(25, heap.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(1, heap.removeMin());
        }
    }
    
    // insert in ascending then descending order
    @Test(timeout=SECOND)
    public void testInsertBothOrders() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 13; i++) {
            heap.insert(i);
        }
        
        for (int i = 24; i >= 13; i--) {
            heap.insert(i);
        }
        
        assertEquals(25, heap.size());
        for (int i = 0; i < 25; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout = SECOND)
    public void testNullInput() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException ");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testNegativeInput() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(0);
        for (int i = 1; i < 11; i++) {
            heap.insert(i);
            heap.insert(-i);
        }
        assertEquals(21, heap.size());
        for (int i = -10; i < 11; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout = SECOND)
    public void testInvalidRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException ");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
}
