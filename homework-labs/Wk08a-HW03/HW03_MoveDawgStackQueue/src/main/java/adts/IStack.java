package adts;

/**
 * A generic interface for a LIFO (Last-In, First-Out) stack.
 *
 * @param <E> the type of elements in this stack
 */
public interface IStack<E> extends Iterable<E> {

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param element the element to push
     */
    void push(E element);

    /**
     * Removes and returns the element at the top of the stack.
     *
     * @return the popped element
     * @throws RuntimeException if the stack is empty
     */
    E pop();

    /**
     * Returns the element at the top of the stack without removing it.
     *
     * @return the top element
     * @throws RuntimeException if the stack is empty
     */
    E peek();

    /**
     * Checks if the stack is empty.
     *
     * @return true if the stack is empty; false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in the stack.
     *
     * @return the stack size
     */
    int size();

    /**
     * Removes all elements from the stack.
     */
    void clear();
}