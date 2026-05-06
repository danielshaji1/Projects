package adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A linked-list–based implementation of the IStack interface.
 *
 * @param <E> the type of elements stored in the stack
 */
public class LinkedListStack<E> implements IStack<E> {

    /**
     * A private node class representing each element in the stack.
     */
    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    private Node<E> top; // Points to the top of the stack.
    private int size;    // Tracks the number of elements in the stack.

    public LinkedListStack() {
        top = null;
        size = 0;
    }

    @Override
    public void push(E element) {
        top = new Node<>(element, top); // New node points to old top
        size++;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty.");
        }
        E element = top.element;
        top = top.next; // Move top down one node
        size--;
        return element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty.");
        }
        return top.element;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // or top == null
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        top = null;
        size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = top;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E item = current.element;
                current = current.next; // move forward
                return item;
            }
        };
    }
}
