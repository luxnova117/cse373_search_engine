package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    private static final int TEST_CASE_NUM = 1000000;
    
    @Test(timeout=10*SECOND)
    public void testHeapMassRandomInsert() {
        
        int[] nums = new int[TEST_CASE_NUM];
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        Random r = new Random();
        for (int i = 0; i < TEST_CASE_NUM; i++) {
            nums[i] = r.nextInt(TEST_CASE_NUM);
            heap.insert(nums[i]);
        }
       
        Arrays.sort(nums);
        assertEquals(TEST_CASE_NUM, heap.size());
        for (int i = 0; i < TEST_CASE_NUM; i++) {
            assertEquals(nums[i], heap.removeMin());
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testHeapMassDuplicateInsert() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100000; j++) {
                heap.insert(j);
            }
        }
        assertEquals(TEST_CASE_NUM, heap.size());
        
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(i, heap.removeMin());
            }
        }
    }
    
    
    @Test(timeout=10*SECOND)
    public void testHeapMassReverseSortedInsert() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = TEST_CASE_NUM - 1; i >= 0; i--) {
            heap.insert(i);
        }
        assertEquals(TEST_CASE_NUM, heap.size());
        for (int i = 0; i < TEST_CASE_NUM; i++) {
            assertEquals(i, heap.removeMin());
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testSortingStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }
        
        
        IList<Integer> top = Searcher.topKSort(5, list);
        
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(999995 + i, top.get(i));
        }
    }
    
    @Test(timeout=10*SECOND)
    public void testLargeKStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(600000, list);
        assertEquals(600000, top.size());
        Iterator<Integer> iter = top.iterator();
        
        for (int i = 0; i < top.size(); i++) {
            
            assertEquals(400000 + i, iter.next());
        }
    }
    
}
