package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import misc.exceptions.EmptyContainerException;

import static org.junit.Assert.fail;


import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        IList<Integer> top = Searcher.topKSort(5, list);
        long end = System.currentTimeMillis() - start;
        System.out.println("Finished Searcher. (" + (end / 1000.0) + " sec)");
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testLowKValues() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> none = Searcher.topKSort(0, list);
        assertEquals(0, none.size());
        try {
            none.remove();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
        IList<Integer> one = Searcher.topKSort(1, list);
        assertEquals(1, one.size());
        assertEquals(9, one.remove());
    }
    
    @Test(timeout=SECOND)
    public void testNegative() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(-3);

        assertEquals(3, list.size());
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyInput() {
        IList<Integer> list = new DoubleLinkedList<>();
        assertEquals(0, list.size());
        
        IList<Integer> none = Searcher.topKSort(3, list);
        assertEquals(0, none.size());
        IList<Integer> alsoNone = Searcher.topKSort(0, list);
        assertEquals(0, alsoNone.size());
    }
    
    @Test(timeout=SECOND)
    public void testLargeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        
        IList<Integer> all = Searcher.topKSort(20, list);
        assertEquals(10, all.size());
        for (int i = 0; i < all.size(); i++) {
            assertEquals(i, all.get(i));
        }
    }
}
