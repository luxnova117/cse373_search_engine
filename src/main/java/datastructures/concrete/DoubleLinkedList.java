package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;
import java.util.NoSuchElementException;



/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (size == 0) {
            Node<T> node = new Node<T>(item);
            front = node;
            back = node;
        
        } 
        else {
            Node<T> node = new Node<T>(back, item, null);
            back.next = node;
            back = node;
        }
        size +=1;
        
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        Node<T> removed = back;
        if (size == 1) {
            front = null;
            back = null;
            size -= 1;
            return removed.data;
        }
        back = back.prev;
        back.next.prev = null;
        back.next = null;
        size -= 1;
        return removed.data;
      
    }

    @Override
    public T get(int index) {
        if (index >= size || index < 0 || size == 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current;
        if (index > size / 2) {
            current = backIterate(index);
        } else {
            current = iterate(index);
        }
        return current.data;
    }

    @Override
    public void set(int index, T item) {
        
        if (index >= size || index < 0 || size == 0) {
            throw new IndexOutOfBoundsException();
        }
        
        Node<T> current;
        if (index > (size - 1) / 2) {
            current = backIterate(index);
        } else {
            current = iterate(index);
        }
        Node<T> setter = new Node<T>(current.prev, item, current.next);
        if (size == 1) {
            front = setter;
            back = setter;
        } else if (index == 0) {
            front = setter;
            current.next.prev = setter;
            
        } else {
            current.prev.next = setter;
            if (index < size - 1) {
                current.next.prev = setter;
            } else {
                back = setter;
            }
            current.prev = null;
        }
        current.next = null;
        
        
        
    }

    @Override
    public void insert(int index, T item) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current;
        if (index == size) {
            add(item);
        } else {
            if (index > ((size - 1) / 2)){
                current = backIterate(index);
            } else {
                current = iterate(index);
            }
            
            Node<T> insertion = new Node<T>(current.prev, item, current);
            if (index == 0) {
                front = insertion;
            } else {
                current.prev.next = insertion;
            }
            current.prev = insertion;
            size += 1;
        }
        
        
        
    }
    
    
    @Override
    public T delete(int index) {
        if (index >= size || index < 0 || size == 0) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current;
        if (index > (size - 1) / 2) {
            current = backIterate(index);
        } else {
            current = iterate(index);
        }
        if (index == 0 && size == 1) {
            front = null;
            back = null;
        } else if (index == 0) {
            front = current.next;
            current.next.prev = current.prev;
            current.next = null;
        } else if (index == size - 1) {
            return remove();  
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current.prev = null;
            current.next = null;
        }
        size -= 1;
        return current.data;
    }

    @Override
    public int indexOf(T item) {
        return iterate(item);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        return iterate(other) >= 0;
    }
    
    private Node<T> iterate(int index){
        Node<T> current = front;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }
    
    private Node<T> backIterate(int index){
        Node<T> current = back;
        for (int i = size - 1; i > index; i--) {
            current = current.prev;
        }
        return current;
    }

    private int iterate(T item){
        Iterator<T> itr = iterator();
        int index = 0;
        if (item == null) {
            while (itr.hasNext()) {
                if (itr.next() == item) {
                   return index;
                }
                index += 1;                
            }
        } else {
            while (itr.hasNext()) {
                if (itr.next().equals(item)) {
                   return index;
                }
                index += 1;
            }
        }
        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
            
        }
    }
}
