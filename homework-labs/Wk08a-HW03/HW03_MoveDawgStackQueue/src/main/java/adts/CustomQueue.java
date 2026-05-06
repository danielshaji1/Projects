package adts;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A wrapper class for the IQueue interface.
 * This class delegates all operations to an underlying queue implementation.
 *
 * By default, this wrapper uses the linked-list–based implementation (LinkedListQueue).
 * To use an array-based implementation instead, simply comment out the current assignment
 * and uncomment the alternative.
 *
 * @param <E> the type of elements stored in this queue
 */

public class CustomQueue<E> implements IQueue<E> {

    private IQueue<E> queueImpl;

    /**
     * Constructs a CustomQueue using the default underlying implementation.
     * By default, this uses LinkedListQueue.
     */
    // Default constructor that uses ArrayStack as the underlying implementation.
    public CustomQueue() {
        this(new LinkedListQueue<>());
    }
    public CustomQueue(IQueue<E> queueImpl) {
        this.queueImpl = queueImpl;
    }

    // enabling swapping of implementations
    public void setDelegate(IQueue<E> newImpl) {
        this.queueImpl = newImpl;
    }

    @Override
    public void enqueue(E element) {
        queueImpl.enqueue(element);
    }

    @Override
    public E dequeue() {
        return queueImpl.dequeue();
    }

    @Override
    public E peek() {
        return queueImpl.peek();
    }

    @Override
    public boolean isEmpty() {
        return queueImpl.isEmpty();
    }

    @Override
    public int size() {
        return queueImpl.size();
    }

    @Override
    public void clear() {
        queueImpl.clear();
    }


    @Override
    public Iterator<E> iterator() {
        // Delegate iterator if the underlying implementation is Iterable
        if (queueImpl instanceof Iterable) {
            return ((Iterable<E>) queueImpl).iterator();
        }
        throw new UnsupportedOperationException("Underlying queue is not iterable");
    }
}