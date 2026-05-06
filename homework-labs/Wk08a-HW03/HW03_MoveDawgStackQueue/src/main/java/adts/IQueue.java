package adts;

/**
 * A generic interface for a FIFO (First-In, First-Out) queue.
 *
 * @param <E> the type of elements in this queue
 */
public interface IQueue<E> extends Iterable<E> {

    /**
     * Inserts the specified element into the end of the queue.
     *
     * @param element the element to enqueue
     */
    void enqueue(E element);

    /**
     * Removes and returns the element at the front of the queue.
     *
     * @return the dequeued element
     * @throws RuntimeException if the queue is empty
     */
    E dequeue();

    /**
     * Returns (but does not remove) the element at the front of the queue.
     *
     * @return the element at the front
     * @throws RuntimeException if the queue is empty
     */
    E peek();

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no elements; false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in the queue.
     *
     * @return the size of the queue
     */
    int size();

    /**
     * Removes all elements from the queue.
     */
    void clear();
}